package com.mct.client;

import com.mct.service.MsgService;
import com.mct.service.UserListLoadService;
import com.mct.util.Common;
import com.mct.util.PostMsgTask;
import com.mct.util.XmppTool;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class LoadingActivity extends Activity {
	private MyBroadcastReceiver receiver;
	private TextView helpText;
	private SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_loading);
		helpText = (TextView) findViewById(R.id.helptext);
		// 启动后台服务
		startService(new Intent(Common.ACTION_CONNECTION_SERVICE));
		receiver = new MyBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.mct.xmpp.loading");
		filter.addAction("com.mct.action.loaddone");
		registerReceiver(receiver, filter);
		handler.sendEmptyMessageDelayed(-2, 120 * 1000);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Common.VALUE_OK:
				// 登录成功
				XmppTool.setStop(true);
				if (settings.getBoolean("isfriendlistloaded", false)) {
					Intent intent = new Intent();
					intent.setClass(LoadingActivity.this, UserActivity.class);
					intent.putExtra("TAG", 0);
					LoadingActivity.this.startActivity(intent);
					LoadingActivity.this.finish();
				

				} else {
					Intent intent2 = new Intent(LoadingActivity.this,
							UserListLoadService.class);
					startService(intent2);
					helpText.setText("初始化通讯录中...");
				}

				break;
			case Common.VALUE_FAIL:
				Toast.makeText(LoadingActivity.this, "登录失败！",
						Toast.LENGTH_SHORT).show();
				startLogin();
				break;
			case -1:
				helpText.setText("自动登录中...");
				break;
			case -2:
				Toast.makeText(LoadingActivity.this, "连接服务器异常，请检查网络后重试！",
						Toast.LENGTH_SHORT).show();
				LoadingActivity.this.finish();
				Intent Intent = new Intent(LoadingActivity.this,
						MsgService.class);
				stopService(Intent);
				XmppTool.setStop(true);
				XmppTool.closeConnection();
				settings.edit().putBoolean("autologin", false).commit();
				break;
			}
		}

	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		settings = getSharedPreferences("setting_info", 0);

	}

	class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (arg1.getAction().equals("com.mct.xmpp.loading")) {
				Log.e("loading", "接收到广播");
				String userId = settings.getString("userid", "");
				String password = settings.getString("password", "");
				if (settings.getBoolean("autologin", false)) {
					handler.sendEmptyMessage(-1);

					new PostMsgTask(LoadingActivity.this, Common.OPT_LOGIN,
							handler).execute(userId, password);
				} else {
					startLogin();
				}
			} else if (arg1.getAction().equals("com.mct.action.loaddone")) {
				Intent intent = new Intent();
				intent.setClass(LoadingActivity.this, UserActivity.class);
				intent.putExtra("TAG", 0);
				LoadingActivity.this.startActivity(intent);
				LoadingActivity.this.finish();
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		handler.removeMessages(-2);
	}

	private void startLogin() {
		Intent intent = new Intent();
		intent.setClass(LoadingActivity.this, LoginActivity.class);
		LoadingActivity.this.startActivity(intent);
		LoadingActivity.this.finish();

	}

}
