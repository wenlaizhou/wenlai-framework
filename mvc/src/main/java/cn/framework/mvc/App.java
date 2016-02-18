package cn.framework.mvc;

import cn.framework.core.container.TomcatContainer;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main(String[] args) throws Exception {
        TomcatContainer container = new TomcatContainer("server.xml");
        container.init();
        container.start();
    }
}
