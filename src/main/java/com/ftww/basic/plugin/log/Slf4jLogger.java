package com.ftww.basic.plugin.log;

import org.slf4j.LoggerFactory;

import com.jfinal.log.Logger;

/**
 * 
 * @author tangdongyu
 *
 */
public class Slf4jLogger extends Logger {
	
	private org.slf4j.Logger log;
	
	Slf4jLogger(Class<?> clazz){
		log=LoggerFactory.getLogger(clazz);
	}
	
	Slf4jLogger(String name){
		log=LoggerFactory.getLogger(name);
	}

	@Override
	public void debug(String message) {
		log.debug(message);
	}

	@Override
	public void debug(String message, Throwable t) {
		log.debug(message, t);
	}

	@Override
	public void info(String message) {
		log.info(message);
	}

	@Override
	public void info(String message, Throwable t) {
		log.info(message, t);
	}

	@Override
	public void warn(String message) {
		log.warn(message);
	}

	@Override
	public void warn(String message, Throwable t) {
		log.warn(message, t);
	}

	@Override
	public void error(String message) {
		log.error(message);
	}
	
	public void error(String format,Object arg){
		log.error(format, arg);
	}
	
	@Override
	public void error(String message, Throwable t) {
		log.error(message, t);
	}

	@Override
	public void fatal(String message) {
		log.error(message);
	}

	@Override
	public void fatal(String message, Throwable t) {
		log.error(message, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}

	@Override
	public boolean isFatalEnabled() {
		return log.isErrorEnabled();
	}

}
