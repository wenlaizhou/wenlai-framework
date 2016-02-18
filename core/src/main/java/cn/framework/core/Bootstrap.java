/**
 * @项目名称: core
 * @文件名称: Bootstrap.java
 * @Date: 2015年11月18日
 * @author: wenlai
 * @type: Bootstrap
 */
package cn.framework.core;

import cn.framework.core.utils.Arrays;
import cn.framework.core.utils.Packages;
import cn.framework.core.utils.Reflects;

/**
 * @author wenlai
 *
 */
public class Bootstrap {
    
    /**
     * 应用入口<br>
     * core 测试使用
     * java.util.logging.config.file<br>
     * java.util.logging.manager
     * 
     * @param args
     * @throws Exception
     */
    @SuppressWarnings("restriction")
    public static void main(String[] args) throws Exception {
        
        sun.misc.Unsafe unsafe = sun.misc.Unsafe.getUnsafe();
        unsafe.compareAndSwapInt("", 10, 10, 10);
        
        
        hellos(Reflects.invoke("cn.framework.core.Bootstrap", "hello", null));
        
        // Property.set(Property.MAIN_JAR_PATH, "E:/main.jar");
        // Property.set(Property.UNPACK_ENABLED, "false");
        System.out.println(Arrays.print(Packages.scanClasses("cn.framework.core.async")));
        System.out.println(Class.forName("cn.framework.core.async.AsyncProvider$AsyncHandler"));
        // Property.printAll();
        // Projects.printAll();
        // String[] files = Packages.scan("cn.framework.core");
        // System.out.println(Arrays.print(files));
        //
        // System.setProperty("java.util.logging.config.file", "log.properties");
        // Logger.getLogger("jdk").info("wenlai is logging!");
        // LogManager.getLogManager().getLogger("jdk").info("wenlai is logging!");
        // System.out.println(Strings.format("${h}, ${s}", newPair("h", 1), newPair("s", new Date())));
        // System.out.println(String.format("%1$tF %1$tT.%1$tL", new Date()));
        // System.out.println(MessageFormat.format("{0}, {1}, {2,date,long}, {2,time,long}", 1, 2, new Date()));
        // TomcatContainer container = new TomcatContainer(args[0]);
        // container.init();
        // container.start();
    }
    
    public static void hello() {
        
    }
    
    public static void hellos(Object data) {
        
        System.out.println(data);
    }
}
