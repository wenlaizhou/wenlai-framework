package cn.framework.db.init;

import cn.framework.core.container.AuthFilter;
import cn.framework.core.container.Context;
import cn.framework.core.container.InitProvider;
import cn.framework.core.utils.KVMap;
import com.alibaba.druid.support.http.StatViewServlet;

/**
 * project code
 * package cn.framework.db.init
 * create at 16-3-8 下午6:05
 * <p>
 * Druid监控初始化器
 *
 * @author wenlai
 */
public class DruidMonitorInitProvider implements InitProvider {

    /**
     * @param context 配置上下文
     *
     * @throws Exception
     */
    @Override
    public void init(Context context) throws Exception {
        //构建filter参数
        KVMap druidFilterParams = KVMap.newKvMap("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        druidFilterParams.addKV("sessionStatEnable", "true");
        druidFilterParams.addKV("profileEnable", "true");
        //bug fix use tomcat auth replace druid self auth
        //设置用户名密码
        //KVMap druidServletParams = KVMap.newKvMap("resetEnable", "true");
        //druidServletParams.addKV("loginUsername", TomcatContainer.getUsername());
        //druidServletParams.addKV("loginPassword", TomcatContainer.getPassword());
        context.addFilter("DruidWebStatFilter", "com.alibaba.druid.support.http.WebStatFilter", "/*", druidFilterParams);
        context.addServlet(null, "DruidStatView", StatViewServlet.class.getName(), "/druid/*", null, -1, true);
        context.addServlet(null, "druid-ui", DruidServlet.class.getName(), "/druid-ui", null, -1, true);
        context.addFilter("druid-auth", AuthFilter.class.getName(), "/druid/*");
    }
}
