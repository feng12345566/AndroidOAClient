package com.mct.client;

import com.mct.controls.MyPagerAdapter;
import com.mct.fragment.DownLoadFragment;
import com.mct.fragment.UpLoadFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;

public class FileLoadHistoryActivity extends FragmentActivity implements OnClickListener{
private ViewPager mPager;
private RadioButton tab1,tab2;
private MyPagerAdapter adapter;
private TextView backText,titleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fileloadhistory_activity);
		mPager = (ViewPager) findViewById(R.id.loadlistviewpager);
        tab1 = (RadioButton) findViewById(R.id.downloadhistory);
		tab2 = (RadioButton) findViewById(R.id.uploadhistory);
		backText=(TextView) findViewById(R.id.backText);
		backText.setText("共享文件");
		titleText=(TextView) findViewById(R.id.toptext);
		titleText.setText("下载/上传记录");
		backText.setOnClickListener(this);
		init();
		mPager.setOnPageChangeListener(mListener);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		checked(0);
    }

	private void checked(int position) {
		switch (position) {
		case 0:
			tab1.setChecked(true);
			break;
		case 1:
			tab2.setChecked(true);
			break;
			
		}

		}
	private void init() {
		Fragment[] fragments = new Fragment[2];
		if(fragments[0]==null){
		fragments[0] = new DownLoadFragment();
		}
		if(fragments[1]==null){
		fragments[1] = new UpLoadFragment();
		}
		
		adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
		mPager.setAdapter(adapter);
	}

	/**
	 * 页面滑动监听
	 */
	private OnPageChangeListener mListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			checked(arg0);
			mPager.setCurrentItem(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};


	

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.downloadhistory:
			mPager.setCurrentItem(0);
			break;
		case R.id.uploadhistory:
			mPager.setCurrentItem(1);
			break;
		case R.id.backText:
			this.finish();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
        this.finish();
		return true;
	}

}
