package com.mct.fragment;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import com.mct.client.R;
import com.mct.controls.ShowAccessoryAdapter;
import com.mct.util.HttpRequestUtil;
import com.mct.util.MyAnimationUtils;
import com.mct.util.TimeRender;
import com.mct.view.CustomClipLoading;
import com.mct.view.HorizontalListView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShowMessageFragment extends Fragment {
	private String title;
	private long time;
	private long id;
	private String content;
	private String accessory;
	private String name;
	private String group;
	private TextView showMessageSender;// 发送者
	private TextView showMessageTitle;// 标题
	private TextView showMessageContent;// 标题
	private TextView showMessageTime;// 标题
	private HorizontalListView showMessageAccessory;// 附件
	private TextView showMessageAccessoryNum;// 附件数
	private AlertDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_showmessage, null);
		showMessageSender = (TextView) view
				.findViewById(R.id.showmessagesender);
		showMessageTitle = (TextView) view.findViewById(R.id.showmessagetitle);
		showMessageContent = (TextView) view
				.findViewById(R.id.showmessagecontent);
		showMessageTime= (TextView) view
				.findViewById(R.id.showmessagetime);
		showMessageAccessoryNum = (TextView) view
				.findViewById(R.id.showmessageaccessorynum);
		showMessageAccessory = (HorizontalListView) view
				.findViewById(R.id.showmessageaccessory);
		Bundle bundle = getArguments();
		id = bundle.getLong("id");
		time = bundle.getLong("time");
		title = bundle.getString("title");
		getData();
		return view;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getAccessory() {
		return accessory;
	}

	private void getData() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
				String result=HttpRequestUtil.getResponsesByGet("http://nat.nat123.net:14313/oa/message/QueryMessageContent.jsp?id="+id);
				if (result != null) {
					try {
						JSONObject json = new JSONObject(result);
						content = json.getString("content");
						accessory = json.getString("accessory");
						name = json.getString("name");
						group = json.getString("group");
						handler.sendEmptyMessage(1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						handler.sendEmptyMessage(-1);
						e.printStackTrace();
					}
					
					
				} else {
					handler.sendEmptyMessage(-1);
				}
			}
		}).start();
	}
	
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				AlertDialog.Builder builder = new Builder(getActivity());
				builder.setView(new CustomClipLoading(getActivity()));
				dialog = builder.create();
				dialog.show();
				break;

			case 1:
				if(dialog!=null&&dialog.isShowing()){
				dialog.dismiss();
				}
				showMessageTitle.setText(title);
				showMessageContent.setText(content);
				showMessageSender.setText(name + "  " + group);
				showMessageTime.setText(TimeRender.getDate(time, "yy-MM-dd HH:mm"));
				String[] accessorys = accessory.split(",");
				if (accessorys == null||!accessory.contains(",")) {
					showMessageAccessoryNum.setText("附件（0）");
					showMessageAccessory.setVisibility(View.GONE);
				} else {
					showMessageAccessory.setVisibility(View.VISIBLE);
					showMessageAccessoryNum.setText("附件（"
							+ accessorys.length + "）");
					ArrayList<String> list=new ArrayList<String>();
					for(String a:accessorys){
						list.add(a);
					}
					ShowAccessoryAdapter adapter=new ShowAccessoryAdapter(getActivity());
					adapter.setAccessoryList(list);
					showMessageAccessory.setAdapter(adapter);
				}
				break;
			case -1:
				dialog.dismiss();
				getActivity().finish();
				MyAnimationUtils.outActivity(getActivity());
				break;
			}
		}
		
	};
	

}
