package cn.framework.core.container;

import cn.framework.core.pool.SystemUI;
import cn.framework.core.pool.ThreadPoolUI;
import cn.framework.core.pool.ThreadUI;
import cn.framework.core.pool.TomcatUI;
import cn.framework.core.utils.KVMap;
import org.apache.catalina.manager.StatusManagerServlet;

/**
 * project code
 * package cn.framework.core.container
 * create at 16-3-9 下午3:23
 *
 * @author wenlai
 */
public class MonitorInitProvider implements InitProvider {

    /**
     * 初始化
     *
     * @param context 配置上下文
     *
     * @throws Exception
     */
    @Override
    public synchronized void init(Context context) throws Exception {
        /**
         * add custom monitor
         */
        context.addServlet("pool-ui", "cn.framework.core.pool.PoolUI", "/pool-ui");
        context.addServlet("thread-pool-ui", ThreadPoolUI.class.getName(), "/thread-pool-ui");
        context.addServlet("thread-ui", ThreadUI.class.getName(), "/thread-ui");
        context.addServlet("system-ui", SystemUI.class.getName(), "/system-ui");
        context.addServlet("container-ui", TomcatUI.class.getName(), "/tomcat-manager");
        /**
         * add tomcat monitor
         */
        context.addServlet(null, "manager-status", StatusManagerServlet.class.getName(), "/text/*", KVMap.newKvMap("debug", 0), -1, true);
        context.addServlet(null, "manager-cmd", StatusManagerServlet.class.getName(), "/status/*", KVMap.newKvMap("debug", 0), -1, true);
        context.addFilter("manager-status-auth", AuthFilter.class.getName(), "/text/*");
        context.addFilter("manager-cmd-auth", AuthFilter.class.getName(), "/status/*");
    }
}
