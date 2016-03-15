/**
 * @项目名称: core
 * @文件名称: ThreadPoolUI.java
 * @Date: 2016年1月28日
 * @author: wenlai
 * @type: ThreadPoolUI
 */
package cn.framework.core.pool;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.framework.core.async.AsyncProvider;
import cn.framework.core.container.TomcatContainer;
import cn.framework.core.pool.ThreadPool.Watcher;
import cn.framework.core.utils.Files;

/**
 * @author wenlai
 *
 */
public class ThreadPoolUI extends HttpServlet {
    
    /**
     * long
     */
    private static final long serialVersionUID = -1159964741711171321L;
    
    /*
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!TomcatContainer.basicAuth(req, resp))
            return;
        resp.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8");
        PrintWriter outPrinter = resp.getWriter();
        StringBuilder html = new StringBuilder();
        Watcher watcher = ThreadPool.getWatch();
        Watcher asyncWatcher = AsyncProvider.getWatch();
        html.append(String.format(TR, 1, "线程池", watcher.taskCount, watcher.activeCount, watcher.threadCount, watcher.poolSize));
        html.append(String.format(TR, 2, "定时任务池", watcher.scheduleTaskCount, watcher.scheduledActiveCount, watcher.scheduledThreadCount, watcher.scheduledPoolSize));
        html.append(String.format(TR, 3, "异步框架池", asyncWatcher.taskCount, asyncWatcher.activeCount, asyncWatcher.threadCount, asyncWatcher.poolSize));
        outPrinter.append(THREAD_POOL_HTML_TEMPLATE.replace("<content></content>", html.toString()));
    }
    
    public final static String TR = "<tr><th scope=\"row\">%1$s</th><td>%2$s</td><td>%3$s</td><td>%4$s</td><td>%5$s</td><td>%6$s</td></tr>";
    
    public final static String THREAD_POOL_HTML_TEMPLATE = Files.readResourceText("cn/framework/core/pool/thread-pool.html");
}
