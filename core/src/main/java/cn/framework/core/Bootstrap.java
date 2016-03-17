/**
 * @项目名称: core
 * @文件名称: Bootstrap.java
 * @Date: 2015年11月18日
 * @author: wenlai
 * @type: Bootstrap
 */
package cn.framework.core;


import cn.framework.core.container.TomcatContainer;
import cn.framework.core.utils.Projects;

/**
 * @author wenlai
 */
public class Bootstrap {

    /**
     * 应用入口<br>
     * core 测试使用
     * java.util.logging.config.file<br>
     * java.util.logging.manager
     *
     * @param args 输入参数
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        TomcatContainer container = new TomcatContainer(Projects.CONF_DIR + "/server.xml");
        container.init();
        container.start();
    }

}
