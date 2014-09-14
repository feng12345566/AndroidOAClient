package com.mct.model;

import java.util.ArrayList;

/**
 * 聊天室
 * 
 * @author huajian.zhang
 * 
 */
public class ChatRoomGroup {

	// 聊天室服务名
	private String serverName;

	// 聊天室房间列表
	private ArrayList<ChatRoom> roomList;

	public ChatRoomGroup() {
		// TODO Auto-generated constructor stub
	}

	public ChatRoomGroup(String serverName, ArrayList<ChatRoom> roomList) {
		super();
		this.serverName = serverName;
		this.roomList = roomList;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public ArrayList<ChatRoom> getRoomList() {
		return roomList;
	}

	public void setRoomList(ArrayList<ChatRoom> roomList) {
		this.roomList = roomList;
	}

}
