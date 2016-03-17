/**
 * @项目名称: core
 * @文件名称: Exceptions.java
 * @Date: 2016年2月24日
 * @author: wenlai
 * @type: Exceptions
 */
package cn.framework.core.utils;

import cn.framework.core.log.FrameworkLogger;
import cn.framework.core.log.GlobalLogProcessor;

/**
 * 统一framework异常处理<br>
 * 可以自定义日志处理器bean-name is globalLogFilter
 *
 * @author wenlai
 */
public final class Exceptions {

    private final static GlobalLogProcessor FRAMEWORK_LOG_HANDLER = Springs.getContext() != null && Springs.get("globalLogProcessor") != null ? Springs.get("globalLogProcessor") : new FrameworkLogger();

    /**
     * 对异常进行处理
     *
     * @param exception exe
     */
    public static void processException(Throwable exception) {
        FRAMEWORK_LOG_HANDLER.processException(exception);
    }

    /**
     * 获取日志处理器
     *
     * @return
     */
    public static GlobalLogProcessor logProcessor() {
        return FRAMEWORK_LOG_HANDLER;
    }

}
