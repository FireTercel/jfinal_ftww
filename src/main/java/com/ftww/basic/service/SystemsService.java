package com.ftww.basic.service;

import com.ftww.basic.common.DictKeys;
import com.ftww.basic.common.SplitPage;
import com.ftww.basic.model.Menu;
import com.ftww.basic.model.Module;
import com.ftww.basic.model.Systems;
import com.jfinal.log.Logger;

/**
 * 平台系统业务层
 * @author FireTercel 2015年6月1日 
 *
 */
public class SystemsService extends BaseService {
	
	private static Logger log = Logger.getLogger(SystemsService.class);
	
	public static final SystemsService service = new SystemsService();
	
	
	public void save(Systems systems){
		//保存系统
		systems.save();
		
		//初始化模块根节点
		Module module = new Module();
		module.set("systemsids", systems.getPKValue())
					.set("isparent", "true")
					.set("images", "3.png")
					.set("orderids", 1)
					.set("names", "根节点");
		module.save();
		
		//初始化菜单根节点
		Menu menu = new Menu();
		menu.set("systemsids", systems.getPKValue())
				 .set("isparent", "true")
				 .set("images", "3.png")
				 .set("orderids", 1)
				 .set("names_zhcn", "根节点");
		menu.save();
	}
	
	/**
	 * 删除平台系统（未完成）
	 * @param systemsIds
	 */
	public void delete(String systemsIds){
		//删除平台系统
		Systems.dao.deleteById(systemsIds);
		//删除关联模块
		
		//删除关联菜单
		
		//删除关联日志
	}
	
	/**
	 * 分页查询，结果保存到参数SplitPage
	 * @param splitPage
	 */
	public void list(SplitPage splitPage){
		String select = " select * ";
		splitPageBase(DictKeys.db_dataSource_main, splitPage, select, "basic.systems.splitPage");
	}
	

}
