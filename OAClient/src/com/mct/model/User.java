package com.mct.model;

import org.jivesoftware.smackx.packet.VCard;

/**
 * 用户类
 * @author huajian.zhang
 *
 */
public class User {
	private String userJid; // 用户全名，如：user001@aji
	private String userId;// 用户名，如：user001
	private String userName;//昵称
	private String type;// 用户类型
	private String phoneNumber;//座机号
	private String position;//职位
	private String  sex;//性别
	private String  mobilePhone;//手机号
	private String  org;//二级部门
	
	
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
