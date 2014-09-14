package com.mct.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeRender {

	private static SimpleDateFormat formatBuilder;

	public static String getDate() {
		formatBuilder = new SimpleDateFormat("MM-dd HH:mm:ss");
		return formatBuilder.format(new Date(System.currentTimeMillis()));
	}
	/**
	 * 已指定格式输出时间  yy-MM-dd HH:mm:ss
	 * @param time
	 * @param outFormate
	 * @return
	 */
	public static String getDate(long time,String outFormate) {
		formatBuilder = new SimpleDateFormat(outFormate);
		return formatBuilder.format(new Date(time));
	}
	
	/**
	 * 获取系统当前时间
	 * 
	 * @param outFormate
	 *            输出格式:yy-MM-dd HH:mm:ss
	 * @return 返回当前时间
	 */
	public static String getCurTimeToString(String outFormate) {
		SimpleDateFormat formatter = new SimpleDateFormat(outFormate);
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;

	}

	public static Date getCurTimeToDate(String outFormate) {
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return curDate;

	}

	public static Date string2Date(String str, String formate) {
		SimpleDateFormat formatter = new SimpleDateFormat(formate);
		Date date = null;
		try {
			date = formatter.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			date = null;
		}
		return date;

	}
	/**
	 * 判断当前的时间是白天还是晚上 6:00-17:59视为白天
	 * 
	 * @return true 白天 false 晚上
	 */
	public static boolean isDay() {
		String str=getCurTimeToString("yyyy-MM-dd HH:mm:ss");
		int index = str.indexOf(":");
		int hour = 0;
		String hourStr = str.substring(index - 2, index);
		if (hourStr.startsWith("0")) {
			hour = Integer.parseInt(hourStr.substring(1));
		} else {
			hour = Integer.parseInt(hourStr);
		}
	
		if (hour >= 6 && hour < 18) {
			System.out.println("当前时段为白天");
			return true;
		} else {
			System.out.println("当前时段为晚上");

			return false;
		}
	}
	
	
	/**
	 * 获取一天的时间区间
	 * @param date 格式需为yyyy-MM-dd
	 * @return  0点到24点的起始时间
	 */
	public static long[] getDayBetween(String date){
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long startTime = 0;
		long endTime = 0;
		try {
			startTime = simpleDateFormat.parse(date+" 00:00:00").getTime();
			endTime=simpleDateFormat.parse(date+" 23:59:59").getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new long[]{startTime,endTime};
	}
	
	
}
