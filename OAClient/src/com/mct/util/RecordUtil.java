package com.mct.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mct.model.ChatMessage;
import com.mct.model.Conversation;

public class RecordUtil {
	// 聊天记录数据库名
	private static final String DB_NAME = "record.db";

	// ===========================会话表开始================================//
	// 会话表名
	private static final String TABLE_CONVERSATION = "conversation";

	// id字段
	private static final String KEY_ID = "_id";

	// 时间字段
	private static final String KEY_DATE = "date";

	// 最后一条消息内容
	private static final String KEY_SNIPPET = "snippet";

	private static final String KEY_ISGROUP = "isgroup";

	// 会话的当前用户（登录用户本身）
	private static final String KEY_LOGIN_USER = "login_user";

	// 会话的对方用户（跟谁的聊天）
	private static final String KEY_WITH_USER = "with_user";
	// 会话类型
	private static final String KEY_CONTYPE = "type";
	// 会话字段
	private static final String[] CONVERSATION_OBJECT = { KEY_ID, KEY_DATE,
			KEY_SNIPPET, KEY_LOGIN_USER, KEY_WITH_USER, KEY_ISGROUP,
			KEY_CONTYPE };

	// ===========================会话表结束================================//

	// ===========================信息表开始================================//
	// 信息表名
	private static final String TABLE_CHAT_MSG = "chat";

	// 会话id
	private static final String KEY_THREAD_ID = "thread_id";

	// 对方账号
	private static final String KEY_ADDRESS = "address";

	// 消息类型 流向
	private static final String KEY_TYPE = "type";
	// 是否已读，0未读，1已读
	private static final String KEY_READ = "read";

	// 内容
	private static final String KEY_BODY = "body";
	// 消息类型0聊天消息 1邮件 2通知 3系统推送 4流程
	private static final String KEY_MSGTYPE = "msgtype";

	// ===========================信息表结束================================//

	private static RecordUtil util;

	private DbHelper mDbHelper;

	private SQLiteDatabase db;

	private static Context mContext;

	private RecordUtil(Context context) {
		mDbHelper = new DbHelper(context);
	}

	public static RecordUtil shareRecordUtil(Context context) {
		mContext = context;
		if (util == null) {
			util = new RecordUtil(context);
		}
		return util;
	}

