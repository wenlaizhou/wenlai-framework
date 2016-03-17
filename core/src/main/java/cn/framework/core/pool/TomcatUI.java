package cn.framework.core.pool;

import cn.framework.core.container.TomcatContainer;
import cn.framework.core.utils.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * project code
 * package cn.framework.core.pool
 * create at 16/3/16 上午11:54
 * pattern : /tomcat-manager
 *
 * @author wenlai
 */
public class TomcatUI extends HttpServlet {

    private static String TEMPLATE;

    @Override
    public void init() throws ServletException {
        TEMPLATE = Files.readResourceText("cn/framework/core/pool/tomcat.html");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!TomcatContainer.basicAuth(req, resp)) {
            return;
        }
        resp.getOutputStream().print(TEMPLATE);
    }

}
