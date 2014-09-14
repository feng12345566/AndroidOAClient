package com.mct.client;

import com.mct.fragment.EmailSearchFragment;

import android.os.Bundle;

public class EmailSearchActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		EmailSearchFragment fragment=new EmailSearchFragment();
		setParameter("ËÑË÷ÓÊ¼þ", fragment, null, "·µ»Ø", null);
	}

}
