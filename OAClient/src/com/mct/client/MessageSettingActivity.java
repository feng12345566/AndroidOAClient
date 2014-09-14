package com.mct.client;

import com.mct.fragment.MessageSettingFragment;

import android.os.Bundle;

public class MessageSettingActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		MessageSettingFragment messageSettingFragment=new MessageSettingFragment();
		setParameter("消息设置", messageSettingFragment, null, "设置", null);
	}

}
