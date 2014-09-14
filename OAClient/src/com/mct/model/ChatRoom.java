package com.mct.model;

public class ChatRoom {
	// 房间名称
	private String roomName;

	// 房间id
	private String roomId;

	// 房间人数
	private int memberNum;

	// 描述
	private String description;

	// 主题
	private String subjection;

	// 是否需要密码
	private boolean isPasswordProtected;

	public ChatRoom() {
		// TODO Auto-generated constructor stub
	}

	public ChatRoom(String roomName, String roomId, int memberNum,
			String description, String subjection, boolean isPasswordProtected) {
		this.roomName = roomName;
		this.roomId = roomId;
		this.memberNum = memberNum;
		this.description = description;
		this.subjection = subjection;
		this.isPasswordProtected = isPasswordProtected;
	}

	public String getRoomName() {
		return roomName;
	}

	public ChatRoom setRoomName(String roomName) {
		this.roomName = roomName;
		return this;
	}

	public String getRoomId() {
		return roomId;
	}

	public ChatRoom setRoomId(String roomId) {
		this.roomId = roomId;
		return this;
	}

	public int getMemberNum() {
		return memberNum;
	}

	public ChatRoom setMemberNum(int memberNum) {
		this.memberNum = memberNum;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public ChatRoom setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getSubjection() {
		return subjection;
	}

	public ChatRoom setSubjection(String subjection) {
		this.subjection = subjection;
		return this;
	}

	public boolean isPasswordProtected() {
		return isPasswordProtected;
	}

	public ChatRoom setPasswordProtected(boolean isPasswordProtected) {
		this.isPasswordProtected = isPasswordProtected;
		return this;
	}

}
