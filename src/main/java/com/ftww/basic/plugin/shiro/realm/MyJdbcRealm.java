package com.ftww.basic.plugin.shiro.realm;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import com.ftww.basic.kits.ValidateKit;
import com.ftww.basic.model.Permission;
import com.ftww.basic.model.Role;
import com.ftww.basic.model.User;
import com.ftww.basic.plugin.shiro.core.SubjectKit;

/**
 * 自定义Realm 认证、授权。
 * @author FireTercel 2015年7月13日 
 *
 */
public class MyJdbcRealm extends AuthorizingRealm {

	/**
	 * 登陆调用认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		UsernamePasswordToken userToken = (UsernamePasswordToken) token;
		User user = null;
		String username = userToken.getUsername();
		if(ValidateKit.isEmail(username)) {
			user = User.dao.findFirstBy("`user`.email = ? AND `user`.deleted_at is null", username);
		} else if(ValidateKit.isMobile(username)) {
			user = User.dao.findFirstBy("`user`.mobilephone = ? AND `user`.deleted_at is null", username);
		} else {
			user = User.dao.findFirstBy("`user`.username = ? AND `user`.deleted_at is null", username);
		} 
		if( user != null){
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user , user.getStr("password"),getName());
			return info;
		}else {
			return null;
		}
	}
	
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String loginName = ((User) principals.fromRealm(getName()).iterator().next()).get("username");
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
		//角色集合
		Set<String> roleSet=new LinkedHashSet<String>();
		//权限集合
		Set<String> permissionSet=new LinkedHashSet<String>();
		List<Role> roles = null;
		
		User user = User.dao.findFirstBy(" `user`.username =? AND `user`.deleted_at is null", loginName);
		if(user != null){
			roles = Role.dao.findUserBy("", user.getStr("ids"));
		} else {
			SubjectKit.getSubject().logout();
		}
		loadRole(roleSet,permissionSet,roles);
		info.setRoles(roleSet); // 设置角色
	    info.setStringPermissions(permissionSet); // 设置权限
	    return info;
	}
	
	/**
	 * 遍历角色是否可用
	 * @param roleSet
	 * @param permissionSet
	 * @param roles
	 */
	private void loadRole(Set<String> roleSet, Set<String> permissionSet, List<Role> roles){
		List<Permission> permissions;
		for(Role role : roles){
			if( role.getDate("deleted_at") == null){
				roleSet.add(role.getStr("value"));
				permissions = Permission.dao.findByRole("", role.getStr("ids"));
				loadAuthz(permissionSet,permissions);
			}
		}
	}
	
	/**
	 * 遍历权限是否可用
	 * @param permissionSet
	 * @param permissions
	 */
	private void loadAuthz(Set<String> permissionSet, List<Permission> permissions){
		for(Permission permission : permissions){
			if(permission.getDate("deleted_at") == null){
				permissionSet.add(permission.getStr("value"));
			}
		}
	}
	
	/**
	 * 更新用户授权信息缓存.
	 * @param principal
	 */
	public void clearCachedAuthorizationInfo(Object principal){
		SimplePrincipalCollection principals=new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo(){
		Cache<Object,AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

}
