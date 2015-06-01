package com.ftww.basic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ftww.basic.common.DictKeys;
import com.ftww.basic.common.SplitPage;
import com.ftww.basic.model.Group;
import com.ftww.basic.model.Role;
import com.jfinal.log.Logger;


/**
 * 角色业务层
 * @author FireTercel 2015年6月1日 
 *
 */
public class RoleService extends BaseService {
	
	private static Logger log = Logger.getLogger(RoleService.class);

	public static final RoleService service = new RoleService();
	
	/**
	 * 保存
	 * @param role
	 * @return
	 */
	public String save(Role role) {
		// 保存
		role.save();
		return role.getPKValue();
	}

	/**
	 * 更新
	 * @param role
	 */
	public void update(Role role){
		// 更新
		role.update();
		
		// 缓存
		Role.dao.cacheAdd(role.getPKValue());
	}

	/**
	 * 删除
	 * @param role
	 */
	public void delete(String roleIds){
		// 缓存
		Role.dao.cacheRemove(roleIds);
		
		// 删除
		Role.dao.deleteById(roleIds);
	}
	
	/**
	 * 设置角色功能
	 * @param roleIds
	 * @param moduleIds
	 * @param operatorIds
	 */
	public void setOperator(String roleIds, String moduleIds, String operatorIds){
		Role role = Role.dao.findById(roleIds);
		//role.set("moduleids", moduleIds);
		role.set("operatorids", operatorIds).update();
		
		// 缓存
		Role.dao.cacheAdd(roleIds);
	}
	
	/**
	 * 组角色选择
	 * @param ids 用户ids
	 */
	public Map<String,Object> select(String ids){
		List<Role> noCheckedList = new ArrayList<Role>();
		List<Role> checkedList = new ArrayList<Role>();
		String roleIds = Group.dao.findById(ids).getStr("roleids");
		if(null != roleIds && !roleIds.equals("")){
			String fitler = toSql(roleIds);

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("fitler", fitler);
			
			noCheckedList = Role.dao.find(getSql("basic.role.noCheckedFilter", param));
			checkedList = Role.dao.find(getSql("basic.role.checkedFilter", param));
		}else{
			noCheckedList = Role.dao.find(getSql("basic.role.noChecked"));
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noCheckedList", noCheckedList);
		map.put("checkedList", checkedList);
		return map;
	}
	
	/**
	 * 分页
	 * @param splitPage
	 */
	public void list(SplitPage splitPage){
		String select = " select * ";
		splitPageBase(DictKeys.db_dataSource_main, splitPage, select, "basic.role.splitPage");
	}

}
