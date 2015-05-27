package com.ftww.basic.plugin;

import java.util.List;
import java.util.Map;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.controller.BaseController;
import com.ftww.basic.kits.ClassSearcherKit;
import com.ftww.basic.model.BaseModel;
import com.ftww.basic.plugin.properties.PropertiesPlugin;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * 扫描model上的注解，绑定model和table
 * @author FireTercel 2015年5月27日 
 *
 */
public class TablePlugin implements IPlugin {
	
	private final static Logger log= Logger.getLogger(TablePlugin.class);
	
	private Map<String,ActiveRecordPlugin> arpMap;
	
	public TablePlugin (Map<String,ActiveRecordPlugin> arpMap){
		this.arpMap=arpMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean start() {
		List<String> jars = (List<String>) PropertiesPlugin.getParamMapValue(DictKeys.config_scan_jar);
		List<Class<? extends BaseController>> modelClasses = null;// 查询所有继承BaseModel的类
		if(jars.size() > 0){
			modelClasses = ClassSearcherKit.of(BaseModel.class).includeAllJarsInLib(ClassSearcherKit.isValiJar()).injars(jars).search();// 可以指定查找jar包，jar名称固定，避免扫描所有文件
		}else{
			modelClasses = ClassSearcherKit.of(BaseModel.class).search();
		}
		
		// 循环处理自动注册映射
		for (Class model : modelClasses) {
			// 获取注解对象
			Table tableBind = (Table) model.getAnnotation(Table.class);
			if (tableBind == null) {
				log.error(model.getName() + "继承了BaseModel，但是没有注解绑定表名 ！！！");
				break;
			}

			// 获取映射属性
			String dataSourceName = tableBind.dataSourceName().trim();
			String tableName = tableBind.tableName().trim();
			String pkName = tableBind.pkName().trim();
			if(dataSourceName.equals("") || tableName.equals("") || pkName.equals("")){
				log.error(model.getName() + "注解错误，数据源、表名、主键名为空 ！！！");
				break;
			}
			
			// 映射注册
			ActiveRecordPlugin arp = arpMap.get(dataSourceName);
			if(arp == null){
				log.error(model.getName() + "ActiveRecordPlugin不能为null ！！！");
				break;
			}
			arp.addMapping(tableName, pkName, model);
			log.debug("Model注册： model = " + model + ", tableName = " + tableName + ", pkName: " + pkName);
		}
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

}
