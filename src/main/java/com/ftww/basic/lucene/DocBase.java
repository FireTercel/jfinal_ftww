package com.ftww.basic.lucene;

import org.apache.lucene.analysis.Analyzer;
//import org.wltea.analyzer.lucene.IKAnalyzer;

public class DocBase implements Runnable{
	
	//初始化索引，每批次处理一万行
	protected static final int splitDataSize = 200; 
	
	//protected static final Analyzer analyzer = new IKAnalyzer();//分词器

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
