package com.ftww.basic.servlet;

import java.io.Writer;

import org.apache.log4j.Layout;
import org.apache.log4j.WriterAppender;

/**
 * log4j输出处理类
 * @author FireTercel 2015年5月28日 
 *
 */
public class Log4jAppender extends WriterAppender {
	
	private Writer writer = new Log4jAsyncWriter();
	
	public Log4jAppender(){
		setWriter(writer);
	}
	
	public Log4jAppender(Layout layout){
		this();
		super.layout=layout;
	}

}
