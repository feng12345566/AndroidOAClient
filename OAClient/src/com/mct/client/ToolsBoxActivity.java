package com.mct.client;

import com.mct.fragment.ToolsBoxFragment;

import android.os.Bundle;

public class ToolsBoxActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		ToolsBoxFragment fragment=new ToolsBoxFragment();
		setParameter("������", fragment, null, "��������", null);
	}

}
