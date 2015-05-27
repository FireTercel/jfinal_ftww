package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.jfinal.log.Logger;

/**
 * 系统Model
 * @author FireTercel 2015年5月27日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_systems")
public class Systems extends BaseModel<Systems> {

	private static final long serialVersionUID = 1805005435709399757L;
	
	private static Logger log = Logger.getLogger(Systems.class);
	
	public static final Systems dao = new Systems();
	
	@Override
	public void cacheAdd(String ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cacheRemove(String ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public Systems cacheGet(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
