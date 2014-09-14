package com.mct.fragment;

import java.util.LinkedList;

import com.mct.client.R;
import com.mct.controls.DoneFileAdapter;
import com.mct.controls.FileTransferAdapter;
import com.mct.fragment.DownLoadFragment.MyReceiver;
import com.mct.model.TransferredFile;
import com.mct.util.Common;
import com.mct.util.FileDbUtil;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class UpLoadFragment extends Fragment implements OnClickListener{
	private LinearLayout loadingLayout;
	private ListView loadingListView;
	private TextView loadingNum;
	private LinearLayout loadedLayout;
	private LinearLayout loadingRecLayout;
	private LinearLayout loadedRecLayout;
	private ListView loadedListView;
	private TextView loadedNum;
	private TextView noloadinghistory;
	private TextView noloadedhistory;
	private MyReceiver myBroadcastReceiver;

	private LinkedList<TransferredFile> fileList;// 未下载完成文件列表
	private LinkedList<TransferredFile> doneFileList;// 下载完成文件列表
	private FileTransferAdapter loadingAdapter;// 文件下载适配器
	private DoneFileAdapter loadedAdapter;// 完成文件适配器

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.loadfragment, null);
		loadingLayout = (LinearLayout) view.findViewById(R.id.loadinglayout);
		loadedLayout = (LinearLayout) view.findViewById(R.id.loadedlayout);
		loadingListView = (ListView) view.findViewById(R.id.loadinglistview);
		loadedListView = (ListView) view.findViewById(R.id.loadedlistview);
		loadingNum = (TextView) view.findViewById(R.id.loadingnum);
		loadedNum = (TextView) view.findViewById(R.id.loadednum);
		loadingRecLayout = (LinearLayout) view
				.findViewById(R.id.loadingreclayout);
		loadedRecLayout = (LinearLayout) view
				.findViewById(R.id.loadedreclayout);
		noloadinghistory = (TextView) view.findViewById(R.id.noloadinghistory);
		noloadedhistory = (TextView) view.findViewById(R.id.noloadedhistory);
		loadingLayout.setOnClickListener(this);
		loadedLayout.setOnClickListener(this);
		// 实例化文件传输适配器
		loadingAdapter = new FileTransferAdapter(getActivity());
		loadedAdapter=new DoneFileAdapter(getActivity()); 
		// 获取下载的文件列表
		fileList = FileDbUtil.getInstance(getActivity()).queryFileLoadHistory(
				2, 0,"time asc");
		doneFileList=FileDbUtil.getInstance(getActivity()).queryFileLoadHistory(
				2, 1,"time desc");
		// 设置下载导航条文本
		loadingNum.setText("正在上传(" + fileList.size() + ")");
		loadedNum.setText("已上传(" + doneFileList.size() + ")");
		loadingAdapter.setTransferredFileList(fileList);
		loadedAdapter.setDoneFileList(doneFileList);
		loadingListView.setAdapter(loadingAdapter);
		loadedListView.setAdapter(loadedAdapter);
		// 注册广播接收
		myBroadcastReceiver=new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Common.ACTION_REFRESH);
		filter.addAction(Common.ACTION_REMOVELOAD);
		filter.addAction(Common.ACTION_COMPLETED);
		filter.addAction(Common.ACTION_COMPLETED);
		Log.e("opt", "注册广播接收");
		getActivity().registerReceiver(myBroadcastReceiver, filter);
		return view;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		getActivity().unregisterReceiver(myBroadcastReceiver);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.loadinglayout:
			if (loadingRecLayout.getVisibility() == View.VISIBLE) {
				loadingRecLayout.setVisibility(View.GONE);
			} else if (loadingRecLayout.getVisibility() == View.GONE) {
				loadingRecLayout.setVisibility(View.VISIBLE);
				if (fileList.size() == 0) {
					noloadinghistory.setVisibility(View.VISIBLE);
					loadingListView.setVisibility(View.GONE);
				} else {
					noloadinghistory.setVisibility(View.GONE);
					loadingListView.setVisibility(View.VISIBLE);
				}
			}
			break;

		case R.id.loadedlayout:
			if (loadedRecLayout.getVisibility() == View.VISIBLE) {
				loadedRecLayout.setVisibility(View.GONE);
			} else if (loadedRecLayout.getVisibility() == View.GONE) {
				loadedRecLayout.setVisibility(View.VISIBLE);
				if (doneFileList.size() == 0) {
					noloadedhistory.setVisibility(View.VISIBLE);
					loadedListView.setVisibility(View.GONE);
				} else {
					noloadedhistory.setVisibility(View.GONE);
					loadedListView.setVisibility(View.VISIBLE);
				}
			}
			break;
		}
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			long id = arg1.getLongExtra("id", 0);
			for (int i = 0; i < fileList.size(); i++) {
				TransferredFile mTransferredFile = fileList.get(i);
				if (mTransferredFile.getId() == id) {
					if (arg1.getAction() == Common.ACTION_REFRESH) {
						System.out.println("接收到刷新广播" + id);

						mTransferredFile.setLength(arg1.getLongExtra("length",
								0));
						mTransferredFile.setStart(arg1.getBooleanExtra(
								"isstart", false));
						mTransferredFile.setStarting(arg1.getBooleanExtra(
								"isstarting", false));
						mTransferredFile.setSpeed(arg1.getDoubleExtra("speed",
								0));
						fileList.set(i, mTransferredFile);
						break;

					} else if (arg1.getAction() == Common.ACTION_REMOVELOAD) {
						System.out.println("接收到传输中断广播");
						handler.sendEmptyMessage(0);
					} else if (arg1.getAction() == Common.ACTION_COMPLETED) {
						System.out.println("接收到传输完成广播");
						fileList.remove(i);
						loadingNum.setText("正在上传(" + fileList.size() + ")");
						doneFileList.addFirst(mTransferredFile);
						loadedNum.setText("已上传(" + doneFileList.size() + ")");
						loadedAdapter.notifyDataSetChanged();
						break;

					}

				}
				
			}
			handler.sendEmptyMessage(0);
		}

	}
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			loadingAdapter.notifyDataSetChanged();
		}
		
	};
}


