/**
 * @项目名称: db
 * @文件名称: ViewBuilder.java
 * @Date: 2015年11月20日
 * @author: wenlai
 * @type: ViewBuilder
 */
package cn.framework.db.view;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Node;
import cn.framework.core.container.Context;
import cn.framework.core.utils.KVMap;
import static cn.framework.core.utils.Xmls.*;
import cn.framework.db.sql.Procedure;
import cn.framework.db.sql.Result;

/**
 * 遵循配置即业务理念，根据配置文件自动增加接口
 * 
 * @author wenlai
 */
public final class ViewBuilder {
    
    /**
     * 根据配置构建view接口信息
     * 
     * @param procedureNode
     * @param context
     * @throws Exception xpath执行失败
     */
    public static void build(Node procedureNode, Context context) throws Exception {
        Node viewNode = xpathNode(".//view", procedureNode);
        if (viewNode == null)
            return;
        String id = attr("id", procedureNode.getParentNode(), "") + "/" + attr("id", procedureNode);
        String path = attr("path", viewNode, "/" + id + "/view");
        String method = attr("method", viewNode, "get");
        context.addServlet(null, id, DbServlet.class.getName(), path, new KVMap("procedureId", id).addKV("method", method.toUpperCase()));
        System.out.println(String.format("register dbservlet : %1$s , servlet path : %2$s", id, path));
    }
    
    /**
     * db-view接口路由
     * 
     * @author wenlai
     */
    public static class DbServlet extends HttpServlet {
        
        /**
         * long
         */
        private static final long serialVersionUID = -982343133736787476L;
        
        /*
         * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            execute(req, resp);
        }
        
        /*
         * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            execute(req, resp);
        }
        
        /**
         * 执行对应procedureId的过程
         * 
         * @param req
         * @param resp
         * @throws IOException
         */
        private void execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            if (!req.getMethod().toUpperCase().equals(this.getInitParameter("method"))) {
                resp.sendError(405);
                return;
            }
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Methods", "GET,POST");
            KVMap params = new KVMap();
            Enumeration<String> keys = req.getParameterNames();
            if (keys != null) {
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    params.addKV(key, req.getParameter(key));
                }
                Result result = Procedure.getProcedure(this.getInitParameter("procedureId")).process(params);
                resp.setContentType("text/json;charset=UTF-8");
                resp.getWriter().append(result != null ? result.toString() : "参数错误");
            }
            else {
                resp.getWriter().append(String.format("you are visit %1$s", this.getInitParameter("procedureId")));
            }
        }
    }
}