	private class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DB_NAME, null, 1);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String create_coversation_table = "create table conversation (_id integer primary key autoincrement ,"
					+ " date integer not null, snippet text not null ,"
					+ "login_user text not null , with_user text not null,isgroup integer not null,type integer not null);";
			String create_chat_table = "create table chat (_id integer primary key autoincrement, "
					+ "thread_id integer not null, address text not null, date integer not null ,"
					+ " read integer not null , isgroup integer not null,type integer not null , body text not null,msgtype integer not null);";
			db.execSQL(create_coversation_table);
			db.execSQL(create_chat_table);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 打开数据库
	 */
	public void openDatabase() {
		db = mDbHelper.getWritableDatabase();
	}

	/**
	 * 关闭数据库
	 */
	public void closeDatabase() {
		db.close();
		mDbHelper.close();
		db = null;
	}

	/**
	 * 获取登录用户会话列表
	 * 
	 * @param loginUser登录用户id
	 *            ， openfire用户id中'@'前面部分，如user001@aji传递user001即可
	 * @return
	 */
	public LinkedList<Conversation> getConversation(String loginUser) {
		Cursor cursor = db.query(TABLE_CONVERSATION, CONVERSATION_OBJECT,
				KEY_LOGIN_USER + "='" + loginUser + "'", null, null, null,
				KEY_DATE + " desc");
		Log.e("m_tag", "count:" + cursor.getCount());
		LinkedList<Conversation> list = null;
		if (cursor.getCount() > 0) {
			list = new LinkedList<Conversation>();
			while (cursor.moveToNext()) {
				list.add(getConversation(cursor));
			}
		}
		cursor.close();
		return list;
	}

	/**
	 * 根据会话id获取会话对象
	 * 
	 * @param _id
	 * @return
	 */
	public Conversation getConversation(long _id) {
		Cursor cursor = db.query(TABLE_CONVERSATION, CONVERSATION_OBJECT,
				KEY_ID + "=" + _id, null, null, null, KEY_DATE + " desc");
		Conversation c = null;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			c = getConversation(cursor);
		}
		cursor.close();
		return c;
	}

	/**
	 * 将Cursor对象转为Conversation对象
	 * 
	 * @param cursor
	 * @return
	 */
	private Conversation getConversation(Cursor cursor) {
		long _id = cursor.getLong(cursor.getColumnIndex(KEY_ID));
		long date = cursor.getLong(cursor.getColumnIndex(KEY_DATE));
		String snippet = cursor.getString(cursor.getColumnIndex(KEY_SNIPPET));
		String login_user = cursor.getString(cursor
				.getColumnIndex(KEY_LOGIN_USER));
		String with_user = cursor.getString(cursor
				.getColumnIndex(KEY_WITH_USER));
		int isgroup = cursor.getInt(cursor.getColumnIndex(KEY_ISGROUP));
		Conversation conversation = new Conversation(_id, date, snippet,
				getUnreadMessage(_id), login_user, with_user, isgroup,
				getMsgType(snippet));
		return conversation;
	}

	/**
	 * 获取会话id
	 * 
	 * @param loginUser登录用户名
	 * @param withUser会话的对方账号
	 * @param type会话类型
	 * @return
	 */
	public long getConversationID(String loginUser, String withUser, int type) {
		String[] projections = { KEY_ID };
		Cursor cursor = db
				.query(TABLE_CONVERSATION, projections, KEY_LOGIN_USER + "='"
						+ loginUser + "' AND " + KEY_WITH_USER + "='"
						+ withUser + "' AND " + KEY_CONTYPE + "=" + type, null,
						null, null, null);
		long id = -1;
		if (cursor.getCount() > 0) {
			cursor.moveToNext();
			id = cursor.getLong(cursor.getColumnIndex(KEY_ID));
		}
		cursor.close();
		return id;
	}

	/**
	 * 更新会话
	 * 
	 * @param loginUser当前登录账号
	 * @param withAccount对方用户
	 *            （群）账号
	 * @param snippet消息内容
	 * @param isGroup会话类型
	 *            0：单聊 1：群聊 -1通知
	 * @param Type消息类型
	 * @return [会话id,消息id]
	 */
	public long[] updateConversation(String loginUser, String withAccount,
			String snippet, int isGroup, int Type) {
		long[] id = new long[] { -1, -1 };
		int type = getMsgType(snippet);
		Log.e("msgtype", type + "");
		switch (type) {
		case 0:
			// 获取聊天会话
			id[0] = getConversationID(loginUser, withAccount, type);
			break;

		default:
			// 获取其他类型会话
			id[0] = isTypeExist(type);
			break;

		}
		if (type == 0 || Type == 1) {
			// 无会话
			if (id[0] == -1) {

				// 创建新会话
				id[0] = insertConversation(snippet, loginUser, withAccount,
						isGroup);
			} else {
				// 更新会话表
				String update_sql;
				long date = 0;
				switch (type) {
				case 0:
					date = System.currentTimeMillis();
					break;

				default:
					date = Long.valueOf(snippet.split("@")[1]);
					break;
				}

				update_sql = "update conversation set date=" + date
						+ ",snippet='" + snippet + "',type=" + type
						+ " where _id=" + id[0];
				db.execSQL(update_sql);
			}
		}
		if (id[0] != -1) {
			// 插入信息
			id[1] = insertChatMessage(id[0], withAccount,
					Type == Common.MSG_OUT ? 1 : 0, Type, snippet, isGroup);
			return id;
		} else {
			if (type != 0) {
				id[1] = insertChatMessage(-2, withAccount, 0, Type, snippet,
						isGroup);
				return id;
			}
		}
		return new long[] { -1, -1 };
	}

	public long isTypeExist(int type) {
		long id = -1;
		String[] projections = { KEY_ID };
		Cursor cursor = db.query(TABLE_CONVERSATION, projections, KEY_CONTYPE
				+ "=" + type, null, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			id = cursor.getLong(0);
		}
		cursor.close();
		return id;

	}

	/**
	 * 插入会话
	 * 
	 * @param snippet
	 * @param read
	 * @param login_user
	 * @param with_user
	 * @return
	 */
	public long insertConversation(String snippet, String login_user,
			String with_user, int isGroup) {
		ContentValues values = new ContentValues();
		long date = System.currentTimeMillis();
		values.put(KEY_DATE, date);
		values.put(KEY_SNIPPET, snippet);
		values.put(KEY_LOGIN_USER, login_user);
		values.put(KEY_WITH_USER, with_user);
		values.put(KEY_ISGROUP, isGroup);
		values.put(KEY_CONTYPE, getMsgType(snippet));
		long _id = db.insert(TABLE_CONVERSATION, null, values);
		// if (_id > 0) {
		// Conversation conversation = new Conversation(_id, date, 1, snippet,
		// read, login_user, with_user);
		// return conversation;
		// }
		return _id;
	}

	/**
	 * 功能描述：获取消息列表 Administrator 2014年7月19日 下午3:53:12
	 * 
	 * @param thread_id
	 *            会话id -2表示非聊天消息
	 * @param page
	 *            页数
	 * @param msgType
	 *            0聊天消息 1邮件 2通知 3系统推送 4流程
	 * @param orderBy
	 *            "date asc"或"date desc"
	 * @return
	 */
	public ArrayList<ChatMessage> getChatMessage(long thread_id, int page,
			int msgType, String orderBy) {
		String[] projections = { KEY_ID, KEY_THREAD_ID, KEY_ADDRESS, KEY_DATE,
				KEY_READ, KEY_TYPE, KEY_BODY, KEY_MSGTYPE, KEY_ADDRESS };
		String whereStr = "";
		if (thread_id == -2) {
			whereStr = KEY_MSGTYPE + "=" + msgType;
		} else {
			whereStr = KEY_THREAD_ID + "=" + thread_id + " AND " + KEY_MSGTYPE
					+ "=" + msgType;
		}
		Cursor cursor = db.query(TABLE_CHAT_MSG, projections, whereStr, null,
				null, null, orderBy, 10 * (page - 1) + "," + 10);
		ArrayList<ChatMessage> list = null;
		Log.e("query", "查询" + cursor.getCount() + "条记录");
		if (cursor.getCount() > 0) {
			list = new ArrayList<ChatMessage>();
			while (cursor.moveToNext()) {
				String address = cursor.getString(cursor
						.getColumnIndex(KEY_ADDRESS));
				long date = cursor.getLong(cursor.getColumnIndex(KEY_DATE));
				int type = cursor.getInt(cursor.getColumnIndex(KEY_TYPE));
				String body = cursor.getString(cursor.getColumnIndex(KEY_BODY));
				ChatMessage msg = new ChatMessage(address, date, type, body);
				list.add(msg);
			}
		}
		cursor.close();
		return list;
	}

	/**
	 * 获取指定id消息
	 * 
	 * @param msgId
	 * @return
	 */
	public ChatMessage getChatMessage(long msgId) {
		String[] projections = { KEY_ID, KEY_THREAD_ID, KEY_ADDRESS, KEY_DATE,
				KEY_READ, KEY_TYPE, KEY_BODY };
		Cursor cursor = db.query(TABLE_CHAT_MSG, projections, "_id=" + msgId,
				null, null, null, null, null);
		cursor.moveToPosition(0);
		String address = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS));
		long date = cursor.getLong(cursor.getColumnIndex(KEY_DATE));
		int type = cursor.getInt(cursor.getColumnIndex(KEY_TYPE));
		String body = cursor.getString(cursor.getColumnIndex(KEY_BODY));
		ChatMessage msg = new ChatMessage(address, date, type, body);
		cursor.close();
		return msg;
	}

	public boolean checkMsgExist(String body) {
		Cursor cursor = db.query(TABLE_CHAT_MSG, new String[] { KEY_BODY },
				"body='" + body + "' and msgtype>0", null, null, null, null,
				null);
		if (cursor.moveToNext()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据会话id获取未读信息数
	 * 
	 * @param thread_id
	 * @return
	 */
	public int getUnreadMessage(long thread_id) {
		Cursor cursor = db.query(TABLE_CHAT_MSG, null, KEY_THREAD_ID + "="
				+ thread_id + " AND read=0", null, null, null, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	/**
	 * 将一个会话中所有未读消息更新为已读消息
	 * 
	 * @param thread_id会话
	 */
	public void updateConversationMessage(long thread_id) {
		ContentValues values = new ContentValues();
		values.put(KEY_READ, 1);
		db.update(TABLE_CHAT_MSG, values, KEY_THREAD_ID + "=" + thread_id
				+ " AND " + KEY_READ + "=0", null);
	}

	/**
	 * 插入一条聊天信息
	 * 
	 * @param thread_id会话id
	 * @param address对方账号
	 * @param read读取标志
	 * @param type收发类型
	 * @param body内容
	 * @return
	 */
	public long insertChatMessage(long thread_id, String address, int read,
			int type, String body, int isGroup) {
		ContentValues values = new ContentValues();
		long date = System.currentTimeMillis();

		values.put(KEY_THREAD_ID, thread_id);
		values.put(KEY_ADDRESS, address);
		values.put(KEY_DATE, date);
		values.put(KEY_READ, read);
		values.put(KEY_TYPE, type);
		values.put(KEY_BODY, body);
		values.put(KEY_ISGROUP, isGroup);
		values.put(KEY_MSGTYPE, getMsgType(body));
		System.out.println("存入1条消息");
		return db.insert(TABLE_CHAT_MSG, null, values);

	}

	private int getMsgType(String body) {
		int msgType = 0;
		if (body.startsWith("[%email%]")) {
			msgType = 1;
		} else if (body.startsWith("[%news%]")) {
			msgType = 2;
		} else if (body.startsWith("[%system%]")) {
			msgType = 3;
		} else if (body.startsWith("[%flow%]")) {
			msgType = 4;
		}
		return msgType;
	}

	/**
	 * 删除单条消息
	 * 
	 * @param _id消息id
	 * @return
	 */
	public boolean deleteSingleMessage(long _id) {
		long row_id = db.delete(TABLE_CHAT_MSG, "_id=" + _id, null);
		if (row_id > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除整个会话
	 * 
	 * @param _id
	 *            会话id
	 * @return
	 */
	public boolean deleteConversation(long _id) {
		db.delete(TABLE_CHAT_MSG, KEY_THREAD_ID + "=" + _id, null);
		long rowID = db.delete(TABLE_CONVERSATION, KEY_ID + "=" + _id, null);
		if (rowID > 0){
			return true;
		}
		return false;
	}

	public LinkedList<HashMap<String, Object>> getMultiChatList(String loginUser) {
		LinkedList<HashMap<String, Object>> list = new LinkedList<HashMap<String, Object>>();
		Cursor cursor = db.query(TABLE_CONVERSATION, new String[] { KEY_ID,
				KEY_WITH_USER }, KEY_LOGIN_USER + "='" + loginUser + "' and "
				+ KEY_ISGROUP + "=1", null, null, null,KEY_DATE + " desc");
		while (cursor.moveToNext()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", cursor.getLong(0));

			String ids = cursor.getString(1).split("@")[0];
			map.put("members",idToName(ids));
			list.add(map);
		}
		return list;
	}

	public String idToName(String ids) {
		StringBuffer members = new StringBuffer();
		String[] users = ids.split("、");
		String loginUser=XmppTool.loginUser.getUserId();
		for (int i = 0; i < users.length; i++) {
			if (i < 3) {
				if(!users[i].equals(loginUser)){
				members.append(UserDbUtil.shareUserDb(mContext)
						.getUserNameById(users[i]));
				}else{
					SharedPreferences settings=mContext.getSharedPreferences("setting_info", 0);
					members.append(settings.getString("username", ""));
				}
				members.append("、");
			}
		}
		if (users.length >3) {
			members.append("...");
		} else {
			members.replace(members.length() - 1, members.length(), "");
		}
		members.append("(");
		members.append(users.length);
		members.append("人)");
		return members.toString();
	}
}
