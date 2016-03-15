package cn.framework.db.view;

import cn.framework.core.utils.Exceptions;
import cn.framework.core.utils.KVMap;
import cn.framework.core.utils.KVPair;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * project code
 * package cn.framework.db.view
 * create at 16-3-11 上午11:34
 *
 * @author wenlai
 */
public class DaoUI extends HttpServlet {

    private static HashMap<String, ProcedureHandler> GET_ = null;
    private static HashMap<String, ProcedureHandler> POST_ = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProcedureHandler handler = GET_.get(req.getRequestURI());
        try {
            processHandler(handler, req, resp);
        }
        catch (Exception x) {
            Exceptions.processException(x);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProcedureHandler handler = POST_.get(req.getRequestURI());
        try {
            processHandler(handler, req, resp);
        }
        catch (Exception x) {
            Exceptions.processException(x);
        }
    }

    public void processHandler(ProcedureHandler handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        KVPair<KVMap, Object> params = buildParams(req);
        if (handler.getBefore() != null) {
            params = handler.handler().doHandler(handler.getBefore().doHandler(params));
        }
        if (handler.after() != null) {
            handler.after().doHandler(params);
        }
        if (params != null) {
            resp.getWriter().append(JSON.toJSONString(params.value()));
        }
        resp.getWriter().close();
    }

    private KVPair<KVMap, Object> buildParams(HttpServletRequest request) {
        KVMap key = new KVMap();
        Enumeration<String> names = request.getParameterNames();
        if (names != null) {
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                key.addKV(name, request.getParameter(name));
            }
        }
        return new KVPair<>(key, null);
    }

    /**
     *
     */
    //    public interface DbHandler
    //    {
    //        Object doHandler(KVMap sqlParams, KVMap languagesParams, DbHandlerChain chain);
    //    }
    public interface DbHandler {

        void procedureId(String id);

        String procedureId();

        KVPair<KVMap, Object> doHandler(KVPair<KVMap, Object> paramAndResult);
    }

    /**
     *
     */
    public class ProcedureHandler {
        private DbHandler before;

        private DbHandler after;

        private DbHandler handler;

        public DbHandler getBefore() {
            return before;
        }

        public void before(DbHandler before) {
            this.before = before;
        }

        public DbHandler after() {
            return after;
        }

        public void after(DbHandler after) {
            this.after = after;
        }

        public DbHandler handler() {
            return handler;
        }

        public void handler(DbHandler handler) {
            this.handler = handler;
        }
    }
}



