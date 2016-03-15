package cn.framework.core.utils;

/**
 * project code
 * package cn.framework.core.utils
 * create at 16-3-8 下午1:42
 *
 * @author wenlai
 */

import cn.framework.core.log.LogProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * bean工具类
 */
public final class Springs {

    /**
     * context实例
     */
    private volatile static ApplicationContext context;

    /**
     * 是否被初始化过
     */
    private static volatile boolean INITED = false;

    /**
     * 获取bean实例
     *
     * @param beanId 名称
     *
     * @return 获取bean实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String beanId) {
        try {
            if (Springs.context != null) {
                Object bean = Springs.context.getBean(beanId);
                if (bean != null) {
                    return (T) bean;
                }
                else {
                    LogProvider.getFrameworkInfoLogger().info("bean {} is not found", beanId);
                    return null;
                }
            }
            else {
                LogProvider.getFrameworkErrorLogger().error("Springs context is not initialized successed !");
            }
        }
        catch (Exception x) {
            Exceptions.processException(x);
        }
        return null;
    }


    /**
     * 获取Spring上下文
     *
     * @return
     */
    public static final ApplicationContext getContext() {
        return Springs.context;
    }

    /**
     * 获取上下文实体
     */
    public static class ContextRegister implements ServletContextListener {

        @Override
        public void contextInitialized(ServletContextEvent servletContextEvent) {
            try {
                if (Springs.context == null && !INITED) {
                    Object context = servletContextEvent.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
                    if (context != null) {
                        Springs.context = (ApplicationContext) context;
                    }
                }
            }
            catch (Exception x) {
                Exceptions.processException(x);
            }
            finally {
                INITED = true;
            }
        }

        @Override
        public void contextDestroyed(ServletContextEvent servletContextEvent) {

        }
    }
}
