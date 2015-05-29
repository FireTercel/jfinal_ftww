package com.ftww.basic.service;

import java.util.LinkedList;
import java.util.Map;

import com.ftww.basic.common.DictKeys;
import com.ftww.basic.common.SplitPage;
import com.ftww.basic.kits.MailKit;
import com.ftww.basic.kits.SqlXmlKit;
import com.ftww.basic.plugin.i18n.I18NPlugin;
import com.ftww.basic.plugin.properties.PropertiesPlugin;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * 基础Service层，放置业务逻辑。供controller调用。
 * @author FireTercel 2015年5月28日 
 *
 */
public abstract class BaseService {
	
	private static Logger log = Logger.getLogger(BaseService.class);
	
	/**
	 * 获取SQL，固定的SQL
	 * @param sqlId
	 * @return
	 */
	protected String getSql(String sqlId){
		return SqlXmlKit.getSql(sqlId);
	}
	
	/**
	 * 获取SQL，动态的SQL语句
	 * @param sqlId
	 * @param param
	 * @return
	 */
	protected String getSql(String sqlId,Map<String,Object> param){
		return SqlXmlKit.getSql(sqlId, param);
	}
	
	/**
	 * 获取SQL，动态的SQL语句
	 * @param sqlId
	 * @param param
	 * @param list
	 * @return
	 */
	protected String getSql(String sqlId,Map<String,String> param,LinkedList<Object> list){
		return SqlXmlKit.getSql(sqlId, param, list);
	}
	
	/**
	 * 根据i18n参数查询获取哪个字段的值
	 * @param i18n
	 * @return
	 */
	protected String i18n(String i18n){
		return I18NPlugin.i18n(i18n);
	}
	
	/**
	 * 把11,22,33...转成'11','22','33'...
	 * @param ids
	 * @return
	 */
	protected String toSql(String ids){
		if(null == ids || ids.trim().isEmpty()){
			return null;
		}
		
		String[] idsArr = ids.split(",");
		StringBuilder sqlSb = new StringBuilder();
		int length = idsArr.length;
		
		for (int i = 0, size = length -1; i < size; i++) {
			sqlSb.append(" '").append(idsArr[i]).append("', ");
		}
		if(length != 0){
			sqlSb.append(" '").append(idsArr[length-1]).append("' ");
		}
		
		return sqlSb.toString();
	}
	
	/**
	 * 把数组转成'11','22','33'...
	 * @param ids
	 * @return
	 */
	protected String toSql(String[] idsArr){
		if(idsArr == null || idsArr.length == 0){
			return null;
		}
		
		StringBuilder sqlSb = new StringBuilder();
		int length = idsArr.length;
		for (int i = 0, size = length -1; i < size; i++) {
			sqlSb.append(" '").append(idsArr[i]).append("', ");
		}
		if(length != 0){
			sqlSb.append(" '").append(idsArr[length-1]).append("' ");
		}
		
		return sqlSb.toString();
	}
	
	/**
	 * 分页
	 * @param dataSource
	 * @param splitPage
	 * @param select
	 * @param sqlId
	 */
	protected void splitPageBase(String dataSource , SplitPage splitPage, String select , String sqlId){
		// 接收返回值对象
		StringBuilder formSqlSb = new StringBuilder();
		LinkedList<Object> paramValue = new LinkedList<Object>();
		
		// 调用生成from sql，并构造paramValue
		String sql = SqlXmlKit.getSql(sqlId, splitPage.getQueryParam(), paramValue);
		formSqlSb.append(sql);
		
		// 行级：过滤
		rowFilter(formSqlSb);
		
		// 排序
		String orderColunm = splitPage.getOrderColunm();
		String orderMode = splitPage.getOrderMode();
		if(null != orderColunm && !orderColunm.isEmpty() && null != orderMode && !orderMode.isEmpty()){
			formSqlSb.append(" order by ").append(orderColunm).append(" ").append(orderMode);
		}
		
		String formSql = formSqlSb.toString();

		Page<?> page = Db.use(dataSource).paginate(splitPage.getPageNumber(), splitPage.getPageSize(), select, formSql, paramValue.toArray());
		splitPage.setTotalPage(page.getTotalPage());
		splitPage.setTotalRow(page.getTotalRow());
		splitPage.setList(page.getList());
		splitPage.compute();
	}
	
	/**
	 * 行级：过滤
	 * @return
	 */
	protected void rowFilter(StringBuilder formSqlSb){
		
	}
	
	/**
	 * 发送邮件对象
	 */
	protected MailKit sendTextMail(){
		MailKit mail = new MailKit();
		mail.setHost((String)PropertiesPlugin.getParamMapValue(DictKeys.config_mail_host));
		mail.setPort((String)PropertiesPlugin.getParamMapValue(DictKeys.config_mail_port));
		mail.setValidate(true);
		mail.setUserName((String)PropertiesPlugin.getParamMapValue(DictKeys.config_mail_userName));
		mail.setPassword((String)PropertiesPlugin.getParamMapValue(DictKeys.config_mail_password));
		mail.setFrom((String)PropertiesPlugin.getParamMapValue(DictKeys.config_mail_from));
		return mail;
		
	}

}
