package com.mct.model;

import org.jivesoftware.smackx.packet.VCard;

/**
 * �û���
 * @author huajian.zhang
 *
 */
public class User {
	private String userJid; // �û�ȫ�����磺user001@aji
	private String userId;// �û������磺user001
	private String userName;//�ǳ�
	private String type;// �û�����
	private String phoneNumber;//������
	private String position;//ְλ
	private String  sex;//�Ա�
	private String  mobilePhone;//�ֻ���
	private String  org;//��������
	
	
	public String getUserJid() {
		return userJid;
	}

	public void setUserJid(String userJid) {
		this.userJid = userJid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public User(String userId, String userJid,String type) {
		this.userId = userId;
		this.type = type;
		this.userJid=userJid;
	}

	/**
	 * 
	 */
	public User() {
		// TODO Auto-generated constructor stub
	}


}
