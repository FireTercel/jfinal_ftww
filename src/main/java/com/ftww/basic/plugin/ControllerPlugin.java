package com.ftww.basic.plugin;

import java.util.List;

import com.ftww.basic.annotation.Controller;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.controller.BaseController;
import com.ftww.basic.kits.ClassSearcherKit;
import com.ftww.basic.plugin.properties.PropertiesPlugin;
import com.jfinal.config.Routes;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

/**
 * 扫描Controller上的注解，绑定Controller和controllerKey
 * @author FireTercel 2015年5月27日 
 *
 */
public class ControllerPlugin implements IPlugin {
	
	private static Logger log= Logger.getLogger(ControllerPlugin.class);
	
	private Routes me;
	
	public ControllerPlugin(Routes me){
		this.me=me;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean start() {
		// 查询所有继承BaseController的类
				List<String> jars = (List<String>) PropertiesPlugin.getParamMapValue(DictKeys.config_scan_jar);
				List<Class<? extends BaseController>> controllerClasses = null;
				if(jars.size() > 0){
					controllerClasses = ClassSearcherKit.of(BaseController.class).includeAllJarsInLib(ClassSearcherKit.isValiJar()).injars(jars).search();// 可以指定查找jar包，jar名称固定，避免扫描所有文件
				}else{
					controllerClasses = ClassSearcherKit.of(BaseController.class).search();
				}
				
				// 循环处理自动注册映射
				for (Class controller : controllerClasses) {
					// 获取注解对象
					Controller controllerBind = (Controller) controller.getAnnotation(Controller.class);
					if (controllerBind == null) {
						log.error(controller.getName() + "继承了BaseController，但是没有注解绑定映射路径");
						continue;
					}

					// 获取映射路径数组
					String[] controllerKeys = controllerBind.controllerKey();
					for (String controllerKey : controllerKeys) {
						controllerKey = controllerKey.trim();
						if(controllerKey.equals("")){
							log.error(controller.getName() + "注解错误，映射路径为空");
							continue;
						}
						// 注册映射
						me.add(controllerKey, controller);
						log.debug("Controller注册： controller = " + controller + ", controllerKey = " + controllerKey);
					}
				}
				return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

}
