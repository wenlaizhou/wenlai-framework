/**
 * @项目名称: framework
 * @文件名称: Process.java
 * @Date: 2015年11月6日
 * @author: wenlai
 * @type: Process
 */
package cn.framework.db.sql;

import cn.framework.core.log.LogProvider;
import cn.framework.core.pool.ResourceProcessor;
import cn.framework.core.utils.*;
import org.w3c.dom.Node;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 过程对象<br>
 * 配置说明:<br>
 * 遵循配置及逻辑理念<br>
 *
 * @author wenlai
 * @see Procedure#getProcedure(String)
 */
public class Procedure {

    /**
     * sql群组容器
     */
    private final static Map<String, Procedure> PROCEDURE_CONTAINER = new ConcurrentHashMap<>();
    public final List<Sql> sqls = new ArrayList<>();// TODO public 转为 private
    public final String id;
    public final boolean cached;
    public final boolean transaction;
    public final int cacheMiliSeconds;
    public final String connectionId;

    /**
     * 根据配置节点构造Procedure对象
     *
     * @param procedureNode 数据配置节点
     *
     * @throws Exception procedure配置节无id或id重复，系统异常
     */
    private Procedure(Node procedureNode) throws Exception {
        this.id = Xmls.attr("id", procedureNode.getParentNode(), "") + "/" + Xmls.attr("id", procedureNode);
        if (!Strings.isNotNullOrEmpty(this.id) || PROCEDURE_CONTAINER.containsKey(this.id)) {
            throw new Exception("procedure配置节无id或id重复");
        }
        this.connectionId = Xmls.attr("connection", procedureNode, "");
        this.transaction = Boolean.parseBoolean(Xmls.attr("transaction", procedureNode, "false"));
        this.cached = Boolean.parseBoolean(Xmls.attr("cached", procedureNode, "false"));
        this.cacheMiliSeconds = this.cached ? Integer.parseInt(Xmls.attr("cachedMiliSeconds", procedureNode, "600000")) : -1;
        ArrayList<Node> sqlNodeList = Xmls.xpathNodesArray(".//sql", procedureNode);
        int sqlOrder = 0;
        for (Node sqlNode : sqlNodeList) {
            try { // 构建sql对象
                Sql sql = new Sql();
                sql.id = Xmls.attr("id", sqlNode, Integer.toString(sqlOrder)); // 获取sql-id 如果没有则按照序号排列，起始序号为0
                sql.returnId = Boolean.parseBoolean(Xmls.attr("returnId", sqlNode, "false"));
                sql.sql = sqlNode.getTextContent().trim();
                sql.cached = Boolean.parseBoolean(Xmls.attr("cached", sqlNode, "false"));
                sql.cachedMiliseconds = sql.cached ? Integer.parseInt(Xmls.attr("cachedMiliSeconds", sqlNode, "600000")) : -1;
                sql.isQuery = Regexs.test("^select.*", sql.sql) || Regexs.test("^show.*", sql.sql);
                sql.hasResultParam = Regexs.test("@\\{(.*?)\\}", sql.sql);
                if (sql.hasResultParam) {
                    sql.sqlResultParams = getSqlResultParams(sql.sql);
                    sql.sql = processResultParams(sql);
                }
                sql.params = Xmls.getProperties(sql.sql);
                sql.realParams = Xmls.getPropertiesDistinct(sql.sql);
                sql.sql = processProperties(sql.sql).trim();
                sql.hasLanguageParam = Regexs.test("#\\{(.*?)\\}", sql.sql);
                if (sql.hasLanguageParam) {
                    sql.languageParams = getLanguageParams(sql.sql);
                }
                this.sqls.add(sql);
            }
            catch (Exception x) {
                LogProvider.getFrameworkErrorLogger().error(x.getMessage(), x);
            }
            sqlOrder++;
        }
        PROCEDURE_CONTAINER.put(id, this);
    }

    /**
     * 根据配置节点创建procedure
     *
     * @param processConfNode 配置
     *
     * @throws Exception
     */
    public static void createProcedure(Node processConfNode) throws Exception {
        new Procedure(processConfNode);
    }

    /**
     * 获取procedure
     *
     * @param id
     *
     * @return
     */
    public final static Procedure getProcedure(String id) {
        return PROCEDURE_CONTAINER.get(id);
    }

    /**
     * 使用KVMap和sql拼出全部完整sql<br>
     * 其他系统进行调用前要注意：<blockquote>
     * 该方法不会进行任何的错误判断包括：
     * 1、参数数量校验
     * 2、参数名称校验
     * </blockquote>
     *
     * @param sql
     * @param params
     *
     * @return
     */
    @SuppressWarnings("unused")
    private static String buildFullSql(Sql sql, KVMap params) {
        // executeHandler.setObject(index + 1, params.get(sql.params.get(index)));
        String result = sql.sql;
        for (int i = 0; i < sql.params.size(); i++)
            result = result.replaceFirst("\\?", params.getString(sql.params.get(i)));
        return result;
    }

