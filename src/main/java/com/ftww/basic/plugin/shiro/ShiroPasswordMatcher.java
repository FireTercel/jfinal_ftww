package com.ftww.basic.plugin.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftww.basic.model.BaseModel;
import com.ftww.basic.plugin.shiro.hasher.Hasher;

/**
 * 重写PasswordMatcher类
 * @author FireTercel 2015年6月23日 
 *
 */
public class ShiroPasswordMatcher extends PasswordMatcher {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ShiroPasswordMatcher.class);

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token,AuthenticationInfo info) {
		boolean match = false;
		String hasher = ((BaseModel<?>) info.getPrincipals().getPrimaryPrincipal()).get("hasher");
		String default_hasher = Hasher.DEFAULT.value();
		if(default_hasher.equals(hasher)){
			match = super.doCredentialsMatch(token, info);
		}
		return match;
	}

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
