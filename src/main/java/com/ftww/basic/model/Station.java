package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.thread.ThreadParamInit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 岗位model
 * @author FireTercel 2015年5月27日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_station")
public class Station extends BaseModel<Station> {

	private static final long serialVersionUID = 4864746059184310821L;

	private static Logger log = Logger.getLogger(Station.class);
	
	public static final Station dao = new Station();
	
	/**
	 * 添加或者更新缓存
	 */
	@Override
	public void cacheAdd(String ids) {
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_station + ids, Station.dao.findById(ids));

	}

	/**
	 * 删除缓存
	 */
	@Override
	public void cacheRemove(String ids) {
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_station + ids);

	}

	/**
	 * 获取缓存
	 * @param ids
	 * @return
	 */
	@Override
	public Station cacheGet(String key) {
		Station station = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_station + key);
		return station;
	}

}
