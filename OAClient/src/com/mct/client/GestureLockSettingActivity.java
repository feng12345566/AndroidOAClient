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
		backText.setText("����");

		backLayout.setOnClickListener(this);
		// ��ȡ���������
		int[] pass = null;
		try {
			pass = SharePeferenceUtils.getInstance(this).getGestrueLockPass();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (pass==null||pass.equals("")) {
			// ����������
			isEditOldPassWord = false;
			// ֮ǰδ��������
			// ����Ϊ�༭����ģʽ
			gestureView.setMode(GestureLock.MODE_EDIT);
			drawhelptext.setText("�������������ͼ��");
			topText.setText("������������");
			isSetting = true;
		} else {
			topText.setText("�޸���������");
			// �������������
			isEditOldPassWord = true;
			int[] correctGestures = null;
			try {
				correctGestures = SharePeferenceUtils.getInstance(this)
						.getGestrueLockPass();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ����Ϊ��֤����ģʽ
			gestureView.setMode(GestureLock.MODE_NORMAL);
			gestureView.setCorrectGesture(correctGestures);
			drawhelptext.setText("�����ԭ��������ͼ��");
			topText.setText("�޸���������");
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
				// ��Ϊ��������
				if (isSetting) {
					// ����ƥ��
					if (matched) {
						if (isFrist) {
							// ���Ϊ��֤ģʽ
							gestureView.setMode(GestureLock.MODE_NORMAL);
							// �ٴ���������
							isFrist = false;
							int[] psd = gestureView.getGesturesContainer();
							// ����
							gestureView.invalidate();
							// ������֤����Ϊ������������
							gestureView.setCorrectGesture(psd);
							drawhelptext.setText("���ٴλ���������ͼ��");
						} else {
							Toast.makeText(GestureLockSettingActivity.this,
									"���óɹ����´����г���ʹ�ø����������½��",
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
						// ����
						gestureView.invalidate();
						gestureView.setMode(GestureLock.MODE_EDIT);
						drawhelptext.setText("�������������ͼ��");
					}

				} else {
					// ��Ϊ�޸�����
					// ����ƥ��
					if (matched) {
						// ������ƥ��
						if (isEditOldPassWord) {
							// ����༭������
							isEditOldPassWord = false;
							drawhelptext.setText("���������������");
							// ����Ϊ�༭ģʽ����ƥ������
							gestureView.setMode(GestureLock.MODE_EDIT);
							gestureView.invalidate();
						} else {
							// �༭������ƥ��
							Toast.makeText(GestureLockSettingActivity.this,
									"�޸ĳɹ������´�ʹ�������������½��", Toast.LENGTH_SHORT)
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
								"���벻ƥ�䣬�����»��ƣ�", Toast.LENGTH_SHORT).show();
						gestureView.invalidate();
					}
				}
			}

			@Override
			public void onUnmatchedExceedBoundary() {
				// TODO Auto-generated method stub
				// ����ԭ������󳬹�5��
				if (!isSetting && isEditOldPassWord) {
					Toast.makeText(GestureLockSettingActivity.this,
							"��������ﵽ���ޣ������µ�½��", Toast.LENGTH_SHORT).show();
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
