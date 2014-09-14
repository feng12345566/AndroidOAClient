package com.mct.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mct.controls.FlowsendAdapter;
import com.mct.flow.FlowNode;
import com.mct.flowutil.FlowDbUtil;
import com.mct.model.User;
import com.mct.util.HttpRequestUtil;
import com.mct.util.XmppTool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author fengyouchun
 * @version 创建时间：2014年7月6日 下午12:49:11
 */
public class SendFlowActivty extends Activity implements OnClickListener {
	private ListView selectFriendListView;
	private FlowsendAdapter adapter;
	private ArrayList<FlowNode> list;
	private long flowId;
	private long nodeId;
	private String sendData;
	private String projectId;
	private TextView sendFlowBtn;
	private FlowNode flowNode;// 当前节点对象
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flowsendactivity);
		Intent intent = getIntent();
		flowId = intent.getLongExtra("flowid", 0);// 当前处理的流程id
		nodeId = intent.getLongExtra("nodeid", 0);// 当前节点id
		sendData = intent.getStringExtra("data");// 获取表单数据
		projectId = intent.getStringExtra("projectid");
		selectFriendListView = (ListView) findViewById(R.id.selectfriend);
		sendFlowBtn = (TextView) findViewById(R.id.sendflow);
		adapter = new FlowsendAdapter(this, handler);
		selectFriendListView.setAdapter(adapter);
		flowNode = FlowDbUtil.shareFlowUtil(this).getNode(nodeId);
		list = FlowDbUtil.shareFlowUtil(this).getNextNode(flowId,
				flowNode.getNodeNo());
		adapter.setNextNodeList(list);
		adapter.notifyDataSetChanged();
		sendFlowBtn.setOnClickListener(this);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				FlowNode flowNode2 = list.get((Integer) msg.obj);
				System.out.println("列表" + (Integer) msg.obj + "被点击");
				Intent intent = new Intent(SendFlowActivty.this,
						SortListViewActivity.class);
				intent.putExtra("flowid", flowNode2.getFlowId());
				intent.putExtra("nodeno", flowNode2.getNodeNo());
				startActivityForResult(intent, 1);
				break;
			case 2:
				progressDialog = new ProgressDialog(SendFlowActivty.this);
				progressDialog.setMessage("发送中...");
				progressDialog.show();
				break;
			case 3:
				progressDialog.dismiss();
				Toast.makeText(SendFlowActivty.this, "发送成功！",
						Toast.LENGTH_SHORT).show();
				Intent intent2 = new Intent("com.mct.action.finishTable");
				sendBroadcast(intent2);
				SendFlowActivty.this.finish();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						ArrayList<ArrayList<User>> selectUserList = adapter
								.getSelectedUserList();
						for (int i = 0; i < selectUserList.size(); i++) {
							ArrayList<User> mUsers = selectUserList.get(i);
							for (int j = 0; j < mUsers.size(); j++) {
								XmppTool.shareConnectionManager(
										SendFlowActivty.this).sendMessage(
										mUsers.get(j).getUserId(), "[flow]"+projectId);
							}

						}
					}
				}).start();

				break;
			case 4:
				progressDialog.dismiss();
				Toast.makeText(SendFlowActivty.this, "发送失败，请重试！",
						Toast.LENGTH_SHORT).show();
				break;
			}

		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			// data.getS
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.sendflow:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					handler.sendEmptyMessage(2);//
					int userNum = 0;// 发送人总数
					int count = 0;// 发送成功数量
					for (int i = 0; i < list.size(); i++) {
						StringBuffer users = new StringBuffer();
						ArrayList<User> mUsers = adapter.getSelectedUserList()
								.get(i);

						for (int m = 0; m < mUsers.size(); i++) {
							userNum++;
							String result2 = sendUserToService(projectId, list
									.get(i).getNodeNo(), mUsers.get(m)
									.getUserId());
							if (Long.valueOf(result2) > 0) {
								count++;
							}
						}
					}
					if (count == userNum) {
						handler.sendEmptyMessage(3);// 数据发送成功
					} else {
						handler.sendEmptyMessage(4);// 数据发送失败
					}
				}
			}).start();

			break;

		default:
			break;
		}
	}

	/**
	 * 功能描述：发送后续步骤处理人到服务器 Administrator 2014年7月6日 下午12:53:28
	 * 
	 * @param projectId
	 */
	private String sendUserToService(String projectId, int nodeNo, String users) {
		String httpUrl = "http://feng1233445566.eicp.net:24009/oa/flow/senduser.jsp";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("projectId", projectId));
		params.add(new BasicNameValuePair("nodeNo", String.valueOf(nodeNo)));
		params.add(new BasicNameValuePair("users", users));
		return HttpRequestUtil.getResponsesByPost(httpUrl, params);

	}

}
