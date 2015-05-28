package com.ftww.basic.config;

import java.util.HashMap;
import java.util.Map;

import org.beetl.core.GroupTemplate;

import com.ftww.basic.beetl.format.DateFormat;
import com.ftww.basic.beetl.func.EscapeXml;
import com.ftww.basic.beetl.func.HasPrivilegeUrl;
import com.ftww.basic.beetl.func.OrderBy;
import com.ftww.basic.beetl.render.MyBeetlRenderFactory;
import com.ftww.basic.beetl.tag.DictTag;
import com.ftww.basic.beetl.tag.ParamTag;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.handler.GlobalHandler;
import com.ftww.basic.interceptor.AuthenticationInterceptor;
import com.ftww.basic.interceptor.ParamPkgInterceptor;
import com.ftww.basic.kits.StringKit;
import com.ftww.basic.plugin.ControllerPlugin;
import com.ftww.basic.plugin.TablePlugin;
import com.ftww.basic.plugin.i18n.I18NPlugin;
import com.ftww.basic.plugin.log.Slf4jLogFactory;
import com.ftww.basic.plugin.properties.PropertiesPlugin;
import com.ftww.basic.plugin.sqlinxml.SqlXmlPlugin;
import com.ftww.basic.thread.ThreadParamInit;
import com.ftww.basic.thread.ThreadSysLog;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.activerecord.tx.TxByActionKeys;
import com.jfinal.plugin.activerecord.tx.TxByActionMethods;
import com.jfinal.plugin.activerecord.tx.TxByRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;

/**
 * 
 * @author FireTercel
 *
 */
public class AppConfig extends JFinalConfig {
	
	private static Logger log=Logger.getLogger(AppConfig.class);

	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		log.info(" == Constants == 缓存 Properties配置文件中的配置信息！");
		new PropertiesPlugin(loadPropertyFile("init.properties")).start();
		
		log.info(" == Constants == 设置字符集！");
		me.setEncoding(StringKit.encoding);
		
		log.info(" == Constants == 设置开发模式");
		me.setDevMode(getPropertyToBoolean(DictKeys.config_devMode, false));
		
		log.info("  == Constants == 设置日志--Slf4j--");
		//Logger.setLoggerFactory(new Slf4jLogFactory());  
		
		log.info("  == Constants == 设置Beetl视图");
		me.setMainRenderFactory(new MyBeetlRenderFactory());
		
		GroupTemplate groupTemplate = MyBeetlRenderFactory.groupTemplate;
		
		groupTemplate.registerFunction("hasPrivilegeUrl", new HasPrivilegeUrl());
		groupTemplate.registerFunction("orderBy", new OrderBy());
		groupTemplate.registerFunction("escapeXml", new EscapeXml());
		
		groupTemplate.registerTag("dict", DictTag.class);
		groupTemplate.registerTag("param", ParamTag.class);

		//groupTemplate.registerFormat("dateFormat", new DateFormat());
		
