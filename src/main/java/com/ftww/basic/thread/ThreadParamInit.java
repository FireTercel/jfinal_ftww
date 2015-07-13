package com.ftww.basic.thread;

import java.util.List;

import com.ftww.basic.kits.SqlXmlKit;
import com.ftww.basic.model.Dict;
import com.ftww.basic.model.Group;
import com.ftww.basic.model.Operator;
import com.ftww.basic.model.Param;
import com.ftww.basic.model.Role;
import com.ftww.basic.model.Station;
import com.ftww.basic.model.User;
import com.jfinal.log.Logger;

/**
 * 系统初始化缓存操作类
 * @author FireTercel
 *
 */
public class ThreadParamInit extends Thread {
	
	private static Logger log = Logger.getLogger(ThreadParamInit.class);
	
	public static String cacheStart_user = "user_";
	public static String cacheStart_group = "group_";
	public static String cacheStart_role = "role_";
	public static String cacheStart_permission = "permission_";
	public static String cacheStart_station = "station_";
	public static String cacheStart_operator = "operator_";
	public static String cacheStart_dict = "dict_";
	public static String cacheStart_dict_child =  "dict_child_";
	public static String cacheStart_param = "param_";
	public static String cacheStart_param_child =  "param_child_";
	
	@Override
	public void run() {
		cacheAll();
	}
	
	/**
	 * 缓存所有参数
	 */
	public static synchronized void cacheAll(){
		log.info("初始化缓存参数……  开始……");

		// 1.缓存用户
		basic_cacheUser();

		// 2.缓存组
		basic_cacheGroup();

		// 3.缓存角色
		basic_cacheRole();

		// 4.缓存岗位
		basic_cacheStation();

		// 5.缓存功能
		basic_cacheOperator();

		// 6.缓存字典
		basic_cacheDict();

		// 6.缓存参数
		basic_cacheParam();

		log.info("初始化缓存参数……  结束……");
	}
	
	/**
	 * 缓存所有用户
	 * @author 董华健    2012-10-16 下午1:16:48
	 */
	public static void basic_cacheUser() {
		log.info("缓存加载：【User】……  开始……");
		String sql = SqlXmlKit.getSql("basic.user.all");
		List<User> userList = User.dao.find(sql);
		for (User user : userList) {
			User.dao.cacheAdd(user.getPKValue());
			user = null;
		}
		log.info("缓存加载：【User】……  结束……, size = " + userList.size());
		userList = null;
	}
	
	/**
	 * 缓存所有组
	 * @author 董华健    2012-10-16 下午1:17:20
	 */
	public static void basic_cacheGroup() {
		log.info("缓存加载：【Group】……  开始……");
		String sql = SqlXmlKit.getSql("basic.group.all");
		List<Group> groupList = Group.dao.find(sql);
		for (Group group : groupList) {
			Group.dao.cacheAdd(group.getPKValue());
		}
		log.info("缓存加载：【Group】……  结束……, size = " + groupList.size());
		groupList = null;
	}
	
	/**
	 * 缓存所有角色
	 * @author 董华健    2012-10-16 下午1:17:20
	 */
	public static void basic_cacheRole() {
		log.info("缓存加载：【Role】……  开始……");
		String sql = SqlXmlKit.getSql("basic.role.all");
		List<Role> roleList = Role.dao.find(sql);
		for (Role role : roleList) {
			Role.dao.cacheAdd(role.getPKValue());
		}
		log.info("缓存加载：【Role】……  结束……, size = " + roleList.size());
		roleList = null;
	}
	
	/**
	 * 缓存所有的岗位
	 * @author 董华健    2013-07-16 下午1:17:20
	 */
	public static void basic_cacheStation() {
		log.info("缓存加载：【Station】……  开始……");
		String sql = SqlXmlKit.getSql("basic.station.all");
		List<Station> stationList = Station.dao.find(sql);
		for (Station station : stationList) {
			Station.dao.cacheAdd(station.getPKValue());
		}
		log.info("缓存加载：【Station】……  结束……, size = " + stationList.size());
		stationList = null;
	}
	
	/**
	 * 缓存操作
	 * @author 董华健    2012-10-16 下午1:17:12
	 */
	public static void basic_cacheOperator() {
		log.info("缓存加载：【Operator】……  开始……");
		String sql = SqlXmlKit.getSql("basic.operator.all");
		List<Operator> operatorList = Operator.dao.find(sql);
		for (Operator operator : operatorList) {
			Operator.dao.cacheAdd(operator.getPKValue());
			operator = null;
		}
		log.info("缓存加载：【Operator】……  结束……, size = " + operatorList.size());
		operatorList = null;
	}
	
	/**
	 * 缓存业务字典
	 * @author 董华健    2012-10-16 下午1:17:04
	 */
	public static void basic_cacheDict() {
		log.info("缓存加载：【Dict】……  开始……");
		String sql = SqlXmlKit.getSql("basic.dict.all");
		List<Dict> dictList = Dict.dao.find(sql);
		for (Dict dict : dictList) {
			Dict.dao.cacheAdd(dict.getPKValue());
			dict = null;
		}
		log.info("缓存加载：【Dict】……  结束……, size = " + dictList.size());
		dictList = null;
	}
	
	/**
	 * 缓存业务参数
	 * @author 董华健    2012-10-16 下午1:17:04
	 */
	public static void basic_cacheParam() {
		log.info("缓存加载：【Param】……  开始……");
		String sql = SqlXmlKit.getSql("basic.param.all");
		List<Param> paramList = Param.dao.find(sql);
		for (Param param : paramList) {
			Param.dao.cacheAdd(param.getPKValue());
			param = null;
		}
		log.info("缓存加载：【Param】……  结束……, size = " + paramList.size());
		paramList = null;
	}

}
