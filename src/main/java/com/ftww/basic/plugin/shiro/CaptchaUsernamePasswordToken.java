package com.ftww.basic.plugin.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 验证登录
 * @author DONGYU
 *
 */
public class CaptchaUsernamePasswordToken extends UsernamePasswordToken{
	
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
