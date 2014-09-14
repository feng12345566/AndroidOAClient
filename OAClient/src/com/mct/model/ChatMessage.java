package com.mct.model;

public class ChatMessage {
	private long _id;// ��words���ڵ�source_id����

	private long thread_id;// �Ựid��һ����ϵ�˵ĻỰһ��id����Ự���ڵ�_id���� integer

	private String address;// ������id

	private long date; // ��Ϣ�շ����� integer

	private int read;// �Ƿ��Ķ� integer default 0 0��δ���� 1���Ѷ�

	private int type;// ��Ϣ���� integer 1��inbox
	// 2��sent

	private String body;// ����


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
