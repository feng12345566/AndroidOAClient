package com.mct.service;

import java.util.ArrayList;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKOLSearchRecord;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.mct.application.MyApplication;
import com.mct.util.Common;
import com.mct.util.SharePeferenceUtils;
import com.mct.util.TimeRender;
import com.mct.util.UserDataDbUtil;
import com.mct.util.XmppTool;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class LocationService extends Service {
	private LocationClient mLocClient;
	private MyApplication app;
	private int locationType;// 定位类型 1单次定位 2持续定位
	private String useage;// 用途
	private boolean isAuto = false;
	private MKOfflineMap mOffline;
	private NotificationCompat.Builder mBuilder;
	private NotificationManager manager;
	private String city;
	private boolean isDownloading;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		locationType = intent.getIntExtra("locationType", 0);
		useage = intent.getStringExtra("useage");
		if (intent.hasExtra("isAuto")) {
			isAuto = true;
		}
		app = (MyApplication) getApplicationContext();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(new MyApplication.MyGeneralListener());
		}
		getLocation();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		stopLoc();

	}

	private void getLocation() {
		mLocClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setScanSpan(4000);// 间隔4秒
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null) {
					return;
				}
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				Log.e("location", latitude + "," + longitude);
				switch (locationType) {
				case 1:
					// 单次定位
					if (latitude != 0 && longitude != 0) {
						SharedPreferences locationData = getSharedPreferences(
								"locationData", 0);
						locationData
								.edit()
								.putString("latitude", latitude + "")
								.putString("longitude", longitude + "")
								.putString("address",
										location.getAddrStr() + "").commit();
						mLocClient.unRegisterLocationListener(this);
						if (useage.equals("attendance")) {
							UserDataDbUtil
									.shareUserDataDb(LocationService.this)
									.insertLocation(
											XmppTool.loginUser.getUserId(),
											location.getAddrStr(),
											latitude + "," + longitude,
											location.getTime());
						} else if (useage.equals("weather")) {
							SharedPreferences location2 = getSharedPreferences(
									"locationData", 0);
							location2
									.edit()
									.putString("latitude",
											String.valueOf(latitude))
									.putString("longitude",
											String.valueOf(longitude)).commit();
						}
						Intent intent = new Intent(Common.LOCSUCCESS);
						Bundle bundle = new Bundle();
						bundle.putDouble("latitude", latitude);
						bundle.putDouble("longitude", longitude);
						sendBroadcast(intent);
						// 若为自动签到且开启通知提醒
						if (isAuto
								&& SharePeferenceUtils
										.getInstance(LocationService.this)
										.getSwitch(
												SharePeferenceUtils.ATTENDANCENOTIFICATION)) {
							NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
							PendingIntent pendingIntent = PendingIntent
									.getActivity(LocationService.this, 0,
											new Intent(),
											PendingIntent.FLAG_ONE_SHOT);
							android.app.Notification noti = new NotificationCompat.Builder(
									LocationService.this)
									.setTicker("成功签到")
									.setContentTitle(
											"系统已在"
													+ TimeRender
															.getCurTimeToString("HH:mm")
													+ "自动为您成功签到")
									.setContentText(
											"签到地址：" + location.getAddrStr())
									.setSmallIcon(getApplicationInfo().icon)
									.setContentIntent(pendingIntent).build();
							notificationManager.notify(0, noti);
						}
						stopSelf();
					}
					break;

				case 2:
					// 持续定位
					if (latitude != 0 && longitude != 0) {
						Intent intent = new Intent(Common.LOCSUCCESS);
						Bundle bundle = new Bundle();
						bundle.putString("address", location.getAddrStr());
						city = location.getCity();
						bundle.putDouble("latitude", latitude);
						bundle.putDouble("longitude", longitude);
						bundle.putFloat("direction", location.getDirection());
						bundle.putString("time", location.getTime());
						intent.putExtra("location", bundle);
						sendBroadcast(intent);
					}
					break;
				}

			}
		});

		mLocClient.start();
		// mLocClient.requestLocation();
	}

	public void stopLoc() {
		if (mLocClient != null) {
			mLocClient.stop();
		}
		stopSelf();
	}

	private void downloadOfflineMap() {
		manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
		mOffline = new MKOfflineMap();
		mOffline.init(null, new MKOfflineMapListener() {

			@Override
			public void onGetOfflineMapState(int arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});
		ArrayList<MKOLSearchRecord> list = mOffline.searchCity(city);
		if (list != null && list.size() == 1) {
			int cityId = list.get(0).cityID;
			MKOLUpdateElement update = mOffline.getUpdateInfo(cityId);
			if (update.status != MKOLUpdateElement.FINISHED) {

				mOffline.start(cityId);
			}
		}
	}

	class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					"android.net.conn.CONNECTIVITY_CHANGE")) {
				ConnectivityManager manager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mobileInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo wifiInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo activeInfo = manager.getActiveNetworkInfo();
				SharedPreferences preferences = getSharedPreferences(
						"filesettings", 0);
				if (preferences.getBoolean(
						SharePeferenceUtils.MAPDOWNLOADENABLE, false)) {
                     if(preferences.getBoolean(SharePeferenceUtils.MAPDOWNLOADNETENABLE, false)
                    	 &&mobileInfo.isConnected()){
                    		 
                    	 }
				}
			}
		}

	}
}
