package com.ftww.basic.plugin.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 验证登录
 * @author Created by wangrenhui on 14-1-3.
 *
 */
public class CaptchaUsernamePasswordToken extends UsernamePasswordToken{
	
	private static final long serialVersionUID = -6819808695165301930L;
	private String captcha;
	
	public String getCaptcha(){
		return captcha;
	}
	
	public void setCaptcha(String captcha){
		this.captcha = captcha;
	}
	
	public CaptchaUsernamePasswordToken(String username, String password,
			boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

	public CaptchaUsernamePasswordToken(String username, String password,
			boolean rememberMe, String host, String captcha) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
	}
	
}
