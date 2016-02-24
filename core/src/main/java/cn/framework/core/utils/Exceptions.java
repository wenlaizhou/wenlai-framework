/**
 * @项目名称: core
 * @文件名称: Exceptions.java
 * @Date: 2016年2月24日
 * @author: wenlai
 * @type: Exceptions
 */
package cn.framework.core.utils;

import static cn.framework.core.log.LogProvider.*;

/**
 * @author wenlai
 *
 */
public final class Exceptions {
    
    /**
     * 对异常进行处理
     * 
     * @param exception
     */
    public static void processException(Throwable exception) {
        if (exception != null) {
            getFrameworkErrorLogger().error(exception.getMessage(), exception);
        }
    }
}
