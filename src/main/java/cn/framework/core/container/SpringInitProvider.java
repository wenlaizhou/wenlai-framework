package cn.framework.core.container;

import cn.framework.core.utils.Springs;
import cn.framework.core.utils.Strings;
import org.springframework.web.context.ContextLoaderListener;
import org.w3c.dom.Node;

import static cn.framework.core.utils.Xmls.*;

/**
 * project code
 * package cn.framework.core.container
 * create at 16-3-8 下午5:09
 *
 * @author wenlai
 */
public class SpringInitProvider implements InitProvider {

    /**
     * @param context 配置上下文
     *
     * @throws Exception
     * @code ContextLoaderListener
     */
    @Override
    public void init(Context context) throws Exception {
        Node springNode = xpathNode("//spring", context.getConf());
        if (springNode != null) {
            String springConfPath = attr("src", springNode);
            if (Strings.isNotNullOrEmpty(springConfPath)) {
                context.getContext().addParameter("contextConfigLocation", springConfPath);
                context.getContext().addApplicationListener(ContextLoaderListener.class.getName());
                context.getContext().addApplicationListener(Springs.ContextRegister.class.getName());
            }
        }
    }
}
