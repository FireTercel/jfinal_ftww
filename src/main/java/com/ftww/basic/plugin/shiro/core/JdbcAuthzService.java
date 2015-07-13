package com.ftww.basic.plugin.shiro.core;

import java.util.Map;

import com.ftww.basic.plugin.shiro.core.handler.AuthzHandler;

/**
 * 组合模式访问控制处理器接口
 * Created by wangrenhui on 14-1-7.
 */
public interface JdbcAuthzService {
	
	public Map<String, AuthzHandler> getJdbcAuthz();

}
