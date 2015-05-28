package com.ftww.basic.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ftww.basic.common.DictKeys;
import com.ftww.basic.common.SplitPage;
import com.ftww.basic.kits.ContextKit;
import com.ftww.basic.kits.StringKit;
import com.ftww.basic.model.BaseModel;
import com.ftww.basic.model.Syslog;
import com.ftww.basic.plugin.properties.PropertiesPlugin;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

/**
 * 公共Controller
 * @author FireTercel 2015年5月27日 
 *
 */
public abstract class BaseController extends Controller {

	private final static Logger log = Logger.getLogger(BaseController.class);
	
	protected String ids;		//主键
	protected SplitPage splitPage;		//分页封装
	protected List<?> list;		//公共List
	protected Syslog reqSysLog;		//访问日志
	
	/**
	 * 请求/WEB-INF/下的视图文件
	 */
	public void toUrl(){
		String toUrl = getPara("toUrl");
		render(toUrl);
	}
	
	/**
	 * 获取当前请求国际化参数
	 * @return
	 */
	protected String getI18nPram() {
		return getAttr("localePram");
	}
	
	/**
	 * 获取项目请求根路径
	 * @return
	 */
	protected String getCxt() {
		return getAttr("cxt");
	}
	
	/**
	 * 获取当前用户id
	 * @return
	 */
	protected String getCUserIds(){
		return getAttr("cUserIds");
	}
	
	/**
	 * 获取当前用户
	 * @return
	 */
	protected String getCUser(){
		return getAttr("cUser");
	}
	
	/**
	 * 获取参数集合paramMap
	 * @return
	 */
	protected Map<String, String> getParamMap(){
		return getAttr("paramMap");
	}
	
	/**
	 * 添加值到ParamMap
	 * @return
	 */
	protected void addToParamMap(String key, String value){
		Map<String, String> map = getAttr("paramMap");
		map.put(key, value);
	}
	
	/**
	 * 重写getPara，进行二次decode解码
	 */
	@Override
	public String getPara(String name) {
		String value = getRequest().getParameter(name);
		if(null != value && !value.isEmpty()){
			try {
				value = URLDecoder.decode(value, StringKit.encoding);
			} catch (UnsupportedEncodingException e) {
				log.error("decode异常：" + value + e.getMessage());
				e.printStackTrace();
			}
		}
		return value;
	}
	
	/**
	 * 获取checkbox值，数组
	 * @param name
	 * @return
	 */
	protected String[] getParas(String name) {
		return getRequest().getParameterValues(name);
	}
	
	/**
	 * 判断验证码是否正确
	 * @return
	 */
	protected boolean authCode(){
		String authCodeParam = getPara("authCode");
		String authCodeCookie = ContextKit.getAuthCode(getRequest());
		if(null != authCodeParam && null != authCodeCookie){
			authCodeParam = authCodeParam.toLowerCase();
			authCodeCookie = authCodeCookie.toLowerCase();
			if(authCodeParam.equals(authCodeCookie)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 效验Referer有效性（防止别人盗用系统链接）
	 * @return
	 */
	protected boolean authReferer() {
		String referer = getRequest().getHeader("Referer");
		if (null != referer && !referer.trim().equals("")) {
			referer = referer.toLowerCase();
			String domainStr = (String) PropertiesPlugin.getParamMapValue(DictKeys.config_domain_key);
			String[] domainArr = domainStr.split(",");
			for (String domain : domainArr) {
				if (referer.indexOf(domain.trim()) != -1) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断请求者和数据创建者是否一致
	 * @param entity
	 * @param user
	 * @return
	 */
	protected <T extends BaseModel<?>> boolean authCreate(T model){
		String createids = model.getStr("createids");
		if(null != createids && !createids.isEmpty()){
			String createUserIds = ContextKit.getCurrentUser(getRequest(),false).getPKValue();
			if(createids.equals(createUserIds)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取查询参数
	 * 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 * @return
	 */
	protected Map<String, String> getQueryParam(){
		Map<String, String> queryParam = new HashMap<String, String>();
		Enumeration<String> paramNames = getParaNames();
		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement();
			String value = getPara(name);
			if (name.startsWith("_query") && !value.isEmpty()) {// 查询参数分拣
				String key = name.substring(7);
				if(null != value && !value.trim().equals("")){
					queryParam.put(key, value.trim());
				}
			}
		}
		
		return queryParam;
	}
	
	/**
	 * 设置默认排序
	 * @param colunm
	 * @param mode
	 */
	protected void defaultOrder(String colunm, String mode){
		if(null == splitPage.getOrderColunm() || splitPage.getOrderColunm().isEmpty()){
			splitPage.setOrderColunm(colunm);
			splitPage.setOrderMode(mode);
		}
	}
	
	/**
	 * 排序条件
	 * 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 * @return
	 */
	protected String getOrderColunm(){
		String orderColunm = getPara("orderColunm");
		return orderColunm;
	}
	
	/**
	 * 排序方式
	 * 说明：和分页分拣一样，但是应用场景不一样，主要是给查询导出的之类的功能使用
	 * @return
	 */
	protected String getOrderMode(){
		String orderMode = getPara("orderMode");
		return orderMode;
	}
	
	/************************************		get 	set 	方法		************************************************/

	public Syslog getReqSysLog() {
		return reqSysLog;
	}

	public void setReqSysLog(Syslog reqSysLog) {
		this.reqSysLog = reqSysLog;
	}

	public void setSplitPage(SplitPage splitPage) {
		this.splitPage = splitPage;
	}
	
	
	
	
	
}
