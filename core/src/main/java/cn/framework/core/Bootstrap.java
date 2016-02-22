/**
 * @项目名称: core
 * @文件名称: Bootstrap.java
 * @Date: 2015年11月18日
 * @author: wenlai
 * @type: Bootstrap
 */
package cn.framework.core;

import org.hyperic.sigar.Sigar;
import cn.framework.core.utils.Number;

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
    public static void main(String[] args) throws Exception {
        // -Djava.library.path="./sigarlib"
        Sigar sigar = new Sigar();
        System.out.println(Number.roundUp(10.223123123, 2));
        System.out.println(String.format("%.2f%%", sigar.getCpuPerc().getIdle() * 100));
        System.out.println(sigar.getProcMem(sigar.getPid()).getSize() / 1024 / 1024);
        // System.out.println((1 - + "%");
        
        // System.out.println(sigar.getCpuPerc().getIdle());
        // System.out.println(sigar.getMem().getUsedPercent());
        // System.out.println(sigar.getCpu().toMap());
        // System.out.println(sigar.getTcp().toMap());
        // System.out.println(sigar.getNetStat());
        // System.out.println(sigar.getProcStat());
        // System.out.println(sigar.getProcMem(sigar.getPid()));
        // System.out.println(sigar.getProcCpu(sigar.getPid()));
        // System.out.println(sigar.getProcState(sigar.getPid()));
        // System.out.println(sigar.getCpuPerc().);
        // System.out.println(sigar.getTcp().toMap());
        // // System.out.println(sigar.getCpu());
        // // System.out.println(sigar.getMem());
        // System.out.println(sigar.getSwap().toMap());
        // System.out.println(sigar.getDirUsage("."));
        // System.out.println(sigar.getNetInfo());
        // System.out.println(sigar.getProcStat().toMap());
        // System.out.println(sigar.getTcp());
        // System.out.println(sigar.getProcCpu(sigar.getPid()));
        // sun.misc.Unsafe unsafe = sun.misc.Unsafe.getUnsafe();
        // unsafe.compareAndSwapInt("", 10, 10, 10);
        //
        // hellos(Reflects.invoke("cn.framework.core.Bootstrap", "hello", null));
        //
        // // Property.set(Property.MAIN_JAR_PATH, "E:/main.jar");
        // // Property.set(Property.UNPACK_ENABLED, "false");
        // System.out.println(Arrays.print(Packages.scanClasses("cn.framework.core.async")));
        // System.out.println(Class.forName("cn.framework.core.async.AsyncProvider$AsyncHandler"));
        // // Property.printAll();
        // // Projects.printAll();
        // // String[] files = Packages.scan("cn.framework.core");
        // // System.out.println(Arrays.print(files));
        // //
        // // System.setProperty("java.util.logging.config.file", "log.properties");
        // // Logger.getLogger("jdk").info("wenlai is logging!");
        // // LogManager.getLogManager().getLogger("jdk").info("wenlai is logging!");
        // // System.out.println(Strings.format("${h}, ${s}", newPair("h", 1), newPair("s", new Date())));
        // // System.out.println(String.format("%1$tF %1$tT.%1$tL", new Date()));
        // // System.out.println(MessageFormat.format("{0}, {1}, {2,date,long}, {2,time,long}", 1, 2, new Date()));
        // // TomcatContainer container = new TomcatContainer(args[0]);
        // // container.init();
        // // container.start();
    }
    
    public static void hello() {
        
    }
    
    public static void hellos(Object data) {
        
        System.out.println(data);
    }
}
