/**
 * @项目名称: db
 * @文件名称: Boot.java
 * @Date: 2015年11月18日
 * @author: wenlai
 * @type: Boot
 */
package cn.framework.db;

import cn.framework.core.container.TomcatContainer;
import cn.framework.core.utils.Projects;

/**
 * @author wenlai
 */
public class Boot {

    //    private static DataSource dataSourceInstance = null;

    /**
     * 应用入口，测试使用
     *
     * @param args 启动参数
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //        Springs b = Springs.getInstance("/Users/junrayz/Documents/code/db/spring.xml");
        //        System.out.println(b);
        //        DruidDataSource ds = Springs.get("dataSource");
        //        System.out.println(ds);
        //        ApplicationContext context = new FileSystemXmlApplicationContext("file:/Users/junrayz/Documents/code/db/spring.xml");
        //        DruidDataSource ds = context.getBean("dataSource", DruidDataSource.class);
        //        PreparedStatement ps = ds.getConnection().prepareStatement("select now();");
        //        ResultSet rs = ps.executeQuery();
        //        while(rs.next()) {
        //            System.out.println(rs.getObject(1));
        //        }

        TomcatContainer tomcat = new TomcatContainer(Projects.CONF_DIR + "/druid.xml");
        tomcat.init();
        tomcat.start();
    }

    //    public static DataSource getDatasource() {
    //        if (dataSourceInstance != null) {
    //            return dataSourceInstance;
    //        }
    //        synchronized (Boot.class) {
    //            if (dataSourceInstance == null) {
    //                try {
    //                    Property.set("url", "jdbc:mysql://101.201.211.1:3336/hr");
    //                    Property.set("username", "root");
    //                    Property.set("password", "Etcp2012@Etcp2012");
    //                    Property.set("filters", "stat,log4j");
    //                    DruidDataSource dataSource = new DruidDataSource();
    //                    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    //                    dataSource.setUsername("root");
    //                    dataSource.setPassword("Etcp2012@Etcp2012");
    //                    dataSource.setUrl("jdbc:mysql://101.201.211.1:3336/hr");
    //                    dataSource.setInitialSize(5);
    //                    dataSource.setMinIdle(1);
    //                    dataSource.setMaxActive(10);
    //                    dataSource.setFilters("stat.log4j");// 启用监控统计功能
    //                    dataSource.setPoolPreparedStatements(false); // for mysql
    //                    dataSourceInstance = dataSource;
    //                }
    //                catch (Exception x) {
    //                    Exceptions.processException(x);
    //                }
    //            }
    //        }
    //        return dataSourceInstance;
    //    }

    //    public static class Myinitor implements WebApplicationInitializer {
    //
    //        @Override
    //        public void onStartup(ServletContext servletContext) throws ServletException {
    //            System.out.println("init on start up");
    //            AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    //            context.setConfigLocations("file:" + Projects.CONF_DIR + "/spring.xml");
    //            System.out.println(context.getBean("dataSource"));
    //        }
    //
    //
    ////        public void init() {
    ////            System.out.println("init");
    ////        }
    //
    //        public static void init () {
    //            System.out.println("init static");
    //        }
    //
    //    }
}
