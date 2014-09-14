package com.mct.client;

import com.mct.fragment.SafetySettingFragment;
import com.mct.util.MyAnimationUtils;
import com.mct.util.SharePeferenceUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

public class SafetySettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Fragment safetySettingFragment = new SafetySettingFragment();
		setParameter("安全", safetySettingFragment, null, "设置", null);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		back();
	}

	private void back() {
		boolean gestrueLockEnable = SharePeferenceUtils.getInstance(this)
				.getGestrueLockEnable();
		boolean isPassWdSetted = false;
		if (gestrueLockEnable) {
			int[] pass=null;
			try {
				pass = SharePeferenceUtils.getInstance(this)
						.getGestrueLockPass();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (pass != null ) {
				isPassWdSetted = true;
			}
		}
		if (gestrueLockEnable == true && isPassWdSetted == false) {
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("提示");
			builder.setMessage("您已打开手势登陆，但未设置手势密码,请选择相应操作。");
			builder.setPositiveButton("退出本页面", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					goBack();
				}
			});
			builder.setNegativeButton("设置手势密码", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(SafetySettingActivity.this,GestureLockSettingActivity.class);
					startActivity(intent);
					MyAnimationUtils.inActivity(SafetySettingActivity.this);
				}
			});
			builder.create().show();
		} else {
			goBack();
		}
	}
	
	private void goBack(){
		Intent intent = new Intent(this, UserActivity.class);
		intent.putExtra("tag", 3);
		startActivity(intent);
		finish();
		MyAnimationUtils.outActivity(this);
	}

}
