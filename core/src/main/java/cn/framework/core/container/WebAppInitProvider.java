/**
 * @项目名称: core
 * @文件名称: WebAppInitProvider.java
 * @Date: 2016年1月4日
 * @author: wenlai
 * @type: WebAppInitProvider
 */
package cn.framework.core.container;

import cn.framework.core.log.LogProvider;
import cn.framework.core.utils.Exceptions;
import cn.framework.core.utils.KVMap;
import cn.framework.core.utils.Strings;
import org.w3c.dom.Node;

import java.util.ArrayList;

import static cn.framework.core.utils.Xmls.*;

/**
 * @author wenlai
 */
public class WebAppInitProvider implements InitProvider {

    /**
     * @see cn.framework.core.container.InitProvider#init(cn.framework.core.container.Context)
     */
    @Override
    public void init(final Context context) throws Exception {
        try {
            buildWebapps(context);
            buildServlets(context);
            buildFilters(context);
        }
        catch (Exception x) {
            Exceptions.processException(x);
        }
    }

    /**
     * 构建过滤器
     *
     * @param context context
     */
    private void buildFilters(Context context) {
        try {
            Node filters = xpathNode("//filters", context.getConf());
            if (filters != null) {
                ArrayList<Node> filterNodeList = xpathNodesArray(".//filter", filters);
                if (filterNodeList != null && filterNodeList.size() > 0) {
                    for (Node filterNode : filterNodeList) {
                        try {
                            context.addFilter(attr("name", filterNode), attr("class", filterNode), attr("pattern", filterNode), buildParams(".//param", filterNode));
                            LogProvider.getFrameworkInfoLogger().error("add buildFilters : {}", attr("class", filterNode));
                        }
                        catch (Exception x) {
                            Exceptions.processException(x);
                        }
                    }
                }
            }
        }
        catch (Exception x) {
            Exceptions.processException(x);
        }
    }

    /**
     * 构建servlet
     *
     * @param context context
     */
    private void buildServlets(Context context) {
        try {
            Node servlets = xpathNode("//servlets", context.getConf());
            if (servlets != null) {
                ArrayList<Node> servletNodeList = xpathNodesArray(".//servlet", servlets);
                if (servletNodeList != null && servletNodeList.size() > 0) {
                    for (Node servletNode : servletNodeList) {
                        try {
                            String loadUp = childTextContent("loadOnStartup", servletNode);
                            int loadOnStart = Strings.isNotNullOrEmpty(loadUp) ? Strings.parseInt(loadUp, -1) : -1;
                            boolean auth = Boolean.parseBoolean(childTextContent("auth", servletNode, "false"));
                            context.addServlet(null, attr("name", servletNode), attr("class", servletNode), attr("pattern", servletNode), buildParams(".//param", servletNode), loadOnStart, auth);
                            LogProvider.getFrameworkInfoLogger().error("add buildServlets : {}", attr("class", servletNode));
                        }
                        catch (Exception x) {
                            Exceptions.processException(x);
                        }
                    }
                }
            }
        }
        catch (Exception x) {
            Exceptions.processException(x);
        }
    }

    /**
     * 构建webapp
     *
     * @param context context
     */
    private void buildWebapps(Context context) {
        try {
            Node webApps = xpathNode("//web-apps", context.getConf());
            if (webApps != null) {
                ArrayList<Node> apps = xpathNodesArray(".//web-app", webApps);
                if (apps != null && apps.size() > 0) {
                    for (Node node : apps) {
                        try {
                            context.getTomcat().addWebapp(attr("pattern", node), attr("path", node));
                            LogProvider.getFrameworkInfoLogger().error("add buildWebapps : {}", attr("path", node));
                        }
                        catch (Exception x) {
                            Exceptions.processException(x);
                        }
                    }
                }
            }
        }
        catch (Exception x) {
            Exceptions.processException(x);
        }
    }

    /**
     * 构建参数列表
     *
     * @param express  获取参数节点的xpath表达式 例如: .//param<br>
     *                 会获取所有param节点的key - value值并存入结果中
     * @param mainNode 主节点
     *
     * @return
     */
    private KVMap buildParams(String express, Node mainNode) {
        try {
            KVMap params = new KVMap();
            ArrayList<Node> paramNodes = xpathNodesArray(express, mainNode);
            if (paramNodes != null && paramNodes.size() > 0) {
                for (Node paramNode : paramNodes) {
                    params.addKV(attr("name", paramNode), attr("value", paramNode));
                }
            }
            return params;
        }
        catch (Exception x) {
            Exceptions.processException(x);
        }
        return null;
    }

}
