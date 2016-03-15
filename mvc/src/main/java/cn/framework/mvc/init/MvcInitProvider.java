/**
 * framework-mvc
 * 
 * @项目名称: framework
 * @文件名称: MvcInitProvider.java
 * @Date: 2015年10月26日
 * @author: wenlai
 * @type: MvcInitProvider
 */
package cn.framework.mvc.init;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.w3c.dom.Node;
import cn.framework.core.container.Context;
import cn.framework.core.container.InitProvider;
import cn.framework.core.log.LogProvider;
import cn.framework.mvc.route.ActionContainer;
import cn.framework.mvc.route.ActionContainer.METHOD;
import cn.framework.mvc.tag.Action;
import cn.framework.mvc.tag.DefaultAction;
import cn.framework.mvc.tag.Resource;
import cn.framework.mvc.tag.Controller;
import cn.framework.mvc.tag.Get;
import cn.framework.mvc.tag.Post;
import cn.framework.mvc.tag.View;
import cn.framework.core.utils.Arrays;
import cn.framework.core.utils.Jsps;
import cn.framework.core.utils.Packages;
import cn.framework.core.utils.Projects;
import cn.framework.core.utils.Reflects;
import cn.framework.core.utils.Strings;
import static cn.framework.core.utils.Xmls.*;

/**
 * mvc初始化入口，在配置文件中增加:<br>
 * provider 入口<br>
 * 使用如下:<br>
 * 
 * <pre>
 * 
 * <code>@Action("/xxxx/xx.jsp")</code>
 * public static {@link ActionResult} funcName(<strong><b>@Request</b> {@link HttpServletRequest} req</strong>)
 * {
 *     .....
 * }
 * </pre>
 * 
 * @author wenlai
 */
public class MvcInitProvider implements InitProvider {
    
    /**
     * 构建mvc路由需要的action容器
     * 
     * @param mvcNode mvc node
     */
    private Map<String, ActionContainer> buildActionObj(Node mvcNode) throws Exception {
        Map<String, ActionContainer> container = new HashMap<>();
        ArrayList<Node> controllerNodes = xpathNodesArray(".//controller", mvcNode);
        if (controllerNodes != null && controllerNodes.size() > 0) {
            for (Node controllerNode : controllerNodes) {
                String controllerPkg = attr("package", controllerNode);
                String[] classes = Packages.scanClasses(controllerPkg);
                if (classes != null && classes.length > 0) {
                    for (String className : classes) {
                        Class<?> clazz = Class.forName(className);
                        Controller controller = clazz.getDeclaredAnnotation(Controller.class);
                        if (controller != null) {
                            String controllerPath = controller.value().startsWith("/") ? controller.value() : "/" + controller.value();
                            Method[] methods = clazz.getMethods();
                            if (Arrays.isNotNullOrEmpty(methods)) {
                                for (Method method : methods) {
                                    Action action = method.getDeclaredAnnotation(Action.class);
                                    DefaultAction defaultAction = method.getDeclaredAnnotation(DefaultAction.class);
                                    if (action != null || defaultAction != null) {
                                        ActionContainer actionContainer = new ActionContainer();
                                        actionContainer.actionClassName = clazz.getName();
                                        Parameter[] params = method.getParameters();
                                        actionContainer.methodName = method.getName();
                                        actionContainer.method = method.getDeclaredAnnotation(Get.class) != null ? method.getDeclaredAnnotation(Post.class) != null ? METHOD.ALL : METHOD.GET : method.getDeclaredAnnotation(Post.class) != null ? METHOD.POST : METHOD.GET;
                                        if (Arrays.isNotNullOrEmpty(params) && params.length == 1) {
                                            Parameter param = params[0];
                                            Resource req = param.getDeclaredAnnotation(Resource.class);
                                            if (req != null) {
                                                actionContainer.hasContextResource = true;
                                            }
                                            else {
                                                break;
                                            }
                                        }
                                        View view = method.getDeclaredAnnotation(View.class);
                                        if (view != null) {
                                            actionContainer.hasView = true;
                                            actionContainer.viewPath = view.value().startsWith("/") ? view.value() : "/" + view.value();
                                        }
                                        String actionPath = defaultAction != null ? Strings.EMPTY : action.value().startsWith("/") ? action.value() : "/" + action.value();
                                        container.put(controllerPath + actionPath, actionContainer);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return container;
    }
    
    /*
     * @see cn.framework.core.container.InitProvider#init(cn.framework.core.container.Context)
     */
    @Override
    public void init(final Context context) throws Exception {
        // TODO 无view的附加上通用view 是否需要？
        Node mvcNode = xpathNode(".//mvc", context.getConf());
        if (mvcNode != null) {
            context.addServlet("mvc-route", "cn.framework.mvc.route.Route", "/*");
            ArrayList<Node> viewNodes = xpathNodesArray(".//view", mvcNode);
            String outputDir = new StringBuilder(Projects.TOMCAT_DIR).append("/output/jsp").toString();
            if (Arrays.isNotNullOrEmpty(viewNodes)) {
                for (Node viewNode : viewNodes) {
                    if (viewNode != null) {
                        try {
                            String prefix = attr("prefix", viewNode);
                            String jspPkg = attr("package", viewNode);
                            if (Strings.isNullOrEmpty(jspPkg))
                                continue;
                            Jsps.compile(outputDir, jspPkg);
                            Packages.loadClass(outputDir);
                            File dir = new File(new StringBuilder(outputDir).append("/").append(jspPkg.replace(".", "/")).toString());
                            File[] fs = dir.listFiles();
                            if (fs == null || fs.length < 1)
                                continue;
                            for (File f : fs) {
                                if (!f.isFile())
                                    continue;
                                String filename = f.getName();
                                if (filename.endsWith(".class"))// xxx_jsp.class
                                {
                                    String className = jspPkg + "." + filename.replace(".class", "");
                                    String path = (Strings.isNotNullOrEmpty(prefix) ? (prefix.startsWith("/") ? prefix : "/" + prefix) : Strings.EMPTY) + "/" + filename.replace(".class", "").replace("_jsp", ".jsp");
                                    Class.forName(className);
                                    context.addServlet(filename.replace(".class", ""), className, path);
                                    System.out.println(String.format("register jsp : class : %1$s, path : %2$s", className, path));
                                }
                            }
                        }
                        catch (Exception x) {
                            LogProvider.getFrameworkErrorLogger().error(x.getMessage(), x);
                        }
                    }
                }
            }
            Reflects.setField("cn.framework.mvc.route.ActionContainer", "instance", buildActionObj(context.getConf()), null);
        }
        
    }
    
}
