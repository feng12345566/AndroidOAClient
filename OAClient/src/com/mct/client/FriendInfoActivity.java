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
		backText.setText("����");
		topText = (TextView) findViewById(R.id.toptext);
		backLayout.setOnClickListener(this);
		createCall = (TextView) findViewById(R.id.createcall);
		createCall.setOnClickListener(this);
		createSMS = (TextView) findViewById(R.id.createsms);
		createSMS.setOnClickListener(this);
		createEmail = (TextView) findViewById(R.id.createemail);
		createEmail.setOnClickListener(this);
		topText.setText("��Ƭ");
		// ��ȡ��Ƭ������
		manager = getSupportFragmentManager();
		// ��ȡת����
		transaction = manager.beginTransaction();
		// ʵ������Ƭ
		FriendInfoFragment fragment = new FriendInfoFragment();
		// �滻֡����λ��Ƭ
		transaction.replace(R.id.friendinfolayout, fragment);
		// ���ݲ���
		Bundle bundle = new Bundle();
		bundle.putString("userid", withUserId);
		fragment.setArguments(bundle);
		// ȷ��ת��
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
					// ����������ǿ�
					final String[] items = new String[] {
							user.getMobilePhone(), user.getPhoneNumber() };
					callPhone=items[0];
					AlertDialog.Builder builder = new Builder(this);
					builder.setIcon(R.drawable.icon_hint);
					builder.setTitle("��ѡ��Ҫ����ĺ���");
					builder.setSingleChoiceItems(items,0,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									callPhone = items[which];
								}

							});
					builder.setPositiveButton("����",
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
					builder.setNegativeButton("ȡ��",
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
					// ��һ���ǿ�
					callPhone = !isNull(user.getMobilePhone()) ? user
							.getMobilePhone() : user.getPhoneNumber();
					AlertDialog.Builder builder = new Builder(this);
					builder.setIcon(R.drawable.icon_hint);
					builder.setTitle("��ѡ�����");
					builder.setMessage("ȷ�ϲ�����룺" + callPhone + "��");
					builder.setPositiveButton("����",
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
					builder.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
					builder.create().show();
				} else {
					Toast.makeText(this, "�Է�δ�����ֻ��ź������ţ�", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(this, "δ�ҵ��Է��ֻ��ź������ţ�", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case R.id.createsms:
			if (user== null||isNull(user.getMobilePhone())){
				Toast.makeText(this, "�Է�δ�����ֻ��ţ�", Toast.LENGTH_SHORT)
				.show();
			}else{
				Intent intent2 = new Intent();

				//ϵͳĬ�ϵ�action��������Ĭ�ϵĶ��Ž���
				intent2.setAction(Intent.ACTION_SENDTO);

				//��Ҫ����Ϣ�ĺ���
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
