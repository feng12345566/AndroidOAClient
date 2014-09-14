package com.mct.fragment;

import java.util.LinkedList;
import com.mct.client.PostMessageActivity;
import com.mct.client.R;
import com.mct.client.ShowMessageActivity;
import com.mct.controls.OutBoxListAdapter;
import com.mct.model.CustomMessage;
import com.mct.util.DrawUtil;
import com.mct.util.MyAnimationUtils;
import com.mct.util.UserDataDbUtil;
import com.mct.util.XmppTool;
import com.mct.view.XListView;
import com.mct.view.XListView.IXListViewListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class OutboxFragment extends Fragment implements OnItemClickListener,
		IXListViewListener,OnItemLongClickListener{
	private XListView outbox_xlistview;
	private LinkedList<CustomMessage> list;
	private LinkedList<CustomMessage> customMessages;
	private int page = 0;
	private OutBoxListAdapter adapter;
	private String tag="outbox";
	private int l;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_outbox, null);
		
		outbox_xlistview = (XListView) view.findViewById(R.id.outbox_xlistview);
		customMessages = new LinkedList<CustomMessage>();
		outbox_xlistview.setPullLoadEnable(true);
		outbox_xlistview.setPullLoadEnable(true);
		adapter = new OutBoxListAdapter(getActivity(), customMessages);
		outbox_xlistview.setAdapter(adapter);
		outbox_xlistview.setXListViewListener(this);
		if(getArguments()!=null&&getArguments().containsKey("tag")){
			   tag = getArguments().getString("tag");
			}
		page=0;
		outbox_xlistview.startLoadMore();
		outbox_xlistview.setOnItemClickListener(this);
		outbox_xlistview.setOnItemLongClickListener(this);
		return view;
	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// handler.sendEmptyMessage(0);
				if (tag.equals("outbox")) {
					list = UserDataDbUtil.shareUserDataDb(getActivity())
							.queryMessageList(XmppTool.loginUser.getUserId(),
									1, page);
					Log.e("size", "收件箱查询到"+list.size()+"条记录");
				} else if (tag.equals("draft")) {
					list = UserDataDbUtil.shareUserDataDb(getActivity())
							.queryMessageList(XmppTool.loginUser.getUserId(),
									0, page);
				}
				handler.sendEmptyMessage(2);
			}
		}).start();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				Log.e("status", "停止加载");
				outbox_xlistview.stopLoadMore();
				outbox_xlistview.stopRefresh();
				if (list != null && list.size() != 0) {
					customMessages.addAll(list);
					adapter.setList(customMessages);
					adapter.notifyDataSetChanged();
				} else {
					page--;
				}
				outbox_xlistview.setPullLoadEnable(true);
				outbox_xlistview.setPullRefreshEnable(true);
				if (customMessages.size() == 0
						|| customMessages.size() % 10 != 0) {
					outbox_xlistview.setPullLoadEnable(false);
				}
				
				break;
			}
		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		CustomMessage customMessage = customMessages.get(position - 1);
		String title = customMessage.getTitle();
		String sender = customMessage.getSender();
		Intent intent=new Intent();
		if (tag.equals("outbox")) {
			intent.setClass(getActivity(), ShowMessageActivity.class);
		} else if (tag.equals("draft")) {
			intent.setClass(getActivity(), PostMessageActivity.class);
		}
		intent.putExtra("title", title);
		intent.putExtra("sender", sender);
		startActivity(intent);
		MyAnimationUtils.inActivity(getActivity());
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		customMessages.clear();
		if(list!=null){
		list.clear();
		}
		page = 1;
		outbox_xlistview.setPullLoadEnable(false);
		getData();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if(list!=null){
		list.clear();
		}
		page++;
		getData();
		outbox_xlistview.setPullRefreshEnable(false);
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		l = position;
		LayoutInflater layoutInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view1 = layoutInflater.inflate(R.layout.outbox_pop_menu, null);
		
		final PopupWindow pop = new PopupWindow(view1,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		pop.setOutsideTouchable(true);
		pop.setTouchable(true);
		pop.setFocusable(true);
		pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		int[] size = DrawUtil.getViewSize(view1);
		pop.showAtLocation(view, Gravity.NO_GRAVITY,
				location[0] + view.getWidth() / 2 - size[1], location[1]
						- size[0]);
		TextView modifyText = (TextView) view1.findViewById(R.id.modifymailpopbtn);
		modifyText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), PostMessageActivity.class);
				intent.putExtra("opt",1);
				intent.putExtra("title",customMessages.get(l-1).getTitle());
				intent.putExtra("content",customMessages.get(l-1).getContent());
				intent.putExtra("accessory",customMessages.get(l-1).getAccessory());
				startActivity(intent);
				MyAnimationUtils.inActivity(getActivity());
				pop.dismiss();
			}
		});
		TextView deleteText = (TextView) view1.findViewById(R.id.deletemailpopbtn);
		deleteText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserDataDbUtil.shareUserDataDb(getActivity()).deleteCustomMessage(customMessages.get(l-1).getId());
				list.remove(l-1);
		        adapter.notifyDataSetChanged();
                pop.dismiss();
			}
		});
		return true;
	}


}
