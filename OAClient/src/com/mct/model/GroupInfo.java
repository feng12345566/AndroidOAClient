package com.mct.model;

import java.util.ArrayList;

public class GroupInfo {
	private String groupName;//����
	private long id;
	private ArrayList<User> FriendInfoList;//���еĺ�����Ϣ

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public ArrayList<User> getFriendInfoList() {
		return FriendInfoList;
	}

	public void setFriendInfoList(ArrayList<User> friendInfoList) {
		FriendInfoList = friendInfoList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

}
