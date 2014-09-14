package com.mct.model;

public class CustomMessage {
	private long id;// id
	private String title;// 主题
	private String content;// 正文
	private String accessory;// 附件
	private String receiver;// 接收者
	private long time;
	private String sender;
	private int type;//类型 1通知消息  2邮件消息
	private int isRead;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAccessory() {
		return accessory;
	}
	public void setAccessory(String accessory) {
		this.accessory = accessory;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

}
