package com.ftww.basic.plugin.shiro.core;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;

import com.ftww.basic.encription.EncriptionKit;
import com.ftww.basic.model.BaseModel;

/**
 * 
 * @author Created by wangrenhui on 14-4-24.
 *
 */
public class SubjectKit {
	
	private static String[] baseRole = new String[]{"R_ADMIN", "R_MANAGER", "R_MEMBER", "R_USER"};
	
	private SubjectKit() {
	}
	
	/**
	 * 获取当前登陆用户
	 * @return
	 */
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取shiro的session。
	 * @return
	 */
	public static Session getSession() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		if (session == null) {
			throw new UnknownSessionException("Unable found required Session");
		} else {
			return session;
		}
	}
	
	/**
	 * 获取用户对象
	 * @param <T> User
	 * @return T User
	 */
	public static <T extends BaseModel> T getUser() {
		Subject subject = getSubject();
		Object user = subject.getPrincipal();
		if (user == null)
			return null;
		else {
			return (T) user;
		}
	}
	
	/**
	 * 调用login方法登陆，不记住我。
	 *
	 * @param username
	 *            用户名
	 * @param password
	 *            密码 // * @param user 完整用户对象 // * @param <T> User
	 * @return bolean
	 */
	public static boolean login(String username, String password) {
		return login(username, password, false);
	}
	
	/**
	 * 用户登陆
	 * @param username
	 * @param password
	 * @param rememberMe
	 * @return
	 */
	public static boolean login(String username, String password, boolean rememberMe) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try {
			token.setRememberMe(rememberMe);
			SecurityUtils.getSubject().login(token);
			return true;
		} catch (AuthenticationException e) {
			return false;
		}
	}
	
	/**
	 * 验证验证码
	 *
	 * @param captchaToken
	 *            token
	 * @return boolean
	 */
	public static boolean doCaptcha(String captchaName, String captchaToken) {
		Session session = getSession();
		if (session.getAttribute(captchaName) != null) {
			String captcha = session.getAttribute(captchaName).toString();
			if (captchaToken != null && captcha.equalsIgnoreCase(EncriptionKit.encrypt(captchaToken))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否已经登录
	 *
	 * @return boolean
	 */
	public static boolean isAuthed() {
		Subject subject = getSubject();
		if (subject == null || subject.getPrincipal() == null || (!subject.isAuthenticated() && !subject.isRemembered())) {
			return false;
		} else
			return true;
	}
	
	/**
	 * 判断是否基本角色
	 * @param roleValue
	 * @return
	 */
	public static boolean wasBaseRole(String roleValue) {
		if (ArrayUtils.contains(baseRole, roleValue)) {
			return true;
		}
		return false;
	}

	public String[] getBaseRole() {
		return baseRole;
	}

	public void setBaseRole(String... baseRole) {
		SubjectKit.baseRole = baseRole;
	}

}
