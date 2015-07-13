package com.ftww.basic.plugin.shiro.core.handler;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;

/**
 * 基于JDBC的角色访问控制处理器
 * @author Created by wangrenhui on 14-1-7.
 *
 */
public class JdbcRoleAuthzHandler extends AbstractAuthzHandler{
	private final String jdbcRole;

	public JdbcRoleAuthzHandler(String jdbcRole) {
		this.jdbcRole = jdbcRole;
	}

	@Override
	public void assertAuthorized() throws AuthorizationException {
		Subject subject = getSubject();
		//数据库角色
		if(jdbcRole != null){
			subject.checkRole(jdbcRole);
			return;
		}
		
	}

}
