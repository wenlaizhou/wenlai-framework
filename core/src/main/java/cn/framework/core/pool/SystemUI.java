/**
 * @项目名称: core
 * @文件名称: SystemUI.java
 * @Date: 2016年2月19日
 * @author: wenlai
 * @type: SystemUI
 */
package cn.framework.core.pool;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hyperic.sigar.Sigar;
import cn.framework.core.container.TomcatContainer;
import cn.framework.core.log.LogProvider;
import cn.framework.core.utils.Files;
import cn.framework.core.utils.Projects;
import static cn.framework.core.utils.Pair.*;
import cn.framework.core.utils.Strings;

/**
 * @author wenlai
 *
 */
public class SystemUI extends HttpServlet {
    
    /**
     * long
     */
    private static final long serialVersionUID = -592302644015329197L;
    
    /*
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!TomcatContainer.basicAuth(req, resp))
            return;
        resp.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8");
        PrintWriter outPrinter = resp.getWriter();
        try {
            Sigar sigar = new Sigar();
            sigar.enableLogging(true);
            StringBuilder html = new StringBuilder();
            // 1、cpu占用率
            // 2、内存占用率
            // 3、硬盘剩余量
            // 4、tcp监听数量
            // 5、tcp连接数
            // 5、当前进程占用cpu率
            // 6、当前进程占用内存数
            html.append(Strings.format(TEMPLATE, newPair("id", 1), newPair("name", "cpu"), newPair("value", String.format("%.2f%%", (1 - sigar.getCpuPerc().getIdle()) * 100))));
            html.append(Strings.format(TEMPLATE, newPair("id", 2), newPair("name", "memory"), newPair("value", String.format("%.2f%%", sigar.getMem().getUsedPercent()))));
            html.append(Strings.format(TEMPLATE, newPair("id", 3), newPair("name", "disk"), newPair("value", String.format("%.2f%%", sigar.getFileSystemUsage(Projects.PROJECT_DIR).getUsePercent()))));
            html.append(Strings.format(TEMPLATE, newPair("id", 4), newPair("name", "tcp-listens"), newPair("value", sigar.getTcp().getActiveOpens())));
            html.append(Strings.format(TEMPLATE, newPair("id", 5), newPair("name", "tcp-estabs"), newPair("value", sigar.getTcp().getCurrEstab())));
            html.append(Strings.format(TEMPLATE, newPair("id", 6), newPair("name", "process-cpu"), newPair("value", String.format("%.2f%%", sigar.getProcCpu(sigar.getPid()).getPercent()))));
            html.append(Strings.format(TEMPLATE, newPair("id", 7), newPair("name", "process-mem"), newPair("value", sigar.getProcMem(sigar.getPid()).getSize())));
            sigar.close();
            outPrinter.append(SYSTEM_INFO_UI.replace("${total}", TABLE_TOTAL).replace("<content></content>", html));
        }
        catch (Throwable x) {
            LogProvider.getFrameworkErrorLogger().error(x.getMessage(), x);
            outPrinter.append(SYSTEM_INFO_UI.replace("${total}", "<h3>系统未部署完整，请查看文档</h3>"));
        }
    }
    
    public static final String TEMPLATE = "<tr><th scope=\"row\">${id}</th><td>${name}</td><td>${value}</td></tr>";
    
    public static final String TABLE_TOTAL = "<table class=\"table table-striped\"><thead><tr><th>#</th><th>系统项</th><th>值</th></tr></thead><tbody><content></content></tbody></table>";
    
    public static final String SYSTEM_INFO_UI = Files.readResourceText("cn/framework/core/pool/system.html");
    
}
