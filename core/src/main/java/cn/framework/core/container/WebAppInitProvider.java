/**
 * @项目名称: core
 * @文件名称: WebAppInitProvider.java
 * @Date: 2016年1月4日
 * @author: wenlai
 * @type: WebAppInitProvider
 */
package cn.framework.core.container;

import java.util.ArrayList;
import org.w3c.dom.Node;
import cn.framework.core.utils.Xmls;

/**
 * @author wenlai
 *
 */
public class WebAppInitProvider implements InitProvider {
    
    /*
     * @see cn.framework.core.container.InitProvider#init(cn.framework.core.container.Context)
     */
    @Override
    public void init(final Context context) throws Exception {
        Node webApps = Xmls.xpathNode("//web-apps", context.getConf());
        ArrayList<Node> apps = Xmls.xpathNodesArray(".//web-app", webApps);
        if (apps != null && apps.size() > 0)
            for (Node node : apps)
                context.getTomcat().addWebapp(Xmls.attr("pattern", node), Xmls.attr("path", node));
    }
    
}
