package com.mct.client;

import java.util.ArrayList;

import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKOLSearchRecord;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mct.service.LocationService;
import com.mct.util.Common;
import com.mct.util.SharePeferenceUtils;
import com.mct.util.UserDataDbUtil;
import com.mct.util.XmppTool;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyLocationActivity extends Activity implements OnClickListener {
	private MapView mapView;
	private MyReceiver myReceiver;
	private MyLocationOverlay myLocationOverlay;
	private MapController mMapController;
	private String addressStr;
	private LinearLayout backlayout;
	private TextView backText, topText, sureBtn;
	private LocationData locationData;
	private String time;
	private String coordinate;// 签到点，查看时使用


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationactivity);
		coordinate = getIntent().getStringExtra("coordinate");
		mapView = (MapView) findViewById(R.id.bmapView);
		backlayout = (LinearLayout) findViewById(R.id.backlayout);
		backText = (TextView) findViewById(R.id.backText);
		sureBtn = (TextView) findViewById(R.id.sureBtn);
		topText = (TextView) findViewById(R.id.toptext);
		backText.setText("签到列表");
		myLocationOverlay = new MyLocationOverlay(mapView);
		mapView.getOverlays().add(myLocationOverlay);
		mapView.setBuiltInZoomControls(true);
		if (coordinate == null || coordinate.equals("")) {
			topText.setText("签到");
			sureBtn.setText("确认");
			sureBtn.setVisibility(View.VISIBLE);
		} else {
			topText.setText("签到地点");
			sureBtn.setVisibility(View.GONE);
			locationData = new LocationData();
			locationData.latitude = Double.valueOf(coordinate.split(",")[0]);
			locationData.longitude = Double.valueOf(coordinate.split(",")[1]);

			myLocationOverlay.setMarker(getResources().getDrawable(
					R.drawable.location));
			myLocationOverlay.setData(locationData);
			animateToPoint(locationData, mapView);
		}

		backlayout.setOnClickListener(this);
		sureBtn.setOnClickListener(this);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			Intent intent = new Intent(MyLocationActivity.this,
					LocationService.class);
			stopService(intent);
			overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
		}
		return true;
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (arg1.getAction() == Common.LOCSUCCESS) {
				Bundle bundle = arg1.getBundleExtra("location");
				locationData = new LocationData();
				locationData.direction = bundle.getFloat("direction");
				locationData.latitude = bundle.getDouble("latitude");
				locationData.longitude = bundle.getDouble("longitude");
				time = bundle.getString("time");
				addressStr = bundle.getString("address");
				animateToPoint(locationData, mapView);

			} 
		}
	}

	private void animateToPoint(LocationData locData, MapView mMapView) {
		mMapController = mMapView.getController();
		mMapController.setZoom(20);
		myLocationOverlay.setData(locData);
		mMapView.refresh();

		mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6),
				(int) (locData.longitude * 1e6)));

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.backlayout:
			this.finish();
			break;

		case R.id.sureBtn:
			if (locationData != null) {
				UserDataDbUtil.shareUserDataDb(this).insertLocation(
						XmppTool.loginUser.getUserId(), addressStr,
						locationData.latitude + "," + locationData.longitude,
						time);
				Intent intent = new Intent(this, AttendanceActivity.class);
				intent.putExtra("loc", addressStr);
				intent.putExtra("coordinate", locationData.latitude + ","
						+ locationData.longitude);
				intent.putExtra("time", time);
				setResult(RESULT_OK, intent);
				this.finish();
			} else {
				Toast.makeText(this, "还未定位完成，请稍后！", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (coordinate == null || coordinate.equals("")) {
			unregisterReceiver(myReceiver);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (coordinate == null || coordinate.equals("")) {
			IntentFilter filter = new IntentFilter();
			filter.addAction(Common.LOCSUCCESS);
			myReceiver = new MyReceiver();
			registerReceiver(myReceiver, filter);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.destroy();
	}

	

}
