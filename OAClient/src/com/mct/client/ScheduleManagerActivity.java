package com.mct.client;

import com.mct.fragment.ScheduleManagerFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * �ճ̹���
 * @author fengyouchun
 * @version ����ʱ�䣺2014��7��13�� ����8:00:46
 */
public class ScheduleManagerActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		 Fragment contentFragment=new ScheduleManagerFragment();
		 setParameter("�ճ̹���",contentFragment,null,"��������",null);
	}

	@Override
	public void setParameter(String title, Fragment contentFragment,
			Bundle bundle, String leftTitle, String rightTitle) {
		// TODO Auto-generated method stub
		super.setParameter(title, contentFragment, bundle, leftTitle, rightTitle);
	}

}
