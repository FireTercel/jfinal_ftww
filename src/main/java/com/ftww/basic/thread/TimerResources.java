package com.ftww.basic.thread;

import java.util.Timer;
import java.util.TimerTask;

import com.ftww.basic.kits.DateTimeKit;
import com.ftww.basic.kits.OSKit;
import com.ftww.basic.model.Resources;
import com.jfinal.log.Logger;

public class TimerResources extends Timer{
	
	private final static Logger log = Logger.getLogger(TimerResources.class);
	
	/**
	 * 定时任务对象
	 */
	private static TimerResources timer = null;
	
	/**
	 * 启动任务
	 */
	public static void start(){
		if(null != timer){
			log.info("启动任务失败，任务已经启动！");
			return;
		}
		log.info("开始启动任务");
		timer=new TimerResources();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				log.info("任务执行……   开始……");
				String osName = OSKit.getOsName();//  获取操作系统类型名称
				String ip = OSKit.getOsLocalHostIp();//  获取本机IP
				String hostName = OSKit.getOsLocalHostName();//  获取本机名称
				int cpuNumber = OSKit.getOsCpuNumber();//  获取CPU数量
				double cpuRatio = OSKit.getOsCpuRatio();//  获取CPU使用率
				
				if(cpuRatio < 0){
					cpuRatio = 0;
				}
				
				long phyMemory = OSKit.getOsPhysicalMemory();//  物理内存，内存总量
				long phyFreeMemory = OSKit.getOsPhysicalFreeMemory();//  物理内存，剩余可使用
				
				long jvmTotalMemory = OSKit.getJvmTotalMemory();//  JVM内存，内存总量
				long jvmFreeMemory = OSKit.getJvmFreeMemory();//  JVM内存，空闲内存量
				long jvmMaxMemory = OSKit.getJvmMaxMemory();//  JVM内存，最大内存量
				long gcCount = OSKit.getJvmGcCount();//  获取JVM GC次数
				
				Resources resources = new Resources();
				resources.set("osname", osName);
				resources.set("ips", ip);
				resources.set("hostname", hostName);
				resources.set("cpunumber", cpuNumber);
				resources.set("cpuratio", cpuRatio);
				resources.set("phymemory", phyMemory);
				resources.set("phyfreememory", phyFreeMemory);
				resources.set("jvmtotalmemory", jvmTotalMemory);
				resources.set("jvmfreememory", jvmFreeMemory);
				resources.set("jvmmaxmemory", jvmMaxMemory);
				resources.set("gccount", gcCount);
				resources.set("createdate", DateTimeKit.getSqlTimestamp(null));
				resources.save();
				
				log.info("任务执行……  结束……");
			}
		}, 1000 ,1000 * 60 * 2);//启动项目一秒后执行，然后每次间隔2分钟
		log.info("启动任务完成！");
	}
	
	/**
	 * 停止任务
	 */
	public static void stop(){
		if(null != timer){
			log.info("任务退出……  开始……");
			timer.cancel();
			log.info("任务退出……  结束……");
		}else{
			log.info("任务退出失败，当前任务为空！");
		}
	}

}
