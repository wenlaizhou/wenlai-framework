package cn.framework.core.log;

import java.io.ByteArrayInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import cn.framework.core.utils.Files;

/**
 * 日志提供类
 * 
 * @author wenlai
 */
public final class LogProvider {
    
    /**
     * 根据logger名称，获取logger
     * 
     * @param loggerName
     * @return
     */
    public static Logger getLogger(String loggerName) {
        return LogManager.getLogger(loggerName);
    }
    
    /**
     * 获取error日志<br>
     * 方法名称取的很长原因：不建议其他系统使用framework日志<br>
     * 尽量framework专用日志
     * 
     * @return
     */
    public static Logger getFrameworkErrorLogger() {
        return FRAMEWORK_ERROR;
    }
    
    /**
     * 获取info日志<br>
     * 方法名称取的很长原因：不建议其他系统使用framework日志<br>
     * 尽量framework专用日志
     * 
     * @return
     */
    public static Logger getFrameworkInfoLogger() {
        return FRAMEWORK_INFO;
    }
    
    /**
     * framework-infolog名称
     */
    private final static String FRAMEWORK_INFO_LOGNAME = "cn.framework.info";
    
    /**
     * framework-errorlog名称
     */
    private final static String FRAMEWORK_ERROR_LOGNAME = "cn.framework.error";
    
    /**
     * framework-errorlog
     */
    private static Logger FRAMEWORK_ERROR = LogManager.getLogger(FRAMEWORK_ERROR_LOGNAME);
    
    /**
     * framework-infolog
     */
    private static Logger FRAMEWORK_INFO = LogManager.getLogger(FRAMEWORK_INFO_LOGNAME);
    
    /**
     * 根据配置文件路径初始化log4j
     * 
     * @param confOrPath
     */
    public static void init(String confOrPath) {
        try {
            if (Files.exist(confOrPath)) {
                System.out.println("get log4j xml");
                ConfigurationSource source = new ConfigurationSource(new ByteArrayInputStream(Files.read(confOrPath).getBytes("utf-8")));
                System.out.println(Configurator.initialize(null, source));
            }
            else {
                ConfigurationSource source = new ConfigurationSource(new ByteArrayInputStream(confOrPath.getBytes("utf-8")));
                System.out.println(Configurator.initialize(null, source));
            }
            System.out.println("log init done");
            // System.setErr(new PrintStream(new Log4jErrorOut()));
            // System.setOut(new PrintStream(new Log4jStdOut()));
        }
        catch (Exception x) {
            getFrameworkErrorLogger().error(x.getMessage(), x);
            x.printStackTrace();
            rawLog(x.getMessage());
        }
    }
    
    /**
     * fatal级别错误日志
     * 
     * @param message
     */
    public static void rawLog(String message) {
        System.err.println(message);
    }
}
