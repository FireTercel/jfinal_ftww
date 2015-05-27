package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.thread.ThreadParamInit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 系统功能model
 * @author FireTercel
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_operator")
public class Operator extends BaseModel<Operator> {

	private static final long serialVersionUID = 4975269559800448141L;
	
	private static Logger log = Logger.getLogger(Operator.class);
	
	public static final Operator dao = new Operator();
	
	/**
	 * 添加或者更新缓存
	 * @param ids
	 */
	public void cacheAdd(String ids){
		Operator operator = Operator.dao.findById(ids);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_operator + ids, operator);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_operator + operator.getStr("url"), operator);
		
	}
	
	/**
	 * 删除缓存
	 * @param ids
	 */
	public void cacheRemove(String ids){
		Operator operator = Operator.dao.findById(ids);
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_operator + ids);
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_operator + operator.getStr("url"));
	}
	
	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	public Operator cacheGet(String key){
		Operator operator = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_operator + key);
		return operator;
	}

}
