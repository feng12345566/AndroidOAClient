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
	protected Bundle bundle;// 传递数据
	protected TextView nextPage;// 顶端右上角功能按钮
	private TextView titleText;// 顶端标题
	protected LinearLayout lastPage;// 返回按钮
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
		/* 获取manager */
		manager = getSupportFragmentManager();
		/* 创建事物 */
		transaction = manager.beginTransaction();
		lastPage.setOnClickListener(this);
	}

	/**
	 * 子类需实现此方法设置必要参数
	 * 
	 * @param title
	 *            中间标题
	 * @param contentFragment
	 *            内容
	 * @param bundle
	 *            传递到fragment的参数
	 * @param leftTitle
	 *            左上角按钮名称
	 * @param rightTitle
	 *            右上角按钮名称
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
		/* 把Fragment添加到对应的位置 */
		transaction.replace(R.id.baselayout, contentFragment);
		/* 提交事物 */
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
