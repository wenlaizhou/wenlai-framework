package cn.framework.db.init;

import cn.framework.core.utils.Springs;
import cn.framework.core.utils.Exceptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * project code
 * package cn.framework.db.init
 * create at 16-3-8 下午11:55
 *
 * @author wenlai
 */
public class DruidServlet extends HttpServlet {

    /**
     * 数据源
     */
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        dataSource = Springs.get("dataSource");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /**
         * 基于代码创建
         * dataSource = new DruidDataSource(); dataSource.setDriverClassName("com.mysql.jdbc.Driver");
         * dataSource.setUsername("root"); dataSource.setPassword("11111111");
         * dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/jspdemo");
         * dataSource.setInitialSize(5); dataSource.setMinIdle(1);
         * dataSource.setMaxActive(10);
         * dataSource.setFilters("stat");// 启用监控统计功能
         * dataSource.setPoolPreparedStatements(false); // for mysql
         */

        DataSource ds = Springs.get("dataSource");
        resp.getWriter().append("datasource found !");
        try (Connection connection = ds.getConnection(); PreparedStatement statement = connection.prepareStatement("select * from user;");) {
            resp.getWriter().append("connected !");
            ResultSet rs = statement.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    resp.getWriter().write(rs.getObject(1).toString());
                }
            }
        }
        catch (Exception x) {
            Exceptions.processException(x);
        }
    }

}
