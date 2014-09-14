package com.mct.controls;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
	private Fragment[] fragments;
     
	public MyPagerAdapter(FragmentManager fm, Fragment[] fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments[arg0];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == fragments ? 0 : fragments.length;
	}

}
