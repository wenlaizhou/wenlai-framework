# wenlai-framework
a fast,powerful,easy framework
***
monitor anywhere
***
![image](https://raw.githubusercontent.com/wenlaizhou/wenlai-framework/master/img/m1.png)
***
seperated module
```xml
		<dependency>
			<groupId>cn.framework</groupId>
			<artifactId>core</artifactId>
			<version>2.0.0</version>
		</dependency>
		<dependency>
			<groupId>cn.framework</groupId>
			<artifactId>mvc</artifactId>
			<version>2.0.0</version>
		</dependency>
		<dependency>
			<groupId>cn.framework</groupId>
			<artifactId>cache</artifactId>
			<version>2.0.0</version>
		</dependency>
		<dependency>
			<groupId>cn.framework</groupId>
			<artifactId>db</artifactId>
			<version>2.0.0</version>
		</dependency>
		<dependency>
			<groupId>cn.framework</groupId>
			<artifactId>rest</artifactId>
			<version>2.0.0</version>
		</dependency>
```
***
config defines business
***
```xml
<?xml version="1.0" encoding="UTF-8"?>
<server>
	<address value="localhost" />
	<port value="6677" />
	<thread-pool>
		<common size="100" />
		<schedule size="10" />
	</thread-pool>
	<security username="admin" password="admin" />
	<init>
		<provider class="cn.framework.core.container.ServerInitProvider" />
		<provider class="cn.framework.db.init.DbInitProvider" />
		<!-- <provider class="cn.framework.rest.init.RestServiceInitProvider" /> -->
		<!-- <provider class="cn.framework.rest.init.RestUiInitProvider" /> -->
		<!-- <provider class="cn.framework.cache.init.CacheInitProvider" /> -->
		<provider class="cn.framework.core.plug.PlugInitProvider" />
		<provider class="cn.framework.core.log.LogInitProvider" />
		<provider class="cn.framework.mvc.init.MvcInitProvider" />
	</init>
	<include src="${conf.dir}/mvc.xml" />
	<include src="${conf.dir}/business.xml" />
	<include src="${conf.dir}/cache.xml" />
	<include src="${conf.dir}/config.xml" />
	<include src="${conf.dir}/database.xml" />
</server>
```
