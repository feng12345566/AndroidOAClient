package com.mct.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.mct.controls.RefreshListAdapter;
import com.mct.model.ChatMessage;
import com.mct.service.LocationService;
import com.mct.util.RecordUtil;
import com.mct.util.TimeRender;
import com.mct.util.UserDataDbUtil;
import com.mct.util.XmppTool;
import com.mct.view.XListView.IXListViewListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 考勤管理界面 通过百度地图定位实现
 * 
 * @author fengyouchun
 * @version 创建时间：2014年7月13日 下午5:27:52
 */
public class AttendanceActivity extends RefreshActivity implements
		OnClickListener, IXListViewListener ,OnItemClickListener{
	private  SimpleAdapter adapter;
	private int page = 1;
    private LinkedList<HashMap<String,String>> contentList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		backView.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		backText.setText("个人中心");
		titleText.setText("签到记录");
		nextBtn.setText("签到");
		nextBtn.setOnClickListener(this);
		backView.setOnClickListener(this);
		contentList=UserDataDbUtil.shareUserDataDb(this).getLocationList(XmppTool.loginUser.getUserId(), 1);
		adapter=new SimpleAdapter(this, contentList, R.layout.attendancelistitem, new String[]{"loc","time"},
				new int[]{R.id.atttendanceloc,R.id.atttendancetime});
		refreshListView.setAdapter(adapter);
		refreshListView.setPullLoadEnable(true);
		refreshListView.setPullLoadEnable(true);
		refreshListView.setXListViewListener(this);
		refreshListView.setOnItemClickListener(this);
		refreshListView.startLoadMore();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.backview:
			Intent intent = new Intent(AttendanceActivity.this,
					UserActivity.class);
			intent.putExtra("TAG", 2);
			startActivity(intent);
			break;

		case R.id.nextBtn:
			
			Intent intent3=new Intent(AttendanceActivity.this,LocationService.class);
			intent3.putExtra("locationType", 2);
			startService(intent3);
			Intent intent2 = new Intent(AttendanceActivity.this,
					MyLocationActivity.class);
			startActivityForResult(intent2, 1);
			overridePendingTransition(R.anim.in_zoom, R.anim.none);
			break;
		}
	}

	//
	// @Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		page=1;
		 contentList=UserDataDbUtil.shareUserDataDb(AttendanceActivity.this).getLocationList(XmppTool.loginUser.getUserId(), page);
		 refreshListView.stopRefresh();
		 String refreshTime=TimeRender.getDate(System.currentTimeMillis(), "yy-MM-dd HH:mm:ss");
		 SharedPreferences preferences=getSharedPreferences("data", 0);
		 refreshListView.setRefreshTime(preferences.getString("refreshTime", "刚刚"));
		 preferences.edit().putString("refreshTime", refreshTime).commit();
		 if(contentList!=null){
		 adapter.notifyDataSetChanged();
		 }
	
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		page++;
		System.out.println("加载数据");
		LinkedList<HashMap<String,String>> list = UserDataDbUtil.shareUserDataDb(
				AttendanceActivity.this).getLocationList(XmppTool.loginUser.getUserId(), page);
		refreshListView.stopLoadMore();
		if (list != null) {
			
		    contentList.addAll(list);
			adapter.notifyDataSetChanged();
		} else {
			page--;
			Toast.makeText(AttendanceActivity.this, "没有更多数据",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			HashMap<String,String> map=new HashMap<String, String>();
			map.put("loc", data.getStringExtra("loc"));
			map.put("time",  data.getStringExtra("time"));
			map.put("coordinate", data.getStringExtra("coordinate"));
			contentList.addFirst(map);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent4 = new Intent(AttendanceActivity.this,
				MyLocationActivity.class);
		Log.e("position", (arg2-1)+"");
		intent4.putExtra("coordinate", contentList.get(arg2-1).get("coordinate"));
		startActivity(intent4);
		overridePendingTransition(R.anim.in_zoom, R.anim.none);
	}
	
	
}
