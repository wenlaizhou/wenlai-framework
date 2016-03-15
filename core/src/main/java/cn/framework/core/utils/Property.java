/**
 * @项目名称: framework
 * @文件名称: Property.java
 * @Date: 2015年10月27日
 * @author: wenlai
 * @type: Property
 */
package cn.framework.core.utils;

import java.util.Properties;
import java.util.Set;
import cn.framework.core.log.LogProvider;

/**
 * 属性类<br>
 * 约定：1、dir为目录路径；2、path为文件路径；3、前缀为名称；4、dir结尾都没有/号<br>
 * 配置属性：<br>
 * 1、user.dir<br>
 * 2、conf.dir<br>
 * 3、log.dir<br>
 * 4、upk.enabled<br>
 * 5、upk.dir<br>
 * 6、mjar.path<br>
 * 7、tomcat.dir<br>
 * 
 * @author wenlai
 */
public final class Property {
    
    /**
     * 没有这个key
     */
    public static final String NO_THIS_KEY = "NO_THIS_KEY";
    
    /**
     * 程序工作路径
     */
    public static final String USER_DIR = "user.dir";
    
    /**
     * 配置文件路径
     */
    public static final String CONF_DIR = "conf.dir";
    
    /**
     * 日志路径
     */
    public static final String LOG_DIR = "log.dir";
    
    /**
     * tomcat工作目录
     */
    public static final String TOMCAT_DIR = "tomcat.dir";
    
    /**
     * jsp文件目录
     */
    public static final String JSP_DIR = "jsp.dir";
    
    /**
     * 是否解压
     */
    public static final String UNPACK_ENABLED = "upk.enabled";
    
    /**
     * 解压路径
     */
    public static final String UNPACK_DIR = "upk.dir";
    
    /**
     * 主jar路径
     */
    public static final String MAIN_JAR_PATH = "mjar.path";

    /**
     * 启动类名称
     */
    public static final String MAIN_CLASS = "main.class";
    
    /**
     * 判断是否存在这个key
     * 
     * @param key
     * @return
     */
    public static boolean exist(String key) {
        return System.getProperties().containsKey(key);
    }
    
    /**
     * 打印所有属性信息
     */
    public static void printAll() {
        Properties properties = System.getProperties();
        Set<Object> keys = properties.keySet();
        for (Object key : keys) {
            String formattedLine = Strings.format("${key} : ${value}", Pair.newPair("key", key), Pair.newPair("value", properties.get(key)));
            System.out.println(formattedLine);
            // LogProvider.getFrameworkInfoLogger().info(formattedLine);
        }
    }
    
    /**
     * 获取系统属性
     * 
     * @param key
     * @return
     */
    public static String get(String key) {
        try {
            String result = System.getProperty(key);
            return Strings.isNotNullOrEmpty(result) ? result : Strings.EMPTY;
        }
        catch (Exception x) {
            LogProvider.getFrameworkErrorLogger().error(x.getMessage(), x);
        }
        return Strings.EMPTY;
    }
    
    /**
     * 获取系统属性
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public static String get(String key, String defaultValue) {
        try {
            String result = System.getProperty(key);
            return Strings.isNotNullOrEmpty(result) ? result : defaultValue;
        }
        catch (Exception x) {
            LogProvider.getFrameworkErrorLogger().error(x.getMessage(), x);
        }
        return defaultValue;
    }
    
    /**
     * 设置系统属性
     * 
     * @param key
     * @param value
     */
    public static void set(String key, String value) {
        if (Strings.isNotNullOrEmpty(key) && Strings.isNotNullOrEmpty(value)) {
            System.setProperty(key, value);
            System.out.println(key + " : " + value);
        }
    }
    
}
