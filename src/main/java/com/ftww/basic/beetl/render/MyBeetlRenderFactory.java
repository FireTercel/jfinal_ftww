package com.ftww.basic.beetl.render;

import org.beetl.ext.jfinal.BeetlRender;
import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.log.Logger;
import com.jfinal.render.Render;

/**
 * 为了实现视图耗时计算功能。
 * @author FireTercel
 *
 */
public class MyBeetlRenderFactory extends BeetlRenderFactory {
	
	private static Logger log=Logger.getLogger(MyBeetlRenderFactory.class);

	@Override
	public Render getRender(String view) {
		log.debug("MyBeetlRenderFactory 启动开始！");
		
		BeetlRender render = new MyBeetlRender(groupTemplate, view);
		
		log.debug("MyBeetlRenderFactory 启动完成！");
		return render;
	}

	@Override
	public String getViewExtension() {
		return ".html";
	}
	
	

}
