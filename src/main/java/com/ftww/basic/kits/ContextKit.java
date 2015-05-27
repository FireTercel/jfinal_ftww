package com.ftww.basic.kits;

import com.ftww.basic.model.Group;
import com.ftww.basic.model.Operator;
import com.ftww.basic.model.Role;
import com.ftww.basic.model.Station;
import com.ftww.basic.model.User;
import com.jfinal.log.Logger;

/**
 * WEB上下文工具类
 * @author FireTercel
 *
 */
public class ContextKit {
	
	private static Logger log = Logger.getLogger(ContextKit.class);
	
	public static boolean hasPrivilegeUrl(String url,String userIds){
		// 基于缓存查询operator
		Operator operator = Operator.dao.cacheGet(url);
		if (null == operator) {
			log.error("URL缓存不存在：" + url);
			return false;
		}
		// 基于缓存查询user
		Object userObj = User.dao.cacheGet(userIds);
		if (null == userObj) {
			log.error("用户缓存不存在：" + userIds);
			return false;
		}
		User user = (User) userObj;
		
		// 权限验证对象
		String operatorIds = operator.getPKValue() + ",";
		String groupIds = user.getStr("groupids");
		String stationIds = user.getStr("stationids");
		
		// 根据分组查询权限
		if (null != groupIds) {
			String[] groupIdsArr = groupIds.split(",");
			for (String groupIdsTemp : groupIdsArr) {
				Group group = Group.dao.cacheGet(groupIdsTemp);
				String roleIdsStr = group.getStr("roleids");
				if(null == roleIdsStr || roleIdsStr.equals("")){
					continue;
				}
				String[] roleIdsArr = roleIdsStr.split(",");
				for (String roleIdsTemp : roleIdsArr) {
					Role role = Role.dao.cacheGet(roleIdsTemp);
					String operatorIdsStr = role.getStr("operatorids");
					if (operatorIdsStr.indexOf(operatorIds) != -1) {
						return true;
					}
				}
			}
		}
		
		// 根据岗位查询权限
		if (null != stationIds) {
			String[] stationIdsArr = stationIds.split(",");
			for (String ids : stationIdsArr) {
				Station station = Station.dao.cacheGet(ids);
				String operatorIdsStr = station.getStr("operatorids");
				if(null == operatorIdsStr || operatorIdsStr.equals("")){
					continue;
				}
				if (operatorIdsStr.indexOf(operatorIds) != -1) {
					return true;
				}
			}
		}
		
		return false;
		
	}
	

}
