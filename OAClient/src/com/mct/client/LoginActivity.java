package com.mct.client;

import com.loopj.android.image.SmartImageView;
import com.mct.client.R;
import com.mct.service.UserListLoadService;
import com.mct.util.AESUtil;
import com.mct.util.Common;
import com.mct.util.PostMsgTask;
import com.mct.util.XmppTool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private final static String SETTING_INFO = "setting_info";
	private final static String UER_ID = "userid";
	private final static String PASSWORD = "password";
	private final static String SERVER = "server";
	private final static String REMEBERPASSWORD = "remeberpassword";
	private final static String AUTOLOGIN = "autologin";
	private XmppTool connect;
	private EditText useridText, pwdText;
	private LinearLayout severview;
	private LinearLayout supersetting;
	private EditText sever;
	private int count = 0;
	private String userId, passWd;
	private CheckBox remeber, outologin;
	private ProgressDialog dialog;
	private SharedPreferences settings;
	private MyBroadcastReceiver myBroadcastReceiver;
	private boolean isListLoaded;
	private SmartImageView myheadimage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		settings = getSharedPreferences(SETTING_INFO, 0);
		// 获取用户和密码
		useridText = (EditText) findViewById(R.id.formlogin_userid);
		pwdText = (EditText) findViewById(R.id.formlogin_pwd);
		supersetting = (LinearLayout) findViewById(R.id.supersetting);
		myheadimage=(SmartImageView) findViewById(R.id.myheadimage);
		sever = (EditText) findViewById(R.id.sever);
		severview = (LinearLayout) findViewById(R.id.severview);
		useridText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				myheadimage.setImageUrl(Common.HTTPSERVICE+s.toString()+".jpg", R.drawable.touxiang);
			}
		});
		remeber = (CheckBox) findViewById(R.id.remeber);
		outologin = (CheckBox) findViewById(R.id.outologin);
		Button btsave = (Button) findViewById(R.id.formlogin_btsubmit);
		btsave.setOnClickListener(this);
		Button btcancel = (Button) findViewById(R.id.formlogin_btcancel);
		btcancel.setOnClickListener(this);
		supersetting.setOnClickListener(this);

		outologin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				settings.edit().putBoolean(AUTOLOGIN, arg1).commit();
				if (arg1) {
					remeber.setChecked(true);

				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		// 根据ID来进行提交或者取消
		switch (v.getId()) {
		case R.id.formlogin_btsubmit:
			// 取得填入的用户和密码
			userId = useridText.getText().toString();
			passWd = pwdText.getText().toString();
			if (userId.equals("") || passWd.equals("")) {
				Toast.makeText(LoginActivity.this, "账号或密码不能为空！",
						Toast.LENGTH_SHORT).show();
			} else {
				isListLoaded = settings.getString(UER_ID, "").equals(userId);
				settings.edit().putString(UER_ID, userId)
						.putString(SERVER, sever.getText().toString()).commit();
                Log.e("passWd", passWd);
               
				try {
					 Log.e("passWd", AESUtil.encryptDES(passWd, AESUtil.KEY));
					settings.edit().putString(PASSWORD,AESUtil.encryptDES(passWd, AESUtil.KEY))
							.putBoolean(REMEBERPASSWORD, remeber.isChecked())
							.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(-1);
				login();
			}
			break;
		case R.id.formlogin_btcancel:
			stopService(new Intent(Common.ACTION_CONNECTION_SERVICE));
			finish();
			break;

		case R.id.supersetting:

			Log.e("mtag", "" + count);
			if (count % 2 == 0) {
				severview.setVisibility(0);
			} else {
				severview.setVisibility(8);
			}
			count++;
			break;
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1:
				dialog = showProgressDialog("登录中...");
				dialog.show();
				break;
			case Common.VALUE_OK:
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				if (isListLoaded) {
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, UserActivity.class);
					intent.putExtra("TAG", 0);
					LoginActivity.this.startActivity(intent);
					LoginActivity.this.finish();

				} else {
					Intent intent2 = new Intent(LoginActivity.this,
							UserListLoadService.class);
					startService(intent2);
					dialog = showProgressDialog("初始化通讯录中...");
					dialog.show();
				}

				break;
			case Common.VALUE_FAIL:
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				outologin.setChecked(false);
				Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		useridText.setText(settings.getString(UER_ID, ""));
		String pd=settings.getString(PASSWORD, "");
		Log.e("pd",pd);
		if(settings.getBoolean(REMEBERPASSWORD, false)&&!pd.equals("")){
		   try {
			pwdText.setText(AESUtil.decryptDES(pd, AESUtil.KEY));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else{
			pwdText.setText("");
		}
		sever.setText(settings.getString(SERVER, ""));
		remeber.setChecked(settings.getBoolean(REMEBERPASSWORD, false));
		outologin.setChecked(settings.getBoolean(AUTOLOGIN, false));
		myBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.mct.action.loaddone");
		registerReceiver(myBroadcastReceiver, filter);
		isListLoaded = settings.getBoolean("isfriendlistloaded", false);
	}

	private ProgressDialog showProgressDialog(String str) {
		ProgressDialog builder = new ProgressDialog(this);
		builder.setMessage(str);
		return builder;

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myBroadcastReceiver);
	}

	private void login() {
		new PostMsgTask(LoginActivity.this, Common.OPT_LOGIN, handler).execute(
				userId, passWd);

	}

	class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (arg1.getAction().equals("com.mct.action.loaddone")) {
				Intent intent = new Intent(LoginActivity.this, UserActivity.class);
				intent.putExtra("TAG", 0);
				LoginActivity.this.startActivity(intent);
				LoginActivity.this.finish();
			}
		}
	}
}