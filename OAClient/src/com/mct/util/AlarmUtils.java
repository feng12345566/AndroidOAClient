package com.mct.util;

import java.util.Calendar;

import com.mct.receiver.AlarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmUtils {
	/**
	 * 获取最近一次闹钟的时间
	 * 
	 * @return
	 */
	public static long getRecentAlarmTime(Context context) {
         long recentAlarmTime=-1;
		// 获取日历实例
		Calendar c = Calendar.getInstance();
		int index=0;
		for (int i = 0; i < 4; i++) {
			boolean isAlarmActive = SharePeferenceUtils.getInstance(context)
					.getAlarmActive(i);
			if (isAlarmActive) {
				long lastTime=getLastTime(context,i,c);
				
				if(lastTime>0){
					switch (index) {
					case 0:
						//第一个不为负值的值设置为比较基质
						recentAlarmTime=lastTime;
						break;

					default:
						if(lastTime<recentAlarmTime){
							recentAlarmTime=lastTime;
						}
						
						break;
					}
					index++;
				}
				
					
			}
		}

		return recentAlarmTime;

	}

	private static long getLastTime(Context context, int i, Calendar c) {
		long lastTime = -1;
		long todaytime=-1;//记录与今日相同星期且已过时的时间设置值
		// 获取重复周期
		boolean[] repeat = SharePeferenceUtils.getInstance(context)
				.getRepeat(i);
		// 获取今天的星期
		int day = c.get(Calendar.DAY_OF_WEEK);
		String date = TimeRender.getDate(c.getTimeInMillis(), "yy-MM-dd");
		// 星期对应数值下标
		int count = day - 1;
		for (int j = 0; j < 7; j++) {
			// 如果今天对应星期的重复周期设置为true
			if (repeat[count]) {
				// 获取设置时间
				String timeStr = SharePeferenceUtils.getInstance(context)
						.getTime(i);
				// 获取设置时间在今天对应的毫秒时间值
				long time = TimeRender.string2Date(date + " " + timeStr,
						"yy-MM-dd HH:mm").getTime();
				// 如果该时间未过时则返回该时间，否则检查后一天
				if (count == day - 1) {
					if (time > c.getTimeInMillis()) {
						lastTime = time;
						return lastTime;
					} else {
						//记录7天之后的时间
						todaytime=time+7*24*60*60*1000;
						count++;
						if (count == 7) {
							count = 0;
						}
						continue;
					}
				} else {
					return time+j*24*60*60*1000;
				}

			} else {
				count++;
				if (count == 7) {
					count = 0;
				}
			}
		}
		//此处代表前面无返回值，即若设置星期与今天相同的7天后的时间，若未设置与今天相同的星期则返回-1
		return todaytime;
	}
	
	public static  void resetAlarm(Context context){
		long recentAlarmTime=getRecentAlarmTime(context);
		AlarmManager m=(AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);                                
		intent.setAction("ALARM_ACTION");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		long lastTime=SharePeferenceUtils.getInstance(context).getLastestAlarmTime();
		Log.e("AlarmTime", recentAlarmTime+" "+lastTime);
		if(recentAlarmTime>0){
            if( recentAlarmTime!=lastTime){
			Log.e("alarm", "设置闹钟时间为"+TimeRender.getDate(recentAlarmTime, "yyyy-MM-dd HH:mm"));
            m.set(AlarmManager.RTC_WAKEUP, recentAlarmTime, pendingIntent);
            SharePeferenceUtils.getInstance(context).setLastestAlarmTime(recentAlarmTime);
			}
		} else {
			Log.e("alarm", "闹钟取消");
			m.cancel(pendingIntent);
			SharePeferenceUtils.getInstance(context).setLastestAlarmTime(-1);
		}
	}
}
