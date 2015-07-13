package com.ftww.basic.model;

import java.util.List;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.kits.tree.TreeNode;
import com.ftww.basic.thread.ThreadParamInit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheKit;

@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_permission")
public class Permission extends BaseModel<Permission> implements TreeNode<Permission>{

	private static final long serialVersionUID = 4691736116343610754L;
	
	private static Logger log = Logger.getLogger(Permission.class);
	
	public static final Permission dao = new Permission();

	@Override
	public String getIds() {
		return this.getStr("ids");
	}

	@Override
	public String getParentIds() {
		return this.getStr("pids");
	}

	@Override
	public List<Permission> getChildren() {
		return this.get("children");
	}

	@Override
	public void setChildren(List<Permission> children) {
		this.put("children", children);
	}

	/**
	 * 添加或者更新缓存
	 */
	@Override
	public void cacheAdd(String ids) {
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_permission + ids, Permission.dao.findById(ids));
	}

	/**
	 * 删除缓存
	 */
	@Override
	public void cacheRemove(String ids) {
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_permission + ids);
	}

	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	@Override
	public Permission cacheGet(String key) {
		Permission permission = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_permission +key);
		return permission;
	}
	
	/**
	 * 获取指定角色的所有权限。
	 * @param where
	 * @param paras
	 * @return
	 */
	public List<Permission> findByRole(String where , Object... paras ){
		return find(getSelectSql() + getSql("basic.permission.byRole") + blank + getWhere(where) , paras);
	}

}
