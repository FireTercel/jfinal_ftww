package com.ftww.basic.plugin.shiro;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftww.basic.kits.ReturnKit;
import com.ftww.basic.kits.SubjectKit;

/**
 * 提供用户名、密码、记住我和登录失败信息。<br>
 * Requires the requesting user to be authenticated for the request to continue, and if they are not, forces the user
 * to login via by redirecting them to the {@link #setLoginUrl(String) loginUrl} you configure.
 * <p/>
 * <p>This filter constructs a {@link org.apache.shiro.authc.UsernamePasswordToken UsernamePasswordToken} with the values found in</p>
 * {@link #setUsernameParam(String) username}, {@link #setPasswordParam(String) password},
 * and {@link #setRememberMeParam(String) rememberMe} request parameters.  It then calls
 * {@link org.apache.shiro.subject.Subject#login(org.apache.shiro.authc.AuthenticationToken) Subject.login(usernamePasswordToken)},
 * effectively automatically performing a login attempt.  Note that the login attempt will only occur when the
 * {@link #isLoginSubmission(javax.servlet.ServletRequest, javax.servlet.ServletResponse) isLoginSubmission(request,response)}
 * is <code>true</code>, which by default occurs when the request is for the {@link #setLoginUrl(String) loginUrl} and
 * is a POST request.
 * <p/>
 * <p>If the login attempt fails, the resulting <code>AuthenticationException</code> fully qualified class name will</p>
 * be set as a request attribute under the {@link #setFailureKeyAttribute(String) failureKeyAttribute} key.  This
 * FQCN can be used as an i18n key or lookup mechanism to explain to the user why their login attempt failed
 * (e.g. no user, incorrect password, etc).
 * <p/>
 * <p>If you would prefer to handle the authentication validation and login in your own code, consider using the</p>
 * {@link org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter} instead, which allows requests to the
 * {@link #loginUrl} to pass through to your application's code directly.
 *
 * @see org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter
 * @since 0.9
 */
public class ShiroFormAuthenticationFilter extends ShiroAuthenticatingFilter {

	public static final String DEFAULT_ERROR_KEY_ATTRIBUTE_NAME = "shiroLoginFailure";

	public static final String DEFAULT_USERNAME_PARAM = "username";
	public static final String DEFAULT_PASSWORD_PARAM = "password";
	public static final String DEFAULT_REMEMBER_ME_PARAM = "rememberMe";

	private static final Logger log = LoggerFactory.getLogger(ShiroFormAuthenticationFilter.class);

	private String usernameParam = DEFAULT_USERNAME_PARAM;
	private String passwordParam = DEFAULT_PASSWORD_PARAM;
	private String rememberMeParam = DEFAULT_REMEMBER_ME_PARAM;

	private String failureKeyAttribute = DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;

	/**
	 * 初始化，设置登陆url
	 */
	public ShiroFormAuthenticationFilter() {
		setLoginUrl(DEFAULT_LOGIN_URL);
	}
	
	/**
	 * 是否使用验证码
	 * @param useCaptcha
	 */
	public void setUseCaptcha(boolean useCaptcha) {
		this.useCaptcha = useCaptcha;
	}
	
	/**
	 * 登陆信息提交路径
	 */
	@Override
	public void setLoginUrlMap(Map<String, String> loginUrlMap) {

		if (log.isTraceEnabled()) {
			log.trace("Adding more login url to applied paths.");
		}
		// 添加到拦截路径
		setUrlPath(loginUrlMap, getLoginUrlMap());
		super.setLoginUrlMap(loginUrlMap);
	}
	
	/**
	 * 多个成功登陆跳转路径
	 */
	public void setSuccessUrlMap(Map<String,String> successUrlMap){
		if(log.isTraceEnabled()){
			log.trace("Adding more success url to applied paths.");
		}
		//添加到拦截路径
		setUrlPath(successUrlMap, getSuccessUrlMap());
	    super.setSuccessUrlMap(successUrlMap);
	}
	
	/**
	 * 多个失败登陆跳转路径
	 */
	public void setFailureUrlMap(Map<String,String> failureUrlMap){
		if (log.isTraceEnabled()) {
			log.trace("Adding more success url to applied paths.");
		}
		// 添加到拦截路径
		setUrlPath(failureUrlMap, getSuccessUrlMap());
		super.setFailureUrlMap(failureUrlMap);
	}
	
	
	/**
	 * 添加到拦截路径并删除原来的链接
	 *
	 * @param now            now
	 * @param previous            previous
	 */
	private void setUrlPath(Map<String, String> now,Map<String, String> previous) {
		if (previous != null) {
			for (String key : previous.keySet()) {
				/*
				 * appliedPaths是PathMatchingFilter的一个属性
				 */
				this.appliedPaths.remove(previous.get(key));
			}
		}

		if (now != null) {
			for (String key : now.keySet()) {
				this.appliedPaths.put(now.get(key), null);
			}
		}
	}
	
	/**
	 * 重写ShiroAccessControlFilter中的该方法
	 */
	@Override
	public void setLoginUrl(String loginUrl) {
		String previous = getLoginUrl();
		if(previous != null){
			this.appliedPaths.remove(previous);
		}
		//father method
		super.setLoginUrl(loginUrl);
		
		if (log.isTraceEnabled()) {
			log.trace("Adding login url to applied paths.");
		}
		this.appliedPaths.put(getLoginUrl(), null);
	}
	
	public String getUsernameParam(){
		return usernameParam;
	}
	
	/**
	 * Sets the request parameter name to look for when acquiring the username.
	 * Unless overridden by calling this method, the default is
	 * <code>username</code>.
	 *
	 * @param usernameParam
	 *            the name of the request param to check for acquiring the
	 *            username.
	 */
	public void setUsernameParam(String usernameParam) {
		this.usernameParam = usernameParam;
	}

