/**
 * @项目名称: db
 * @文件名称: Initor.java
 * @Date: 2016年1月6日
 * @author: wenlai
 * @type: Initor
 */
package cn.framework.core.container;

import static cn.framework.core.utils.Exceptions.processException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.w3c.dom.Node;
import cn.framework.core.utils.KVMap;
import cn.framework.core.utils.Maps;
import cn.framework.core.utils.Strings;

/**
 * 初始化参数<br>
 * 内部使用
 * 
 * @author wenlai
 */
public class Context {
    
    /**
     * 自定义初始化参数
     */
    private KVMap params = null;
    
    /**
     * 获取初始化参数
     * 
     * @param name
     * @return
     */
    public synchronized String getInitParameter(String name) {
        return params == null ? Strings.EMPTY : params.getString(name);
    }
    
    /**
     * 设置初始化参数
     * 
     * @param params
     * @return
     */
    public synchronized Context setParams(KVMap params) {
        if (this.params != null)
            this.params.clear();
        this.params = params;
        return this;
    }
    
    /**
     * 创建新的初始化参数
     * 
     * @param conf
     * @param tomcat
     * @param context
     * @return
     */
    static synchronized Context buildContext(Node conf, Tomcat tomcat, StandardContext context) {
        return new Context(conf, tomcat, context);
    }
    
    /**
     * 生成测试参数
     * 
     * @param conf
     * @return
     */
    public synchronized static Context buildByConf(Node conf) {
        return new Context(conf, null, null);
    }
    
    /**
     * 上下文
     */
    private StandardContext context = null;
    
    /**
     * tomcat
     */
    private Tomcat tomcat = null;
    
    /**
     * 配置项
     */
    private Node conf = null;
    
    /**
     * 构造函数
     * 
     * @param conf
     * @param tomcat
     * @param context
     */
    private Context(Node conf, Tomcat tomcat, StandardContext context) {
        this.conf = conf;
        this.tomcat = tomcat;
        this.context = context;
    }
    
    /**
     * 获取配置
     * 
     * @return
     */
    public synchronized final Node getConf() {
        return this.conf;
    }
    
    /**
     * 获取tomcat
     * 
     * @return
     */
    public synchronized final Tomcat getTomcat() {
        return this.tomcat;
    }
    
    /**
     * 获取上下文
     * 
     * @return
     */
    public synchronized final StandardContext getContext() {
        return this.context;
    }
    
    /**
     * 向上下文中添加servlet
     */
    public synchronized void addServlet(String servletName, String servletClassName, String pattern) {
        addServlet(null, servletName, servletClassName, pattern, null);
    }
    
    /**
     * 向上下文中添加servlet
     */
    public synchronized void addServlet(String servletName, String servletClassName, String pattern, KVMap initParams) {
        addServlet(null, servletName, servletClassName, pattern, initParams);
    }
    
    /**
     * 向上下文中添加servlet
     * 
     * @param c
     * @param servletName
     * @param servletClassName
     * @param pattern
     */
    public synchronized void addServlet(StandardContext c, String servletName, String servletClassName, String pattern) {
        addServlet(c, servletName, servletClassName, pattern, null);
    }
    
    /**
     * 向上下文中添加servlet
     * 
     * @param c 上下文
     * @param servletName servlet名称
     * @param servletClassName servlet类名
     * @param pattern 匹配路径
     * @param initParams 初始化参数列表
     */
    public synchronized void addServlet(StandardContext c, String servletName, String servletClassName, String pattern, KVMap initParams) {
        try {
            c = c == null ? this.context : c;
            if (c == null)
                return;
            Wrapper newServlet = c.createWrapper();
            newServlet.setEnabled(true);
            newServlet.setName(servletName);
            newServlet.setServletClass(servletClassName);
            if (Maps.isNotNullOrEmpty(initParams))
                for (int i = 0; i < initParams.keySet().size(); i++)
                    newServlet.addInitParameter(initParams.getIndexedKey(i), initParams.getString(initParams.getIndexedKey(i)));// 由于在开始执行，所以为了代码美观，没有做优化
            newServlet.setAsyncSupported(true);
            newServlet.setLoadOnStartup(1);
            c.addChild(newServlet);
            c.addServletMapping(pattern, servletName);
        }
        catch (Exception x) {
            processException(x);
        }
    }
}
