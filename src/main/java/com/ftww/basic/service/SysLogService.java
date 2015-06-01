package com.ftww.basic.service;

import com.ftww.basic.common.DictKeys;
import com.ftww.basic.common.SplitPage;
import com.ftww.basic.model.Syslog;
import com.jfinal.log.Logger;

/**
 * 日志业务层
 * @author FireTercel 2015年6月1日 
 *
 */
public class SysLogService extends BaseService {
	
	private static Logger log= Logger.getLogger(SysLogService.class);
	
	public static final SysLogService service = new SysLogService();
	
	/**
	 * 获取Syslog对象，外连接pt_user、pt_operator表
	 * @param ids
	 * @return
	 */
	public Syslog view(String ids){
		String sql = getSql("basic.sysLog.view");
		return Syslog.dao.findFirst(sql, ids);
	}
	
	/**
	 * 分页查询，结果保存到参数SplitPage
	 * @param splitPage
	 */
	public void list(SplitPage splitPage){
		String select = " select o.names onames, o.url ourl, u.username, s.* ";
		splitPageBase(DictKeys.db_dataSource_main, splitPage, select, "basic.sysLog.splitPage");
	}

}
