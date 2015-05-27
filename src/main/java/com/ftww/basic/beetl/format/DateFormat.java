package com.ftww.basic.beetl.format;

import org.beetl.core.Format;

/**
 * beetl页面日期格式化，重写是为了处理Oracle的TIMESTAMP
 * （未导入oracle的jar包，该类暂不使用）
 * @author FireTercel 2015年5月27日 
 *
 */
public class DateFormat implements Format{

	/**
	 * 实现格式化处理方法
	 */
	@Override
	public Object format(Object data, String pattern) {
		// TODO Auto-generated method stub
		return null;
	}

}
