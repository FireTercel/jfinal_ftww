package com.ftww.basic.plugin.shiro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import com.jfinal.kit.JsonKit;

/**
 * 处理登录成功。
 * @author DONGYU
 *
 */
public abstract class ShiroAuthenticationFilter extends ShiroAccessControlFilter {

	public static final String DEFAULT_SUCCESS_URL = "/";

	/*
	 * 定义了successUrlMap登录成功跳转路径
	 */
	private String successUrl = DEFAULT_SUCCESS_URL;
	
	/**
	 * Returns the success url to use as the default location a user is sent
	 * after logging in. Typically a redirect after login will redirect to the
	 * originally request URL; this property is provided mainly as a fallback in
	 * case the original request URL is not available or not specified.
	 * <p/>
	 * The default value is {@link #DEFAULT_SUCCESS_URL}.
	 * 
	 * @return the success url to use as the default location a user is sent
	 *         after logging in.
	 */
	public String getSuccessUrl() {
		return successUrl;
	}

	/**
	 * Sets the default/fallback success url to use as the default location a
	 * user is sent after logging in. Typically a redirect after login will
	 * redirect to the originally request URL; this property is provided mainly
	 * as a fallback in case the original request URL is not available or not
	 * specified.
	 * <p/>
	 * The default value is {@link #DEFAULT_SUCCESS_URL}.
	 * 
	 * @param successUrl
	 *            the success URL to redirect the user to after a successful
	 *            login.
	 */
	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}
	
	/**
	 * 多登录成功路径
	 */
	private Map<String, String> successUrlMap;

	public Map<String, String> getSuccessUrlMap() {
		return successUrlMap;
	}

	public void setSuccessUrlMap(Map<String, String> successUrlMap) {
		this.successUrlMap = successUrlMap;
	}
	
	/**
	 * 已授权
	 * Determines whether the current subject is authenticated.
	 * <p/>
	 * The default implementation
	 * {@link #getSubject(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 * acquires} the currently executing Subject and then returns
	 * {@link org.apache.shiro.subject.Subject#isAuthenticated()
	 * subject.isAuthenticated()};
	 * 
	 * @return true if the subject is authenticated; false if the subject is
	 *         unauthenticated
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request,ServletResponse response, Object mappedValue) throws Exception {
		Subject subject = getSubject(request, response);
		/*
		 * 已授权
		 */
		return subject.isAuthenticated();
	}

	/**
	 * 登录成功后跳转
	 * Redirects to user to the previously attempted URL after a successful
	 * login. This implementation simply calls
	 * <code>{@link org.apache.shiro.web.util.WebUtils WebUtils}.{@link org.apache.shiro.web.util.WebUtils#redirectToSavedRequest(javax.servlet.ServletRequest, javax.servlet.ServletResponse, String) redirectToSavedRequest}</code>
	 * using the {@link #getSuccessUrl() successUrl} as the {@code fallbackUrl}
	 * argument to that call.
	 * 
	 * @param request
	 *            the incoming request
	 * @param response
	 *            the outgoing response
	 * @throws Exception
	 *             if there is a problem redirecting.
	 */
	protected void issueSuccessRedirect(ServletRequest request,ServletResponse response, boolean returnJson) throws Exception {
		dynaRedirect(request, response, getRealSuccessUrl(request, response),returnJson);
		// WebUtils.redirectToSavedRequest(request, response,
		// getRealSuccessUrl(request, response));
	}

	protected void issueFailureRedirect(ServletRequest request,ServletResponse response, boolean returnJson) throws Exception {
		// RequestDispatcher rd =
		// request.getServletContext().getRequestDispatcher(getRealFailureUrl(request));
		// rd.forward(request, response);
		dynaRedirect(request, response, getRealFailureUrl(request), returnJson);
		// WebUtils.redirectToSavedRequest(request, response,
		// getRealFailureUrl(request));
	}

	/**
	 * 获取请求数据，转为 json 数据
	 * @param request
	 * @param response
	 * @param url
	 * @param returnJson
	 * @throws Exception
	 */
	protected void dynaRedirect(ServletRequest request,ServletResponse response, String url, boolean returnJson)
			throws Exception {
		if (returnJson) {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			Enumeration<String> attrNames = request.getAttributeNames();
			String name = "";
			while (attrNames.hasMoreElements()) {
				name = attrNames.nextElement();
				parameterMap.put(name, request.getAttribute(name));
			}

			PrintWriter writer = null;
			try {
				response.setCharacterEncoding("UTF-8");
				writer = response.getWriter();
				writer.write(JsonKit.toJson(parameterMap));
				writer.flush();
			} catch (IOException e) {
				throw new IOException(e);
			} finally {
				if (writer != null)
					writer.close();
			}
		} else
			WebUtils.redirectToSavedRequest(request, response, url);
	}

	/**
	 * 获取真实的登录url
	 * 
	 * @param request
	 *            the incoming request
	 * @return url
	 */
	String getRealSuccessUrl(ServletRequest request, ServletResponse response) {
		String successUrl = "";
		// 多点登录
		if (successUrlMap != null) {
			Subject subject = getSubject(request, response);
			for (String key : successUrlMap.keySet()) {
				// 得到权限key数组
				if (subject.hasRole(key)) {
					successUrl = successUrlMap.get(key);
					break;
				}

			}
		}
		if (successUrl.isEmpty())
			successUrl = getSuccessUrl();
		return successUrl;
	}


}
