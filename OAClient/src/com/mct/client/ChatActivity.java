package com.mct.client;

import java.util.ArrayList;
import java.util.List;

import com.mct.controls.ChatMessageAdapter;
import com.mct.model.ChatMessage;
import com.mct.util.Common;
import com.mct.util.PostMsgTask;
import com.mct.util.RecordUtil;
import com.mct.util.XmppTool;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity implements OnClickListener {

	// private static final int MSG_GET_INFO = 1;
	private static final int MSG_OK = 10;
	private static final int MSG_FAIL = 20;
	private static final int MSG_IN = 30;
	private EditText sendEt;
	private TextView chatUser;
	private ListView mListView;
	private TextView backText;
    private LinearLayout backLayout;
	private static ChatMessageAdapter adapter;
	private ArrayList<ChatMessage> list;
	private long thread_id;

	private String with_user;
	private String with_userName;

	private String login_user;

	// ���͵���Ϣ���ͣ����ģ�Ⱥ�ģ�
	private int sendType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.chat);
		Intent intent = getIntent();
		with_user = intent.getStringExtra(Common.KEY_WITH_USER);// �Է���id
		with_userName = intent.getStringExtra(Common.KEY_WITH_NAME);// �Է�������
		sendEt = (EditText) findViewById(R.id.formclient_text);
		mListView = (ListView) findViewById(R.id.formclient_listview);
		adapter = new ChatMessageAdapter(this);
		mListView.setAdapter(adapter);

		findViewById(R.id.formclient_btsend).setOnClickListener(this);
		chatUser = (TextView) findViewById(R.id.toptext);
		login_user = XmppTool.loginUser.getUserId();// ��¼�û�id
		backText=(TextView) findViewById(R.id.backText);
		backText.setText("����");
		chatUser.setText(with_userName);
		Log.e("m_tag", "����ҳ��====with_userName:" + with_userName);
		if (with_user.contains("@conference") || with_user.contains("@private")) {
			sendType = Common.TYPE_SEND_CHATROOM;
			List<String> list = XmppTool.shareConnectionManager(
					ChatActivity.this).findMulitUser(with_user);
			for (String s : list) {
				Log.e("m_tag", "======" + s);
			}
		} else {
			sendType = Common.TYPE_SEND_SINGLE;
		}

		thread_id = intent.getLongExtra(Common.KEY_THREAD_ID, -1);
		// ��ͨѶ¼����
		if (thread_id == -1) {
			// ��ȡ�Ựid
			thread_id = RecordUtil.shareRecordUtil(ChatActivity.this)
					.getConversationID(login_user, with_user, 0);
		}
		if (thread_id != -1) {
			// ����δ��Ϊ�Ѷ�
			RecordUtil.shareRecordUtil(ChatActivity.this)
					.updateConversationMessage(thread_id);
			loadData();// ���������¼
		}
		receiver = new MsgReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Common.ACTION_GET_ONLINE_MSG);
		registerReceiver(receiver, filter);

	}

	// ��ȡ�����¼
	private void loadData() {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					Thread.sleep(10);
					list = RecordUtil.shareRecordUtil(ChatActivity.this)
							.getChatMessage(thread_id, 1,0,"date asc");
					if (list != null) {
						Log.e("oncreate", "���������¼" + list.size());
						adapter.setList(list);
						mHandler.sendEmptyMessage(MSG_OK);
					} else {
						mHandler.sendEmptyMessage(MSG_FAIL);
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
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_OK:
				adapter.notifyDataSetChanged();
				// �������ײ�
				mListView.setSelection(adapter.getCount() - 1);
				break;
			case MSG_FAIL:
				Toast.makeText(ChatActivity.this, "û�����ݻ����ʧ��",
						Toast.LENGTH_LONG).show();
				break;
			case MSG_IN:
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// ������Ϣ
		case R.id.formclient_btsend:
			String content = sendEt.getText().toString();
			if (null != content && !"".equals(content)) {
				new PostMsgTask(ChatActivity.this, Common.OPT_SEND_MSG,
						mHandler).execute(sendType, with_user, content);

				addMsg(new ChatMessage(login_user, System.currentTimeMillis(),
						Common.MSG_OUT, content));
				sendEt.setText("");
			}

			break;
		}

	}

	// ˢ���б�
	private void addMsg(ChatMessage msg) {
		adapter.addMsg(msg);
		adapter.notifyDataSetChanged();
	}

	// ������Ϣ
	class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.e("msg_in", "�����յ�����Ϣ");
			long _id = intent.getLongExtra(Common.KEY_THREAD_ID, -1);
			long msg_id = intent.getLongExtra("msg_id", -1);
			long current_id = RecordUtil.shareRecordUtil(ChatActivity.this)
					.getConversationID(login_user, with_user, 0);
			// ��������Ϣ���Ե�ǰ�������
			if (current_id == _id) {
				if (thread_id == -1) {
					thread_id = current_id;
				}
				// ������ϢΪ�Ѷ�
				RecordUtil.shareRecordUtil(ChatActivity.this)
						.updateConversationMessage(_id);
				ChatMessage msg = RecordUtil.shareRecordUtil(ChatActivity.this)
						.getChatMessage(msg_id);
				adapter.addMsg(msg);
				mHandler.sendEmptyMessage(MSG_OK);

			}

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		this.finish();
		return true;
	}

	private MsgReceiver receiver;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		RecordUtil.shareRecordUtil(ChatActivity.this)
				.updateConversationMessage(thread_id);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		unregisterReceiver(receiver);
		receiver = null;
	}

}