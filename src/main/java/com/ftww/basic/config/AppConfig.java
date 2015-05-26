package com.ftww.basic.config;

import com.ftww.basic.plugin.log.Slf4jLogFactory;
import com.ftww.basic.plugin.properties.PropertiesPlugin;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.EhCachePlugin;

/**
 * 
 * @author tangdongyu
 *
 */
public class AppConfig extends JFinalConfig {
	
	private static Logger log=Logger.getLogger(AppConfig.class);

	@Override
	public void configConstant(Constants me) {
		log.info(" == Constants == 缓存 Properties配置文件中的配置信息！");
		new PropertiesPlugin(loadPropertyFile("init.properties")).start();
		
		log.info(" == Constants == 设置字符集！");
		
		
		Logger.setLoggerFactory(new Slf4jLogFactory());
	}

	@Override
	public void configRoute(Routes me) {

	}

	@Override
	public void configPlugin(Plugins me) {
		
		
		log.info("EhCachePlugin EhCache缓存");
		me.add(new EhCachePlugin());
	}

	@Override
	public void configInterceptor(Interceptors me) {

	}

	@Override
	public void configHandler(Handlers me) {

	}

	@Override
	public void afterJFinalStart() {
		super.afterJFinalStart();
	}

	@Override
	public void beforeJFinalStop() {
		super.beforeJFinalStop();
	}
	
	/**
	 * 运行此 main 方法可以启动项目
	 * @param args
	 */
	public static void main(String [] args){
		JFinal.start("src/main/webapp", 80, "/", 5);
	}

}
