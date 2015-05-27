package com.ftww.basic.beetl.func;

import org.beetl.core.Context;
import org.beetl.core.Function;

import com.ftww.basic.kits.ContextKit;
import com.ftww.basic.model.Syslog;
import com.jfinal.log.Logger;

/**
 * 页面按钮权限验证函数
 * @author FireTercel
 *
 */
public class HasPrivilegeUrl implements Function {

	private static Logger log = Logger.getLogger(HasPrivilegeUrl.class);
	
	@Override
	public Object call(Object[] paras, Context ctx) {
		
		if(paras.length != 1 || null == paras[0]){
			log.error("权限标签验证，参数不正确");
			return false;
		}
		
		String url = null;
		String userIds = null;
		try {
			url = (String) paras[0]; 
			Syslog reqSysLog = (Syslog) ctx.getGlobal("reqSysLog");
			userIds = reqSysLog.getStr("userids");
		} catch (Exception e) {
			log.error("权限标签验证，获取参数异常：" + e.getMessage());
			return false;
		}
		
		boolean bool = ContextKit.hasPrivilegeUrl(url, userIds);
		
		log.debug("beetl HasPrivilegeUrl 权限标签验证：userIds=" + userIds + "，url=" + url + "，验证结果" + bool);
		
		return bool;
	}

}
