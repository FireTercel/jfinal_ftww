package com.ftww.basic.service;

import java.util.List;

import com.ftww.basic.common.DictKeys;
import com.ftww.basic.model.Module;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 模块业务层
 * @author FireTercel 2015年6月1日 
 *
 */
public class ModuleService extends BaseService {
	
	private static Logger log = Logger.getLogger(ModuleService.class);
	
	public static final ModuleService service = new ModuleService();
	
	/**
	 * 获取子节点数据
	 * @param systemsIds
	 * @param parentIds
	 * @return
	 */
	public String childNodeData(String systemsIds,String parentIds){
		List<Module> list = null;
		
		if(null != systemsIds && null == parentIds){
			//  1.根据系统ID查询模块树 "where parentModuleIds is null"
			String sql = getSql("basic.module.rootBySystemIds");
			list = Module.dao.find(sql, systemsIds);
			
		}else if(null == systemsIds && null == parentIds){
			//  2.模块单选初始化调用
			String sql =  getSql("basic.module.root");
			list = Module.dao.find(sql);
		}else if(null != parentIds){
			//  3.通用子节点查询
			String sql = getSql("basic.module.child");
			list = Module.dao.find(sql,parentIds);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		int size = list.size() - 1;
		for (Module module : list) {
			sb.append(" { ");
			sb.append(" id : '").append(module.getPKValue()).append("', ");
			sb.append(" name : '").append(module.getStr("names")).append("', ");
			sb.append(" isParent : true, ");
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" icon : '").append("/jsFile/zTree/css/zTreeStyle/img/diy/").append(module.getStr("images")).append("' ");
			sb.append(" }");
			if(list.indexOf(module) < size){
				sb.append(", ");
			}
		}
		
		sb.append("]");
		return sb.toString();
		
	}
	
	/**
	 * 为id为pIds的节点（变更为根节点）添加子节点。
	 * @param pIds
	 * @param names
	 * @param orderIds
	 * @return
	 */
	public String save(String pIds,String names, int orderIds){
		//设置为父节点
		Module pDept = Module.dao.findById(pIds);
		pDept.set("isparent", "true").update();
		
		String images = "";
		if(orderIds < 2 || orderIds > 9){
			orderIds = 2;
			images = "2.png";
		}else{
			images = orderIds + ".png";
		}
		
		Module module = new Module();
		module.set("isparent", "true")
					.set("parentmoduleids", pIds)
					.set("systemsids", pDept.getStr("systemsids"))
					.set("orderids", orderIds)
					.set("names", names)
					.set("images", images);
		module.save();
		
		return module.getPKValue();
		
	}
	
	/**
	 * 更新节点名称，或父节点。
	 * @param ids
	 * @param pIds
	 * @param names
	 */
	public void update(String ids,String pIds,String names){
		Module module = Module.dao.findById(ids);
		if(null != names && !names.isEmpty()){
			//更新模块名称
			module.set("names", names).update();
		}else if(null != pIds && !pIds.isEmpty()){
			//更新上级模块
			module.set("parentmoduleids", pIds).update();
		}
	}
	
	/**
	 * 删除节点，判断其子节点数量>1?不删除：删除
	 * @param ids
	 * @return
	 */
	public boolean delete(String ids){
		String sql = getSql("basic.module.childCount");
		Record record = Db.use(DictKeys.db_dataSource_main).findFirst(sql,ids);
		Long counts = record.getNumber("counts").longValue();
		if(counts > 1){
			return false;
		}
		Module.dao.deleteById(ids);
		return true;
	}
	
}
