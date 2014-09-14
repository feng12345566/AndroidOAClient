package com.mct.client;

import com.mct.fragment.ShowNotificationFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

public class ShowNotificationActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Intent intent=getIntent();
		ShowNotificationFragment contentFragment=new ShowNotificationFragment();
		Bundle bundle=new Bundle();
		bundle.putString("id", intent.getStringExtra("id"));
		bundle.putString("title", intent.getStringExtra("title"));
		bundle.putString("time", intent.getStringExtra("time"));
		setParameter("通知内容",contentFragment,bundle,"通知列表",null);
	}

	@Override
	public void setParameter(String title, Fragment contentFragment,
			Bundle bundle, String leftTitle, String rightTitle) {
		// TODO Auto-generated method stub
		super.setParameter(title, contentFragment, bundle, leftTitle, rightTitle);
	}

	

}
