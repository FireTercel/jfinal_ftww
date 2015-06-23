package com.ftww.basic.freemarker.render;

import com.jfinal.log.Logger;
import com.jfinal.render.FreeMarkerRender;

public class MyFreeMarkerRender extends FreeMarkerRender{
	
	private static Logger log = Logger.getLogger(MyFreeMarkerRender.class);
	
	public static final String viewTimeKey = "viewTime";
	
	public MyFreeMarkerRender(String view) {
		super(view);
	}

	@Override
	public void render() {
		long start = System.currentTimeMillis();
		log.debug("MyFreeMarkerRender render start time = "+ start);
		super.render();
		long end=System.currentTimeMillis();
		long viewTime = end - start;
		
		log.debug("MyFreeMarkerRender render end time = "+ end +" , viewTime = "+ viewTime);
		
		request.setAttribute(MyFreeMarkerRender.viewTimeKey, viewTime);
		
	}
	
	

}
