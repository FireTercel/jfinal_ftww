package com.ftww.basic.interceptor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.ftww.basic.common.SplitPage;
import com.ftww.basic.controller.BaseController;
import com.ftww.basic.kits.DateTimeKit;
import com.ftww.basic.model.Operator;
import com.ftww.basic.model.Syslog;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.log.Logger;

/**
 * 参数封装拦截器
 * @author FireTercel 2015年5月28日 
 *
 */
public class ParamPkgInterceptor implements Interceptor {

	private static Logger log = Logger.getLogger(ParamPkgInterceptor.class);
	
	@Override
	public void intercept(ActionInvocation ai) {
		
		BaseController controller = (BaseController) ai.getController();
		
		Class<?> controllerClass = controller.getClass();
		Class<?> superControllerClass = controllerClass.getSuperclass();
		
		Field[] fields = controllerClass.getDeclaredFields();
		Field[] parentFields = superControllerClass.getDeclaredFields();
		
		log.debug("*********************** 封装参数值到 controller 全局变量  start ***********************");
		
		Syslog reqSysLog = controller.getReqSysLog();
		String operatorids = reqSysLog.getStr("operatorids");
		Operator operator = Operator.dao.cacheGet(operatorids);
		String splitpage = operator.getStr("splitpage");
		
		
		
		
		
	}
	
	/**
	 * 分页参数处理
	 * @param controller
	 * @param superControllerClass
	 */
	public void splitPage(BaseController controller, Class<?> superControllerClass){
		SplitPage splitPage = new SplitPage();
		
		//分页参数
		Map<String,String> queryParam = new HashMap<String,String>();
		Enumeration<String> paramNames = controller.getParaNames();
		while(paramNames.hasMoreElements()){
			String name = paramNames.nextElement();
			if(name.startsWith("_query.")){
				String value = controller.getPara(name);
				log.debug(" 分页，查询参数：name = "+ name + " value = "+ value);
				if(null != value){
					value = value.trim();
					if(!value.isEmpty()){
						String key = name.substring(7);
						queryParam.put(key, value);
					}
				}
			}
		}
		splitPage.setQueryParam(queryParam);
		// 排序条件
		String orderColunm = controller.getPara("orderColunm");
		if(null != orderColunm && !orderColunm.isEmpty()){
			log.debug("分页，排序条件：orderColunm = " + orderColunm);
			splitPage.setOrderColunm(orderColunm);
		}
		// 排序方式
		String orderMode = controller.getPara("orderMode");
		if(null != orderMode && !orderMode.isEmpty()){
			log.debug("分页，排序方式：orderMode = " + orderMode);
			splitPage.setOrderMode(orderMode);
		}
		// 第几页
		String pageNumber = controller.getPara("pageNumber");
		if(null != pageNumber && !pageNumber.isEmpty()){
			log.debug("分页，第几页：pageNumber = " + pageNumber);
			splitPage.setPageNumber(Integer.parseInt(pageNumber));
		}
		// 每页显示数量
		String pageSize = controller.getPara("pageSize");
		if(null != pageSize && !pageSize.isEmpty()){
			log.debug("分页，每页显示几多：pageSize = " + pageSize);
			splitPage.setPageSize(Integer.parseInt(pageSize));
		}
		
		controller.setSplitPage(splitPage);
	}
	
	/**
	 * 反射set值到全局变量
	 * @param controller
	 * @param field
	 */
	public void setControllerFieldValue(BaseController controller,Field field){
		try {
			field.setAccessible(true);
			String name = field.getName();
			String value = controller.getPara(name);
			
			if(null == value || value.trim().isEmpty()){// 参数值为空直接结束
				log.debug("封装参数值到全局变量：field name = " + name + " value = 空");
				return;
			}
			log.debug("封装参数值到全局变量：field name = " + name + " value = " + value);
			String fieldType = field.getType().getSimpleName();
			if(fieldType.equals("String")){
				field.set(controller, value);
			
			}else if(fieldType.equals("int")){
				field.set(controller, Integer.parseInt(value));
				
			}else if(fieldType.equals("Date")){
				int dateLength = value.length();
				if(dateLength == DateTimeKit.pattern_ymd.length()){
					field.set(controller, DateTimeKit.parse(value, DateTimeKit.pattern_ymd));
				
				}else if(dateLength == DateTimeKit.pattern_ymd_hms.length()){
					field.set(controller, DateTimeKit.parse(value, DateTimeKit.pattern_ymd_hms));
				
				}else if(dateLength == DateTimeKit.pattern_ymd_hms_s.length()){
					field.set(controller, DateTimeKit.parse(value, DateTimeKit.pattern_ymd_hms_s));
				}
			}else if(fieldType.equals("BigDecimal")){
				BigDecimal bdValue = new BigDecimal(value);
				field.set(controller, bdValue);
				
			}else{
				log.debug("没有解析到有效字段类型");
			}
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} finally {
			field.setAccessible(false);
		}
	}
	
	/**
	 * 反射全局变量值到request
	 * @param controller
	 * @param field
	 */
	public void setRequestValue(BaseController controller, Field field){
		
		try {
			field.setAccessible(true);
			String name = field.getName();
			Object value = field.get(controller);
			// 参数值为空直接结束
			if(null == value || (value instanceof String && ((String)value).isEmpty())){
				log.debug("设置全局变量到request：field name = " + name + " value = 空");
				return;
			}
			log.debug("设置全局变量到request：field name = " + name + " value = " + value);
			controller.setAttr(name, value);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} finally {
			field.setAccessible(false);
		}
		
	}
	
	

}
