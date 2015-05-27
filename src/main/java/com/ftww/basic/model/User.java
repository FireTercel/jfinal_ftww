package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.thread.ThreadParamInit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 用户Model
 * @author FireTercel 2015年5月27日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_user")
public class User extends BaseModel<User> {

	private static final long serialVersionUID = 5607902675983214072L;
	
	private static Logger log = Logger.getLogger(User.class);
	
	public static final User dao = new User();
	
	/**
	 * 关联查询，获取用户详细信息
	 * @return
	 */
	public UserInfo getUserInfo(){
		String userinfoIds = get("userinfoids");
		if(null != userinfoIds && !userinfoIds.isEmpty()){
			return UserInfo.dao.findById(userinfoIds);
		}
		return null;
	}
	
	/**
	 * 关联查询，获取用户部门信息
	 * @return
	 */
	public Department getDepartment(){
		String departmentids = get("departmentids");
		if(null != departmentids && !departmentids.isEmpty()){
			return Department.dao.findById(departmentids);
		}
		return null;
	}
	
	/**
	 * 关联查询，获取用户岗位信息
	 * @return
	 */
	public Station getStation(){
		String stationids= get("stationids");
		if(null != stationids && !stationids.isEmpty()){
			return Station.dao.findById(stationids);
		}
		return null;
	}
	
	/**
	 * 添加或者更新缓存
	 */
	@Override
	public void cacheAdd(String ids) {
		User user = User.dao.findById(ids);
		UserInfo userInfo = user.getUserInfo();
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + ids, user);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("username"), user);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + userInfo.getStr("email"), user);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + userInfo.getStr("mobile"), user);

	}

	/**
	 * 删除缓存
	 */
	@Override
	public void cacheRemove(String ids) {
		User user = User.dao.findById(ids);
		UserInfo userInfo = user.getUserInfo();
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + ids);
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("username"));
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + userInfo.getStr("email"));
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + userInfo.getStr("mobile"));

	}

	/**
	 * 获取缓存
	 * @param ids
	 * @return
	 */
	@Override
	public User cacheGet(String key) {
		User user = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + key);
		return user;
	}

}