	public String getPasswordParam() {
		return passwordParam;
	}

	/**
	 * Sets the request parameter name to look for when acquiring the password.
	 * Unless overridden by calling this method, the default is
	 * <code>password</code>.
	 *
	 * @param passwordParam
	 *            the name of the request param to check for acquiring the
	 *            password.
	 */
	public void setPasswordParam(String passwordParam) {
		this.passwordParam = passwordParam;
	}

	public String getRememberMeParam() {
		return rememberMeParam;
	}

	/**
	 * Sets the request parameter name to look for when acquiring the rememberMe
	 * boolean value. Unless overridden by calling this method, the default is
	 * <code>rememberMe</code>.
	 * <p/>
	 * RememberMe will be <code>true</code> if the parameter value equals any of
	 * those supported by
	 * {@link org.apache.shiro.web.util.WebUtils#isTrue(javax.servlet.ServletRequest, String)
	 * WebUtils.isTrue(request,value)}, <code>false</code> otherwise.
	 *
	 * @param rememberMeParam
	 *            the name of the request param to check for acquiring the
	 *            rememberMe boolean value.
	 */
	public void setRememberMeParam(String rememberMeParam) {
		this.rememberMeParam = rememberMeParam;
	}

	public String getFailureKeyAttribute() {
		return failureKeyAttribute;
	}

	public void setFailureKeyAttribute(String failureKeyAttribute) {
		this.failureKeyAttribute = failureKeyAttribute;
	}
	
	/**
	 * 是否处理访问拒绝的情况
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request,ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (log.isTraceEnabled()) {
					log.trace("Login submission detected.  Attempting to execute login.");
				}
				return executeLogin(request, response);
			} else {
				if (log.isTraceEnabled()) {
					log.trace("Login page view.");
				}
				// allow them to see the login page ;)
				return true;
			}
		} else {

			if (log.isTraceEnabled()) {
				log.trace("Attempting to access a path which requires authentication.  Forwarding to the "
						+ "Authentication url [" + getLoginUrl() + "]");
			}

			saveRequestAndRedirectToLogin(request, response);
			return false;
		}
	}
	
	/**
	 * 如果请求是http 的post方式则返回true，其余情况返回false<br>
	 * This default implementation merely returns <code>true</code> if the
	 * request is an HTTP <code>POST</code>, <code>false</code> otherwise. Can
	 * be overridden by subclasses for custom login submission detection
	 * behavior.
	 *
	 * @param request
	 *            the incoming ServletRequest
	 * @param response
	 *            the outgoing ServletResponse.
	 * @return <code>true</code> if the request is an HTTP <code>POST</code>,
	 *         <code>false</code> otherwise.
	 */
	@SuppressWarnings({ "UnusedDeclaration" })
	protected boolean isLoginSubmission(ServletRequest request,
			ServletResponse response) {
		return (request instanceof HttpServletRequest)&& WebUtils.toHttp(request).getMethod().equalsIgnoreCase(POST_METHOD);
	}
	
	@Override
	protected UsernamePasswordToken createToken(ServletRequest request,ServletResponse response) throws Exception {
		String username = getUsername(request);
	    String password = getPassword(request);
	    String captcha = getCaptcha(request);
	    return createToken(username, password, captcha, request, response);
	}

	
	protected boolean isRememberMe(ServletRequest request) {
		return WebUtils.isTrue(request, getRememberMeParam());
	}
	
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
            ServletRequest request, ServletResponse response) throws Exception {
		boolean returnJson = ReturnKit.isJson((HttpServletRequest) request);
		// setUserAttribute(request, response);
		clearFailureAttribute(request, response, returnJson);
		issueSuccessRedirect(request, response, returnJson);
		// we handled the success redirect directly, prevent the chain from
		// continuing:
		return false;
	}
	
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
              ServletRequest request, ServletResponse response) throws Exception {
		boolean returnJson = ReturnKit.isJson((HttpServletRequest) request);
		setFailureAttribute(request, response, returnJson, e);
		issueFailureRedirect(request, response, returnJson);
		if (returnJson) {
			return false;
		} else {
			// login failed, let request continue back to the login page:
			return true;
		}
	}
	
	protected void setFailureAttribute(ServletRequest request,
			ServletResponse response, boolean returnJson,
			AuthenticationException ae) {
		String className = ae.getClass().getSimpleName();
		if (returnJson) {
			request.setAttribute(getFailureKeyAttribute(), className);
		} else {
			Session session = getSubject(request, response).getSession();
			session.setAttribute(getFailureKeyAttribute(), className);
		}
	}

	protected void clearFailureAttribute(ServletRequest request,
			ServletResponse response, boolean returnJson) {
		if (returnJson) {
			request.setAttribute("user", SubjectKit.getUser());
			request.removeAttribute(getFailureKeyAttribute());
		} else {
			Session session = getSubject(request, response).getSession();
			session.removeAttribute(getFailureKeyAttribute());
			session.removeAttribute(usernameParam);
		}
	}

	// @Deprecated
	// protected void setUserAttribute(ServletRequest request, ServletResponse
	// response) {
	// Session session = getSubject(request, response).getSession();
	// session.setAttribute(AppConstants.CURRENT_USER,
	// session.getAttribute(AppConstants.TEMP_USER));
	// }

	protected String getUsername(ServletRequest request) {
		return WebUtils.getCleanParam(request, getUsernameParam());
	}

	protected String getPassword(ServletRequest request) {
		return WebUtils.getCleanParam(request, getPasswordParam());
	}

}
