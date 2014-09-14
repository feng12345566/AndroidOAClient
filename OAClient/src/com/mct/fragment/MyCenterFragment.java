package com.mct.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.mct.client.AttendanceActivity;
import com.mct.client.FileShareActivity;
import com.mct.client.FlowmanagerActivity;
import com.mct.client.ForumActivity;
import com.mct.client.GroupChatListActivity;
import com.mct.client.MailManagerActivity;
import com.mct.client.MeetingManagerActivity;
import com.mct.client.NotificationListActivity;
import com.mct.client.R;
import com.mct.client.ReserachActivity;
import com.mct.client.ScheduleManagerActivity;
import com.mct.client.ToolsBoxActivity;
import com.mct.client.WeatherSearchActivity;
import com.mct.controls.MyCenterAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MyCenterFragment extends Fragment implements OnItemClickListener {
	private Context context;
	private GridView myCenterBtn;
	private Intent[] intents;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.mycenterfragment, null);
		context = getActivity();
		myCenterBtn = (GridView) view.findViewById(R.id.mycenter);
		MyCenterAdapter adapter = new MyCenterAdapter(context);
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		int[] iv = new int[] { R.drawable.qunzu, R.drawable.flowmanager,
				R.drawable.fileshare, R.drawable.mail, R.drawable.notification,
				R.drawable.kaoqin, R.drawable.richeng, R.drawable.research,R.drawable.tools
			    };
		String[] tv = new String[] { "讨论组", "流程管理", "文件共享", "我的邮件", "通知管理",
				"考勤管理", "日程管理", "问卷调查", "工具箱"};
		for (int i = 0; i < iv.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("iv", iv[i]);
			map.put("tv", tv[i]);
			list.add(map);
			map = null;
		}
		adapter.setList(list);
		myCenterBtn.setAdapter(adapter);
		myCenterBtn.setOnItemClickListener(this);
		intents = new Intent[] {
				new Intent(context, GroupChatListActivity.class),
				new Intent(context, FlowmanagerActivity.class),
				new Intent(context, FileShareActivity.class),
				new Intent(context, MailManagerActivity.class),
				new Intent(context, NotificationListActivity.class),
				new Intent(context, AttendanceActivity.class),
				new Intent(context, ScheduleManagerActivity.class),
				new Intent(context, ReserachActivity.class),
				new Intent(context, ToolsBoxActivity.class)
				};
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(arg2==2){
			intents[arg2].putExtra("tag", "sharefile");
		}
		startActivity(intents[arg2]);

	}

}
