package com.ftww.basic.plugin.sqlinxml;

import com.ftww.basic.kits.SqlXmlKit;
import com.jfinal.plugin.IPlugin;

/**
 * 加载SQL文件
 * @author FireTercel 2015年5月27日 
 *
 */
public class SqlXmlPlugin implements IPlugin {
	
	public SqlXmlPlugin(){
		
	}

	@Override
	public boolean start() {
		SqlXmlKit.init(true);
		return true;
	}

	@Override
	public boolean stop() {
		SqlXmlKit.destroy();
		return true;
	}

}
