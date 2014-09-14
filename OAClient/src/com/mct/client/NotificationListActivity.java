package com.mct.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.mct.controls.RefreshListAdapter;
import com.mct.model.ChatMessage;
import com.mct.util.RecordUtil;
import com.mct.view.XListView.IXListViewListener;
import com.mct.view.XListView.OnXScrollListener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class NotificationListActivity extends RefreshActivity implements OnClickListener,IXListViewListener,OnItemClickListener{
    private ArrayList<ChatMessage> contentList;
    private int count=1;
    private RefreshListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		backView.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		backText.setText("个人中心");
		titleText.setText("通知列表"); 
		nextBtn.setText("发布通知");
		nextBtn.setOnClickListener(this);
		backView.setOnClickListener(this);
		contentList=RecordUtil.shareRecordUtil(NotificationListActivity.this).getChatMessage(-2, count, 2, "date desc");
		adapter=new RefreshListAdapter(this);
		adapter.setContentList(contentList);
		refreshListView.setAdapter(adapter);
		refreshListView.setPullLoadEnable(true);
		refreshListView.setXListViewListener(this);
		refreshListView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.backview:
			Intent intent=new Intent(NotificationListActivity.this,UserActivity.class);
			intent.putExtra("TAG", 2);
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;

		case R.id.nextBtn:
			Intent intent2=new Intent(NotificationListActivity.this,PostMessageActivity.class);
			intent2.putExtra("opt", 2);
			startActivity(intent2);
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}
	}
	
	
	


	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		contentList=RecordUtil.shareRecordUtil(NotificationListActivity.this).getChatMessage(-2, count, 2, "date desc");
		refreshListView.stopRefresh();
		refreshListView.setRefreshTime("刚刚");
		if(contentList!=null){
		adapter.setContentList(contentList);
		adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		count++;
		ArrayList<ChatMessage> list=RecordUtil.shareRecordUtil(NotificationListActivity.this).getChatMessage(-2, count, 2, "date desc");
		refreshListView.stopLoadMore();
		if(list!=null){
		adapter.addData(list);
		adapter.notifyDataSetChanged();
		}else{
			count--;
			Toast.makeText(NotificationListActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.e("position", arg2+"");
		Intent intent3=new Intent(this,ShowNotificationActivity.class);
		ChatMessage chatMessage=contentList.get(arg2-1);
		String[] content=chatMessage.getBody().split("@");
		intent3.putExtra("id", content[2]);
		intent3.putExtra("title", content[0].replace("[%news%]", ""));
		intent3.putExtra("time", content[1]);
		startActivity(intent3);
	}
    
}
