package com.mct.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * 保存设置工具类
 * 
 * @author fengyouchun
 * @version 创建时间：2014年7月31日 下午5:39:42
 */
public class SharePeferenceUtils {
	private static SharePeferenceUtils sharePeferenceUtils;
	private Context context;
	private SharedPreferences settingSharedPreferences;
	public static final String FILETRANSFORENABLE = "filetransforenable";
	public static final String FILEDONENOTIFICATION = "filedonenotification";
	public static final String FILETRANSFORNETENABLE = "filetransfornetenable";
	public static final String FILETRANSFORWIFIENABLE = "filetransforwifienable";
	public static final String MAPDOWNLOADENABLE = "mapdownloadenable";
	public static final String MAPDOWNLOADNETENABLE = "mapdownloadnetenable";
	public static final String MAPDOWNLOADWIFIENABLE = "mapdownloadwifienable";
	public static final String GESTYRELOCKENABLE = "gesturelockenable";
	public static final String GESTYRELOCK = "gestyrelock";
	public static final String AUTOATTENDANCE = "autoattendance";
	public static final String ATTENDANCENOTIFICATION = "attendancenotification";
	public static final String ALARM0 = "alarm0";
	public static final String ALARM1 = "alarm1";
	public static final String ALARM2 = "alarm2";
	public static final String ALARM3 = "alarm3";
	public static final String AUTOUPDATECHECK = "autodateupdatecheck";

	/**
	 * @param context
	 */
	private SharePeferenceUtils(Context context) {
		super();
		this.context = context;
		settingSharedPreferences = context
				.getSharedPreferences("mysettings", 0);
	}

	/**
	 * 功能描述：单例模式 Administrator 2014年8月6日 上午11:32:02
	 * 
	 * @param context
	 * @return
	 */
	public static SharePeferenceUtils getInstance(Context context) {
		if (sharePeferenceUtils == null) {
			sharePeferenceUtils = new SharePeferenceUtils(context);
		}
		return sharePeferenceUtils;
	}

	public void setSwitch(String key, boolean value) {
		settingSharedPreferences.edit().putBoolean(key, value).commit();
		AlarmUtils.resetAlarm(context);
	}

	public boolean getSwitch(String key) {
		return settingSharedPreferences.getBoolean(key, false);
	}

	/**
	 * 功能描述：修改定时时间 Administrator 2014年8月6日 上午11:34:51
	 * 
	 * @param num
	 *            需修改的时间编号
	 * @param time
	 *            需修改为的时间
	 */
	public void modifyTime(int num, String time) {
		Editor editor = settingSharedPreferences.edit();
		editor.putString("attendanceTime" + num, time);
		editor.commit();
		AlarmUtils.resetAlarm(context);
	}

	/**
	 * 功能描述：获取时间 Administrator 2014年8月6日 上午11:39:19
	 * 
	 * @param num
	 * @return
	 */
	public String getTime(int num) {
		String[] dafoultTime = new String[] { "08:30", "12:00", "13:30",
				"18:00" };
		return settingSharedPreferences.getString("attendanceTime" + num,
				dafoultTime[num]);

	}

	/**
	 * 功能描述：修改重复周期 Administrator 2014年8月6日 上午11:34:51
	 * 
	 * @param num
	 *            需修改的时间编号
	 * @param time
	 *            需修改为的时间
	 */
	public void modifyRepeat(int num, boolean[] repeat) {
		Editor editor = settingSharedPreferences.edit();
		for (int i = 0; i < 7; i++) {
			editor.putBoolean("repeat" + num + "_" + i, repeat[i]);
		}
		editor.commit();
		AlarmUtils.resetAlarm(context);
	}

	/**
	 * 功能描述：获取重复周期 Administrator 2014年8月6日 上午11:39:19
	 * 
	 * @param num
	 * @return
	 */
	public boolean[] getRepeat(int num) {
		boolean[] xq = new boolean[] { false, false, false, false, false,
				false, false };
		for (int i = 0; i < 7; i++) {
			xq[i] = settingSharedPreferences.getBoolean("repeat" + num + "_"
					+ i, false);
		}
		return xq;

	}

	public boolean getAlarmActive(int i) {

		return settingSharedPreferences.getBoolean("alarm" + i, false);
	}

	/**
	 * 设置第i个闹钟是否激活
	 * 
	 * @param i
	 * @param isActive
	 */
	public void setAlarmActive(int i, boolean isActive) {
		settingSharedPreferences.edit().putBoolean("alarm" + i, isActive)
				.commit();
		AlarmUtils.resetAlarm(context);
	}

	/**
	 * 获取登陆者姓名
	 * 
	 * @return
	 */
	public String getLoginUserName() {
		SharedPreferences settings = context
				.getSharedPreferences("username", 0);
		return settings.getString("username", "");
	}

	/**
	 * 数据传输设置
	 * 
	 * @param key
	 * @param enable
	 */
	public void setFileTransfor(String key, boolean enable) {
		SharedPreferences filesettings = context.getSharedPreferences(
				"filesettings", 0);
		filesettings.edit().putBoolean(key, enable).commit();

	}

	/**
	 * 获取数据传输设置
	 * 
	 * @param key
	 * @return
	 */
	public boolean getFileTransfor(String key) {
		SharedPreferences filesettings = context.getSharedPreferences(
				"filesettings", 0);
		return filesettings.getBoolean(key, false);
	}

	/**
	 * 获取消息设置
	 * 
	 * @param key
	 * @return
	 */
	public boolean getMessageSettings(String key) {
		SharedPreferences messagesettings = context.getSharedPreferences(
				"messagesettings", 0);
		return messagesettings.getBoolean(key, false);
	}

	public void setMessageSettings(String key, boolean enable) {
		SharedPreferences messagesettings = context.getSharedPreferences(
				"messagesettings", 0);
		messagesettings.edit().putBoolean(key, enable).commit();

	}

	public void setGestrueLockPass(int[] pass) throws Exception {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < pass.length; i++) {
			sb.append(pass[i]);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		SharedPreferences messagesettings = context.getSharedPreferences(
				"safetysettings", 0);
		messagesettings
				.edit()
				.putString(GESTYRELOCK,
						AESUtil.encryptDES(sb.toString(), AESUtil.KEY))
				.commit();
	}

	public int[] getGestrueLockPass() throws Exception {
		SharedPreferences messagesettings = context.getSharedPreferences(
				"safetysettings", 0);
		String wd = AESUtil.decryptDES(
				messagesettings.getString(GESTYRELOCK, ""), AESUtil.KEY);
		String[] points = wd.split(",");
		int[] correctGestures = null;
		if (points != null) {
			correctGestures = new int[points.length];
			for (int i = 0; i < points.length; i++) {
				correctGestures[i] = Integer.valueOf(points[i]);
			}
		}
		return correctGestures;

	}

	public void setGestrueLockEnable(boolean enable) {
		SharedPreferences messagesettings = context.getSharedPreferences(
				"safetysettings", 0);
		messagesettings.edit().putBoolean(GESTYRELOCKENABLE, enable).commit();
	}

	public boolean getGestrueLockEnable() {
		SharedPreferences messagesettings = context.getSharedPreferences(
				"safetysettings", 0);
		return messagesettings.getBoolean(GESTYRELOCKENABLE, false);
	}

	public void setLastestAlarmTime(long time) {
		settingSharedPreferences.edit().putLong("lastestAlarmTime", time)
				.commit();
	}

	public long getLastestAlarmTime() {
		return settingSharedPreferences.getLong("lastestAlarmTime", -1);
	}
}
