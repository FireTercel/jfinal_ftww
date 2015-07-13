package com.ftww.basic.plugin.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftww.basic.model.BaseModel;
import com.ftww.basic.plugin.shiro.hasher.Hasher;

/**
 * 用户密码的匹配，涉及到密码的加密encrypt等。
 * @author Created by wangrenhui on 14-1-3.
 *
 */
public class ShiroPasswordMatcher extends PasswordMatcher {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ShiroPasswordMatcher.class);

	/**
	 * token为表单提交数据<br>
	 * ino为数据库查询数据。
	 */
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token,AuthenticationInfo info) {
		boolean match = false;
		String hasher = ((BaseModel<?>) info.getPrincipals().getPrimaryPrincipal()).get("hasher");//获得hasher字段
		String default_hasher = Hasher.DEFAULT.value();
		if(default_hasher.equals(hasher)){//判断数据库取出的和系统默认值是否相同。
			match = super.doCredentialsMatch(token, info);
		}
		return match;
	}

	/**
	 * 获得提交密码
	 */
	@Override
	protected Object getSubmittedPassword(AuthenticationToken token) {
		/*
		 * 返回的是char数组
		 */
		Object submit = super.getSubmittedPassword(token);
		
		if(submit instanceof char[]){
			/*
        	 * 转为String字符串（或者Hash）
        	 */
			submit = String.valueOf((char[])submit);
		}
		return submit;
	}

	/**
	 * 获得数据库保存的密码
	 */
	@Override
	protected Object getStoredPassword(AuthenticationInfo storedAccountInfo) {
		/*
		 * 返回的是char数组
		 */
		Object stored = super.getStoredPassword(storedAccountInfo);  
		  
        if (stored instanceof char[]) {
        	/*
        	 * 转为String字符串（或者Hash）
        	 */
            stored = String.valueOf((char[]) stored);  
        }  
        return stored;
	}
	
	
	
}
