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
	private long flowId;// 流程id
	private String flowName;// 流程名称
	private long nodeId;// 节点id
	private String jsonStr;
	private FlowTableFragment flowTableFragment;
	private ProgressDialog dialog;
	private long projectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 获取传递参数
		Intent intent = getIntent();
		flowId = intent.getLongExtra("flowid", 0);
		flowName = intent.getStringExtra("flowname");
		nodeId = intent.getLongExtra("nodeid", 0);// 当前节点id
		projectId = intent.getLongExtra("projectid", 0);
		jsonStr = intent.getStringExtra("jsonstr");
		flowTableFragment = new FlowTableFragment();
		Bundle mBundle = new Bundle();
		mBundle.putLong("flowid", flowId);
		mBundle.putString("flowname", flowName);
		mBundle.putString("jsonstr", jsonStr);
		mBundle.putLong("nodeid", nodeId);
		setParameter("发起流程", flowTableFragment, mBundle,"流程管理","发送");
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
			System.out.println("点击发送");
			if (flowTableFragment.checkNull()) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(0);
						// 若未获取到实例id则从网络上获取实例id
						if (projectId < 1) {
							projectId = Long.valueOf(flowTableFragment
									.getFlowProjectIdFromNet(flowId,
											System.currentTimeMillis(),
											XmppTool.loginUser.getUserId()));
							if (projectId == -1) {
								// 从网络获取实例id失败
								handler.sendEmptyMessage(1);
							}
						}
						// 再次判断实例id是否存在
						if (projectId > 0) {
							// 若存在则发送表单数据到服务器
							String result2 = sendDataToService(projectId + "",
									nodeId, flowTableFragment.getValue());
							if (Long.valueOf(result2) > 0) {
								// 发送数据成功
								handler.sendEmptyMessage(2);
							} else {
								// 发送数据失败
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
	 * 功能描述： Administrator 2014年7月6日 下午11:06:19
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
				dialog.setMessage("加载中...");
				dialog.show();
				break;

			case 1:
				dialog.dismiss();
				Toast.makeText(FlowTableLayoutView.this, "加载失败，请检查网络是否连接！",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// 发送数据成功则进行跳转
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
