package com.mct.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.mct.client.ChatActivity;
import com.mct.client.FlowmanagerActivity;
import com.mct.client.MailManagerActivity;
import com.mct.client.NotificationListActivity;
import com.mct.client.R;
import com.mct.controls.ConversationListAdapter;
import com.mct.model.Conversation;
import com.mct.model.User;
import com.mct.util.Common;
import com.mct.util.RecordUtil;
import com.mct.util.UserDbUtil;
import com.mct.util.XmppTool;

public class MsgFragment extends Fragment implements OnItemClickListener{
	private static final int MSG_OK = 1;
	private static final int MSG_ERROE = 2;
	private ListView msglist;
	private String mUserJid;
    private Context context;
    private ConversationListAdapter adapter;
 // 更新消息广播
 	private MsgReceiver receiver;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View msgview=inflater.inflate(R.layout.msglist, null);
		context=getActivity().getApplicationContext();
		
		msglist = (ListView) msgview.findViewById(R.id.msg_list);
		adapter=new ConversationListAdapter(context);
		msglist.setAdapter(adapter);
		msglist.setOnItemClickListener(this);
		receiver = new MsgReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Common.ACTION_UPDATE_MSG);
		getActivity().registerReceiver(receiver, filter);
		mUserJid=XmppTool.loginUser.getUserJid();
		return msgview;
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View listview, int position, long id) {
//		 TODO Auto-generated method stub
		Conversation conversation=adapter.getList().get(position);
		Intent intent=null;
		switch(conversation.getType()){
		case 0:
			String clickUserID=conversation.getWith_user();
			Log.e("mtag","开始与"+clickUserID+"聊天");
			intent=new Intent(context,ChatActivity.class);
			intent.putExtra(Common.KEY_WITH_USER, clickUserID);
			if(conversation.getIsGroup()==0){
			   intent.putExtra(Common.KEY_WITH_NAME,UserDbUtil.shareUserDb(getActivity()).getUserNameById(clickUserID));
			}else{
				intent.putExtra(Common.KEY_WITH_NAME,RecordUtil.shareRecordUtil(context).idToName(clickUserID.split("@")[0]));
			}
			startActivity(intent);
			break;
		case 1:
			intent=new Intent(context,MailManagerActivity.class);
			startActivity(intent);
			break;
		case 2:
			intent=new Intent(context,NotificationListActivity.class);
			startActivity(intent);
			break;
		case 3:
			intent=new Intent(context,MailManagerActivity.class);
			startActivity(intent);
			break;
		case 4:
			intent=new Intent(context,FlowmanagerActivity.class);
			startActivity(intent);
			break;

		}
		

	}

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			
		}

		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			loadData();
			adapter.notifyDataSetChanged();
			
			
		}

		

		@Override
		public void onDestroyView() {
			// TODO Auto-generated method stub
			super.onDestroyView();
			getActivity().unregisterReceiver(receiver);
			receiver = null;
		}




		/**
		 * 根据id更新会话
		 * 
		 * @param _id
		 */
		private void updateConversation(long _id) {
			LinkedList<Conversation> list = adapter.getList();
			boolean isFound = false;
			Conversation conversation = RecordUtil.shareRecordUtil(getActivity())
					.getConversation(_id);
			for (int i = 0; i < list.size(); i++) {
				if (_id == list.get(i).get_id()) {
					adapter.updateItem(i, conversation);
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				adapter.addItem(conversation);
			}
			adapter.notifyDataSetChanged();
		}
		private void loadData() {

			new Thread() {

				/* (non-Javadoc)
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					try {
						Thread.sleep(10);
						LinkedList<Conversation> list = RecordUtil.shareRecordUtil(
								context).getConversation(mUserJid.split("@")[0]
								);
						if (list != null) {
							Log.e("msg", "查询到"+list.size()+"条会话");
							adapter.setList(list);
							mHandler.sendEmptyMessage(MSG_OK);
						} else {
							mHandler.sendEmptyMessage(MSG_ERROE);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}.start();
		}
		
		private Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case MSG_OK:
					adapter.notifyDataSetChanged();
					break;
				case MSG_ERROE:
					Toast.makeText(getActivity(), "没有数据或者数据加载失败", Toast.LENGTH_LONG)
							.show();
					break;
				}
			}

		};

		class MsgReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				
				long _id = intent.getLongExtra(Common.KEY_THREAD_ID, -1);
				Log.e("msg", "会话列表收到消息id"+_id);
				updateConversation(_id);
				
				
			}

		}
	
	
	
	
	
}
