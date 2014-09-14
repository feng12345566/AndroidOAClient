package com.mct.model;

public class Conversation {
	// �Ựid
	private long _id;
	
	// �Ự���ʱ��
	private long date;


	/**
	 * snippetΪ����յ�/���͵���Ϣ��
	 */
	private String snippet;

	/**
	 * δ����Ϣ��
	 */
	private int unReadNum;

	/**
	 * ��ǰ��¼���û�
	 */
	private String login_user;

	/**
	 * �Ự�ĶԷ��û���
	 */
	private String with_user;
	
	private int isGroup;
	//�Ự����
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
	 * ������������ȡ�Ự����
	 * Administrator
	 * 2014��7��14�� ����10:35:23
	 * @return 0������Ϣ 1�ʼ����� 2���� 3 ϵͳ֪ͨ 4��������
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