    /**
     * 处理resultParams
     *
     * @param sql
     *
     * @return
     */
    private String processResultParams(Sql sql) {
        String result = sql.sql;
        for (Sql.SqlResult sqlResult : sql.sqlResultParams) {
            String origin = new StringBuilder("@{").append(sqlResult.placeHolder).append("}").toString();
            String newStr = new StringBuilder("${").append(sqlResult.placeHolder).append("}").toString();
            result = result.replace(origin, newStr);
        }
        return result;
    }

    /**
     * 获取resultParams
     *
     * @param sql sql
     *
     * @return
     */
    private List<Sql.SqlResult> getSqlResultParams(String sql) {
        List<Sql.SqlResult> result = new ArrayList<>();
        String[] properties = Regexs.match("@\\{(.*?)\\}", sql);
        if (properties != null && properties.length > 0) {
            for (String property : properties) {
                try {
                    // result.add(property);
                    Sql.SqlResult sqlResult = new Sql.SqlResult();
                    String[] splits = property.split("\\.");
                    if (splits != null && splits.length == 2) {
                        String[] index = Regexs.match("\\[(.*?)\\]", splits[0]);
                        if (index != null && index.length > 0) {
                            sqlResult.index = Strings.parseInt(index[0]);
                            sqlResult.id = splits[0].replaceAll("\\[(.*?)\\]", "");
                            sqlResult.isNonQueryResult = splits[1].equals("nonQueryResult");
                            sqlResult.column = splits[1];
                            sqlResult.placeHolder = property;
                            result.add(sqlResult);
                        }
                    }
                }
                catch (Exception x) {
                    LogProvider.getFrameworkErrorLogger().error(x.getMessage(), x);
                }
            }
        }
        return result;
    }

    /**
     * 获取语言参数
     *
     * @param sql sql
     *
     * @return
     */
    private List<String> getLanguageParams(String sql) {
        List<String> result = new ArrayList<>();
        String[] properties = Regexs.match("#\\{(.*?)\\}", sql);
        if (properties != null && properties.length > 0) {
            for (String property : properties)
                result.add(property);
        }
        return result;
    }

    /**
     * 处理属性
     *
     * @param sql sql
     *
     * @return
     */
    private String processProperties(String sql) {
        String[] properties = Regexs.match("\\$\\{(.*?)\\}", sql);
        if (properties != null && properties.length > 0) {
            for (String property : properties)
                sql = sql.replace(String.format("${%1$s}", property), "?");
        }
        return sql;
    }

