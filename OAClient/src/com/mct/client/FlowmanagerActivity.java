package com.mct.client;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mct.flow.Flow;
import com.mct.flow.FlowNode;
import com.mct.flowutil.FlowDbUtil;
import com.mct.util.Common;
import com.mct.util.HttpRequestUtil;
import com.mct.util.XmppTool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class FlowmanagerActivity extends Activity  {
//	private RadioButton allFlowBtn;
//	private RadioGroup flowCheck;
//	private LinearLayout backBtn;
//	private ListView flowListView;
//	private ArrayList<Flow> allFlowList;
//	private ArrayList<Flow> flowList;
//	private SimpleAdapter adapter;
//	private String mUserId;
//	private Spinner flowTypeSpinner;//�����б�
//	private int selected;//��ѡ�ѡ����
//	private long projectId;//��������ʵ��id
//	private long flowId;//������������id
//	private long nodeId;//�������Ľڵ�id
//	private ProgressDialog dialog;//���ؽ��ȿ�
//	private String flowName;//����������������
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.flowmanageractivity);
//		mUserId = XmppTool.loginUser.getUserId();
//		flowListView = (ListView) findViewById(R.id.flowlistview);
//		allFlowList=new ArrayList<Flow>();
//		flowList=new ArrayList<Flow>();
//		allFlowBtn = (RadioButton) findViewById(R.id.allflow);
//		backBtn=(LinearLayout) findViewById(R.id.backlayout);
//		backBtn.setOnClickListener(this);
//		refresh();
//		flowListView.setOnItemClickListener(this);
//		flowTypeSpinner = (Spinner) findViewById(R.id.flowtypespinner);
//		flowTypeSpinner.setVisibility(View.VISIBLE);
//		String[] item = new String[] { "��������", "�з�����", "�ͻ�����", "��������", "���ڹ���",
//				"�������", "������Դ����", "��Ϣ����" };
//		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_item, item);
//		typeAdapter
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		flowTypeSpinner.setAdapter(typeAdapter);
//		flowTypeSpinner.setOnItemSelectedListener(this);
//		flowCheck = (RadioGroup) findViewById(R.id.flowcheck);
//		flowCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup arg0, int arg1) {
//				// TODO Auto-generated method stub
//
//				switch (arg0.getCheckedRadioButtonId()) {
//				case R.id.allflow:
//					selected = 0;
//					flowList.clear();
//					getData(0);
//					break;
//
//				case R.id.undoneflow:
//					selected = 1;
//					flowList.clear();
//					flowList = FlowDbUtil.shareFlowUtil(FlowmanagerActivity.this)
//							.queryProject(0, 1);
//					break;
//				case R.id.doneflow:
//					selected = 2;
//					flowList.clear();
//					flowList = FlowDbUtil.shareFlowUtil(FlowmanagerActivity.this)
//							.queryProject(0, 2);
//					break;
//				}
//				flowTypeSpinner.setSelection(0);
//				Log.e("selected", selected + "");
//				Log.e("size", list.size() + "");
//				refresh();
//			}
//		});
//		allFlowBtn.setChecked(true);
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		// TODO Auto-generated method stub
//
//		// �����̱�
//		flowId = (Long) list.get(arg2).get("flowid");
//		flowName = FlowDbUtil.shareFlowUtil(this)
//				.getFlowNameById(flowId);
//		nodeId=(Long)list.get(arg2).get("nodeid");
//		Log.e("mUserId", mUserId + "");
//		switch (selected) {
//		case 0:
//			if (FlowDbUtil.shareFlowUtil(this).getUserInfoList(flowId, 1, 1)
//					.contains(mUserId)) {
//				FlowNode flowNode = FlowDbUtil.shareFlowUtil(this).getNode(
//						flowId, 1);
//				Intent intent = new Intent(this, FlowTableLayoutView.class);
//				intent.putExtra("flowid", flowId);
//				intent.putExtra("flowname", flowName);
//				intent.putExtra("nodeid", flowNode.getNodeId());
//				startActivity(intent);
//			} else {
//				Toast.makeText(this, "����Ȩʹ�ô����̣�", Toast.LENGTH_SHORT).show();
//			}
//			break;
//
//		case 1:
//			intoTable(arg2);
//			break;
//		case 2:
//			intoTable(arg2);
//			break;
//		}
//
//	}
//
//
//	
//
//	@Override
//	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
//			long arg3) {
//		// TODO Auto-generated method stub
//		switch (selected) {
//		case 0:
//			list.clear();
//			getData(arg0.getSelectedItemPosition());
//			break;
//
//		case 1:
//			list.clear();
//			list = FlowDbUtil.shareFlowUtil(this).queryProject(
//					arg0.getSelectedItemPosition(), 1);
//			break;
//		case 2:
//			list.clear();
//			list = FlowDbUtil.shareFlowUtil(this).queryProject(
//					arg0.getSelectedItemPosition(), 2);
//			break;
//		}
//		adapter.notifyDataSetChanged();
//
//	}
//
//	@Override
//	public void onNothingSelected(AdapterView<?> arg0) {
//		// TODO Auto-generated method stub
//		switch (selected) {
//		case 0:
//			list.clear();
//			getData(0);
//			break;
//
//		case 1:
//			list.clear();
//			list = FlowDbUtil.shareFlowUtil(this).queryProject(0, 1);
//			break;
//		case 2:
//			list.clear();
//			list = FlowDbUtil.shareFlowUtil(this).queryProject(0, 2);
//			break;
//		}
//		adapter.notifyDataSetChanged();
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	private void refresh() {
//		System.out.println("ˢ��");
//		adapter = null;
//		adapter = new SimpleAdapter(this, list,
//				android.R.layout.simple_list_item_1,
//				new String[] { "flowname" }, new int[] { android.R.id.text1 });
//		flowListView.setAdapter(adapter);
//	}
//	/**
//	 * ����ʵ��id�ӷ���������������
//	 * 
//	 * @param projectId
//	 *            ʵ��id
//	 * @param nodeNo
//	 *            �ڵ���
//	 * @return
//	 */
//	private String getDataFromNet(String projectId) {
//		String httpUrl = "http://feng1233445566.eicp.net:24009/oa/flow/getdata.jsp";
//		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		params.add(new BasicNameValuePair("projectid", projectId));
//		return HttpRequestUtil.getResponsesByPost(httpUrl, params);
//	}
//	private Handler handler=new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 0:
//				dialog.setMessage("���������У����Ժ�");
//				dialog.setCancelable(false);
//				dialog.show();
//				break;
//
//			case 1:
//				dialog.dismiss();
//				Intent intent = new Intent(FlowmanagerActivity.this, FlowTableLayoutView.class);
//				intent.putExtra("flowid", flowId);
//				intent.putExtra("flowname", flowName);
//				intent.putExtra("nodeid",nodeId );
//				intent.putExtra("projectid", projectId);
//				startActivity(intent);
//				break;
//			case 2:
//				dialog.dismiss();
//				AlertDialog.Builder builder=new Builder(FlowmanagerActivity.this);
//				builder.setTitle("��ʾ");
//				builder.setMessage("���ݼ���ʧ�ܣ��Ƿ����ԣ�");
//				break;
//			}
//		}
//		
//	};
//	private void intoTable(int position){
//		
//		projectId=(Long)list.get(position).get("projectid");
//		
//		//���ü��ر����ݵ��߳�
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				handler.sendEmptyMessage(0);//��ʾ���ؿ�
//				String result=getDataFromNet(String.valueOf(projectId));
//				if(result!=null&&result.startsWith("{")){
//					//����ȡ���ݲ�Ϊ�գ�������ת��Ϣ
//					Message message=new  Message();
//					message.what=1;
//					message.obj=result;
//					handler.sendMessage(message);
//				}else{
//					//����ȡ���ݲ�Ϊ�գ����ͼ���ʧ�ܹ㲥
//					handler.sendEmptyMessage(2);
//				}
//			}
//		}).start();
//	}
//	
//	private void getAllFlow(){
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				handler.sendEmptyMessage(0);
//				String json=HttpRequestUtil.getResponsesByGet(Common.HTTPJSPSERVICE+"getallflow.jsp");
//			}
//		}).start();
//	}
//	
//	
}
