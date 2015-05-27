package com.ftww.basic.beetl.func;

import org.apache.commons.lang3.StringEscapeUtils;
import org.beetl.core.Context;
import org.beetl.core.Function;
import com.jfinal.log.Logger;

/**
 * 过滤xml文档函数
 * @author FireTercel
 *
 */
public class EscapeXml implements Function {

	private static Logger log = Logger.getLogger(EscapeXml.class);
	
	/**
	 * 过滤xml文档函数实现
	 */
	@Override
	public Object call(Object[] paras, Context ctx) {
		if(paras.length != 1 || null == paras[0] || !(paras[0] instanceof String)){
			return "";
		}
		String content = null;// 
		try {
			content = (String) paras[0];
		} catch (Exception e) {
			return "";
		}

		log.debug("EscapeXml，xml转换处理，content=" + content);
		
		return StringEscapeUtils.escapeXml(content);
	}

}
