package com.mct.client;

import com.mct.fragment.InboxFragment;
import com.mct.fragment.RecycleEmailFragment;

import android.os.Bundle;

public class RecycleEmailActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		RecycleEmailFragment fragment=new RecycleEmailFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("isdelete", 1);
		setParameter("ªÿ ’’æ", fragment, bundle, "∑µªÿ", null);
	}

}
