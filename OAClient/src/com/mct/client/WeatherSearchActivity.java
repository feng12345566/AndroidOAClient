package com.mct.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loopj.android.image.SmartImageView;
import com.mct.service.LocationService;
import com.mct.service.WeatherSearchService;
import com.mct.util.AssetsDatabaseManager;
import com.mct.util.Common;
import com.mct.util.TimeRender;
import com.mct.view.CustomSpinner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherSearchActivity extends Activity implements
		OnItemSelectedListener, OnClickListener {
	private TextView city;
	private TextView today;
	private TextView today_xq;
	private TextView today_weather;// 今天的天气
	private TextView today_wind;
	private TextView today_temp;// 今天的温度
	private TextView temp_now;
	private TextView backText;
	private TextView toptext;
	private TextView sureBtn;
	private LinearLayout backlayout;
	private SmartImageView day_weather;
	private SmartImageView night_weather;
	private ListView otherWeatherListView;// 后几天的天气列表
	private List<Map<String, Object>> otherWeatherList;// 后几天的天气数据
	private ArrayList<String> list;
	private SharedPreferences myWeatherInfo;// 本地存储的天气信息
	private Spinner sheng;// 省
	private Spinner shi;// 市
	private List<Map<String, Object>> list1;// 省名称及id列表
	private List<String> list2;// 省份名称列表
	private int provinceId;// 被选择的省份id
	private SQLiteDatabase db1;// 城市数据库
	private MyReceiver mReceiver;// 广播接收器
	private String currentCity;// 查询的城市
	private String todayStr;// 今天的日期
	private String path;// SD卡路径
	private String dayFileName;// 白天天气图标文件名
	private String nightFileName;// 晚上天气图标文件名
	private SimpleAdapter simpleAdapter;// 后几天天气数据列表适配器
	private Button serachbtn;
	private boolean isOnCreate;
	private LinearLayout weather_today;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 初始化
		isOnCreate = true;
		setContentView(R.layout.tianqichaxun);
		backText=(TextView) findViewById(R.id.backText);
		backText.setText("个人中心");
		backlayout=(LinearLayout) findViewById(R.id.backlayout);
		backlayout.setOnClickListener(this);
		city = (TextView) findViewById(R.id.mcity);
		today = (TextView) findViewById(R.id.weather_date);
		today_xq = (TextView) findViewById(R.id.weather_xingqi);
		temp_now = (TextView) findViewById(R.id.weather_sswd);
		today_temp = (TextView) findViewById(R.id.today_temp);
		today_weather = (TextView) findViewById(R.id.today_weather);
		today_wind = (TextView) findViewById(R.id.today_wind);
		day_weather = (SmartImageView) findViewById(R.id.day_weather);
		toptext=(TextView) findViewById(R.id.toptext);
		toptext.setText("天气查询");
		sureBtn = (TextView) findViewById(R.id.sureBtn);
		sureBtn.setText("刷新");
		sureBtn.setOnClickListener(this);
		night_weather = (SmartImageView) findViewById(R.id.night_weather);
		otherWeatherListView = (ListView) findViewById(R.id.otherweather);
		weather_today = (LinearLayout) findViewById(R.id.weather_today);
		today = (TextView) findViewById(R.id.weather_date);
		sheng = (Spinner) findViewById(R.id.sheng_spinner);
		shi = (Spinner) findViewById(R.id.shi_spinner);
		serachbtn = (Button) findViewById(R.id.serachbtn);
		serachbtn.setOnClickListener(this);
		// 获取城市数据库
		AssetsDatabaseManager.initManager(getApplication());
		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
		db1 = mg.getDatabase("citylist.db");
		// 获取省份列表
		getAllProvinces();
		// 省份下拉列表适配及监听
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list2);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sheng.setAdapter(adapter1);
		sheng.setOnItemSelectedListener(this);
		SharedPreferences myInfo = getSharedPreferences("MyInfo", 0);
		String mWeatherSearchDay = myInfo.getString("weatherSearchDay",
				"2014-01-01 00:00:00");
		sheng.setSelection(myInfo.getInt("sheng", 0), true);

		otherWeatherList = new ArrayList<Map<String, Object>>();
		long between = TimeRender.getCurTimeToDate("yyyy-MM-dd HH:mm:ss")
				.getTime()
				- TimeRender
						.string2Date(mWeatherSearchDay, "yyyy-MM-dd HH:mm:ss")
						.getTime();
		System.out.println("上一次查询在" + between / 60000 + "分钟前");
		simpleAdapter = new SimpleAdapter(WeatherSearchActivity.this,
				otherWeatherList, R.layout.weatheritem, new String[] { "星期",
						"天气", "风速", "温度" }, new int[] { R.id.other_xq,
						R.id.other_weather, R.id.other_wind, R.id.other_temp });
		otherWeatherListView.setAdapter(simpleAdapter);
		// 若今天已经查询过天气，则先显示查询过的天气
		if (between < 3600000) {
			myWeatherInfo = getSharedPreferences("MyWeatherInfo", 0);
			city.setText(myWeatherInfo.getString("mCity", ""));
			today_xq.setText(myWeatherInfo.getString("today_xq", ""));
			today_weather.setText(myWeatherInfo.getString("today_weather", ""));
			today_wind.setText(myWeatherInfo.getString("today_wind", ""));
			today_temp.setText(myWeatherInfo.getString("today_temp", ""));
			temp_now.setText("--");
			today.setText(TimeRender.getCurTimeToString("yy-MM-dd"));
			path = Environment.getExternalStorageDirectory() + "/";
			dayFileName = myWeatherInfo.getString("day_weather", "");
			nightFileName = myWeatherInfo.getString("night_weather", "");
			day_weather.setImageUrl("http://api.map.baidu.com/images/weather/day/"+dayFileName);
			night_weather.setImageUrl("http://api.map.baidu.com/images/weather/night/"+nightFileName);
			changeBackground();
			String ow = myWeatherInfo.getString("other_weather", "");
			System.out.println(ow);
			otherWeatherList.clear();
			for (int i = 0; i < (ow.split("&")).length / 4; i++) {
				Log.e("mtag", ow.split("&").length + "");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("星期", ow.split("&")[4 * i]);
				map.put("天气", "天气:" + ow.split("&")[4 * i + 1]);
				map.put("风速", "风速:" + ow.split("&")[4 * i + 2]);
				map.put("温度", "温度:" + ow.split("&")[4 * i + 3]);
				otherWeatherList.add(map);
				map = null;
			}
			simpleAdapter.notifyDataSetChanged();

		} else {

			startService();
			startReceiver();
			city.setText("定位中...");
			showProgressDialog();
		}
	}

	/**
	 * 查询数据库获取全部的省
	 * 
	 * @return 省份列表
	 */
	private void getAllProvinces() {
		list1 = new ArrayList<Map<String, Object>>();
		list2 = new ArrayList<String>();

		Cursor cursor = db1.query("province", new String[] { "province_name",
				"province_id" }, null, null, null, null, null);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("provinceName", cursor.getString(0));
			map.put("provinceId", cursor.getInt(1));
			list1.add(map);
			list2.add(cursor.getString(0));
			map = null;
		}
		cursor.close();
	}

	/**
	 * @param id
	 * @return
	 */
	private String[] getCitysByProvinceId(int id) {
		Cursor cursor = db1.query("city", new String[] { "city_name" },
				"province_id='" + id + "'", null, null, null, null);
		String[] citys = new String[cursor.getCount()];
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			citys[i] = cursor.getString(0);
		}
		cursor.close();
		return citys;

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 4:
				// 定位失败，设置当前城市为空
				progressDialog.dismiss();
				city.setText("");
				Toast.makeText(WeatherSearchActivity.this, "定位失败，请重新查询！",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				progressDialog.dismiss();
				// 查询成功，加载视图
				city.setText(currentCity);
				today.setText(todayStr);
				today_xq.setText(list.get(0).substring(0, 2));
				if (list.get(0).split("：").length > 1) {
					temp_now.setText(list.get(0).split("：")[1].trim().replace(
							")", ""));
				}
				day_weather.setImageUrl(list.get(1));
				night_weather.setImageUrl(list.get(2));
				dayFileName=list.get(1).substring(list.get(1).lastIndexOf("/")+1);
				nightFileName=list.get(2).substring(list.get(2).lastIndexOf("/")+1);
				today_weather.setText(list.get(3));
				today_wind.setText(list.get(4));
				today_temp.setText(list.get(5));
				otherWeatherList.clear();
				String temp = "";
				for (int i = 1; i < list.size() / 6; i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("星期", list.get(6 * i));
					map.put("天气", "天气:" + list.get(6 * i + 3));
					map.put("风速", "风速:" + list.get(6 * i + 4));
					map.put("温度", "温度:" + list.get(6 * i + 5));
					System.out.println(map);
					otherWeatherList.add(map);
					map = null;
					if (i != list.size() / 6 - 1) {
						temp = temp + list.get(6 * i) + "&"
								+ list.get(6 * i + 3) + "&"
								+ list.get(6 * i + 4) + "&"
								+ list.get(6 * i + 5) + "&";
					} else {
						temp = temp + list.get(6 * i) + "&"
								+ list.get(6 * i + 3) + "&"
								+ list.get(6 * i + 4) + "&"
								+ list.get(6 * i + 5);
					}
				}
				simpleAdapter.notifyDataSetChanged();
				changeBackground();
				myWeatherInfo = getSharedPreferences("MyWeatherInfo", 0);
				myWeatherInfo.edit().putString("weatherSearchDay", todayStr)
						.putString("mCity", currentCity)
						.putString("today_xq", list.get(0).substring(0, 2))
						.putString("today_weather", list.get(3))
						.putString("today_wind", list.get(4))
						.putString("today_temp", list.get(5))
						.putString("other_weather", temp)
						.putString("day_weather",dayFileName)
						.putString("night_weather",nightFileName).commit();

				SharedPreferences myInfo = getSharedPreferences("MyInfo", 0);
				myInfo.edit()
						.putString("locCity", currentCity)
						.putString(
								"weatherSearchDay",
								TimeRender.getCurTimeToString("yyyy-MM-dd HH:mm:ss"))
						.commit();

				break;
			}
			
		}

	};

	/**
	 * @author fengyouchun
	 * @version 创建时间：2014年5月21日 下午8:38:04
	 */
	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(Common.LOCSUCCESS)) {
				System.out.println("接收到定位成功广播");
				// 启动天气查询服务
				Intent searchIntent = new Intent(WeatherSearchActivity.this,
						WeatherSearchService.class);
				searchIntent.putExtra("type", 1);// 发送坐标点
				startService(searchIntent);

			} else if (action.equals(Common.LOCFAIL)) {
				System.out.println("接收到定位失败广播");
				Toast.makeText(WeatherSearchActivity.this, "定位失败，请重新查询",
						Toast.LENGTH_SHORT).show();
				handler.sendEmptyMessage(4);// 更新当前城市的消息
			} else if (action.equals(Common.WEATHERSEARCHSUCCESS)) {
				// 获取传递参数
				currentCity = intent.getStringExtra("currentCity");
				todayStr = intent.getStringExtra("today");
				list = intent.getStringArrayListExtra("list");
//				System.out.println(list);
				handler.sendEmptyMessage(3);// 更新所有数据的消息

			} else if (action.equals(Common.WEATHERSEARCHFAIL)) {

				Toast.makeText(WeatherSearchActivity.this, "查询失败，请重试",
						Toast.LENGTH_SHORT).show();
				handler.sendEmptyMessage(4);// 更新当前城市的消息
			} 
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.AdapterView.OnItemSelectedListener
	 * #onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		// 通过被选中的省份的id查询数据库，结果作为市下拉才菜单的选项
		provinceId = (Integer) list1.get(arg2).get("provinceId");
		SharedPreferences myInfo = getSharedPreferences("MyInfo", 0);
		myInfo.edit().putInt("provincePostion", arg2).commit();
		provinceId = (Integer) list1.get(arg2).get("provinceId");
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,
				getCitysByProvinceId(provinceId));
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		shi.setAdapter(mAdapter);
		// 仅在加载视图oncreate时读取数据库，默认选中上次查询的城市
		if (isOnCreate) {
			SharedPreferences myInfo2 = getSharedPreferences("MyInfo", 0);
			shi.setSelection(myInfo2.getInt("shi", 0), true);
			isOnCreate = false;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		// 没有选项被选中则选择上次使用存储在本地的省份
		SharedPreferences myInfo = getSharedPreferences("MyInfo", 0);
		int mLocProvincePostion = myInfo.getInt("provincePostion", 0);
		sheng.setSelection(mLocProvincePostion);
		provinceId = (Integer) list1.get(mLocProvincePostion).get("provinceId");
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item,
				getCitysByProvinceId(provinceId));
		shi.setAdapter(mAdapter);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Intent locIntent = new Intent(WeatherSearchActivity.this,
				LocationService.class);
		stopService(locIntent);
		Intent weatherIntent = new Intent(WeatherSearchActivity.this,
				WeatherSearchService.class);
		stopService(weatherIntent);
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		this.finish();
		return true;
	}

	private void startService() {
		// 启动定位服务
		System.out.println("运行定位服务");
		Intent locIntent = new Intent(WeatherSearchActivity.this,
				LocationService.class);
		locIntent.putExtra("locationType", 1);
		locIntent.putExtra("useage","weather");
		startService(locIntent);

	}

	private void startReceiver() {
		// 注册广播接收器
		if (mReceiver == null) {
			mReceiver = new MyReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Common.LOCFAIL);
			filter.addAction(Common.LOCSUCCESS);
			filter.addAction(Common.WEATHERSEARCHFAIL);
			filter.addAction(Common.WEATHERSEARCHSUCCESS);
			registerReceiver(mReceiver, filter);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.serachbtn:
			showProgressDialog();
			String cityStr = shi.getItemAtPosition(
					shi.getSelectedItemPosition()).toString();
			Intent searchIntent = new Intent(WeatherSearchActivity.this,
					WeatherSearchService.class);
			searchIntent.putExtra("type", 2);// 发送城市名
			searchIntent.putExtra("city", cityStr);
			startService(searchIntent);
			startReceiver();
			SharedPreferences myInfo = getSharedPreferences("MyInfo", 0);
			myInfo.edit().putInt("sheng", sheng.getSelectedItemPosition())
					.putInt("shi", shi.getSelectedItemPosition()).commit();
			break;

		case R.id.sureBtn:
			startService();
			startReceiver();
			showProgressDialog();
			break;
		case R.id.backlayout:
			this.finish();
			break;
		}
	}

	/**
	 * 根据当前时段及天气设置布局背景
	 */
	public void changeBackground() {
		if (TimeRender.isDay()) {
			System.out.println("白天天气文件名" + dayFileName);
			if (dayFileName.contains("lei")) {
				weather_today.setBackgroundResource(R.drawable.leiyu_bg);
			} else if (dayFileName.contains("yun")) {
				weather_today.setBackgroundResource(R.drawable.duoyun_bg);
			} else if (dayFileName.contains("yu")||dayFileName.contains("yin")) {
				weather_today.setBackgroundResource(R.drawable.yu_bg);
			} else if (dayFileName.contains("qing")) {
				weather_today.setBackgroundResource(R.drawable.qing_bg);
			} else if (dayFileName.contains("xue")) {
				weather_today.setBackgroundResource(R.drawable.xue_bg);
			} else if (dayFileName.contains("mai")) {
				weather_today.setBackgroundResource(R.drawable.mai_bg);
			} else if (dayFileName.contains("wu")) {
				weather_today.setBackgroundResource(R.drawable.wu_bg);
			}
		} else {
			System.out.println("晚上天气文件名" + nightFileName);
			if (nightFileName.contains("lei")) {
				weather_today.setBackgroundResource(R.drawable.leiyu_bg);
			} else if (nightFileName.contains("duoyun")) {
				System.out.println("duoyun");
				weather_today.setBackgroundResource(R.drawable.duoyun_bg);
			} else if (nightFileName.contains("yu")||nightFileName.contains("yin")) {
				weather_today.setBackgroundResource(R.drawable.yu_bg);
			} else if (nightFileName.contains("qing")) {
				weather_today.setBackgroundResource(R.drawable.qing_bg);
			} else if (nightFileName.contains("xue")) {
				weather_today.setBackgroundResource(R.drawable.xue_bg);
			} else if (nightFileName.contains("mai")) {
				weather_today.setBackgroundResource(R.drawable.mai_bg);
			} else if (nightFileName.contains("wu")) {
				weather_today.setBackgroundResource(R.drawable.wu_bg);
			}
		}
	}

	/**
	 * 加载进度框
	 */
	public void showProgressDialog() {
		String msg = null;
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setCancelable(false);
				msg = "查询中...";
			try {
				progressDialog.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			View convertView = progressDialog.getLayoutInflater().inflate(
					R.layout.view_progresslayout, null);
			TextView mTextView = (TextView) convertView
					.findViewById(R.id.textView1);
			mTextView.setText(msg);
			progressDialog.setContentView(convertView, new LayoutParams());
			progressDialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
				}
			});
		} else if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}
}


	