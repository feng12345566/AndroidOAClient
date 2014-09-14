package com.mct.client;

import com.mct.fragment.ScheduleManagerFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 日程管理
 * @author fengyouchun
 * @version 创建时间：2014年7月13日 下午8:00:46
 */
public class ScheduleManagerActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		 Fragment contentFragment=new ScheduleManagerFragment();
		 setParameter("日程管理",contentFragment,null,"个人中心",null);
	}

	@Override
	public void setParameter(String title, Fragment contentFragment,
			Bundle bundle, String leftTitle, String rightTitle) {
		// TODO Auto-generated method stub
		super.setParameter(title, contentFragment, bundle, leftTitle, rightTitle);
	}

}
