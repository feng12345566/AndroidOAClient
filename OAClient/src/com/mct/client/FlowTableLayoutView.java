package com.mct.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mct.fragment.FlowTableFragment;
import com.mct.util.Common;
import com.mct.util.HttpRequestUtil;
import com.mct.util.XmppTool;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class FlowTableLayoutView extends BaseActivity implements
		OnClickListener {
	private long flowId;// ����id
	private String flowName;// ��������
	private long nodeId;// �ڵ�id
	private String jsonStr;
	private FlowTableFragment flowTableFragment;
	private ProgressDialog dialog;
	private long projectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��ȡ���ݲ���
		Intent intent = getIntent();
		flowId = intent.getLongExtra("flowid", 0);
		flowName = intent.getStringExtra("flowname");
		nodeId = intent.getLongExtra("nodeid", 0);// ��ǰ�ڵ�id
		projectId = intent.getLongExtra("projectid", 0);
		jsonStr = intent.getStringExtra("jsonstr");
		flowTableFragment = new FlowTableFragment();
		Bundle mBundle = new Bundle();
		mBundle.putLong("flowid", flowId);
		mBundle.putString("flowname", flowName);
		mBundle.putString("jsonstr", jsonStr);
		mBundle.putLong("nodeid", nodeId);
		setParameter("��������", flowTableFragment, mBundle,"���̹���","����");
		nextPage.setOnClickListener(this);
	}

	

	@Override
	public void setParameter(String title, Fragment contentFragment,
			Bundle bundle, String leftTitle, String rightTitle) {
		// TODO Auto-generated method stub
		super.setParameter(title, contentFragment, bundle, leftTitle, rightTitle);
	}



	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.nextpage:
			System.out.println("�������");
			if (flowTableFragment.checkNull()) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(0);
						// ��δ��ȡ��ʵ��id��������ϻ�ȡʵ��id
						if (projectId < 1) {
							projectId = Long.valueOf(flowTableFragment
									.getFlowProjectIdFromNet(flowId,
											System.currentTimeMillis(),
											XmppTool.loginUser.getUserId()));
							if (projectId == -1) {
								// �������ȡʵ��idʧ��
								handler.sendEmptyMessage(1);
							}
						}
						// �ٴ��ж�ʵ��id�Ƿ����
						if (projectId > 0) {
							// ���������ͱ����ݵ�������
							String result2 = sendDataToService(projectId + "",
									nodeId, flowTableFragment.getValue());
							if (Long.valueOf(result2) > 0) {
								// �������ݳɹ�
								handler.sendEmptyMessage(2);
							} else {
								// ��������ʧ��
								handler.sendEmptyMessage(1);
							}
						}
					}
				}).start();

			}

			break;

		default:
			break;
		}
	}

	/**
	 * ���������� Administrator 2014��7��6�� ����11:06:19
	 * 
	 * @param projectId
	 * @param nodeNo
	 * @param data
	 * @return
	 */
	private String sendDataToService(String projectId, long nodeId, String data) {
		String httpUrl = Common.HTTPJSPSERVICE+"senddata.jsp";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("projectId", projectId));
		params.add(new BasicNameValuePair("nodeNo", String.valueOf(nodeId)));
		params.add(new BasicNameValuePair("data", data));
		return HttpRequestUtil.getResponsesByPost(httpUrl, params);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				dialog = new ProgressDialog(FlowTableLayoutView.this);
				dialog.setMessage("������...");
				dialog.show();
				break;

			case 1:
				dialog.dismiss();
				Toast.makeText(FlowTableLayoutView.this, "����ʧ�ܣ����������Ƿ����ӣ�",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// �������ݳɹ��������ת
				dialog.dismiss();
				Intent sendIntent = new Intent(FlowTableLayoutView.this,
						SendFlowActivty.class);
				sendIntent.putExtra("flowid", flowId);
				sendIntent.putExtra("nodeid", nodeId);
				sendIntent.putExtra("projectid", (String) msg.obj);
				sendIntent.putExtra("data", flowTableFragment.getValue());
				startActivity(sendIntent);
				FlowTableLayoutView.this.finish();
				break;
			}

		}

	};

}
