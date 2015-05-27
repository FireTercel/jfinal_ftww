package com.ftww.basic.beetl.func;

import org.beetl.core.Context;
import org.beetl.core.Function;

import com.ftww.basic.common.SplitPage;
import com.jfinal.log.Logger;

/**
 * 分页列表排序函数
 * @author FireTercel
 *
 */
public class OrderBy implements Function {
	
	private static Logger log = Logger.getLogger(OrderBy.class);

	/**
	 * 分页列表排序
	 */
	@Override
	public Object call(Object[] paras, Context ctx) {
		if(paras.length != 1 || null == paras[0]){
			log.error("分页列表排序标签，参数不正确");
			return "";
		}
		
		String orderLaber = null;// 排序列
		String orderColunm = null;// 排序条件
		String orderMode = null;// 排序方式
		
		try {
			orderLaber = (String) paras[0];
			SplitPage splitPage = (SplitPage) ctx.getGlobal("splitPage");
			
			orderColunm = splitPage.getOrderColunm();
			orderMode = splitPage.getOrderMode();
		} catch (Exception e) {
			log.error("分页列表排序标签，获取参数异常：" + e.getMessage());
			return "";
		}
		
		log.debug("分页列表排序，orderLaber=" + orderLaber + "， orderColunm=" + orderColunm + "，orderMode= " + orderMode);
		
		if(null != orderMode && orderLaber.equals(orderColunm)){
			if(orderMode.equals("asc")){
				return "<img src='/files/images/platform/order/asc.gif' />";
			}else if(orderMode.equals("desc")){
				return "<img src='/files/images/platform/order/desc.gif' />";
			}
		}
		
		return "";
	}

}
