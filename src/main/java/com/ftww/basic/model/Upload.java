package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.jfinal.log.Logger;

/**
 * 上次文件Model
 * @author FireTercel 2015年5月27日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_upload")
public class Upload extends BaseModel<Upload> {

	private static final long serialVersionUID = -2229477576165069889L;

	private static Logger log = Logger.getLogger(Upload.class);
	
	public static final Upload dao = new Upload();
	@Override
	public void cacheAdd(String ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cacheRemove(String ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public Upload cacheGet(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
