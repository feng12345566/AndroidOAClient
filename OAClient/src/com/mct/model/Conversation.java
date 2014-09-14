package com.mct.model;

public class Conversation {
	// 会话id
	private long _id;
	
	// 会话最后时间
	private long date;


	/**
	 * snippet为最后收到/发送的信息；
	 */
	private String snippet;

	/**
	 * 未读信息数
	 */
	private int unReadNum;

	/**
	 * 当前登录的用户
	 */
	private String login_user;

	/**
	 * 会话的对方用户名
	 */
	private String with_user;
	
	private int isGroup;
	//会话类型
	private int type;
	public Conversation() {

	}

	public Conversation(long _id,long date,String snippet, int unReadNum,
			String login_user, String with_user,int isGroup,int type) {
		this._id = _id;
		this.date = date;
		this.snippet = snippet;
		this.unReadNum = unReadNum;
		this.login_user = login_user;
		this.with_user = with_user;
		this.isGroup=isGroup;
		this.type=type;
	}

	public int getUnReadNum() {
		return unReadNum;
	}

	public void setUnReadNum(int unReadNum) {
		this.unReadNum = unReadNum;
	}


	public int getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(int isGroup) {
		this.isGroup = isGroup;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	
	public String getLogin_user() {
		return login_user;
	}

	public void setLogin_user(String login_user) {
		this.login_user = login_user;
	}

	public String getWith_user() {
		return with_user;
	}

	public void setWith_user(String with_user) {
		this.with_user = with_user;
	}

	/**
	 * 功能描述：获取会话类型
	 * Administrator
	 * 2014年7月14日 下午10:35:23
	 * @return 0聊天消息 1邮件提醒 2公告 3 系统通知 4流程提醒
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
