package cn.framework.core.utils;

import static cn.framework.core.utils.Property.*;

/**
 * 项目帮助类
 * 
 * @author wenlai
 */
public final class Projects {
    
    /**
     * 项目路径<br>
     * 没有 / 结尾
     */
    public static final String PROJECT_DIR = get(USER_DIR, "");
    
    /**
     * 类路径
     */
    // public static final String CLASS_PATH = get(CLASS_PATH);
    
    public static final String MAIN_CLASS = get(Property.MAIN_CLASS, "cn.framework.core.Bootstrap");
    
    /**
     * 是否解压
     */
    public static final boolean UNPACK_ENABLED = Boolean.parseBoolean(get(Property.UNPACK_ENABLED, "true"));
    
    /**
     * 解压路径
     */
    public static final String UNPACK_DIR = get(Property.UNPACK_DIR, PROJECT_DIR + "/target/classes");
    
    /**
     * 当前运行的jar包路径
     */
    public static final String MAIN_JAR_PATH = get(Property.MAIN_JAR_PATH);
    
    /**
     * 配置路径<br>
     * 没有 / 结尾
     */
    public static final String CONF_DIR = get(Property.CONF_DIR, PROJECT_DIR);
    
    /**
     * 日志路径
     */
    public static final String LOG_DIR = get(Property.LOG_DIR, PROJECT_DIR + "/log");
    
    /**
     * jsp文件路径
     */
    public static final String JSP_DIR = get(Property.JSP_DIR, UNPACK_DIR);
    
    /**
     * tomcat工作目录
     */
    public static final String TOMCAT_DIR;
    
    /**
     * 设置tomcat工作目录
     */
    static {
        if (!exist(Property.TOMCAT_DIR))
            set(Property.TOMCAT_DIR, Projects.PROJECT_DIR + "/tomcat-work");
        TOMCAT_DIR = get(Property.TOMCAT_DIR);
    }
    
    /**
     * 设置属性
     */
    static {
        if (!exist(Property.USER_DIR))
            Property.set(Property.USER_DIR, PROJECT_DIR);
        if (!exist(Property.CONF_DIR))
            Property.set(Property.CONF_DIR, CONF_DIR);
        if (!exist(Property.JSP_DIR))
            Property.set(Property.JSP_DIR, JSP_DIR);
        if (!exist(Property.LOG_DIR))
            Property.set(Property.LOG_DIR, LOG_DIR);
        if (!exist(Property.MAIN_JAR_PATH))
            Property.set(Property.MAIN_JAR_PATH, MAIN_JAR_PATH);
        if (!exist(Property.TOMCAT_DIR))
            Property.set(Property.TOMCAT_DIR, TOMCAT_DIR);
        if (!exist(Property.UNPACK_ENABLED))
            Property.set(Property.UNPACK_ENABLED, Boolean.toString(UNPACK_ENABLED).toLowerCase());
        if (!exist(Property.UNPACK_DIR))
            Property.set(Property.UNPACK_DIR, UNPACK_DIR);
        if (!exist(Property.MAIN_CLASS))
            Property.set(Property.MAIN_CLASS, MAIN_CLASS);
    }
    
    /**
     * 打印所有预设属性
     */
    public static void printAll() {
        StringBuilder result = new StringBuilder("PROJECT_PATH : ");
        result.append(PROJECT_DIR).append("\n");
        result.append("UNPACK_ENABLED : ").append(UNPACK_ENABLED).append("\n");
        result.append("UNPACK_PATH : ").append(UNPACK_DIR).append("\n");
        result.append("MAIN_JAR_PATH : ").append(MAIN_JAR_PATH).append("\n");
        result.append("CONF_PATH : ").append(CONF_DIR).append("\n");
        result.append("LOG_PATH : ").append(LOG_DIR).append("\n");
        result.append("TOMCAT_DIR : ").append(TOMCAT_DIR);
        System.out.println(result.toString());
    }
}
