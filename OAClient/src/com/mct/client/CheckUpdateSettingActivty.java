package com.mct.client;

import com.mct.fragment.CheckUpdataSettingFragment;

import android.os.Bundle;

public class CheckUpdateSettingActivty extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		CheckUpdataSettingFragment checkUpdateSettingFragment=new CheckUpdataSettingFragment();
	    setParameter("������", checkUpdateSettingFragment, null, "����", null);
	}

}