    /**
     * 执行过程，返回执行结果<br>
     * 返回结构化数据：<br>
     * key - value <br>
     * value : {@link DataSet} <br>
     * <br>
     *
     * @param params         sql执行参数，key对应sql配置中的${param}，value即为参数值
     * @param languageParams sql语句参数，key对应sql配置中的#{param}，value即为参数值<br>
     *                       <b>注意符号，执行参数为$，语言参数为#</b>
     *
     * @return {@link Result}
     */
    public Result process(KVMap params, KVMap languageParams) {
        Result result = new Result();
        DataSource dataSource = Springs.get(this.connectionId);
        if (dataSource != null) {
            Connection connection = null;
            try {
                connection = dataSource.getConnection();
                if (this.transaction) {
                    connection.setAutoCommit(false);
                    for (Sql s : this.sqls) {
                        try {
                            executeSqlTransaction(params, result, s, connection, buildSql(languageParams, s));
                        }
                        catch (Exception x) {
                            LogProvider.getFrameworkErrorLogger().error(x.getMessage(), x);
                            result.setSuccess(false);
                        }
                    }
                    connection.commit();
                }
                else {
                    for (Sql s : this.sqls) {
                        try {
                            executeSql(params, result, s, connection, buildSql(languageParams, s));
                        }
                        catch (Exception x) {
                            LogProvider.getFrameworkErrorLogger().error(x.getMessage(), x);
                            result.setSuccess(false);
                        }
                    }
                }
                return result.success ? result.setMessage("sql执行未出现异常") : result.setMessage("执行出现异常");
            }
            catch (Exception e) {
                if (this.transaction) {
                    if (connection != null) {
                        try {
                            connection.rollback();
                        }
                        catch (Exception x) {
                            Exceptions.processException(x);
                        }
                    }
                }
                LogProvider.getFrameworkErrorLogger().error(e.getMessage(), e);
                return result.setSuccess(false).setMessage("执行错误");
            }
            finally {
                if (connection != null) {
                    try {
                        if (!connection.getAutoCommit()) {
                            connection.setAutoCommit(true);
                        }
                        connection.close();
                    }
                    catch (Exception x) {
                        Exceptions.processException(x);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 构建sql
     *
     * @param languageParams 语句参数
     * @param sql            sql
     *
     * @return
     */
    private String buildSql(KVMap languageParams, Sql sql) {
        try {
            if (languageParams != null && !languageParams.isEmpty()) {
                String result = sql.sql;
                for (Pair pair : languageParams)
                    result = result.replace(new StringBuilder("#{").append(pair.key).append("}"), pair.value != null ? pair.value.toString() : Strings.EMPTY);
                return result;
            }
        }
        catch (Exception x) {
            LogProvider.getFrameworkErrorLogger().error(x.getMessage(), x);
        }
        return sql.sql;
    }

    /**
     * 执行过程，返回执行结果<br>
     * 返回结构化数据：<br>
     * key - value <br>
     * value : {@link DataSet} <br>
     * <br>
     *
     * @param params sql执行参数，key对应sql配置中的${param}，value即为参数值
     *
     * @return {@link Result}
     */
    public Result process(KVMap params) {
        return process(params, null);
    }

    /**
     * 执行过程，返回执行结果<br>
     *
     * @return {@link Result}
     */
    public Result process() {
        return process(null);
    }

    /**
     * 执行带事务的sql<br>
     * TODO 功能同步非事务代码<br>
     * 2015-12-15 update：不进行大对象转换
     *
     * @param params
     * @param result
     * @param sql
     * @param connection
     *
     * @throws SQLException
     */
    private void executeSqlTransaction(KVMap params, Result result, Sql sql, Connection connection, String sqlString) throws Exception {
        paramCheck(params, sql);
        PreparedStatement executeHandler = sql.returnId ? connection.prepareStatement(Strings.isNotNullOrEmpty(sqlString) ? sqlString : sql.sql, PreparedStatement.RETURN_GENERATED_KEYS) : connection.prepareStatement(Strings.isNotNullOrEmpty(sqlString) ? sqlString : sql.sql);
        execute(params, result, sql, executeHandler);
    }

    /**
     * 执行sql,内部使用<br>
     * 2015-12-15 update：不进行大对象转换
     *
     * @param params
     * @param result
     * @param sql
     * @param connection
     *
     * @throws SQLException
     */
    private void executeSql(KVMap params, Result result, Sql sql, Connection connection, String sqlString) throws Exception {
        paramCheck(params, sql);
        if (Strings.isNotNullOrEmpty(sql.connectionId)) {
            DataSource dataSource = Springs.get(sql.connectionId);
            try (Connection conn = dataSource.getConnection();) { // 更改成使用Spring获取数据连接
                PreparedStatement executeHandler = sql.returnId ? conn.prepareStatement(Strings.isNotNullOrEmpty(sqlString) ? sqlString : sql.sql, PreparedStatement.RETURN_GENERATED_KEYS) : conn.prepareStatement(Strings.isNotNullOrEmpty(sqlString) ? sqlString : sql.sql);
                execute(params, result, sql, executeHandler);
            }
        }
        else {
            PreparedStatement executeHandler = sql.returnId ? connection.prepareStatement(Strings.isNotNullOrEmpty(sqlString) ? sqlString : sql.sql, PreparedStatement.RETURN_GENERATED_KEYS) : connection.prepareStatement(Strings.isNotNullOrEmpty(sqlString) ? sqlString : sql.sql);
            execute(params, result, sql, executeHandler);
        }
    }

    /**
     * 执行
     *
     * @param params
     * @param result
     * @param sql
     * @param executeHandler
     *
     * @throws Exception
     */
    private void execute(KVMap params, Result result, Sql sql, PreparedStatement executeHandler) throws Exception {
        if (sql.hasResultParam) {
            params = params == null ? new KVMap() : params;
            for (Sql.SqlResult sqlResult : sql.sqlResultParams) {
                DataSet ds = result.data.get(sqlResult.id);
                if (ds != null) {
                    params.addKV(sqlResult.placeHolder, sqlResult.isNonQueryResult ? ds.getNonQueryResult() : ds.get(sqlResult.index).get(sqlResult.column));
                }
            }
        }
        if (Arrays.isNotNullOrEmpty(sql.params)) {
            params = params == null ? new KVMap() : params;
            for (int index = 0; index < sql.params.size(); index++)
                executeHandler.setObject(index + 1, params.get(sql.params.get(index)));
        }
        if (sql.isQuery) {
            result.addData(sql.id, executeHandler.executeQuery());
        }
        else {
            int res = executeHandler.executeUpdate();
            if (sql.returnId) {
                ResultSet data = executeHandler.getGeneratedKeys();
                if (data != null && data.next()) {
                    res = data.getInt(1);
                    ResourceProcessor.closeResource(data);
                }
            }
            result.addData(sql.id, res);
        }
        LogProvider.getFrameworkInfoLogger().info("执行sql ： {}", executeHandler);
        System.out.println(executeHandler);
        ResourceProcessor.closeResource(executeHandler);
    }

    /**
     * 参数和sql检测
     *
     * @param params
     * @param sql
     *
     * @throws Exception
     */
    public void paramCheck(KVMap params, Sql sql) throws Exception {
        if (sql == null || !Strings.isNotNullOrEmpty(sql.sql)) {
            throw new Exception("配置文件出错，没有sql语句");
        }
        // if (sql.params != null && sql.params.size() > 0)
        // {
        // if (params == null)
        // throw new Exception("执行出错，参数为空");
        // if (params.size() != sql.realParams.size())
        // throw new Exception(String.format("执行出错，参数数量不符，参数为：%1$s", params));
        // }
    }
}
