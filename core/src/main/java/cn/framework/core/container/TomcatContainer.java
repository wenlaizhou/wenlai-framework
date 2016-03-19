/**
 * @项目名称: framework
 * @文件名称: TomcatContainer.java
 * @Date: 2015年10月15日
 * @author: wenlai
 * @type: TomcatContainer
 */
package cn.framework.core.container;

import cn.framework.core.utils.*;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.ContextLoaderListener;
import org.w3c.dom.Node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import static cn.framework.core.utils.Exceptions.processException;
import static cn.framework.core.utils.Xmls.*;

/**
 * tomcat容器类
 *
 * @author wenlai
 */
public class TomcatContainer {

    /**
     * 角色名称
     */
    public static final String ROLE_NAME = "admin";

    /**
     * 框架默认用户名
     */
    private static volatile String USER_NAME = "wenlai";
    
    /**
     * 框架默认密码
     */
    private static volatile String PASSWORD = "admin";
    /**
     * 配置
     */
    public final Node config;
    /**
     * 容器实例
     */
    public final Tomcat tomcat;
    /**
     * 上下文
     */
    public final StandardContext context;

    /**
     * 构建容器
     *
     * @param confOrPath config文件路径或直接传递xml文件内容
     *
     * @throws Exception
     */
    public TomcatContainer(String confOrPath) throws Exception {
        /**
         * build node by xml config
         */
        this.config = node(confOrPath);
        System.out.println(toXmlString(this.config));
        /**
         * add property support
         */
        Node propertiesNode = xpathNode("//properties", this.config);
        ArrayList<Node> properties = childs("property", propertiesNode);
        if (Arrays.isNotNullOrEmpty(properties)) {
            for (Node property : properties) {
                String textContent = property.getTextContent();
                Property.set(attr("name", property), Strings.isNotNullOrEmpty(textContent) ? textContent.trim() : attr("value", property));
            }
        }
        /**
         * add security support
         */
        Node securityNode = xpathNode("//security", this.config);
        if (securityNode != null) {
            USER_NAME = attr("username", securityNode, "wenlai");
            PASSWORD = attr("password", securityNode, "admin");
            Property.set("username", USER_NAME);
            Property.set("password", PASSWORD);
        }
        /**
         * build tomcat
         */
        this.tomcat = new Tomcat();
        this.tomcat.setBaseDir(Projects.TOMCAT_DIR);
        this.tomcat.addRole(USER_NAME, ROLE_NAME);
        this.tomcat.addUser(USER_NAME, PASSWORD);
        this.context = (StandardContext) tomcat.addContext("", null);
        this.context.setReloadable(true);
        this.context.setDelegate(true);
        this.context.setPrivileged(true);
        /**
         * add spring support
         */
        Node springNode = xpathNode("//spring", this.config);
        if (springNode != null) {
            String springConfPath = attr("src", springNode);
            if (Strings.isNotNullOrEmpty(springConfPath)) {
                this.context.addParameter("contextConfigLocation", springConfPath);
                this.context.addApplicationListener(ContextLoaderListener.class.getName());
            }
        }
        this.context.addApplicationListener(Springs.ContextRegister.class.getName());
    }

    /**
     * 框架基础认证
     *
     * @param req  request
     * @param resp response
     *
     * @return
     */
    public static boolean basicAuth(HttpServletRequest req, HttpServletResponse resp) {
        if (req == null || resp == null) {
            return false;
        }
        try {
            String auth = req.getHeader("Authorization");
            if (Strings.isNotNullOrEmpty(auth)) {
                String[] token = auth.split(" ");
                if (token != null && token.length > 1) {
                    if (Strings.isNotNullOrEmpty(token[1])) {
                        if (Base64s.decode(token[1]).equals(String.format("%1$s:%2$s", getUsername(), getPassword()))) {
                            return true;
                        }
                    }
                }
                //Strings.isNotNullOrEmpty(auth) && Base64s.decode(auth.split(" ")[1]).equals(String.format("%1$s:%2$s", getUsername(), getPassword()))) {
            }
        }
        catch (Exception x) {
            processException(x);
        }
        try {
            resp.setHeader("Cache-Control", "no-store");
            resp.setDateHeader("Expires", 0);
            resp.setHeader("WWW-authenticate", "Basic Realm=\"wenlai.framework\"");
            resp.sendError(401, "FORBIDDEN");
        }
        catch (Exception e) {
            processException(e);
        }
        return false;
    }

    /**
     * 获取框架用户名
     *
     * @return
     */
    public final static String getUsername() {
        return USER_NAME;
    }

    /**
     * 获取框架密码
     *
     * @return
     */
    public static final String getPassword() {
        return PASSWORD;
    }

    /**
     * 初始化容器
     *
     * @throws Exception
     */
    public synchronized void init() throws Exception {
        Node initNode = xpathNode(".//init", this.config);
        if (initNode != null) {
            ArrayList<Node> providers = xpathNodesArray(".//provider", initNode);
            if (Arrays.isNotNullOrEmpty(providers)) {
                Context param = Context.buildContext(this.config, this.tomcat, this.context);
                for (Node providerNode : providers) {
                    param.setParams(null);
                    if (Strings.isNotNullOrEmpty(attr("class", providerNode))) {
                        try {
                            ArrayList<Node> paramsNodes = xpathNodesArray(".//init-param", providerNode);
                            if (Arrays.isNotNullOrEmpty(paramsNodes)) {
                                KVMap initParams = new KVMap();
                                for (Node node : paramsNodes)
                                    initParams.addKV(attr("name", node), attr("value", node));
                                param.setParams(initParams);
                            }
                            InitProvider provider = Reflects.createInstance(attr("class", providerNode));
                            if (provider != null) {
                                provider.init(param);
                            }
                            else {
                                Exceptions.logProcessor().logger().error("provider not found : {}", attr("class", providerNode));
                            }
                        }
                        catch (Exception x) {
                            processException(x);
                        }
                    }
                }
            }
        }
    }

    /**
     * 启动容器
     *
     * @throws Exception
     */
    public void start() throws Exception {
        this.tomcat.start();
        this.tomcat.getServer().await();
    }
}
