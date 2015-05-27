package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.jfinal.log.Logger;

/**
 * 资源负载Model
 * @author FireTercel 2015年5月27日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_resources")
public class Resources extends BaseModel<Resources> {

	private static final long serialVersionUID = 4367552946581258554L;
	
	private static Logger log = Logger.getLogger(Resources.class);
	
	public static final Resources dao = new Resources();	
	@Override
	public void cacheAdd(String ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cacheRemove(String ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public Resources cacheGet(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
