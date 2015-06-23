package com.ftww.basic.freemarker.render;

import com.jfinal.log.Logger;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;

public class FreeMarkerRenderFactory implements IMainRenderFactory{

	private static Logger log = Logger.getLogger(FreeMarkerRenderFactory.class);
	
	@Override
	public Render getRender(String view) {
		log.debug("FreeMarkerRenderFactory start ");
		FreeMarkerRender render = new FreeMarkerRender(view);
		log.debug("FreeMarkerRenderFactory end ");
		return render;
	}

	@Override
	public String getViewExtension() {
		return ".html";
	}
	

}
