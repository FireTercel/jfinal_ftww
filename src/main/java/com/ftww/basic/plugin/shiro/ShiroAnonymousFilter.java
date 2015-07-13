package com.ftww.basic.plugin.shiro;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Created by wangrenhui on 14-1-9.
 *
 */
public class ShiroAnonymousFilter extends ShiroFormAuthenticationFilter{
	
	private static final Logger log = LoggerFactory.getLogger(ShiroAnonymousFilter.class);
	
	private static final String DEFAULT_USER_NAME = "guest";
	private static final String DEFAULT_PASSWORD = "guest";
	private static final String DEFAULT_ROLE = "guest";
	
	private static String username = DEFAULT_USER_NAME;
	private static String password = DEFAULT_PASSWORD;
	private static String role = DEFAULT_ROLE;
	
	public void setGuest(List<String> guestString){
		if(guestString == null && guestString.size() < 2){
			return ;
		}
		String[] usernamepassword = guestString.get(0).split(":");
		if(usernamepassword.length == 2){
			username = usernamepassword[0];
			password = usernamepassword[1];
		}
		role = guestString.get(1);
	}
	
	/**
	 * Always returns <code>true</code> allowing unchecked access to the underlying path or resource.
	 *
	 * @return <code>true</code> always, allowing unchecked access to the underlying path or resource.
	 */
	@Override
	public boolean onPreHandle(ServletRequest request , ServletResponse response , Object mappedValue){
		//验证是否成功登录的方法
		/*if(!isLoginRequest(request,response)){
			Subject subject = SecurityUtils.getSubject();
			if(!subject.isAuthenticated()){
				UsernamePasswordToken token = new UsernamePasswordToken(username,password);
				//记录该令牌
				token.setRememberMe(false);
				subject.login(token);
				if(log.isTraceEnabled()){
					log.trace("guest user:{} login.",username);
				}
			}
		}else{
			Subject subject = getSubject(request, response);
			try{
				if( subject != null && subject.getPrincipal() != null){
					subject.logout();
				}
			}catch(SessionException ise){
				log.debug("Encountered session exception during before user logout.  This can generally safely be ignored.", ise);
			}
		}*/
		return true;
	}
	
	
	public static String getPassword() {
		return password;
	}

	public static String getRole() {
		return role;
	}

	public static String getUsername() {
		return username;
	}
	

}
