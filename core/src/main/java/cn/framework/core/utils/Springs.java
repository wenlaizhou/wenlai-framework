package cn.framework.core.utils;

/**
 * project code
 * package cn.framework.core.utils
 * create at 16-3-8 下午1:42
 *
 * @author wenlai
 */

import cn.framework.core.container.FrameworkSpringRegister;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * bean工具类<br>
 * 统一设置成WebApplicationContext
 */
public final class Springs {

    /**
     * scope - singleton
     */
    public final static String SCOPE_SINGLETON = "singleton";

    /**
     * scope - prototype
     */
    public final static String SCOPE_PROTOTYPE = "prototype";

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
                    Exceptions.logProcessor().logger().info("bean {} is not found", beanId);
                    return null;
                }
            }
            else {
                Exceptions.logProcessor().logger().error("Springs context is not initialized successed !");
            }
        }
        catch (Exception x) {
            Exceptions.processException(x);
        }
        return null;
    }


    /**
     * 获取bean实例
     *
     * @param beanId     名称
     * @param returnType 返回类型
     * @param <T>        类型
     *
     * @return
     */
    public static <T> T get(String beanId, Class<T> returnType) {
        try {
            if (Springs.context != null) {
                T bean = Springs.context.getBean(beanId, returnType);
                if (bean != null) {
                    return bean;
                }
                else {
                    Exceptions.logProcessor().logger().info("bean {} is not found", beanId);
                    return null;
                }
            }
            else {
                Exceptions.logProcessor().logger().error("Springs context is not initialized successed !");
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
     * 设置全局上下文环境
     *
     * @param context 上下文
     */
    public static synchronized void setContext(ApplicationContext context) {
        Springs.context = context;
        System.out.println("spring context reset done!");
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
                    if (context != null && context instanceof ApplicationContext) {
                        Springs.context = (ApplicationContext) context;
                    }
                }
            }
            catch (Exception x) {
                Exceptions.processException(x);
            }
            finally {
                FrameworkSpringRegister.context.getId();
                INITED = true;
            }
        }

        @Override
        public void contextDestroyed(ServletContextEvent servletContextEvent) {

        }
    }
}
