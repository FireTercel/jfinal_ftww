package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.jfinal.log.Logger;

/**
 * 日志Mode
 * @author FireTercel 2015年5月27日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_syslog")
public class Syslog extends BaseModel<Syslog> {

	private static final long serialVersionUID = 4827736577139719285L;
	
	private static Logger log = Logger.getLogger(Syslog.class);
	public static final Syslog dao = new Syslog();
	
	@Override
	public void cacheAdd(String ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cacheRemove(String ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public Syslog cacheGet(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
