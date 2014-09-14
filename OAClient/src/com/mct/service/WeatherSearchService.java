package com.mct.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mct.util.Common;
import com.mct.util.HttpRequestUtil;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class WeatherSearchService extends Service {
	private String httpUrl;// 天气查询接口地址
	private String result;// 查询结果
	private ArrayList<String> list;// 解析result出的数据列表
	private String path;// 存放天气图标路径
	private String fileName;// 白天天气图标文件名
	private String fileName2;// 晚上天气图标文件名
	private String currentCity;// 当前所在城市
	private String today;// 今天的日期

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		System.out.println("启动天气查询服务");

		int type = intent.getIntExtra("type", 0);
		switch (type) {
		case 1:
			// 根据坐标搜索
			SharedPreferences location=getSharedPreferences("locationData", 0);
			
			getWeather(location.getString("longitude", "")+","+location.getString("latitude", ""));
			break;
		case 2:
			// 根据城市名搜索
			String city = intent.getStringExtra("city");
			try {
				city = URLEncoder.encode(city, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getWeather(city);
			break;

		}

	}

	/**
	 * @param loc
	 */
	public void getWeather(String key) {
		httpUrl = "http://api.map.baidu.com/telematics/v3/weather?location="
				+ key + "&output=json&ak=0is6f21xnwgereW2Kp3LgqG2";
		System.out.println("请求地址"+httpUrl);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				result = HttpRequestUtil.getResponsesByGet(httpUrl);
				try {
					JSONObject json = new JSONObject(result);
					String status = json.getString("status");
					if (status.equals("success")) {
						today = json.getString("date");

						JSONArray results = json.getJSONArray("results");
						JSONObject result = results.optJSONObject(0);
						currentCity = result.getString("currentCity");
						JSONArray weather_data = result
								.getJSONArray("weather_data");
						list = new ArrayList<String>();
						for (int i = 0; i < weather_data.length(); i++) {
							JSONObject weather = weather_data.optJSONObject(i);
							list.add(weather.getString("date"));
							list.add(weather.getString("dayPictureUrl"));
							list.add(weather.getString("nightPictureUrl"));
							list.add(weather.getString("weather"));
							list.add(weather.getString("wind"));
							list.add(weather.getString("temperature"));
						}

						handler.sendEmptyMessage(1);

					} else {
						handler.sendEmptyMessage(0);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block

					handler.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// 发送天气查询失败广播
				Intent intent2 = new Intent();
				intent2.setAction(Common.WEATHERSEARCHFAIL);
				sendBroadcast(intent2);
				break;
			case 1:
				// 发送天气查询成功广播
				Intent intent1 = new Intent();
				intent1.putExtra("currentCity", currentCity);
				intent1.putExtra("today", today);
				intent1.putStringArrayListExtra("list", list);
				intent1.setAction(Common.WEATHERSEARCHSUCCESS);
				sendBroadcast(intent1);
				
				break;
			

			default:
				break;
			}
			WeatherSearchService.this.stopSelf();
		}

	};

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub

		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("停止天气查询服务");
	}
	
}
