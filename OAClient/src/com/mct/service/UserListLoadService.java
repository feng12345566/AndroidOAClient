package com.mct.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.mct.util.HttpRequestUtil;
import com.mct.util.XmppTool;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class UserListLoadService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		System.out.println("启动通讯录下载服务");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				// 获取登陆者信息
				String httpUrl2 = "http://nat.nat123.net:14313/oa/message/GetLoginUserinfo.jsp?userid="
						+ XmppTool.loginUser.getUserId();
				String result2 = HttpRequestUtil.getResponsesByGet(httpUrl2);
				Log.e("result2", result2);
				if (result2 != null) {
					try {
						JSONObject json2 = new JSONObject(result2);
						SharedPreferences settings=getSharedPreferences("setting_info", 0);
						settings.edit().putString("username",json2
								.getString("username")).commit();
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				XmppTool.shareConnectionManager(UserListLoadService.this)
						.setUserList();
				Intent intent = new Intent("com.mct.action.loaddone");
				sendBroadcast(intent);
				SharedPreferences settings = getSharedPreferences(
						"setting_info", 0);
				settings.edit().putBoolean("isfriendlistloaded", true).commit();
				UserListLoadService.this.stopSelf();
			}
		}).start();
	}

}
