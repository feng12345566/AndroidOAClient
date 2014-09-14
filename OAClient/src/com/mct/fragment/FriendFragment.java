package com.mct.fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.mct.client.FriendInfoActivity;
import com.mct.client.R;
import com.mct.controls.MyExpandableListAdapter;
import com.mct.model.GroupInfo;
import com.mct.model.User;
import com.mct.util.Common;
import com.mct.util.UserDbUtil;
import com.mct.util.XmppTool;

public class FriendFragment extends Fragment implements OnChildClickListener,
		OnClickListener {
	private ExpandableListView listView;
	private MyExpandableListAdapter listAdapter;
	private ArrayList<GroupInfo> groupList;// ×éÁÐ±í
	private boolean isLoading = false;
	private View friendview;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		friendview = inflater.inflate(R.layout.activity_buddy, null);
		listView = (ExpandableListView) friendview
				.findViewById(R.id.buddy_expandablelistview);
		context = getActivity().getApplicationContext();
		loadData();
		listAdapter = new MyExpandableListAdapter(context, groupList);
		listView.setAdapter(listAdapter);

		listView.setOnChildClickListener(this);

		return friendview;
	}

	public void loadData() {

		// TODO Auto-generated method stub

		if (isLoading) {
			return;
		}
		isLoading = true;
		groupList = UserDbUtil.shareUserDb(getActivity()).getAllGroup();
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.buddy_expandablelistview:
			User user = groupList.get(groupPosition).getFriendInfoList()
					.get(childPosition);
			Intent intent = new Intent(context, FriendInfoActivity.class);
			intent.putExtra(Common.KEY_WITH_USER, user.getUserId());
			intent.putExtra(Common.KEY_USER_NAME, user.getUserName());
			startActivity(intent);
			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		}
	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

}
