package com.ftww.basic.model;

import java.util.List;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.kits.tree.TreeNode;
import com.ftww.basic.thread.ThreadParamInit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 角色model
 * @author FireTercel 2015年5月27日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_role")
public class Role extends BaseModel<Role> implements TreeNode<Role>{

	private static final long serialVersionUID = 4570328954037300068L;
	
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(Role.class);
	
	public static final Role dao = new Role();
	
	@Override
	public String getIds() {
		return this.getStr("ids");
	}

	@Override
	public String getParentIds() {
		return this.getStr("pids");
	}

	@Override
	public List<Role> getChildren() {
		return this.get("children");
	}

	@Override
	public void setChildren(List<Role> children) {
		this.put("children", children);
	}

	/**
	 * 添加或者更新缓存
	 */
	@Override
	public void cacheAdd(String ids) {
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_role + ids, Role.dao.findById(ids));
	}

	/**
	 * 删除缓存
	 */
	@Override
	public void cacheRemove(String ids) {
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_role + ids);

	}

	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	@Override
	public Role cacheGet(String key) {
		Role role = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_role + key);
		return role;
	}
	
	/**
	 * 根据User的id，获得该User的所有角色；
	 * 因为Role和User也是多对多关系。
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<Role> findUserBy(String where, Object... paras){
		return find(getSelectSql() + getSql("basic.role.byUser") + blank + getWhere(where), paras);
	}

}
