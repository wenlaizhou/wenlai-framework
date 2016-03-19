package cn.framework.core.resource;

import cn.framework.core.utils.Files;
import cn.framework.core.utils.Strings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.framework.core.utils.Pair.newPair;

/**
 * project code
 * package cn.framework.core.resource
 * create at 16/3/18 下午7:04
 * 404 page
 *
 * @author wenlai
 */
public class ErrorUI extends HttpServlet {

    /**
     * 页面资源
     */
    private static final String PAGE = Files.readResourceText("cn/framework/core/resource/Error.html");

    private static final String TEMP = "<h4 class=\"text-center\">${code} ${message}</h4>";

    /**
     * get请求资源
     *
     * @param req  request
     * @param resp response
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (resp.getStatus()) {
            case 404:
                resp.getWriter().append(buildHtml(404, "NOT FOUND"));
                break;
            case 401:
                resp.getWriter().append(buildHtml(401, "FORBIDDEN"));
                break;
            default:
                resp.getWriter().append(Strings.format(PAGE, newPair("placeHolder", Strings.EMPTY)));
                break;
        }
    }

    /**
     * 构建html文件
     *
     * @param code    code
     * @param message message
     *
     * @return
     */
    private String buildHtml(int code, String message) {
        return Strings.format(PAGE, newPair("placeHolder", Strings.format(TEMP, newPair("code", code), newPair("message", message))));
    }
}
