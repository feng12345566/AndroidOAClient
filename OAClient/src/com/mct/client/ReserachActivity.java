package com.mct.client;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mct.util.HttpRequestUtil;
import com.mct.util.TimeRender;
import com.mct.util.XmppTool;
import com.mct.view.XListView.IXListViewListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ReserachActivity extends RefreshActivity implements
		OnClickListener, IXListViewListener, OnItemClickListener {
	private ArrayList<HashMap<String, String>> list;
	private SimpleAdapter adapter;
	private int page = 0;
	private String result;
	private ArrayList<HashMap<String, String>> research;
	private int action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		backText.setText("个人中心");
		backView.setOnClickListener(this);
		nextBtn.setText("设计发布");
		titleText.setText("调查列表");
		nextBtn.setOnClickListener(this);
		list = new ArrayList<HashMap<String, String>>();
		adapter = new SimpleAdapter(this, list, R.layout.researchlistitem,
				new String[] { "title", "endtime", "joinnum", "joined" },
				new int[] { R.id.reshaerchtitleitem, R.id.researchendtimeitem,
						R.id.joinnum, R.id.joinedtext });
		refreshListView.setAdapter(adapter);
		refreshListView.setXListViewListener(this);
		refreshListView.setOnItemClickListener(this);
		refreshListView.startLoadMore();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.backview:
			this.finish();
			break;
		case R.id.nextBtn:
			Intent intent = new Intent(this, EditResearchActivity.class);
			startActivityForResult(intent, 1);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				refreshListView.stopRefresh();
				refreshListView.stopLoadMore();
				if (research != null && research.size() != 0) {
					if (action == 1) {
						list.clear();
					}
					list.addAll(research);
					adapter.notifyDataSetChanged();

				} else {
					Toast.makeText(ReserachActivity.this, "没有发现数据", Toast.LENGTH_SHORT).show();
				}
				refreshListView.setPullLoadEnable(true);
				refreshListView.setPullRefreshEnable(true);

				break;

			default:
				break;
			}
		}

	};

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
				String httpUrl = "http://nat.nat123.net:14313/oa/message/getResearchlist.jsp?page="
						+ page + "&userid=" + XmppTool.loginUser.getUserId();
				result = HttpRequestUtil.getResponsesByGet(httpUrl);
				if (!result.trim().equals("")) {
					if (research == null) {
						research = new ArrayList<HashMap<String, String>>();
					} else {
						research.clear();
					}
					try {
						JSONObject jsonObject = new JSONObject(result);
						JSONArray researchArray = jsonObject
								.getJSONArray("research");
						for (int i = 0; i < researchArray.length(); i++) {
							JSONObject jo = researchArray.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("id", String.valueOf(jo.getLong("id")));
							map.put("title", jo.getString("title"));
							map.put("endtime", TimeRender.getDate(
									jo.getLong("endtime"), "yy-MM-dd HH:mm:ss"));
							map.put("joinnum", jo.getInt("joinnum") + "");
							map.put("joined", jo.getInt("joined") == 1 ? "你已参与"
									: "你还未参与");
							research.add(map);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				handler.sendEmptyMessage(1);
			}
		}).start();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		page = 1;
		action = 1;
		refreshListView.setPullLoadEnable(false);
		getData();
		SharedPreferences time = getSharedPreferences("refreshtime", 0);
		refreshListView.setRefreshTime(time.getString("research", ""));
		time.edit()
				.putString("research",
						TimeRender.getCurTimeToString("yy-MM-dd HH:mm:ss"))
				.commit();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		page++;
		action = 2;
		refreshListView.setPullRefreshEnable(false);
		getData();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (list.size() != 0) {
			Intent intent = null;
			boolean joined = list.get(arg2 - 1).get("joined").equals("你已参与") ? true
					: false;
			if (joined) {
				intent = new Intent(this, ShowStatisticsActivity.class);
			} else {
				intent = new Intent(this, ShowResearchActivity.class);
			}
			intent.putExtra("id", research.get(arg2 - 1).get("id"));
			intent.putExtra("title", research.get(arg2 - 1).get("title"));
			startActivity(intent);
		}
	}

}
