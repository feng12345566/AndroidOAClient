package com.mct.controls;

import java.util.ArrayList;

import org.jivesoftware.smackx.packet.VCard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.mct.client.R;
import com.mct.model.GroupInfo;
import com.mct.model.User;
import com.mct.util.Common;
import com.mct.util.ImageUtil;
import com.mct.util.XmppTool;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
	private ArrayList<GroupInfo> group;
	private LayoutInflater inflater;
	private Context context;

	public MyExpandableListAdapter(Context context, ArrayList<GroupInfo> group) {
		// TODO Auto-generated constructor stub
		this.group = group;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return group.get(groupPosition).getFriendInfoList().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 获取好友信息视图
		ChildViewHolder childViewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.buddy_listview_child_item,
					null);
			childViewHolder = new ChildViewHolder();
			childViewHolder.username_friend = (TextView) convertView
					.findViewById(R.id.username_friend);
			childViewHolder.pic_frined = (SmartImageView) convertView
					.findViewById(R.id.pic_friend);
			childViewHolder.postion = (TextView) convertView
					.findViewById(R.id.postion_friend);
			childViewHolder.unit = (TextView) convertView
					.findViewById(R.id.org_friend);
			convertView.setTag(childViewHolder);
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}

		ArrayList<User> child = group.get(groupPosition).getFriendInfoList();
		User fi = child.get(childPosition);
		childViewHolder.pic_frined.setImageUrl(
				Common.HTTPSERVICE + fi.getUserId() + ".jpg",
				R.drawable.touxiang);

		String name = fi.getUserName();
		childViewHolder.username_friend.setText(name);
		childViewHolder.postion.setText(fi.getPosition()==null?"":fi.getPosition());
		childViewHolder.unit.setText(fi.getOrg()==null?"":fi.getOrg());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return group.get(groupPosition).getFriendInfoList().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		Log.e("mtag", "组数---->" + group.size());
		return group.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder groupViewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.buddy_listview_group_item,
					null);
			groupViewHolder = new GroupViewHolder();
			groupViewHolder.name_group = (TextView) convertView
					.findViewById(R.id.buddy_listview_group_name);
			convertView.setTag(groupViewHolder);
		} else {
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}
		groupViewHolder.name_group.setText(group.get(groupPosition)
				.getGroupName()+"("+group.get(groupPosition).getFriendInfoList().size()+"人)");

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class GroupViewHolder {
		private TextView name_group;
	}

	public class ChildViewHolder {
		SmartImageView pic_frined;
		TextView username_friend;
		TextView unit;
		TextView postion;

		public SmartImageView getPic_frined() {
			return pic_frined;
		}

		public void setPic_frined(SmartImageView pic_frined) {
			this.pic_frined = pic_frined;
		}

	}

}
