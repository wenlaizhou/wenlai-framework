package cn.framework.core.container;

import cn.framework.core.utils.Springs;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * project code
 * package cn.framework.core.container
 * create at 16/3/17 下午3:05
 *
 * @author wenlai
 */
@Configuration
@ComponentScan("cn.framework")
@Service("framework")
@Scope("singleton")
public class FrameworkSpringRegister {

    /**
     * Spring注册类
     */
    public final static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(FrameworkSpringRegister.class);

    /**
     * build Springs tools
     */
    static {
        if (Springs.getContext() != null) {
            context.setParent(Springs.getContext());
        }
        Springs.setContext(context);
    }

    @PostConstruct
    public void init() {

    }

    @PreDestroy
    public void destroy() {

    }

}
