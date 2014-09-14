package com.mct.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mct.client.EmailSearchActivity;
import com.mct.client.R;
import com.mct.client.ShowMessageActivity;
import com.mct.controls.InboxAdapter;
import com.mct.model.CustomMessage;
import com.mct.util.HttpRequestUtil;
import com.mct.util.MyAnimationUtils;
import com.mct.util.TimeRender;
import com.mct.util.XmppTool;
import com.mct.view.XListView;
import com.mct.view.XListView.IXListViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class InboxFragment extends Fragment implements IXListViewListener,
		OnFocusChangeListener,OnItemClickListener{
	private ArrayList<CustomMessage> list;
	private XListView inboxListView;
	private EditText inboxSearchEdit;
	private InboxAdapter inboxAdapter;
	private int page;
	private int isdelete;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.inboxfragment, null);
		isdelete=getArguments().getInt("isdelete");
		inboxSearchEdit = (EditText) view.findViewById(R.id.inboxsearchedit);
		inboxListView = (XListView) view.findViewById(R.id.inboxlistview);
		inboxSearchEdit.setOnFocusChangeListener(this);
		inboxListView.setPullLoadEnable(true);
		inboxAdapter = new InboxAdapter(getActivity());
		list = new ArrayList<CustomMessage>();
		inboxAdapter.setCustomtMessagesList(list);
		inboxListView.setPullRefreshEnable(true);
		inboxListView.setAdapter(inboxAdapter);
		inboxListView.setXListViewListener(this);
		inboxListView.setOnItemClickListener(this);
		inboxListView.startLoadMore();
		return view;
	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
				String httpUrl = "http://nat.nat123.net:14313/oa/message/GetMessageList.jsp?page="
						+ page + "&userid=" + XmppTool.loginUser.getUserId()+"&isdelete="+isdelete;
				String result = HttpRequestUtil.getResponsesByGet(httpUrl);
				if (result != null && result.trim().startsWith("{")) {
					try {
						JSONObject json = new JSONObject(result);
						if (json.getString("status").equals("success")) {
							JSONArray reArray = json.getJSONArray("result");
							for (int i = 0; i < reArray.length(); i++) {
								CustomMessage message = new CustomMessage();
								JSONObject email = reArray.getJSONObject(i);
								message.setSender(email.getString("sender"));
								message.setId(email.getLong("id"));
								message.setTitle(email.getString("title"));
								message.setTime(email.getLong("time"));
								list.add(message);
							}
							handler.sendEmptyMessage(1);
						}else{
							handler.sendEmptyMessage(2);
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(-1);
					}
				} else {
					handler.sendEmptyMessage(-1);
				}

			}
		}).start();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case -1:
				// 加载失败
				page--;
				inboxListView.stopRefresh();
				inboxListView.stopLoadMore();
				inboxListView.setPullLoadEnable(true);
				inboxListView.setPullRefreshEnable(true);
				break;

			
			case 1:
				// 加载成功
				inboxListView.stopRefresh();
				inboxListView.stopLoadMore();
				inboxAdapter.setCustomtMessagesList(list);
				inboxAdapter.notifyDataSetChanged();
				if(list.size()%10==0){
				    inboxListView.setPullLoadEnable(true);
				}else{
					inboxListView.setPullLoadEnable(false);
				}
				inboxListView.setPullRefreshEnable(true);
				break;
			case 2:
				// 无数据
				inboxListView.stopRefresh();
				inboxListView.stopLoadMore();
				inboxListView.setPullLoadEnable(true);
				inboxListView.setPullRefreshEnable(true);
				Toast.makeText(getActivity(), "未发现数据！", Toast.LENGTH_SHORT).show();
				break;
			}
		}

	};

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		// 重置页数
		page = 1;
		list.clear();
		inboxListView.setPullLoadEnable(false);
		// 加载数据
		getData();
		inboxListView.setRefreshTime(TimeRender
				.getCurTimeToString("yy-MM-dd HH:mm:ss"));
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		inboxListView.setPullRefreshEnable(false);
		// 页码递增
		page++;
		// 请求数据
		getData();

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (hasFocus) {
			Intent intent = new Intent(getActivity(), EmailSearchActivity.class);
			startActivityForResult(intent, 1);
			getActivity().overridePendingTransition(R.anim.in_from_left,
					R.anim.out_to_right);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		CustomMessage customMessage=list.get(position-1);
		Intent intent=new Intent(getActivity(),ShowMessageActivity.class);
		intent.putExtra("id", customMessage.getId());
		intent.putExtra("title", customMessage.getTitle());
		intent.putExtra("time", customMessage.getTime());
		intent.putExtra("sender", customMessage.getSender());
		startActivity(intent);
		
		MyAnimationUtils.inActivity(getActivity());
	}

	
	
}
