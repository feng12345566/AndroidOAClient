package com.mct.client;

import com.mct.fragment.AttendanceSettingFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class AttendanceSettingActivity extends BaseActivity implements OnClickListener{
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lastPage.setOnClickListener(this);
		AttendanceSettingFragment contentFragment=new AttendanceSettingFragment();
		setParameter("«©µΩ…Ë÷√",contentFragment,null,"…Ë÷√",null);
	}
	



	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.backlayout:
			this.finish();
			overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
			break;

		default:
			break;
		}
	}

}
