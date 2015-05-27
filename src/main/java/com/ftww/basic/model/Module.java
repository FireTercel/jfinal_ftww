package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.jfinal.log.Logger;


/**
 * 模块model
 * @author FireTercel 2015年5月27日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_module")
public class Module extends BaseModel<Module> {

	private static final long serialVersionUID = -9132533909032693548L;
	private static Logger log = Logger.getLogger(Module.class);
	
	public static final Module dao = new Module();

	@Override
	public void cacheAdd(String ids) {

	}

	@Override
	public void cacheRemove(String ids) {

	}

	@Override
	public Module cacheGet(String key) {
		return null;
	}

}
