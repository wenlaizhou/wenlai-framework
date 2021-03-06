package cn.framework.db.init;

import cn.framework.core.container.TomcatContainer;
import cn.framework.core.utils.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * project code
 * package cn.framework.db.init
 * create at 16-3-8 下午11:55
 *
 * @author wenlai
 */
public class DruidServlet extends HttpServlet {


    private static String TEMPLATE;

    @Override
    public void init() throws ServletException {
        TEMPLATE = Files.readResourceText("cn/framework/db/init/druid.html");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!TomcatContainer.basicAuth(req, resp)) {
            return;
        }
        resp.getOutputStream().print(TEMPLATE);
    }

}
