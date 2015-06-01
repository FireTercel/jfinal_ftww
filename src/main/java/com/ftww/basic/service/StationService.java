package com.ftww.basic.service;

import java.util.List;

import com.ftww.basic.common.DictKeys;
import com.ftww.basic.model.Station;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 职位业务层
 * @author FireTercel 2015年6月1日 
 *
 */
public class StationService extends BaseService {

	private static Logger log = Logger.getLogger(StationService.class);

	public static final StationService service = new StationService();
	
	/**
	 * 获取子节点数据
	 * @param parentIds
	 * @return
	 */
	public String childNodeData(String parentIds){
		List<Station> list = null;
		if(null != parentIds){
			String sql = getSql("basic.station.child");
			list = Station.dao.find(sql, parentIds);
			
		}else{
			String sql = getSql("basic.station.root");
			list = Station.dao.find(sql);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		int size = list.size() - 1;
		for (Station station : list) {
			sb.append(" { ");
			sb.append(" id : '").append(station.getPKValue()).append("', ");
			sb.append(" name : '").append(station.getStr("names")).append("', ");
			sb.append(" isParent : true, ");
			sb.append(" font : {'font-weight':'bold'}, ");
			sb.append(" icon : '").append("/jsFile/zTree/css/zTreeStyle/img/diy/").append(station.getStr("images")).append("' ");
			sb.append(" }");
			if(list.indexOf(station) < size){
				sb.append(", ");
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	
	/**
	 * 保存
	 * @param pIds
	 * @param names
	 * @param orderIds
	 * @return
	 */
	public String save(String pIds, String names, int orderIds) {
		Station pStation = Station.dao.findById(pIds);
		pStation.set("isparent", "true").update();
		
		String images = "";
		if(orderIds < 2 || orderIds > 9){
			orderIds = 2;
			images = "2.png";
		}else{
			images = orderIds + ".png";
		}
		
		Station station = new Station();
		station.set("isparent", "false");
		station.set("parentstationids", pIds);
		station.set("orderids", orderIds);
		station.set("names", names);
		station.set("images", images);
		station.save();
		
		// 缓存
		Station.dao.cacheAdd(station.getPKValue());
		
		return station.getPKValue();
	}
	
	/**
	 * 更新
	 * @param ids
	 * @param pIds
	 * @param names
	 */
	public void update(String ids, String pIds, String names) {
		Station station = Station.dao.findById(ids);
		if(null != names && !names.isEmpty()){
			//更新模块名称
			station.set("names", names).update();
			
		}else if(null != pIds && !pIds.isEmpty()){
			//更新上级模块
			station.set("parentstationids", pIds).update();
		}

		// 缓存
		Station.dao.cacheAdd(ids);
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public boolean delete(String ids) {
		String sql = getSql("basic.station.childCount");
		Record record = Db.use(DictKeys.db_dataSource_main).findFirst(sql, ids);
		Long counts = record.getNumber("counts").longValue();
	    if(counts > 1){
	    	return false;
	    }
	    
		// 缓存
		Station.dao.cacheRemove(ids);
		
		// 删除
	    Station.dao.deleteById(ids);
	    return true;
	}
	
	/**
	 * 设置岗位功能
	 * @param roleIds
	 * @param moduleIds
	 * @param operatorIds
	 */
	public void setOperator(String stationIds, String moduleIds, String operatorIds){
		Station station = Station.dao.findById(stationIds);
		//station.set("moduleids", moduleIds);
		station.set("operatorids", operatorIds).update();
		
		// 缓存
		Station.dao.cacheAdd(stationIds);
	}
	
}
