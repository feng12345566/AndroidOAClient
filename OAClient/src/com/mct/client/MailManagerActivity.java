package com.mct.client;

import com.mct.controls.MyPagerAdapter;
import com.mct.fragment.InboxFragment;
import com.mct.fragment.OutboxFragment;
import com.mct.transform.CubeOutTransformer;
import com.mct.util.MyAnimationUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RadioButton;
import android.widget.TextView;

public class MailManagerActivity extends FragmentActivity implements
		OnClickListener, OnMenuItemClickListener {
	private ViewPager mPager;
	private RadioButton tab1, tab2;
	private MyPagerAdapter adapter;
	private int tag = 0;
	private ImageView moreApplicationBtn;
	private LinearLayout b;
	private TextView backmycenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emailmanager_activity);
		mPager = (ViewPager) findViewById(R.id.emailviewpager);
		tab1 = (RadioButton) findViewById(R.id.inbox);
		tab2 = (RadioButton) findViewById(R.id.outbox);
		backmycenter = (TextView) findViewById(R.id.backmycenter);
		b = (LinearLayout) findViewById(R.id.backlayout);
		moreApplicationBtn = (ImageView) findViewById(R.id.moreapplicationbtn);
		moreApplicationBtn.setOnClickListener(this);
		backmycenter.setText("·µ»Ø");
		init();
		mPager.setCurrentItem(tag);
		mPager.setOnPageChangeListener(mListener);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		b.setOnClickListener(this);
		tab1.setChecked(true);
	}

	private void checked(int position) {
		tag = position;
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
		fragments[0] = new InboxFragment();
		fragments[1] = new OutboxFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("isDelete", 0);
		fragments[0].setArguments(bundle);
		adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
		mPager.setAdapter(adapter);
		if (VERSION.SDK_INT >= 11) {
			mPager.setPageTransformer(true, new CubeOutTransformer());
		}
	}

	/**
	 * Ò³Ãæ»¬¶¯¼àÌý
	 */
	private OnPageChangeListener mListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Log.e("postion", arg0 + "");
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
		case R.id.inbox:
			mPager.setCurrentItem(0);
			break;
		case R.id.outbox:
			mPager.setCurrentItem(1);
			break;

		case R.id.moreapplicationbtn:
			PopupMenu popupMenu = new PopupMenu(this, v);
			popupMenu.getMenuInflater().inflate(R.menu.menu_emailmanager,
					popupMenu.getMenu());
			popupMenu.show();
			popupMenu.setOnMenuItemClickListener(this);
			break;
		case R.id.backlayout:
			finish();
			MyAnimationUtils.outActivity(this);
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mPager.setCurrentItem(tag);
		checked(tag);
		mPager.setOnPageChangeListener(mListener);

		super.onResume();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.email_write:
			Intent intent1 = new Intent(this, PostMessageActivity.class);
			intent1.putExtra("opt", 1);
			startActivity(intent1);
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.email_draftbox:
			Intent intent2 = new Intent(this, DraftBoxActivity.class);
			startActivity(intent2);
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.email_recycle:
			Intent intent3 = new Intent(this, RecycleEmailActivity.class);
			startActivity(intent3);
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.email_tempfile:
			Intent intent4 = new Intent(this, FileShareActivity.class);
			intent4.putExtra("tag", "privateFile");
			startActivity(intent4);
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		}
		return true;
	}

}
