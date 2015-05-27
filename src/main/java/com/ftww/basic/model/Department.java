package com.ftww.basic.model;

import org.apache.log4j.Logger;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;

/**
 * 部门Model
 * @author FireTercel
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_department")
public class Department extends BaseModel<Department> {

	
	private static final long serialVersionUID = 2005820163812101602L;
	
	private static Logger log=Logger.getLogger(Department.class);
	
	public static final Department dao = new Department();

	@Override
	public void cacheAdd(String ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cacheRemove(String ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Department cacheGet(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
