package com.ftww.basic.plugin.shiro.realm;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ftww.basic.model.Permission;
import com.ftww.basic.model.Role;
import com.ftww.basic.plugin.shiro.core.JdbcAuthzService;
import com.ftww.basic.plugin.shiro.core.handler.AuthzHandler;
import com.ftww.basic.plugin.shiro.core.handler.JdbcPermissionAuthzHandler;

/**
 * 实现数据库权限的初始化加载。<br>
 * 只有一个方法getJdbcAuthz()，return一个Map < String,AuthzHandler>
 * @author FireTercel 2015年7月13日 
 *
 */
public class MyJdbcAuthzService implements JdbcAuthzService{

	@Override
	public Map<String, AuthzHandler> getJdbcAuthz() {
		//加载数据库URL配置，并按长度倒叙排列
		Map<String,AuthzHandler> authzJdbcMaps = Collections.synchronizedMap(
				new TreeMap<String,AuthzHandler>(
						new Comparator<String>(){
							//
							@Override
							public int compare(String arg0, String arg1) {
								int result = arg1.length() - arg0.length();
								if(result == 0){
									return arg0.compareTo(arg1);	//长度一样时，对比ascii码
								}
								return result;
							}
						}));
		
		List<Role> roles = Role.dao.findAll();
		List<Permission> permissions = null;
		for(Role role : roles){
			if(role.getDate("deleted_at") == null){
				permissions = Permission.dao.findByRole("",role.getPKValue());
				for(Permission permission : permissions){
					if(permission.getDate("deleted_at") == null){
						if(permission.getStr("url") != null && !permission.getStr("url").isEmpty()){
							authzJdbcMaps.put(permission.getStr("url"), new JdbcPermissionAuthzHandler(permission.getStr("value")));
						}
					}
				}
			}
		}
		return authzJdbcMaps;
	}
}
