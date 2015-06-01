package com.ftww.basic.service;

import java.util.List;

import com.ftww.basic.model.Param;
import com.jfinal.log.Logger;

/**
 * 参数业务层
 * @author FireTercel 2015年6月1日 
 *
 */
public class ParamService extends BaseService {
	
	private static Logger log = Logger.getLogger(ParamService.class);
	
	public static final ParamService service = new ParamService();
	
	/**
	 * 保存
	 * @param param
	 */
	public void save(Param param){
		String pIds = param.getStr("parentids");
		Param parent = Param.dao.findById(pIds);
		parent.set("isparent", "true").update();
		
		Long orderIds = param.getNumber("orderids").longValue();
		if (orderIds < 2 || orderIds > 9) {
			param.set("images", "2.png");
		} else {
			param.set("images", orderIds + ".png");
		}

		param.set("isparent", "false").set("levels", parent.getNumber("levels").longValue() + 1);
		param.save();
		
		param.set("paths", parent.get("paths") + "/" + param.getPKValue()).update();
		
		// 缓存
		Param.dao.cacheAdd(param.getPKValue());
		
	}
	
	public void update(Param param){
		//获取父id，查找父数据，设置父数据isparent属性。
		String pIds = param.getStr("parentids");
		Param parent = Param.dao.findById(pIds);
		parent.set("isparent", "true").update();
		
		//设置父id、设置层次（从0开始）、设置路径，父路径+pid
		param.set("parentids", pIds).set("levels", parent.getNumber("levels").longValue() + 1);
		param.set("paths", parent.get("paths")+"/"+param.getPKValue());
		param.update();
		
		//缓存
		Param.dao.cacheAdd(param.getPKValue());
		
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public boolean delete(String ids){
		//删除缓存
		Param.dao.cacheRemove(ids);
		
		//删除数据库数据
		return Param.dao.deleteById(ids);
	}
	
	/**
	 * 获取子节点数据
	 * @param parentIds
	 * @return
	 */
	public String childNodeData(String parentIds){
		List<Param> list = null;
		if (null != parentIds) {
			String sql = getSql("basic.param.treeChildNode");
			list = Param.dao.find(sql, parentIds);
		} else {
			String sql = getSql("basic.param.treeNodeRoot");
			list = Param.dao.find(sql);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int size = list.size() - 1;
		for (Param param : list) {
			sb.append(" { ");
			sb.append(" id : '").append(param.getPKValue()).append("', ");
			sb.append(" name : '").append(param.getStr("names")).append("', ");
			sb.append(" isParent : ").append(param.getStr("isparent")).append(", ");
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" icon : '/jsFile/zTree/css/zTreeStyle/img/diy/").append(param.getStr("images")).append("' ");
			sb.append(" }");
			if (list.indexOf(param) < size) {
				sb.append(", ");
			}
		}
		sb.append("]");

		return sb.toString();
	}
	

}
