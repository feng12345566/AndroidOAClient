package com.mct.client;


import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.loveplusplus.update.UpdateChecker;
import com.mct.client.R;
import com.mct.controls.MyPagerAdapter;
import com.mct.fragment.FriendFragment;
import com.mct.fragment.MsgFragment;
import com.mct.fragment.MyCenterFragment;
import com.mct.fragment.SettingFragment;
import com.mct.service.FileLoadService;
import com.mct.service.MsgService;
import com.mct.transform.CubeOutTransformer;
import com.mct.util.SharePeferenceUtils;
import com.mct.util.XmppTool;


public class UserActivity extends FragmentActivity implements OnCheckedChangeListener,OnClickListener{
	private int tag=0;
	private TextView mainTopTitle;
	private ViewPager mPager;
	private RadioButton tab1, tab2, tab3, tab4;
	private MyPagerAdapter adapter;
	private final static String[] titles={"消息","通讯录","个人中心","设置"};
	private MyOnPageChangeListener mListener = null;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintabs);
        Intent intent=getIntent();
       
        mainTopTitle=(TextView) findViewById(R.id.maintoptitle);
        SharedPreferences settings = getSharedPreferences("setting_info", 0);
        String userid=settings.getString("userid", "");
        Log.e("userid",userid+"");
        mPager = (ViewPager) findViewById(R.id.m_pager);
        tab1 = (RadioButton) findViewById(R.id.radio_button0);
		tab2 = (RadioButton) findViewById(R.id.radio_button1);
		tab3 = (RadioButton) findViewById(R.id.radio_button2);
		tab4 = (RadioButton) findViewById(R.id.radio_button3);
		init();
		if(intent.hasExtra("tag")){
			tag=intent.getIntExtra("tag", 0);
			mPager.setCurrentItem(tag);
			checked(tag);
        }
		mListener=new MyOnPageChangeListener();
		mPager.setOnPageChangeListener(mListener);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab3.setOnClickListener(this);
		tab4.setOnClickListener(this);
		Intent intent3=new Intent(UserActivity.this,FileLoadService.class);
		startService(intent3);
		 if(SharePeferenceUtils.getInstance(this).getSwitch(SharePeferenceUtils.AUTOUPDATECHECK)){
			 UpdateChecker.setUpdateServerUrl("http://nat.nat123.net:14313/oa/UpdateChecker.jsp");
			 UpdateChecker.checkForNotification(this);
		 }
    }

	private void checked(int position) {
		tag=position;
		mainTopTitle.setText(titles[position]);
		switch (position) {
		case 0:
			tab1.setChecked(true);
			break;
		case 1:
			tab2.setChecked(true);
			break;
		case 2:
			tab3.setChecked(true);
			break;
		case 3:
			tab4.setChecked(true);
			break;
		}

		}
	private void init() {
		Fragment[] fragments = new Fragment[4];
		if(fragments[0]==null){
		fragments[0] = new MsgFragment();
		}
		if(fragments[1]==null){
		fragments[1] = new FriendFragment();
		}
		if(fragments[2]==null){
		fragments[2] = new MyCenterFragment();
		}
		if(fragments[3]==null){
		fragments[3] = new SettingFragment();
		}
		adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
		mPager.setAdapter(adapter);
		if(VERSION.SDK_INT>=11){
		mPager.setPageTransformer(true, new CubeOutTransformer());
		}
	}

	/**
	 * 页面滑动监听
	 */
	
    class MyOnPageChangeListener implements OnPageChangeListener{
    	private EdgeEffectCompat leftEdge;

		private EdgeEffectCompat rightEdge;
    	public MyOnPageChangeListener(){
    		try {

    		    Field leftEdgeField = mPager.getClass().getDeclaredField("mLeftEdge");
    		    Field rightEdgeField = mPager.getClass().getDeclaredField("mRightEdge");
    		    Log.i("xinye", "=======leftEdgeField:" + leftEdgeField + ",rightEdgeField:" + rightEdgeField);
    		    if(leftEdgeField != null && rightEdgeField != null){
                     leftEdgeField.setAccessible(true);

    		        rightEdgeField.setAccessible(true);

    		        

    		        leftEdge = (EdgeEffectCompat) leftEdgeField.get(mPager);

    		        rightEdge = (EdgeEffectCompat) rightEdgeField.get(mPager);

    		        Log.i("xinye", "=======OK啦，leftEdge:" + leftEdge + ",rightEdge:" + rightEdge);

    		    }

    		    

    		} catch (Exception e) {

    		    e.printStackTrace();

    		}
    	}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			if(leftEdge != null && rightEdge != null){

	            leftEdge.finish();

	            rightEdge.finish();

	            leftEdge.setSize(0, 0);

	            rightEdge.setSize(0, 0);

	        }
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			checked(arg0);
			mPager.setCurrentItem(arg0);
		}
    	
    }
	

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.radio_button0:
			mPager.setCurrentItem(0);
			break;
		case R.id.radio_button1:
			mPager.setCurrentItem(1);
			break;
		case R.id.radio_button2:
			mPager.setCurrentItem(2);
			break;
		case R.id.radio_button3:
			mPager.setCurrentItem(3);
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
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
	protected void dialog() { 
        AlertDialog.Builder builder = new Builder(this); 
        builder.setMessage("确定要退出吗?"); 
        builder.setTitle("提示"); 
        builder.setPositiveButton("确认", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                       
                        UserActivity.this.finish();
                        XmppTool.shareConnectionManager(UserActivity.this).logout();
                    }

					
                }); 
        builder.setNegativeButton("取消", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                    } 
                }); 
        builder.create().show(); 
    } 
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
            dialog(); 
            return false; 
        } 
        return false; 
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Intent Intent2=new Intent(UserActivity.this,MsgService.class);
		stopService(Intent2);
		
		Intent intent4=new Intent(UserActivity.this,FileLoadService.class);
		stopService(intent4);
	}

}
