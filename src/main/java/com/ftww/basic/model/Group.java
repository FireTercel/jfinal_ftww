package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.thread.ThreadParamInit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 人员分组model
 * @author FireTercel
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_group")
public class Group extends BaseModel<Group> {

	private static final long serialVersionUID = 4220619522780916584L;

	private static Logger log = Logger.getLogger(Group.class);
	
	public static final Group dao = new Group();
	
	/**
	 * 添加或者更新缓存
	 */
	@Override
	public void cacheAdd(String ids) {
		Group group =Group.dao.findById(ids);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_group+ids, group);

	}

	/**
	 * 删除缓存
	 */
	@Override
	public void cacheRemove(String ids) {
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_group + ids);
	}

	/**
	 * 获取缓存
	 */
	@Override
	public Group cacheGet(String key) {
		Group group = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_group + key);
		return group;
	}

}
