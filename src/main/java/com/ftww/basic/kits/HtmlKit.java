package com.ftww.basic.kits;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Html处理
 * @author FireTercel 2015年5月29日 
 *
 */
public class HtmlKit {
	
	/**
	 * 富文本内容处理返回纯文本
	 * @param unsafe
	 * @return
	 */
	public static String cleanHtml(String unsafe){
		String clear = Jsoup.clean(unsafe, Whitelist.simpleText());
		return clear;
	}
	
	/**
	 * 富文本内容处理返回安全文本
	 * @param unsafe
	 * @return
	 */
	public static String safeHtml(String unsafe){
		String safe = Jsoup.clean(unsafe, Whitelist.basic());
		return safe;
	}

}
