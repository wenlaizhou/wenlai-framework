/**
 * @项目名称: framework2
 * @文件名称: ServiceRegister.java
 * @Date: 2015年10月9日
 */
package cn.framework.rest.register;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import cn.framework.core.log.LogProvider;
import cn.framework.core.utils.Projects;

/**
 * @author wenlai
 */
public class ServiceRegister extends ResourceConfig { // PackagesResourceConfig

    /**
     * 注入配置信息
     */
    public ServiceRegister() {
        try {
            // Thread.currentThread().setContextClassLoader(Class.forName(Projects.MAIN_CLASS).getClassLoader());
            this.setClassLoader(Class.forName(Projects.MAIN_CLASS).getClassLoader());
            this.register(MultiPartFeature.class);
            this.register(JacksonFeature.class);
            // if (PACKAGES != null)
            // this.packages(PACKAGES);
            if (PACKAGES != null && PACKAGES.length > 0) {
                for (String pkg : PACKAGES) {
                    String[] classes = cn.framework.core.utils.Packages.scanClasses(pkg);
                    if (classes != null && classes.length > 0)
                        for (String className : classes)
                            this.register(Class.forName(className));
                }
            }
        }
        catch (Throwable x) {
            LogProvider.getFrameworkErrorLogger().error(x.getLocalizedMessage(), x);
        }
    }
    
    private static String[] PACKAGES = null;
}
