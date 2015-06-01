package com.ftww.basic.service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ftww.basic.common.DictKeys;
import com.ftww.basic.kits.DateTimeKit;
import com.ftww.basic.kits.OSKit;
import com.ftww.basic.model.Resources;
import com.ftww.basic.plugin.properties.PropertiesPlugin;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 系统资源负载
 * @author FireTercel 2015年6月1日 
 *
 */
public class ResourcesService extends BaseService {
	
	private static Logger log = Logger.getLogger(ResourcesService.class);
	
	public static final ResourcesService service = new ResourcesService();
	
	/**
	 * 最近15天PV
	 * @return
	 */
	public Map<String , Object> pv(){
		Date endDate = DateTimeKit.endDateByDay(DateTimeKit.getDate());
		Date startDate = DateTimeKit.startDateByDay(endDate, -14);
		
		List<Record> list = null;
		String db_type = (String) PropertiesPlugin.getParamMapValue(DictKeys.db_type_key);
		String sql = null;
		if(db_type.equals(DictKeys.db_type_postgresql)){ // pg
			sql = getSql("basic.resources.pv_pg");
		
		}else if(db_type.equals(DictKeys.db_type_mysql)){ // mysql
			sql = getSql("basic.resources.pv_mysql");
		
		}else if(db_type.equals(DictKeys.db_type_oracle)){ // oracle
			sql = getSql("basic.resources.pv_oracle");
			
		}
		list = Db.use(DictKeys.db_dataSource_main).find(sql, DateTimeKit.getSqlTimestamp(startDate), DateTimeKit.getSqlTimestamp(endDate));
		
		List<String> adates = new LinkedList<String>();
		List<Long> acounts = new LinkedList<Long>();
		
		for(Record record:list){
			adates.add(DateTimeKit.format(record.getStr("adates"), DateTimeKit.pattern_ymd, "MM-dd"));
			acounts.add(record.getNumber("acounts").longValue());
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("adates", JSON.toJSON(adates).toString());
		map.put("acounts", JSON.toJSON(acounts).toString());
		return map;
	}
	
	/**
	 * 获取系统负载信息
	 * @return
	 */
	public Map<String,Object> getResources(){
		Date endDate = DateTimeKit.endDateByHour(new Date());
		Date startDate = DateTimeKit.startDateByHour(endDate, -24);
		
		String hostName = OSKit.getOsLocalHostName();//获取本机名称
		
		String sql = getSql("basic.resources.24hour");
		List<Resources> list = Resources.dao.find(sql, hostName, DateTimeKit.getSqlTimestamp(startDate), DateTimeKit.getSqlTimestamp(endDate));
		
		List<String> datesList = new LinkedList<String>();
		List<Integer> cpuList = new LinkedList<Integer>();
		List<Long> phymemoryList = new LinkedList<Long>();
		List<Long> jvmmemoryList = new LinkedList<Long>();
		List<Long> gccountList = new LinkedList<Long>();
		
		Long phymemory = 0l;
		Long jvmmaxmemory = 0l;
		
		for (Resources resources : list) {
			phymemory = resources.getNumber("phymemory").longValue();
			jvmmaxmemory = resources.getNumber("jvmmaxmemory").longValue();
			datesList.add(DateTimeKit.format(resources.getDate("createdate"), "HH:mm"));
			int cpuratio = (int)(resources.getBigDecimal("cpuratio").doubleValue() * 100);
			cpuList.add(Integer.valueOf(cpuratio));
			phymemoryList.add(resources.getNumber("phymemory").longValue() - resources.getNumber("phyfreememory").longValue());
			jvmmemoryList.add(resources.getNumber("jvmtotalmemory").longValue() - resources.getNumber("jvmfreememory").longValue());
			gccountList.add(resources.getNumber("gccount").longValue());
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dates", JSON.toJSON(datesList).toString());
		map.put("cpu", JSON.toJSON(cpuList).toString());
		map.put("phymemorymax", phymemory);
		map.put("phymemoryval", JSON.toJSON(phymemoryList).toString());
		map.put("jvmmemorymax", jvmmaxmemory);
		map.put("jvmmemoryval", JSON.toJSON(jvmmemoryList).toString());
		map.put("gccount", JSON.toJSON(gccountList).toString());
		return map;
		
	}
	
}
