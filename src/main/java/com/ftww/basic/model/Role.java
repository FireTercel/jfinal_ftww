package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.thread.ThreadParamInit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 角色model
 * @author FireTercel 2015年5月27日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_role")
public class Role extends BaseModel<Role> {

	private static final long serialVersionUID = 4570328954037300068L;
	
	private static Logger log = Logger.getLogger(Role.class);
	
	public static final Role dao = new Role();
	
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

}
