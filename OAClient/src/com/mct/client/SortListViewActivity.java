package com.mct.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jivesoftware.smackx.muc.MultiUserChat;

import com.mct.controls.GroupListAdapter;
import com.mct.controls.SelectedMemberImageAdapter;
import com.mct.model.GroupInfo;
import com.mct.model.User;
import com.mct.sortlistview.CharacterParser;
import com.mct.sortlistview.ClearEditText;
import com.mct.sortlistview.GroupMemberBean;
import com.mct.sortlistview.PinyinComparator;
import com.mct.sortlistview.SideBar;
import com.mct.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.mct.sortlistview.SortGroupMemberAdapter;
import com.mct.util.RecordUtil;
import com.mct.util.UserDbUtil;
import com.mct.util.XmppTool;
import com.mct.view.HorizontalListView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class SortListViewActivity extends Activity implements OnClickListener {
	private LinearLayout backlayout;
	private TextView backText;
	private TextView title;
	private TextView titleText;
	private TextView sureBtn;
	private ListView groupListView;
	private static ArrayList<GroupInfo> groups;
	private GroupListAdapter groupListAdapter;
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortGroupMemberAdapter adapter;
	private ClearEditText mClearEditText;
	private ProgressDialog progressDialog;
	private FrameLayout memberSelectLayout;
	private Button sureGroupChatMembers;
	private SelectedMemberImageAdapter selectedMemberImageAdapter;
	/**
	 * 上次第一个可见元素，用于滚动时记录标识。
	 */
	private int lastFirstVisibleItem = -1;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<GroupMemberBean> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	private ArrayList<User> userList;// 用户列表
	private ArrayList<String> selectedUserIdList;// 已选用户id列表
	private LinearLayout titleLayout;
	private TextView tvNofriends;
	private HorizontalListView groupchatmembers;
	private int opt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creategroupchat);
		opt = getIntent().getIntExtra("opt",0);
		groups = new ArrayList<GroupInfo>();
		selectedUserIdList = new ArrayList<String>();
		ArrayList<String> selected=getIntent().getStringArrayListExtra("selected");
		if(selected!=null){
		   selectedUserIdList.addAll(selected);
		}
		selectedMemberImageAdapter = new SelectedMemberImageAdapter(this,
				selectedUserIdList);
		initViews();
		getData();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				progressDialog = new ProgressDialog(SortListViewActivity.this);
				progressDialog.setMessage("数据加载中...");
				progressDialog.show();
				break;

			case 1:
				progressDialog.dismiss();
				groupListAdapter.setGroupInfoList(groups);
				groupListAdapter.notifyDataSetChanged();

				break;
			case 7:
				selectedUserIdList.add((String) msg.obj);
				sureGroupChatMembers.setEnabled(true);
				sureGroupChatMembers.setText("确定（" + selectedUserIdList.size()
						+ "）");
				selectedMemberImageAdapter.setList(selectedUserIdList);
				selectedMemberImageAdapter.notifyDataSetChanged();

				break;
			case 8:
				selectedUserIdList.remove((String) msg.obj);
				sureGroupChatMembers.setEnabled(selectedUserIdList.size() > 0);
				sureGroupChatMembers.setText("确定（" + selectedUserIdList.size()
						+ "）");
				selectedMemberImageAdapter.setList(selectedUserIdList);
				selectedMemberImageAdapter.notifyDataSetChanged();
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
				groups = UserDbUtil.shareUserDb(SortListViewActivity.this)
						.getAllGroup();
				handler.sendEmptyMessage(1);
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sureBtn:
			this.finish();
			Intent intent = new Intent(this, GroupChatListActivity.class);
			setResult(1, intent);
			break;

		case R.id.backlayout:
			getData();
			groupListView.setVisibility(View.VISIBLE);
			memberSelectLayout.setVisibility(View.GONE);
			backlayout.setVisibility(View.GONE);
			break;
		case R.id.suregroupchatmembers:
			switch (opt) {
			case 3:
				StringBuffer roomName = new StringBuffer();
				int size = selectedUserIdList.size();
				for (int i = 0; i < size; i++) {
					roomName.append(selectedUserIdList.get(i));
					roomName.append("、");
				}
				roomName.append(XmppTool.loginUser.getUserId());
				XmppTool.shareConnectionManager(this).createRoom(
						roomName.toString(), null, roomName.toString());
				String roomID = roomName.toString()
						+ "@conference."
						+ XmppTool.shareConnectionManager(this).getConnection()
								.getServiceName();
				long id = RecordUtil.shareRecordUtil(this).updateConversation(
						XmppTool.loginUser.getUserId(), roomID, "", 1, 0)[0];
				Intent intent2 = new Intent(this, GroupChatListActivity.class);
				intent2.putExtra("id", id);
				intent2.putExtra("ids", roomName.toString());
				setResult(2, intent2);
				break;
			case 2:
			case 1:
				Intent intent3 = new Intent(this, PostMessageActivity.class);
				intent3.putStringArrayListExtra("selectedmember", selectedUserIdList);
				setResult(3, intent3);	
				Log.e("selectedUserIdList", selectedUserIdList.toString());
				break;
			}
			this.finish();
			overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(groupListView.getVisibility()==View.GONE){
				getData();
				groupListView.setVisibility(View.VISIBLE);
				memberSelectLayout.setVisibility(View.GONE);
				backlayout.setVisibility(View.GONE);
			}else{
				this.finish();
				overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
			}
		}
		return true;
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		titleText = (TextView) findViewById(R.id.toptext);
		backText = (TextView) findViewById(R.id.backText);
		backlayout = (LinearLayout) findViewById(R.id.backlayout);
		sureBtn = (TextView) findViewById(R.id.sureBtn);
		groupListView = (ListView) findViewById(R.id.grouplistview);
		memberSelectLayout = (FrameLayout) findViewById(R.id.memberselectlayout);
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		groupListView.setVisibility(View.VISIBLE);
		sureGroupChatMembers = (Button) findViewById(R.id.suregroupchatmembers);
		sureGroupChatMembers.setOnClickListener(this);
		groupListAdapter = new GroupListAdapter(this, groups);
		groupListView.setAdapter(groupListAdapter);
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				groupListView.setVisibility(View.GONE);
				memberSelectLayout.setVisibility(View.VISIBLE);
				backlayout.setVisibility(View.VISIBLE);
				SourceDateList.clear();
				userList.clear();

				userList = groups.get(position).getFriendInfoList();
				SourceDateList = filledData(userList);

				// 根据a-z进行排序源数据
				Collections.sort(SourceDateList, pinyinComparator);
				adapter.setSelectedList(selectedUserIdList);
				adapter.updateListView(SourceDateList);
			}
		});
		sureBtn.setText("取消");
		titleText.setText("选择联系人");
		backText.setText("返回");
		groupchatmembers = (HorizontalListView) findViewById(R.id.groupchatmembers);
		groupchatmembers.setAdapter(selectedMemberImageAdapter);
		backlayout.setOnClickListener(this);
		backlayout.setVisibility(View.GONE);
		sureBtn.setOnClickListener(this);
		titleLayout = (LinearLayout) findViewById(R.id.title_layout);
		title = (TextView) this.findViewById(R.id.title_layout_catalog);
		tvNofriends = (TextView) this
				.findViewById(R.id.title_layout_no_friends);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		memberSelectLayout.setVisibility(View.GONE);
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(
						getApplication(),
						((GroupMemberBean) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
			}
		});
		userList = new ArrayList<User>();
		SourceDateList = filledData(userList);

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortGroupMemberAdapter(this, SourceDateList, handler);
		adapter.setSelectedList(selectedUserIdList);
		sortListView.setAdapter(adapter);
		sortListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (SourceDateList.size() != 0) {
					int section = getSectionForPosition(firstVisibleItem);
					int nextSection = getSectionForPosition(firstVisibleItem + 1);
					int nextSecPosition = getPositionForSection(+nextSection);
					if (firstVisibleItem != lastFirstVisibleItem) {
						MarginLayoutParams params = (MarginLayoutParams) titleLayout
								.getLayoutParams();
						params.topMargin = 0;
						titleLayout.setLayoutParams(params);
						title.setText(SourceDateList.get(
								getPositionForSection(section))
								.getSortLetters());
					}
					if (nextSecPosition == firstVisibleItem + 1) {
						View childView = view.getChildAt(0);
						if (childView != null) {
							int titleHeight = titleLayout.getHeight();
							int bottom = childView.getBottom();
							MarginLayoutParams params = (MarginLayoutParams) titleLayout
									.getLayoutParams();
							if (bottom < titleHeight) {
								float pushedDistance = bottom - titleHeight;
								params.topMargin = (int) pushedDistance;
								titleLayout.setLayoutParams(params);
							} else {
								if (params.topMargin != 0) {
									params.topMargin = 0;
									titleLayout.setLayoutParams(params);
								}
							}
						}
					}
					lastFirstVisibleItem = firstVisibleItem;
				}
			}
		});
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 这个时候不需要挤压效果 就把他隐藏掉
				titleLayout.setVisibility(View.GONE);
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<GroupMemberBean> filledData(ArrayList<User> userList) {
		List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();

		for (int i = 0; i < userList.size(); i++) {
			GroupMemberBean sortModel = new GroupMemberBean();
			sortModel.setName(userList.get(i).getUserName());
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(userList.get(i)
					.getUserName());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			sortModel.setUserId(userList.get(i).getUserId());
			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<GroupMemberBean> filterDateList = new ArrayList<GroupMemberBean>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
			tvNofriends.setVisibility(View.GONE);
		} else {
			filterDateList.clear();
			for (GroupMemberBean sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
		if (filterDateList.size() == 0) {
			tvNofriends.setVisibility(View.VISIBLE);
		}
	}

	public Object[] getSections() {
		return null;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		if (position >= SourceDateList.size()) {
			position = SourceDateList.size() - 1;
		}
		return SourceDateList.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < SourceDateList.size(); i++) {
			String sortStr = SourceDateList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

}
