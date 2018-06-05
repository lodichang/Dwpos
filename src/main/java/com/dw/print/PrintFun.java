package com.dw.print;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PrintFun {
	/**
	 * 获得系统当前的日期和时间,
	 * @param style -返回的字符串的格式
	 * @return String
	 */
	public static String getSystemDateTime(String style){
		SimpleDateFormat format1 = new SimpleDateFormat(style, Locale.CHINA);
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setTimeInMillis(System.currentTimeMillis());
		return format1.format(cal.getTime());
	}
	
}