		log.info("  == Constants == 视图error page设置");
		me.setError401View("/common/401.html");
		me.setError403View("/common/403.html");
		me.setError404View("/common/404.html");
		me.setError500View("/common/500.html");
		
	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
		log.info("  == Routes ==  路由扫描注册");
		new ControllerPlugin(me).start();
	}

	/**
	 * 配置插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		log.info("  == Plugins--DruidPlugin ==   配置Druid数据库连接池连接属性");
		DruidPlugin druidPlugin = new DruidPlugin(
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_jdbcUrl), 
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_userName), 
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_passWord), 
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_driverClass));
		
		log.info("  == Plugins--DruidPlugin ==    配置Druid数据库连接池大小");
		druidPlugin.set(
				(Integer)PropertiesPlugin.getParamMapValue(DictKeys.db_initialSize), 
				(Integer)PropertiesPlugin.getParamMapValue(DictKeys.db_minIdle), 
				(Integer)PropertiesPlugin.getParamMapValue(DictKeys.db_maxActive));
		
		log.info("  == Plugins--ActiveRecordPlugin == 配置ActiveRecord插件");
		ActiveRecordPlugin arpMain = new ActiveRecordPlugin(DictKeys.db_dataSource_main, druidPlugin);
		//arp.setTransactionLevel(4);//事务隔离级别
		//忽略字段大小写(Container集装箱、sensitive敏感的)
		arpMain.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		// 设置开发模式
		arpMain.setDevMode(getPropertyToBoolean(DictKeys.config_devMode, false)); 
		// 是否显示SQL
		arpMain.setShowSql(getPropertyToBoolean(DictKeys.config_devMode, false)); 
		
		log.info("  == Plugins ==   数据库类型判断");
		String db_type = (String) PropertiesPlugin.getParamMapValue(DictKeys.db_type_key);
		if(db_type.equals(DictKeys.db_type_postgresql)){
			log.info("  == Plugins ==    使用数据库类型是 postgresql");
			arpMain.setDialect(new PostgreSqlDialect());
			
		}else if(db_type.equals(DictKeys.db_type_mysql)){
			log.info("  == Plugins ==    使用数据库类型是 mysql");
			arpMain.setDialect(new MysqlDialect());
		
		}else if(db_type.equals(DictKeys.db_type_oracle)){
			log.info("  == Plugins ==    使用数据库类型是 oracle");
			druidPlugin.setValidationQuery("select 1 FROM DUAL"); //指定连接验证语句(用于保存数据库连接池), 这里不加会报错误:invalid oracle validationQuery. select 1, may should be : select 1 FROM DUAL 
			arpMain.setDialect(new OracleDialect());
		}
		
		log.info("  == Plugins ==    添加druidPlugin插件");
		me.add(druidPlugin); // 多数据源继续添加
		
		log.info("  == Plugins--TablePlugin == 表扫描注册");
		Map<String, ActiveRecordPlugin> arpMap = new HashMap<String, ActiveRecordPlugin>();
		arpMap.put(DictKeys.db_dataSource_main, arpMain); // 多数据源继续添加
		new TablePlugin(arpMap).start();
		me.add(arpMain); // 多数据源继续添加
		
		log.info("  == Plugins--I18NPlugin ==    国际化键值对加载");
		me.add(new I18NPlugin());
		
		log.info("  == Plugins--EhCachePlugin ==  EhCache缓存");
		me.add(new EhCachePlugin());
		
		log.info("  == Plugins--SqlXmlPlugin ==   解析并缓存 xml sql");
		me.add(new SqlXmlPlugin());
	}

	@Override
	public void configInterceptor(Interceptors me) {
		
		log.info("  == Interceptors ==   支持使用session");
		me.add(new SessionInViewInterceptor());
		
		log.info("  == Interceptors ==    权限认证拦截器");
		me.add(new AuthenticationInterceptor());
		
		log.info("  == Interceptors ==    参数封装拦截器");
		me.add(new ParamPkgInterceptor());
		
		
		// 配置开启事物规则
		me.add(new TxByActionMethods("save", "update", "delete"));
		me.add(new TxByRegex(".*save.*"));
		me.add(new TxByRegex(".*update.*"));
		me.add(new TxByRegex(".*delete.*"));
		me.add(new TxByActionKeys("/jf/wx/message", "/jf/wx/message/index"));
	}

	@Override
	public void configHandler(Handlers me) {
		log.info("configHandler 全局配置处理器，主要是记录日志和request域值处理");
		me.add(new GlobalHandler());
	}

	@Override
	public void afterJFinalStart() {
		log.info(" == afterJFinalStart ==  缓存参数");
		new ThreadParamInit().start();

		log.info(" == afterJFinalStart ==  启动操作日志入库线程");
		ThreadSysLog.startSaveDBThread();

		boolean luceneIndex = getPropertyToBoolean(DictKeys.config_luceneIndex, false);
		
		if(luceneIndex){
			log.info("afterJFinalStart 创建自动回复lucene索引");
			//new DocKeyword().run(); 
		}
		
		log.info("afterJFinalStart 系统负载");
		//TimerResources.start();
		
		
	}

	@Override
	public void beforeJFinalStop() {
		log.info("beforeJFinalStop 释放lucene索引资源");
		//new DocKeyword().close();

		log.info("beforeJFinalStop 释放日志入库线程");
		ThreadSysLog.setThreadRun(false);

		log.info("beforeJFinalStop 释放系统负载抓取线程");
		//TimerResources.stop();
	}
	
	/**
	 * 运行此 main 方法可以启动项目
	 * @param args
	 */
	public static void main(String [] args){
		JFinal.start("src/main/webapp", 80, "/", 5);
	}

}
