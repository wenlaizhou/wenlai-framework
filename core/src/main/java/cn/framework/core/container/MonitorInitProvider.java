package cn.framework.core.container;

import cn.framework.core.pool.SystemUI;
import cn.framework.core.pool.ThreadPoolUI;
import cn.framework.core.pool.ThreadUI;

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
        context.addServlet("pool-ui", "cn.framework.core.pool.PoolUI", "/pool-ui");
        context.addServlet("thread-pool-ui", ThreadPoolUI.class.getName(), "/thread-pool-ui");
        context.addServlet("thread-ui", ThreadUI.class.getName(), "/thread-ui");
        context.addServlet("system-ui", SystemUI.class.getName(), "/system-ui");
    }
}
