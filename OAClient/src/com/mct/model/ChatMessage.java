package com.mct.model;

public class ChatMessage {
	private long _id;// 与words表内的source_id关联

	private long thread_id;// 会话id，一个联系人的会话一个id，与会话表内的_id关联 integer

	private String address;// 发出方id

	private long date; // 消息收发日期 integer

	private int read;// 是否阅读 integer default 0 0：未读， 1：已读

	private int type;// 消息类型 integer 1：inbox
	// 2：sent

	private String body;// 内容


	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getThread_id() {
		return thread_id;
	}

	public void setThread_id(long thread_id) {
		this.thread_id = thread_id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}


	public ChatMessage(String address, long date, int type, String body) {
		this.address = address;
		this.date = date;
		this.type = type;
		this.body = body;
	}

}
