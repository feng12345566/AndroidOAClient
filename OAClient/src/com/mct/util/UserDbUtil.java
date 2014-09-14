package com.mct.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.mct.model.GroupInfo;
import com.mct.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbUtil {
	private SQLiteDatabase db;
	private Context context;
	private SQLiteOpenHelper helper;
	private static UserDbUtil userDbUtil;
	private static final String DB_NAME = "user";
	private static final String TABLE_USER_NAME = "user";
	private static final String TABLE_GROUP_NAME = "usergroup";

	private static final String KEY_ID = "_id";

	private static final String KEY_USERID = "userid";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_USERUNIT = "userunit";
	private static final String KEY_USERPHONE = "userphone";
	private static final String KEY_GROUPID = "groupid";
	private static final String KEY_GROUP_ID = "group_id";
	private static final String KEY_GROUPNAME = "groupname";
	private static final String KEY_SEX = "sex";
	private static final String KEY_MOBILE = "mobile";
	private static final String KEY_POSTION = "postion";

	private UserDbUtil(Context context) {
		this.context = context;
		helper = new DbHelper(context);
		openDatabase();
	}

	public static UserDbUtil shareUserDb(Context context) {
		if (userDbUtil == null) {
			userDbUtil = new UserDbUtil(context);
		}
		return userDbUtil;
	}

	private class DbHelper extends SQLiteOpenHelper {
		public DbHelper(Context context) {
			super(context, DB_NAME, null, 1);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase mdb) {
			// TODO Auto-generated method stub
			final String TABLE_USER_CREATE = "create table user (_id integer primary key autoincrement, "
					+ "groupid integer not null, userid text not null, username text not null,userunit text,userphone text,"
					+ "sex text,mobile text,postion text);";
			final String TABLE_GROUP_CREATE = "create table  usergroup (group_id integer primary key autoincrement, "
					+ "groupname text not null);";
			mdb.execSQL(TABLE_GROUP_CREATE);
			mdb.execSQL(TABLE_USER_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
		}

	}

	/**
	 * 打开数据库
	 */
	public void openDatabase() {
		db = helper.getWritableDatabase();
	}

	/**
	 * 关闭数据库
	 */
	public void closeDatabase() {
		db.close();
		helper.close();
		db = null;
	}

	/**
	 * 插入组
	 * 
	 * @param groupName
	 */
	public long insertDataToGroup(String groupName) {
		// 检查组名是否存在，不存在则插入
		long id = queryGroupId(groupName);
		if (id > 0) {
			return id;
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_GROUPNAME, groupName);
		return db.insert(TABLE_GROUP_NAME, null, contentValues);
	}

	/**
	 * 插入用户
	 * 
	 * @param userName
	 */
	public boolean insertDataToUser(long groupId, String userId,
			String userName, String userUnit, String userPhone, String sex,
			String mobile, String postion) {
		String uName = getUserNameById(userId);
		if (uName != null) {
			deleteUser(userId);
		}
		// 不存在
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_GROUPID, groupId);
		contentValues.put(KEY_USERID, userId);
		contentValues.put(KEY_USERNAME, userName);
		contentValues.put(KEY_USERUNIT, userUnit);
		contentValues.put(KEY_USERPHONE, userPhone);
		contentValues.put(KEY_SEX, sex);
		contentValues.put(KEY_MOBILE, mobile);
		contentValues.put(KEY_POSTION, postion);
		return db.insert(TABLE_USER_NAME, null, contentValues) > 0;

	}

	
	/**
	 * 更新用户信息
	 * @param sex
	 * @param mobilePhone
	 * @param phone
	 * @param org
	 * @param postion
	 */
	public void updateUserInfo(String useid,String sex,String mobilePhone,String phone,String org,String postion){
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_USERUNIT, org);
		contentValues.put(KEY_USERPHONE, phone);
		contentValues.put(KEY_SEX, sex);
		contentValues.put(KEY_MOBILE, mobilePhone);
		contentValues.put(KEY_POSTION, postion);
		db.update(TABLE_USER_NAME, contentValues, KEY_USERID+"='"+useid+"'", null);
	}
	/**
	 * 根据组名获取组id
	 * 
	 * @param groupName
	 * @return -1表示该组不存在
	 */
	public long queryGroupId(String groupName) {
		long id = 0;
		Cursor cursor = db.query(TABLE_GROUP_NAME,
				new String[] { KEY_GROUP_ID }, KEY_GROUPNAME + "='" + groupName
						+ "'", null, null, null, null);
		if (cursor.moveToNext()) {
			id = cursor.getLong(0);
		} else {
			id = -1;
		}
		cursor.close();
		return id;

	}

	/**
	 * 获取通讯录列表
	 * 
	 * @param groupName
	 * @return
	 */
	public ArrayList<GroupInfo> getAllGroup() {
		ArrayList<GroupInfo> groupList = new ArrayList<GroupInfo>();
		Cursor cursor = db.query(TABLE_GROUP_NAME, new String[] { KEY_GROUP_ID,
				KEY_GROUPNAME }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			long id = cursor.getLong(0);
			GroupInfo groupInfo = new GroupInfo();
			groupInfo.setId(id);
			groupInfo.setGroupName(cursor.getString(1));
			groupInfo.setFriendInfoList(getUserByGroupId(id));
			groupList.add(groupInfo);
		}
		cursor.close();
		return groupList;

	}

	/**
	 * 通过userId获取用户名
	 * 
	 * @param userId
	 * @return
	 */
	public String getUserNameById(String userId) {
		// TODO Auto-generated method stub
		String userName = null;
		Cursor cursor = db.query(TABLE_USER_NAME,
				new String[] { KEY_USERNAME },
				KEY_USERID + "='" + userId + "'", null, null, null, null);
		if (cursor.moveToNext()) {
			userName = cursor.getString(0);
		}
		cursor.close();
		return userName;
	}

	/**
	 * 删除用户
	 * 
	 * @param userId
	 * @return
	 */
	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
		db.delete(TABLE_USER_NAME, KEY_USERID + "='" + userId + "'", null);
	}

	

	/**
	 * 获取组中的用户
	 * 
	 * @param groupId
	 * @return
	 */
	public ArrayList<User> getUserByGroupId(long groupId) {
		// TODO Auto-generated method stub
		ArrayList<User> userList = new ArrayList<User>();
		Cursor cursor = db.query(TABLE_USER_NAME, new String[] { KEY_USERID,
				KEY_USERNAME, KEY_USERUNIT, KEY_POSTION }, KEY_GROUPID + "="
				+ groupId, null, null, null, null);
		while (cursor.moveToNext()) {
			User user =new User();
			user.setUserId(cursor.getString(0));
			user.setUserName(cursor.getString(1));
			user.setOrg(cursor.getString(2));
			user.setPosition(cursor.getString(3));
			userList.add(user);
		}
		cursor.close();
		return userList;
	}

	/**
	 * 获取所有用户
	 */
	public ArrayList<User> getAllUser() {
		// TODO Auto-generated method stub
		ArrayList<User> userList = new ArrayList<User>();
		Cursor cursor = db.query(TABLE_USER_NAME, new String[] { KEY_USERID,
				KEY_USERNAME }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			userList.add(XmppTool.shareConnectionManager(context).getUser(
					cursor.getString(0)));
		}
		cursor.close();
		return userList;
	}
    public User getUserInfo(String userid){
    	User user=null;
    	Cursor cursor = db.query(TABLE_USER_NAME, new String[] { KEY_USERID,
				KEY_USERNAME,KEY_SEX,KEY_MOBILE,KEY_USERPHONE,KEY_USERUNIT,KEY_POSTION}, KEY_USERID+"='"+userid+"'", null, null, null, null);
    	if(cursor.moveToNext()){
    		user=new User();
    		user.setMobilePhone(cursor.getString(3));
    		user.setOrg(cursor.getString(5));
    		user.setPhoneNumber(cursor.getString(4));
    		user.setSex(cursor.getString(2));
    		user.setUserName(cursor.getString(1));
    		user.setPosition(cursor.getString(6));
    	}
		return user;
    	
    }
	/**
	 * 获取所有用户信息
	 * 
	 * @param type
	 *            返回类型1,2 1返回userid列表，2返回username列表
	 * @return
	 */
	public ArrayList<String> getAllUserInfo(int type) {
		// TODO Auto-generated method stub
		ArrayList<String> userList = new ArrayList<String>();
		Cursor cursor = db.query(TABLE_USER_NAME, new String[] { KEY_USERID,
				KEY_USERNAME }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			switch (type) {
			case 1:
				userList.add(cursor.getString(0));
				break;

			case 2:
				userList.add(cursor.getString(1));
				break;
			}

		}
		cursor.close();
		return userList;
	}

	public void deleteAllData() {
		db.delete(TABLE_USER_NAME, null, null);
		db.delete(TABLE_GROUP_NAME, null, null);
	}
	
}
