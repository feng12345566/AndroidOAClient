package com.mct.client;

import com.mct.fragment.MyInfoFragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MyInfoActivity extends BaseActivity implements OnClickListener {
	private MyInfoFragment myInfoFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		myInfoFragment = new MyInfoFragment();
		setParameter("个人信息", myInfoFragment, null, "设置", "修改");
		nextPage.setOnClickListener(this);
		lastPage.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.nextpage:
			if (!myInfoFragment.isEdit()) {
				nextPage.setText("确定");
				myInfoFragment.editInfo();
				myInfoFragment.setEdit(true);
			} else {
				myInfoFragment.postData();
			}
			break;

		case R.id.backlayout:
			this.finish();
			overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
			break;
		}
		
	}

}
