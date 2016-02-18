/**
 * @项目名称: db
 * @文件名称: Boot.java
 * @Date: 2015年11月18日
 * @author: wenlai
 * @type: Boot
 */
package cn.framework.db;

import cn.framework.core.container.Context;
import cn.framework.core.utils.KVMap;
import cn.framework.core.utils.Xmls;
import cn.framework.db.init.DbInitProvider;
import cn.framework.db.sql.DataSet;
import cn.framework.db.sql.Procedure;
import cn.framework.db.sql.Result;

/**
 * @author wenlai
 *
 */
public class Boot {
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        DbInitProvider provider = new DbInitProvider();
        provider.init(Context.buildByConf(Xmls.node("server.xml")));
        Result result = Procedure.getProcedure("index/select").process(new KVMap("id", 1), new KVMap("table", "candidate"));
        DataSet select = result.data.get("select");
        System.out.println(select.selectSingle());
    }
}
