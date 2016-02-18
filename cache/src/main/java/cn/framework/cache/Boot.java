/**
 * @项目名称: cache
 * @文件名称: Boot.java
 * @Date: 2015年11月21日
 * @author: wenlai
 * @type: Boot
 */
package cn.framework.cache;

import cn.framework.cache.init.CacheInitProvider;
import cn.framework.cache.pool.RedisProvider;
import cn.framework.core.container.Context;
import cn.framework.core.utils.Xmls;

/**
 * @author wenlai
 *
 */
public class Boot {
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        CacheInitProvider provider = new CacheInitProvider();
        provider.init(Context.buildByConf(Xmls.node("cache.xml")));
        System.out.println(RedisProvider.hashGet("wenlai", RedisProvider.buildKey("wenlai", "james"), "id"));
    }
    
}
