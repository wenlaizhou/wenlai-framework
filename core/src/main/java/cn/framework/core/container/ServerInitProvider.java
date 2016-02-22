/**
 * @项目名称: framework
 * @文件名称: ServerInit.java
 * @Date: 2015年10月15日
 * @author: wenlai
 * @type: ServerInit
 */
package cn.framework.core.container;

import java.net.InetAddress;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.w3c.dom.Node;
import cn.framework.core.log.LogProvider;
import cn.framework.core.utils.Reflects;
import cn.framework.core.utils.Strings;
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
            Connector connector = new Connector(Http11Nio2Protocol.class.getName());
            connector.setAttribute("connectionTimeout", childAttribute("time-out", "value", serverNode, "3000"));
            connector.setAllowTrace(true);
            connector.setAttribute("address", childAttribute("address", "value", serverNode, InetAddress.getByName("localhost").getHostAddress()));
            connector.setPort(Integer.parseInt(childAttribute("port", "value", serverNode, "80")));
            context.getContext().setSwallowOutput(true);
            context.getTomcat().getService().addConnector(connector);
            context.getTomcat().setConnector(connector);
            context.getTomcat().getHost().setAppBase("ROOT");
            context.addServlet("pool-ui", "cn.framework.core.pool.PoolUI", "/pool-ui");
            context.addServlet("thread-pool-ui", "cn.framework.core.pool.ThreadPoolUI", "/thread-pool-ui");
            context.addServlet("thread-ui", "cn.framework.core.pool.ThreadUI", "/thread-ui");
            context.addServlet("system-ui", "cn.framework.core.pool.SystemUI", "/system-ui");
            Node threadPoolNode = xpathNode(".//thread-pool", serverNode);
            if (threadPoolNode != null) {
                String commonPoolSize = childAttribute("common", "size", serverNode);
                String scheduleSize = childAttribute("schedule", "size", serverNode);
                Reflects.setField("cn.framework.core.pool.ThreadPool", "commonPoolSize", Strings.parseInt(commonPoolSize, 30), null);
                Reflects.setField("cn.framework.core.pool.ThreadPool", "schedulePoolSize", Strings.parseInt(scheduleSize, 10), null);
            }
        }
        catch (Exception x) {
            LogProvider.getFrameworkErrorLogger().error(x.getMessage(), x);
        }
    }
    
}
