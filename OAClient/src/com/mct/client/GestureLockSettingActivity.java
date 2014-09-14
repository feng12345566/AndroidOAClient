package com.mct.client;

import com.mct.util.MyAnimationUtils;
import com.mct.util.SharePeferenceUtils;
import com.mct.view.GestureLock;
import com.mct.view.GestureLock.OnGestureEventListener;
import com.mct.view.GestureLockView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GestureLockSettingActivity extends Activity implements
		OnClickListener {
	private GestureLock gestureView;
	private LinearLayout backLayout;
	private TextView backText, topText;
	private boolean isSetting;
	private TextView drawhelptext;
	private boolean isEditOldPassWord;
	private boolean isFrist = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesturelocksettingactivity);
		gestureView = (GestureLock) findViewById(R.id.gesture_lock);
		drawhelptext = (TextView) findViewById(R.id.drawhelptext);
		backLayout = (LinearLayout) findViewById(R.id.backlayout);
		topText = (TextView) findViewById(R.id.toptext);
		backText = (TextView) findViewById(R.id.backText);
		backText.setText("返回");

		backLayout.setOnClickListener(this);
		// 获取保存的密码
		int[] pass = null;
		try {
			pass = SharePeferenceUtils.getInstance(this).getGestrueLockPass();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pass==null||pass.equals("")) {
			// 设置新密码
			isEditOldPassWord = false;
			// 之前未设置密码
			// 设置为编辑密码模式
			gestureView.setMode(GestureLock.MODE_EDIT);
			drawhelptext.setText("请绘制手势密码图案");
			topText.setText("设置手势密码");
			isSetting = true;
		} else {
			topText.setText("修改手势密码");
			// 正在输入旧密码
			isEditOldPassWord = true;
			int[] correctGestures = null;
			try {
				correctGestures = SharePeferenceUtils.getInstance(this)
						.getGestrueLockPass();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 设置为验证密码模式
			gestureView.setMode(GestureLock.MODE_NORMAL);
			gestureView.setCorrectGesture(correctGestures);
			drawhelptext.setText("请绘制原手势密码图案");
			topText.setText("修改手势密码");
			isSetting = false;
		}
		gestureView.setOnGestureEventListener(new OnGestureEventListener() {

			@Override
			public void onBlockSelected(int position) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGestureEvent(boolean matched) {
				// TODO Auto-generated method stub
				// 若为设置密码
				if (isSetting) {
					// 密码匹配
					if (matched) {
						if (isFrist) {
							// 变更为验证模式
							gestureView.setMode(GestureLock.MODE_NORMAL);
							// 再次输入密码
							isFrist = false;
							int[] psd = gestureView.getGesturesContainer();
							// 重置
							gestureView.invalidate();
							// 设置验证密码为本次输入密码
							gestureView.setCorrectGesture(psd);
							drawhelptext.setText("请再次绘制新密码图案");
						} else {
							Toast.makeText(GestureLockSettingActivity.this,
									"设置成功，下次运行程序将使用该手势密码登陆！",
									Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(
									GestureLockSettingActivity.this,
									SafetySettingActivity.class);
							setResult(1, intent);
							GestureLockSettingActivity.this.finish();
							MyAnimationUtils
									.outActivity(GestureLockSettingActivity.this);
						}
					} else {
						isFrist = true;
						// 重置
						gestureView.invalidate();
						gestureView.setMode(GestureLock.MODE_EDIT);
						drawhelptext.setText("请绘制手势密码图案");
					}

				} else {
					// 若为修改密码
					// 密码匹配
					if (matched) {
						// 旧密码匹配
						if (isEditOldPassWord) {
							// 进入编辑新密码
							isEditOldPassWord = false;
							drawhelptext.setText("请绘制新手势密码");
							// 设置为编辑模式，不匹配密码
							gestureView.setMode(GestureLock.MODE_EDIT);
							gestureView.invalidate();
						} else {
							// 编辑新密码匹配
							Toast.makeText(GestureLockSettingActivity.this,
									"修改成功，请下次使用新手势密码登陆！", Toast.LENGTH_SHORT)
									.show();
							Intent intent = new Intent(
									GestureLockSettingActivity.this,
									SafetySettingActivity.class);
							setResult(1, intent);
							GestureLockSettingActivity.this.finish();
							MyAnimationUtils
									.outActivity(GestureLockSettingActivity.this);
						}
					} else {
						Toast.makeText(GestureLockSettingActivity.this,
								"密码不匹配，请重新绘制！", Toast.LENGTH_SHORT).show();
						gestureView.invalidate();
					}
				}
			}

			@Override
			public void onUnmatchedExceedBoundary() {
				// TODO Auto-generated method stub
				// 输入原密码错误超过5次
				if (!isSetting && isEditOldPassWord) {
					Toast.makeText(GestureLockSettingActivity.this,
							"错误次数达到上限，请重新登陆！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(GestureLockSettingActivity.this,
							LoginActivity.class);
					startActivity(intent);
					GestureLockSettingActivity.this.finish();
					MyAnimationUtils
							.outActivity(GestureLockSettingActivity.this);
				}

			}

		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backlayout:
			this.finish();
			MyAnimationUtils.outActivity(this);
			break;

		default:
			break;
		}
	}

}
