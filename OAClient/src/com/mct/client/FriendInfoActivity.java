package com.mct.client;

import com.mct.fragment.FriendInfoFragment;
import com.mct.model.User;
import com.mct.util.Common;
import com.mct.util.UserDbUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendInfoActivity extends FragmentActivity implements
		OnClickListener {
	private String withUserId;
	private String userName;
	private TextView chatTV;
	private LinearLayout backLayout;
	private TextView backText;
	private TextView topText;
	private TextView createCall;
	private TextView createSMS;
	private TextView createEmail;
	private FragmentManager manager;
	private FragmentTransaction transaction;
	private User user;
	private String callPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friendinfo);
		Intent intent = getIntent();
		withUserId = intent.getStringExtra(Common.KEY_WITH_USER);
		userName = intent.getStringExtra(Common.KEY_USER_NAME);
		chatTV = (TextView) findViewById(R.id.createchat);
		chatTV.setOnClickListener(this);
		backLayout = (LinearLayout) findViewById(R.id.backlayout);
		backText = (TextView) findViewById(R.id.backText);
		backText.setText("返回");
		topText = (TextView) findViewById(R.id.toptext);
		backLayout.setOnClickListener(this);
		createCall = (TextView) findViewById(R.id.createcall);
		createCall.setOnClickListener(this);
		createSMS = (TextView) findViewById(R.id.createsms);
		createSMS.setOnClickListener(this);
		createEmail = (TextView) findViewById(R.id.createemail);
		createEmail.setOnClickListener(this);
		topText.setText("名片");
		// 获取碎片管理器
		manager = getSupportFragmentManager();
		// 获取转换器
		transaction = manager.beginTransaction();
		// 实例化碎片
		FriendInfoFragment fragment = new FriendInfoFragment();
		// 替换帧布局位碎片
		transaction.replace(R.id.friendinfolayout, fragment);
		// 传递参数
		Bundle bundle = new Bundle();
		bundle.putString("userid", withUserId);
		fragment.setArguments(bundle);
		// 确认转换
		transaction.commit();
		user = UserDbUtil.shareUserDb(this).getUserInfo(withUserId);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.createchat:
			Intent intent = new Intent(FriendInfoActivity.this,
					ChatActivity.class);
			intent.putExtra(Common.KEY_WITH_USER, withUserId);
			intent.putExtra(Common.KEY_WITH_NAME, userName);
			startActivity(intent);
			this.finish();
			break;

		case R.id.backlayout:
			this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.createcall:
			if (user != null) {
				if ((!isNull(user.getMobilePhone()))
						&& !isNull(user.getPhoneNumber())) {
					// 两个号码均非空
					final String[] items = new String[] {
							user.getMobilePhone(), user.getPhoneNumber() };
					callPhone=items[0];
					AlertDialog.Builder builder = new Builder(this);
					builder.setIcon(R.drawable.icon_hint);
					builder.setTitle("请选择要拨打的号码");
					builder.setSingleChoiceItems(items,0,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									callPhone = items[which];
								}

							});
					builder.setPositiveButton("拨打",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Intent phoneIntent = new Intent(
											"android.intent.action.CALL", Uri
													.parse("tel:" + callPhone));
									startActivity(phoneIntent);
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
					builder.create().show();
				} else if ((!isNull(user.getMobilePhone()))
						|| (!isNull(user.getPhoneNumber()))) {
					// 有一个非空
					callPhone = !isNull(user.getMobilePhone()) ? user
							.getMobilePhone() : user.getPhoneNumber();
					AlertDialog.Builder builder = new Builder(this);
					builder.setIcon(R.drawable.icon_hint);
					builder.setTitle("请选择操作");
					builder.setMessage("确认拨打号码：" + callPhone + "？");
					builder.setPositiveButton("拨打",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Intent phoneIntent = new Intent(
											"android.intent.action.CALL", Uri
													.parse("tel:" + callPhone));
									startActivity(phoneIntent);
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
					builder.create().show();
				} else {
					Toast.makeText(this, "对方未设置手机号和座机号！", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(this, "未找到对方手机号和座机号！", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case R.id.createsms:
			if (user== null||isNull(user.getMobilePhone())){
				Toast.makeText(this, "对方未设置手机号！", Toast.LENGTH_SHORT)
				.show();
			}else{
				Intent intent2 = new Intent();

				//系统默认的action，用来打开默认的短信界面
				intent2.setAction(Intent.ACTION_SENDTO);

				//需要发短息的号码
				intent2.setData(Uri.parse("smsto:"+user.getMobilePhone()));
				startActivity(intent2);
			}
			break;
		case R.id.createemail:
			Intent emailIntent = new Intent(this,PostMessageActivity.class);
			emailIntent.putExtra("receiver",withUserId);  
			emailIntent.putExtra("opt", 1);
			startActivity(emailIntent);
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
		}
		return true;
	}

	private boolean isNull(String value) {
		if (value == null || value.equals("null")) {
			return true;
		} else {
			return false;
		}
	}

}
