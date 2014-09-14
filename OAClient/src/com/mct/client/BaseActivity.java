package com.mct.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseActivity extends FragmentActivity implements OnClickListener{
	protected FragmentManager manager;
	protected FragmentTransaction transaction;
	protected Bundle bundle;// ��������
	protected TextView nextPage;// �������Ͻǹ��ܰ�ť
	private TextView titleText;// ���˱���
	protected LinearLayout lastPage;// ���ذ�ť
	protected Fragment contentFragment;
	private TextView lastPageText;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_base);
		titleText = (TextView) findViewById(R.id.toptitle);
		nextPage = (TextView) findViewById(R.id.nextpage);
		lastPage = (LinearLayout) findViewById(R.id.backlayout);
		lastPageText = (TextView) findViewById(R.id.backtitle);
		/* ��ȡmanager */
		manager = getSupportFragmentManager();
		/* �������� */
		transaction = manager.beginTransaction();
		lastPage.setOnClickListener(this);
	}

	/**
	 * ������ʵ�ִ˷������ñ�Ҫ����
	 * 
	 * @param title
	 *            �м����
	 * @param contentFragment
	 *            ����
	 * @param bundle
	 *            ���ݵ�fragment�Ĳ���
	 * @param leftTitle
	 *            ���Ͻǰ�ť����
	 * @param rightTitle
	 *            ���Ͻǰ�ť����
	 */
	public void setParameter(String title, Fragment contentFragment,
			Bundle bundle, String leftTitle, String rightTitle) {
		if(titleText!=null){
		titleText.setText(title);
		}
		if (rightTitle != null && !rightTitle.trim().equals("")) {
			nextPage.setVisibility(View.VISIBLE);
			nextPage.setText(rightTitle);
		} else {
			nextPage.setVisibility(View.GONE);
		}
		lastPageText.setText(leftTitle);
		this.contentFragment = contentFragment;
		if (bundle != null) {
			contentFragment.setArguments(bundle);
		}
		this.bundle = bundle;
		contentFragment.setArguments(bundle);
		/* ��Fragment��ӵ���Ӧ��λ�� */
		transaction.replace(R.id.baselayout, contentFragment);
		/* �ύ���� */
		transaction.commit();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			this.finish();
			overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backlayout:
			this.finish();
			overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
			break;

		default:
			break;
		}
	}
	
}
