package cn.framework.db.init;

import cn.framework.core.container.Context;
import cn.framework.core.container.InitProvider;
import cn.framework.core.utils.KVMap;

/**
 * project code
 * package cn.framework.db.init
 * create at 16-3-8 下午6:05
 *
 * @author wenlai
 */
public class DruidInitProvider implements InitProvider {

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
        //设置用户名密码
        KVMap druidServletParams = KVMap.newKvMap("resetEnable", "true");
        druidServletParams.addKV("loginUsername", "wenlai");
        druidServletParams.addKV("loginPassword", "wenlai");
        context.addFilter("DruidWebStatFilter", "com.alibaba.druid.support.http.WebStatFilter", "/*", druidFilterParams);
        context.addServlet("DruidStatView", "com.alibaba.druid.support.http.StatViewServlet", "/druid/*", druidServletParams);
        context.addServlet("test", DruidServlet.class.getName(), "/test");
    }
}
