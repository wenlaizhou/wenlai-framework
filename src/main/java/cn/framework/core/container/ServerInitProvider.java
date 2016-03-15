/**
 * @项目名称: framework
 * @文件名称: ServerInit.java
 * @Date: 2015年10月15日
 * @author: wenlai
 * @type: ServerInit
 */
package cn.framework.core.container;

import cn.framework.core.log.LogProvider;
import cn.framework.core.utils.Exceptions;
import cn.framework.core.utils.Reflects;
import cn.framework.core.utils.Strings;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.w3c.dom.Node;

import java.net.InetAddress;
import java.util.ArrayList;

import static cn.framework.core.utils.Xmls.*;

/**
 * @author wenlai
 *         set CATALINA_OPTS=\
 *         -Dcom.sun.management.jmxremote \
 *         -Dcom.sun.management.jmxremote.port=%my.jmx.port% \
 *         -Dcom.sun.management.jmxremote.ssl=false \
 *         -Dcom.sun.management.jmxremote.authenticate=false
 */
public class ServerInitProvider implements InitProvider {

    /*
     * @see cn.framework.core.container.InitProvider#init(cn.framework.core.container.Context)
     */
    @Override
    public void init(final Context context) throws Exception {
        try {
            Node serverNode = xpathNode("//server", context.getConf());
            context.getTomcat().setPort(0);
            /**
             * build connector
             */
            Connector connector = new Connector(Http11Nio2Protocol.class.getName());
            connector.setAttribute("connectionTimeout", childAttribute("time-out", "value", serverNode, "3000"));
            connector.setAllowTrace(true);
            connector.setAttribute("address", childAttribute("address", "value", serverNode, InetAddress.getByName("localhost").getHostAddress()));
            connector.setPort(Integer.parseInt(childAttribute("port", "value", serverNode, "80")));
            context.getContext().setSwallowOutput(true);
            context.getTomcat().getService().addConnector(connector);
            context.getTomcat().setConnector(connector);
            context.getTomcat().getHost().setAppBase("ROOT");
            /**
             * build thread-pool
             */
            Node threadPoolNode = xpathNode(".//thread-pool", serverNode);
            if (threadPoolNode != null) {
                String commonPoolSize = childAttribute("common", "size", serverNode);
                String scheduleSize = childAttribute("schedule", "size", serverNode);
                Reflects.setField("cn.framework.core.pool.ThreadPool", "commonPoolSize", Strings.parseInt(commonPoolSize, 30), null);
                Reflects.setField("cn.framework.core.pool.ThreadPool", "schedulePoolSize", Strings.parseInt(scheduleSize, 10), null);
            }
            /**
             * build context-params
             */
            Node contextParamsNode = xpathNode("//context-params", context.getConf());
            if (contextParamsNode != null) {
                ArrayList<Node> contextParams = xpathNodesArray(".//context-param", contextParamsNode);
                if (contextParams != null && contextParams.size() > 0) {
                    for (Node contextParamNode : contextParams) {
                        context.getContext().addParameter(attr("name", contextParamNode), attr("value", contextParamNode));
                    }
                }
            }
            /**
             * build listeners
             */
            Node listenersNode = xpathNode("//listeners", context.getConf());
            if (listenersNode != null) {
                ArrayList<Node> listenerNodes = xpathNodesArray(".//listener", listenersNode);
                if (listenerNodes != null && listenerNodes.size() > 0) {
                    for (Node listenerNode : listenerNodes) {
                        try {
                            context.getContext().addApplicationListener(attr("class", listenerNode));
                            LogProvider.getFrameworkInfoLogger().error("add listener : {}", attr("class", listenerNode));
                        }
                        catch (Exception x) {
                            Exceptions.processException(x);
                        }
                    }
                }
            }
        }
        catch (RuntimeException x) {
            throw x;
        }
        catch (Exception x) {
            Exceptions.processException(x);
        }
    }

}
