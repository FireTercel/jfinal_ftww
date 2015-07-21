package com.ftww.basic.service;

import com.ftww.basic.model.Permission;
import com.jfinal.log.Logger;

public class PermissionService extends BaseService{
	
	private static Logger log = Logger.getLogger(PermissionService.class);
	
	public final static PermissionService service = new PermissionService();
	
	public String save(Permission permission){
		permission.save();
		String ids = permission.getPKValue();
		
		Permission.dao.cacheAdd(ids);
		return ids;
	}
	
	public boolean update(Permission permission){
		boolean result = permission.update();
		String ids = permission.getPKValue();
		Permission.dao.cacheAdd(ids);
		return result;
	}
	
	public boolean delete(String ids){
		Permission.dao.cacheRemove(ids);
		return Permission.dao.deleteById(ids);
	}

}
