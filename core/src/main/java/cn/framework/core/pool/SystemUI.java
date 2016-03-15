/**
 * @项目名称: core
 * @文件名称: SystemUI.java
 * @Date: 2016年2月19日
 * @author: wenlai
 * @type: SystemUI
 */
package cn.framework.core.pool;

import cn.framework.core.container.TomcatContainer;
import cn.framework.core.log.LogProvider;
import cn.framework.core.utils.Files;
import cn.framework.core.utils.Projects;
import cn.framework.core.utils.Strings;
import org.hyperic.sigar.Sigar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;

import static cn.framework.core.utils.Pair.newPair;

/**
 * @author wenlai
 */
public class SystemUI extends HttpServlet {

    /**
     * 模板
     */
    public static final String TEMPLATE = "<tr><th scope=\"row\">${id}</th><td>${name}</td><td>${value}</td></tr>";

    /**
     * 模板
     */
    public static final String TABLE_TOTAL = "<table class=\"table table-striped\"><thead><tr><th>#</th><th>系统项</th><th>值</th></tr></thead><tbody><content></content></tbody></table>";

    /**
     * 模板
     */
    public static final String SYSTEM_INFO_UI = Files.readResourceText("cn/framework/core/pool/system.html");

    /**
     * long
     */
    private static final long serialVersionUID = -592302644015329197L;

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!TomcatContainer.basicAuth(req, resp)) {
            return;
        }
        resp.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8");
        PrintWriter outPrinter = resp.getWriter();
        try {
            Sigar sigar = new Sigar();
            sigar.enableLogging(true);
            StringBuilder html = new StringBuilder();
            html.append(Strings.format(TEMPLATE, newPair("id", 1), newPair("name", "cpu"), newPair("value", String.format("%.2f%%", (1 - sigar.getCpuPerc().getIdle()) * 100))));
            html.append(Strings.format(TEMPLATE, newPair("id", 2), newPair("name", "memory"), newPair("value", String.format("%.2f%%", sigar.getMem().getUsedPercent()))));
            html.append(Strings.format(TEMPLATE, newPair("id", 3), newPair("name", "disk"), newPair("value", String.format("%.2f%%", sigar.getFileSystemUsage(Projects.PROJECT_DIR).getUsePercent()))));
            html.append(Strings.format(TEMPLATE, newPair("id", 4), newPair("name", "tcp-listens"), newPair("value", sigar.getNetStat().getTcpListen())));
            html.append(Strings.format(TEMPLATE, newPair("id", 5), newPair("name", "tcp-estabs"), newPair("value", sigar.getTcp().getCurrEstab())));
            html.append(Strings.format(TEMPLATE, newPair("id", 6), newPair("name", "process-cpu"), newPair("value", String.format("%.2f%%", sigar.getProcCpu(sigar.getPid()).getPercent()))));
            html.append(Strings.format(TEMPLATE, newPair("id", 7), newPair("name", "process-mem"), newPair("value", String.format("%sMB", sigar.getProcMem(sigar.getPid()).getSize() / 1024 / 1024))));
            sigar.close();
            outPrinter.append(SYSTEM_INFO_UI.replace("${total}", TABLE_TOTAL).replace("<content></content>", html));
        }
        catch (Throwable x) {
            LogProvider.getFrameworkErrorLogger().error(x.getMessage(), x);
            StringBuilder html = new StringBuilder();
            html.append(Strings.format(TEMPLATE, newPair("id", 1), newPair("name", "cpu"), newPair("value", String.format("%.2f%%", ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage()))));
            html.append(Strings.format(TEMPLATE, newPair("id", 2), newPair("name", "heap-memory"), newPair("value", String.format("%1sMB", ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024 / 1024))));
            html.append(Strings.format(TEMPLATE, newPair("id", 3), newPair("name", "loaded-class-count"), newPair("value", String.format("%1$s", ManagementFactory.getClassLoadingMXBean().getLoadedClassCount()))));
            outPrinter.append(SYSTEM_INFO_UI.replace("${total}", TABLE_TOTAL).replace("<content></content>", html));
        }
    }
}
