package com.ftww.basic.model;

import com.ftww.basic.annotation.Table;
import com.ftww.basic.common.DictKeys;
import com.jfinal.log.Logger;

/**
 * 菜单model
 * @author FireTercel 2015年5月27日 
 *
 */
@Table(dataSourceName = DictKeys.db_dataSource_main, tableName = "pt_menu")
public class Menu extends BaseModel<Menu> {
	
	private static final long serialVersionUID = -4840820904862218414L;

	private static Logger log = Logger.getLogger(Menu.class);
	
	public static final Menu dao = new Menu();
	
	public Operator getOperator(){
		String operatorIds = get("operatorids");
		if(null != operatorIds && !operatorIds.isEmpty()){
			return Operator.dao.findById(operatorIds);
		}
		return null;
	}
	
	@Override
	public void cacheAdd(String ids) {

	}

	@Override
	public void cacheRemove(String ids) {

	}

	@Override
	public Menu cacheGet(String key) {
		return null;
	}

}
