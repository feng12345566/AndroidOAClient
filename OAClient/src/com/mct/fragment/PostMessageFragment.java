package com.mct.fragment;

import it.sauronsoftware.ftp4j.FTPClient;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mct.client.R;
import com.mct.client.SortListViewActivity;
import com.mct.controls.AccessoryListAdapter;
import com.mct.localefilebrowser.LocaleFileMainActivity;
import com.mct.model.TransferredFile;
import com.mct.util.Common;
import com.mct.util.FTPUtils;
import com.mct.util.FileDbUtil;
import com.mct.util.FileUtil;
import com.mct.util.HttpRequestUtil;
import com.mct.util.UserDataDbUtil;
import com.mct.util.UserDbUtil;
import com.mct.util.XmppTool;
import com.mct.view.CustomClipLoading;
import com.mct.view.HorizontalListView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PostMessageFragment extends Fragment implements OnClickListener {
	private EditText notificationTitle;
	private EditText notificationContent;
	private HorizontalListView accessoryGallery;
	private Button addAccessory;
	private AccessoryListAdapter adapter;
	private ArrayList<TransferredFile> accessoryList;
	private ArrayList<String> pathList;
	private MyReceiver myBroadcastReceiver;
	private ProgressBar accessoryLoading;
	private TextView accessoryNum;
	private String title, content;
	private String accessory = "";
	private int count = 0;// 上传成功附件数
	private static final int EMAIL = 1;
	private static final int NOTIFICATION = 2;
	private Button edit_message_selectallbtn, edit_message_selectbtn;
	private EditText edit_message_receiver; // 接收人编辑框
	private int opt;// 消息类型
	private ArrayList<String> receiverList;// 接收人列表
	private String receiverStr;// 接收人文本
	private CustomClipLoading message_send_progress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle bundle = getArguments();
		String userid = bundle.getString("receiver");
		String receiver = "";
		opt = bundle.getInt("opt");
		if (userid != null) {
			receiver = UserDbUtil.shareUserDb(getActivity()).getUserNameById(
					userid);
		}
		
		View view = inflater.inflate(R.layout.activity_notifacation, null);
		notificationTitle = (EditText) view
				.findViewById(R.id.notificationtitle);
		notificationContent = (EditText) view
				.findViewById(R.id.notificationcontent);
		accessoryGallery = (HorizontalListView) view
				.findViewById(R.id.accessorygallery);
		edit_message_selectallbtn = (Button) view
				.findViewById(R.id.edit_message_selectallbtn);
		edit_message_selectbtn = (Button) view
				.findViewById(R.id.edit_message_selectbtn);
		edit_message_selectallbtn.setOnClickListener(this);
		edit_message_selectbtn.setOnClickListener(this);
		accessoryNum = (TextView) view.findViewById(R.id.accessorynum);
		accessoryLoading = (ProgressBar) view
				.findViewById(R.id.accessoryloading);
		addAccessory = (Button) view.findViewById(R.id.addaccessorybtn);
		edit_message_receiver = (EditText) view
				.findViewById(R.id.edit_message_receiver);
		message_send_progress=(CustomClipLoading) view.findViewById(R.id.message_send_progress);
		edit_message_receiver.setText(receiver);
		addAccessory.setOnClickListener(this);
		accessoryList = new ArrayList<TransferredFile>();
		if(bundle.containsKey("accessory")){
			String[] accessorys=bundle.getString("accessory").split(",");
			for(String a:accessorys){
				TransferredFile t=new TransferredFile(getActivity(), FileUtil.getFileNameByUrl(a), 100, 100, null, a, 2);
				accessoryList.add(t);
			}
		}
		adapter = new AccessoryListAdapter(getActivity());
		
		adapter.setList(accessoryList);
		accessoryGallery.setAdapter(adapter);
		if(bundle.containsKey("content")){
			notificationContent.setText(bundle.getString("content"));
		}
		if(bundle.containsKey("title")){
			notificationTitle.setText(bundle.getString("title"));
		}
		
		// 注册广播接收
		myBroadcastReceiver = new MyReceiver();

		// 实例化接收人列表
		receiverList = new ArrayList<String>();
		return view;
	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.addaccessorybtn:
			Intent intent = new Intent(getActivity(),
					LocaleFileMainActivity.class);
			intent.putExtra("opt", "addaccessory");
			startActivityForResult(intent, 1);
			getActivity().overridePendingTransition(R.anim.in_from_left,
					R.anim.out_to_right);
			break;
		case R.id.edit_message_selectbtn:
			Intent intent2 = new Intent(getActivity(),
					SortListViewActivity.class);
			// 传递已选人员
			intent2.putStringArrayListExtra("selected", receiverList);
			intent2.putExtra("opt", opt);
			startActivityForResult(intent2, 2);
			getActivity().overridePendingTransition(R.anim.in_from_left,
					R.anim.out_to_right);
			break;
		case R.id.edit_message_selectallbtn:
			edit_message_receiver.setText("所有人");
			receiverList.clear();
			break;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Common.ACTION_REFRESH);
		filter.addAction(Common.ACTION_COMPLETED);
		Log.e("opt", "注册广播接收");
		getActivity().registerReceiver(myBroadcastReceiver, filter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			pathList = data.getStringArrayListExtra("result");
			if (pathList != null && pathList.size() != 0) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						handler.sendEmptyMessage(0);// 显示进度框

						try {
							FTPClient client = FTPUtils.getInstance()
									.getClient();
							String ftpPath = "/userfile/"
									+ XmppTool.loginUser.getUserId() + "/";
							boolean isExist = FTPUtils.getInstance().exists(
									client, ftpPath);
							if (!isExist) {
								FTPUtils.getInstance().mkdirs(client, ftpPath);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for (int i = 0; i < pathList.size(); i++) {
							File file = new File(pathList.get(i));
							TransferredFile transferredFile = new TransferredFile(
									getActivity(), file.getName(), file
											.length(), 0, pathList.get(i),
									"/userfile/"
											+ XmppTool.loginUser.getUserId()
											+ "/", 2);
							long id = FileDbUtil.getInstance(getActivity())
									.insertFileLoadHistory(transferredFile);
							accessoryList.add(transferredFile);
							Intent intent = new Intent(Common.ACTION_ADDLOAD);
							intent.putExtra("id", id);
							getActivity().sendBroadcast(intent);
						}

						Message message = new Message();
						message.what = 1;
						message.obj = pathList.size();
						handler.sendMessage(message);// 隐藏进度框
					}
				}).start();

			}
			break;
		case 3:
			ArrayList<String> selectedmember = data
					.getStringArrayListExtra("selectedmember");
			if (selectedmember != null) {
				receiverList = selectedmember;
			}
			Log.e("receiverList", receiverList.toString());
			StringBuffer sb = new StringBuffer();
			for (String userid : receiverList) {
				sb.append(UserDbUtil.shareUserDb(getActivity())
						.getUserNameById(userid));
				sb.append("、");
			}
			sb.delete(sb.length() - 1, sb.length());
			edit_message_receiver.setText(sb.toString());
			break;
		}
	}

	public void sendMessage() {
		if (checkNull()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					handler.sendEmptyMessage(4);

					long time = System.currentTimeMillis();
					String httpUrl = "http://nat.nat123.net:14313/oa/message/postmessage.jsp";
					ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("content", content));
					params.add(new BasicNameValuePair("accessory", accessory));
					params.add(new BasicNameValuePair("sender",
							XmppTool.loginUser.getUserId()));
					params.add(new BasicNameValuePair("type", opt + ""));
					StringBuffer receiverId = new StringBuffer();
					if (receiverStr.equals("所有人")) {
						receiverId.append("all");
					} else {
						for (String userid : receiverList) {
							receiverId.append(userid);
							receiverId.append(",");
						}
						receiverId.delete(receiverId.length() - 1,
								receiverId.length());
					}
					params.add(new BasicNameValuePair("receiver", receiverId
							.toString()));
					Log.e("receiverId",receiverId.toString());
					params.add(new BasicNameValuePair("title", title));
					params.add(new BasicNameValuePair("time", time + ""));
					String id = HttpRequestUtil.getResponsesByPost(httpUrl,
							params);
					if (id != null && id.trim().equals("success")) {
						switch (opt) {
						case 1:
							UserDataDbUtil.shareUserDataDb(getActivity()).insertEmail(XmppTool.loginUser.getUserId(), 
									title, content, accessory, time, receiverId.toString(), 1);
							break;

						case 2:
							XmppTool.shareConnectionManager(getActivity())
									.sendBroadcast(
											"[%news%]" + title + "@" + time
													+ "@" + id);
							break;
						}
						handler.sendEmptyMessage(5);
					}else{
						handler.sendEmptyMessage(6);
					}
					
				}
			}).start();

		}

	}

	private boolean checkNull() {
		title = notificationTitle.getText().toString();
		content = notificationContent.getText().toString();
		receiverStr = edit_message_receiver.getText().toString();
		if (receiverStr.equals("")) {
			Toast.makeText(getActivity(), "接收人不能为空！", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (title.equals("")) {
			Toast.makeText(getActivity(), "主题不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (content.equals("")) {
			Toast.makeText(getActivity(), "正文不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;

	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			long id = arg1.getLongExtra("id", 0);
			for (int i = 0; i < accessoryList.size(); i++) {
				TransferredFile mTransferredFile = accessoryList.get(i);
				if (mTransferredFile.getId() == id) {
					if (arg1.getAction() == Common.ACTION_REFRESH) {
						System.out.println("接收到刷新广播" + id);

						mTransferredFile.setLength(arg1.getLongExtra("length",
								0));
						accessoryList.set(i, mTransferredFile);
						break;

					} else if (arg1.getAction() == Common.ACTION_COMPLETED) {
						System.out.println("接收到传输完成广播");
						count++;
						mTransferredFile.setLength(mTransferredFile
								.getFileSize());
						accessoryList.set(i, mTransferredFile);
						if (count == 1) {
							accessory += mTransferredFile.getFileName();
						} else {
							accessory += ("," + mTransferredFile.getFileName());
						}
						break;

					}

				}

			}
			handler.sendEmptyMessage(2);// 刷新进度条
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				accessoryLoading.setVisibility(View.VISIBLE);
				break;
			case 1:
				accessoryLoading.setVisibility(View.GONE);
				accessoryNum.setText("附件(" + (Integer) msg.obj + ")");
				break;
			case 2:
				adapter.notifyDataSetChanged();
				break;
			case 4:
				message_send_progress.setVisibility(View.VISIBLE);
				break;
			case 5:
				message_send_progress.setVisibility(View.GONE);
				Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT)
						.show();
				getActivity().finish();
				getActivity().overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
				break;
			case 6:
				message_send_progress.setVisibility(View.GONE);
				Toast.makeText(getActivity(), "发送失败，请重试！", Toast.LENGTH_SHORT)
				.show();
				break;
			}

		}

	};

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(myBroadcastReceiver);
	}

}
