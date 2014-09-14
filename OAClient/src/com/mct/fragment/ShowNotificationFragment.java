package com.mct.fragment;

import java.util.ArrayList;

import org.jivesoftware.smackx.packet.VCard;

import com.mct.client.R;
import com.mct.controls.AccessoryAdapter;
import com.mct.util.HttpRequestUtil;
import com.mct.util.TimeRender;
import com.mct.util.UserDbUtil;
import com.mct.util.XmppTool;
import com.mct.view.HorizontalListView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ShowNotificationFragment extends Fragment{
private TextView title,content,time,sender,apartment;
private HorizontalListView listView;
private ProgressDialog dialog;
private String id;
private String contentStr,timeStr,senderStr,apartmentStr;
private ArrayList<String> list;
private FrameLayout accessoryframeview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle bundle=getArguments();
		String titleText=bundle.getString("title");
		timeStr=bundle.getString("time");
		id=bundle.getString("id");
		View view=inflater.inflate(R.layout.activity_shownotification, null);
		title=(TextView) view.findViewById(R.id.nottitle);
		title.setText(titleText);
		time=(TextView) view.findViewById(R.id.notificationtime);
		content=(TextView) view.findViewById(R.id.notificationcontentview);
		sender=(TextView) view.findViewById(R.id.notificationsender);
		apartment=(TextView) view.findViewById(R.id.senderapartment);
		listView=(HorizontalListView) view.findViewById(R.id.accessoryview);
		accessoryframeview=(FrameLayout) view.findViewById(R.id.accessoryframeview);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
				String httpUrl="http://nat.nat123.net:14313/oa/message/QueryMessageContent.jsp?id="+Integer.valueOf(id.trim());
				String result=HttpRequestUtil.getResponsesByGet(httpUrl);
				if(!result.equals("")){
				   String[] str=result.split("@");
				   senderStr=str[3];
				   apartmentStr=str[2];				   
				   contentStr=str[0];
				   list=new ArrayList<String>();
				   if(str[1]!=null&&!str[1].equals("")){
					   for(int i=0;i<str[1].split(",").length;i++){
						   list.add(str[1].split(",")[i]);
					   }
				   }
				}
				handler.sendEmptyMessage(1);
			}
		}).start();
		return view;
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				dialog=new ProgressDialog(getActivity());
				dialog.setMessage("加载中...");
				dialog.show();
				break;

			case 1:
				dialog.dismiss();
				time.setText(TimeRender.getDate(Long.valueOf(timeStr), "yy年MM月dd日  HH:mm"));
				sender.setText(senderStr);
				content.setText(contentStr);
				apartment.setText(apartmentStr);
				if(list==null||list.size()==0){
					accessoryframeview.setVisibility(View.GONE);
				}else{
					accessoryframeview.setVisibility(View.VISIBLE);
				AccessoryAdapter adapter=new AccessoryAdapter(getActivity());
				adapter.setPathList(list);
				listView.setAdapter(adapter);
				}
				break;
			}
		}
		
	};

}
