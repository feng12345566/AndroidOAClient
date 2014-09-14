package com.mct.client;

import java.util.HashMap;
import java.util.LinkedList;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.mct.controls.MultiChatListAdapter;
import com.mct.util.DrawUtil;
import com.mct.util.RecordUtil;
import com.mct.util.XmppTool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GroupChatListActivity extends Activity implements OnClickListener,OnItemClickListener {
	private LinearLayout backlayout;
	private TextView backText;
	private TextView title;
	private TextView sureBtn;
	private SwipeListView groupChatListView;
	private MultiChatListAdapter adapter;
	private LinkedList<HashMap<String, Object>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_groupchat);
		title = (TextView) findViewById(R.id.toptext);
		backText = (TextView) findViewById(R.id.backText);
		backlayout = (LinearLayout) findViewById(R.id.backlayout);
		sureBtn = (TextView) findViewById(R.id.sureBtn);
		groupChatListView = (SwipeListView) findViewById(R.id.groupchatlistview);
		sureBtn.setText("创建");
		title.setText("讨论组");
		backText.setText("个人中心");
		backlayout.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		list = RecordUtil.shareRecordUtil(this).getMultiChatList(
				XmppTool.loginUser.getUserId());
		adapter=new MultiChatListAdapter(this,list);
		groupChatListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
		groupChatListView.setSwipeCloseAllItemsWhenMoveList(true);
		groupChatListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		groupChatListView.setOffsetLeft(DrawUtil.getScreenSize(this)[0]-convertDpToPixel(83));
		groupChatListView.setAdapter(adapter);
	}
	public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sureBtn:
			Intent intent = new Intent(GroupChatListActivity.this,
					SortListViewActivity.class);
			intent.putExtra("fromtag", "GroupChatListActivity");
			intent.putExtra("opt", 3);
			startActivityForResult(intent, 1);
			
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.backlayout:
			this.finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==2){
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("id",data.getLongExtra("id", -1));
			map.put("members", RecordUtil.shareRecordUtil(this).idToName(data.getStringExtra("ids")));
			list.addFirst(map);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

}
