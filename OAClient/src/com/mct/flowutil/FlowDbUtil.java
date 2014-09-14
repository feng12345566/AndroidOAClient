package com.mct.flowutil;

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

import com.mct.flow.Flow;
import com.mct.flow.FlowClass;
import com.mct.flow.FlowNode;
import com.mct.flow.Widget;
import com.mct.model.ChatMessage;
import com.mct.model.Conversation;
import com.mct.model.User;
import com.mct.util.Common;
import com.mct.util.RecordUtil;
import com.mct.util.UserDbUtil;
import com.mct.util.XmppTool;

public class FlowDbUtil {
	// �������ݿ���

	private static final String DB_NAME = "flow.db";

	// ===========================���̱�ű�ʼ================================//
	private static final String TABLE_FLOWNO = "flowno";
	private static final String KEY_FLOWNO = "flowno";// ���̱��
	private static final String KEY_NAMEOFFLOW = "flowname";// ��������
	private static final String CREATE_FLOWNO = "create table  flowno (_id integer primary key autoincrement,flowno integer not null,"
			+ "flowname text not null);";// �������
	// ===========================���̱�ʼ================================//
	// ���̱���
	private static final String TABLE_FLOW = "flow";
	// ���̱��ֶ�
	private static final String KEY_FLOWID = "flowid";// ����id
	private static final String KEY_NOOFFLOW = "flowno";// ���̱��
	private static final String KEY_FLOWTYPEOFID = "flowtypeofid";// ��������id
	private static final String KEY_FLOWVERSION = "flowversion";// ���̰汾
	private static final String KEY_LASTVERSION = "lastversion";// �Ƿ������°汾
	private static final String[] FLOW = { KEY_FLOWID, KEY_NOOFFLOW,
			KEY_FLOWTYPEOFID };
	private static final String CREATE_FLOW = "create table  flow (_id integer primary key autoincrement,flowid integer not null ,"
			+ " flowno integer not null,flowtypeofid integer not null,flowversion integer not null,lastversion integer not null);";// �������
	// ===========================���̱����================================//
	// ===========================���̽ڵ��ʼ================================//
	// ���̽ڵ����
	private static final String TABLE_FLOWNODE = "flownode";
	// ���̽ڵ���ֶ�
	private static final String KEY_FLOWNODEID = "flownodeid";// �ڵ�id
	private static final String KEY_FLOWOFID = "flowofid";// ��������id
	private static final String KEY_FLOWNODENO = "flownodeno";// ���������еı��
	private static final String KEY_FLOWNODENAME = "flownodename";// �ڵ�����
	private static final String KEY_FLOWNEXTNODE = "nextnode";// �����ڵ�no
	private static final String KEY_FLOWNODEUSERID = "userid";// �ɴ�����id
	private static final String KEY_FLOWENDTAG = "endtag";// �������

	private static final String[] FLOWNODE = { KEY_FLOWNODEID, KEY_FLOWOFID,
			KEY_FLOWNODENO, KEY_FLOWNODENAME, KEY_FLOWNEXTNODE,
			KEY_FLOWNODEUSERID, KEY_FLOWENDTAG };
	private static final String CREATE_FLOWNODE = "create table  flownode (_id integer primary key autoincrement,flownodeid integer not null ,"
			+ " flowofid integer not null,flownodeno integer not null,flownodename text not null,nextnode text,userid text,endtag integer not null);";// �������
	// ===========================���̽ڵ�����================================//
	// ===========================���̿ؼ���ʼ================================//
	// ���̿ؼ�����
	private static final String TABLE_FLOWWIDGET = "widget";
	// ���̿ؼ����ֶ�
	private static final String KEY_WIDGETID = "widgetid";// �ؼ�id
	private static final String KEY_ISFLOWID = "flowid";// ����id
	private static final String KEY_NODEID = "nodeid";// �ɱ༭�ڵ�id
	private static final String KEY_WIDGETYPE = "widgettype";// �ؼ�����
	private static final String KEY_WIDGETITLE = "widgettitle";// �ؼ�����
	private static final String KEY_WIDGEITEM = "widgetitem";// �ؼ���ѡ��
	private static final String KEY_WIDGEVALUE = "widgetvalue";// �ؼ�ֵ
	private static final String KEY_WIDGECANNULL = "widgetcannull";// �ؼ��Ƿ�ɿ�
	private static final String[] FLOWWIDGET = { KEY_WIDGETID, KEY_ISFLOWID,
			KEY_NODEID, KEY_WIDGETYPE, KEY_WIDGETITLE, KEY_WIDGEITEM,
			KEY_WIDGEVALUE, KEY_WIDGECANNULL };
	private static final String CREATE_FLOWWIDGET = "create table  widget (_id integer primary key autoincrement,widgetid integer not null ,"
			+ "flowid integer not null,nodeid integer not null,widgettype integer not null,widgettitle text not null,widgetitem text,widgetvalue text,"
			+ "widgetcannull integer not null);";// �������
	// ===========================���̱����================================//
	// ===========================����ʵ����ʼ================================//
	// ����ʵ������
	private static final String TABLE_PROJECT = "project";
	// ����ʵ�����ֶ�
	private static final String KEY_PROJECTID = "projectid";// ����ʵ��id
	private static final String KEY_PROJECTFLOWID = "flowid";// ����id
	private static final String KEY_PROJECTTITLE = "title";// ��������
	private static final String KEY_PROJECTFLOWTYPE = "flowtype";// ��������
	private static final String KEY_PROJECTNODEID = "nodeid";// �ɱ༭�ڵ�id
	private static final String KEY_PROJECTSTATUS = "status";// �������
	private static final String KEY_PROJECTTIME = "time";// ����ʱ��
	private static final String[] PROJECT = { KEY_PROJECTID, KEY_PROJECTFLOWID,
			KEY_PROJECTTITLE, KEY_PROJECTFLOWTYPE, KEY_PROJECTNODEID,
			KEY_PROJECTSTATUS, KEY_PROJECTTIME };
	private static final String CREATE_PROJECT = "create table  project (_id integer primary key autoincrement,"
			+ "projectid integer not null,flowid integer not null,title text not null,flowtype integer not null,nodeid integer not null,"
			+ "status integer not null,time integer not null);";// �������
	// ===========================����ʵ������================================//

	private static FlowDbUtil flowDbUtil;

	private DbHelper mDbHelper;

	private SQLiteDatabase db;

	private static Context mContext;
	private static boolean isFristRun = false;

	private FlowDbUtil(Context context) {

		mDbHelper = new DbHelper(context);
		openDatabase();
	}

	public static FlowDbUtil shareFlowUtil(Context context) {
		mContext = context;
		if (flowDbUtil == null) {
			flowDbUtil = new FlowDbUtil(context);
		}
		return flowDbUtil;
	}

	private class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DB_NAME, null, 1);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_FLOWNO);
			db.execSQL(CREATE_FLOW);
			db.execSQL(CREATE_FLOWNODE);
			db.execSQL(CREATE_FLOWWIDGET);
			db.execSQL(CREATE_PROJECT);
			isFristRun = true;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			// db.execSQL(drop table );
		}

	}

	/**
	 * �����ݿ�
	 */
	public void openDatabase() {
		db = mDbHelper.getWritableDatabase();
	}

	/**
	 * �ر����ݿ�
	 */
	public void closeDatabase() {
		db.close();
		mDbHelper.close();
		db = null;
	}



	/**
	 * @param typeId
	 *            ����id
	 * @return ��Ӧ����id��������������
	 */
	public ArrayList<Flow> getFlowByType(int typeId) {
		ArrayList<Flow> flowList = new ArrayList<Flow>();
		Cursor cursor = db.query(TABLE_FLOW, FLOW, KEY_FLOWTYPEOFID + "="
				+ typeId + " and " + KEY_LASTVERSION + "=1", null, null, null,
				null);
		while (cursor.moveToNext()) {
			Flow flow = new Flow();
			long no = cursor.getLong(1);
			flow.setFlowNo(no);
			flow.setFlowName(getFlowNameByNo(no));
			flow.setFlowClass(typeId);
			flow.setFlowId(cursor.getLong(0));
			flowList.add(flow);
		}
		return flowList;

	}

	/**
	 * 
	 * @return ��������
	 */
	public ArrayList<Flow> getAllFlow() {
		ArrayList<Flow> flowList = new ArrayList<Flow>();
		Cursor cursor = db.query(TABLE_FLOW, FLOW, KEY_LASTVERSION + "=1",
				null, null, null, null);
		while (cursor.moveToNext()) {
			Flow flow = new Flow();
			long no = cursor.getLong(1);
			flow.setFlowNo(no);
			flow.setFlowName(getFlowNameByNo(no));
			flow.setFlowClass(cursor.getInt(2));
			flow.setFlowId(cursor.getLong(0));
			flowList.add(flow);
		}
		return flowList;

	}

	/**
	 * @param flowNo
	 * @return
	 */
	public String getFlowNameByNo(long flowNo) {
		String result = "";
		Cursor cursor = db.query(TABLE_FLOWNO, new String[] { KEY_NAMEOFFLOW },
				KEY_FLOWNO + "=" + flowNo, null, null, null, null);
		if (cursor.moveToNext()) {
			result = cursor.getString(0);
		}
		cursor.close();
		return result;
	}

	/**
	 * @param flowId
	 * @return
	 */
	public String getFlowNameById(long flowId) {
		String result = "";
		Cursor cursor = db.query(TABLE_FLOW, new String[] { KEY_NOOFFLOW },
				KEY_FLOWID + "=" + flowId, null, null, null, null);
		if (cursor.moveToNext()) {
			result = getFlowNameByNo(cursor.getLong(0));
		}
		cursor.close();
		return result;
	}

	/**
	 * ������������������id��ȡ�������� Administrator 2014��7��7�� ����11:00:48
	 * 
	 * @param flowId
	 * @return
	 */
	public int getFlowTypeById(long flowId) {
		int result = -1;
		Cursor cursor = db.query(TABLE_FLOW, new String[] { KEY_FLOWTYPEOFID },
				KEY_FLOWID + "=" + flowId, null, null, null, null);
		if (cursor.moveToNext()) {
			result = cursor.getInt(0);
		}
		cursor.close();
		return result;
	}

	/**
	 * @param flowName
	 * @return
	 */
	public long insertDataToFlowNo(long flowNo, String flowName) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_FLOWNO, flowNo);
		contentValues.put(KEY_NAMEOFFLOW, flowName);
		return db.insert(TABLE_FLOWNO, null, contentValues);
	}

	/**
	 * @param flowNo
	 * @param flowTypeId
	 * @param flowVersion
	 * @param isLastVersion
	 * @return
	 */
	public long insertDataToFlow(long flowId, long flowNo, long flowTypeId,
			int flowVersion, int isLastVersion) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_FLOWID, flowId);
		contentValues.put(KEY_FLOWNO, flowNo);
		contentValues.put(KEY_FLOWTYPEOFID, flowTypeId);
		contentValues.put(KEY_FLOWVERSION, flowVersion);
		contentValues.put(KEY_LASTVERSION, isLastVersion);
		return db.insert(TABLE_FLOW, null, contentValues);
	}

	/**
	 * @param id
	 * @return
	 */
	public ArrayList<Widget> getWidgetByFlowID(long flowId) {
		ArrayList<Widget> list = new ArrayList<Widget>();
		Cursor cursor = db.query(TABLE_FLOWWIDGET, FLOWWIDGET, KEY_ISFLOWID
				+ "=" + flowId, null, null, null, null);
		while (cursor.moveToNext()) {
			Widget widget = new Widget(mContext, (int) cursor.getLong(0),
					cursor.getLong(2), cursor.getString(4), cursor.getInt(3),
					cursor.getString(5), cursor.getString(6), cursor.getInt(7));
			list.add(widget);
		}
		cursor.close();
		return list;
	}

	/**
	 * ͨ�����̵�id��ȡ��nodeNo���ڵ��id
	 * 
	 * @param flowId
	 *            ����id
	 * @param nodeNo
	 *            �ڵ���
	 * @return �ڵ�id
	 */
	public FlowNode getNode(long flowId, int nodeNo) {
		FlowNode flowNode = null;
		Cursor cursor = db.query(TABLE_FLOWNODE, FLOWNODE, KEY_FLOWOFID + "="
				+ flowId + " and " + KEY_FLOWNODENO + "=" + nodeNo, null, null,
				null, null);
		if (cursor.moveToNext()) {
			flowNode = new FlowNode(mContext, cursor.getLong(0), flowId,
					nodeNo, cursor.getString(3), cursor.getString(4),
					cursor.getString(5), cursor.getInt(6));
		}
		cursor.close();
		return flowNode;

	}

	public ArrayList<FlowNode> getNextNode(long flowId, int nodeNo) {
		ArrayList<FlowNode> nextNodeList = new ArrayList<FlowNode>();
		FlowNode flowNode = null;
		Cursor cursor = db.query(TABLE_FLOWNODE, FLOWNODE, KEY_FLOWOFID + "="
				+ flowId + " and " + KEY_FLOWNODENO + "=" + nodeNo, null, null,
				null, null);
		if (cursor.moveToNext()) {
			String[] nextNodeNo = cursor.getString(4).split(",");
			for (int i = 0; i < nextNodeNo.length; i++) {
				Cursor cursor2 = db.query(TABLE_FLOWNODE, FLOWNODE,
						KEY_FLOWOFID + "=" + flowId + " and " + KEY_FLOWNODENO
								+ "=" + nextNodeNo[i], null, null, null, null);
				if (cursor2.moveToNext()) {
					flowNode = new FlowNode(mContext, cursor2.getLong(0),
							flowId, Integer.valueOf(nextNodeNo[i]),
							cursor2.getString(3), cursor2.getString(4),
							cursor2.getString(5), cursor2.getInt(6));
					nextNodeList.add(flowNode);
					flowNode = null;
				}
				cursor2.close();

			}
		}
		cursor.close();

		return nextNodeList;

	}

	/**
	 * ͨ�����̵�id��ȡ��nodeNo���ڵ��id
	 * 
	 * @param flowId
	 *            ����id
	 * @param nodeNo
	 *            �ڵ���
	 * @return �ڵ�id
	 */
	public FlowNode getNode(long nodeId) {
		FlowNode flowNode = null;
		Cursor cursor = db.query(TABLE_FLOWNODE, FLOWNODE, KEY_FLOWNODEID + "="
				+ nodeId, null, null, null, null);
		if (cursor.moveToNext()) {
			flowNode = new FlowNode(mContext, cursor.getLong(0),
					cursor.getLong(1), cursor.getInt(2), cursor.getString(3),
					cursor.getString(4), cursor.getString(5), cursor.getInt(6));
		}
		return flowNode;

	}

	/**
	 * @param flowId
	 *            ����id
	 * @param nodeNo
	 *            �ڵ���
	 * @return �û��б�
	 */
	public ArrayList<User> getUserList(long flowId, int nodeNo) {
		ArrayList<User> userList = new ArrayList<User>();
		FlowNode flowNode = getNode(flowId, nodeNo);
		String userId = flowNode.getUserId();
		Log.e("user", userId + "");
		if (userId == null) {
			userList = UserDbUtil.shareUserDb(mContext).getAllUser();
		} else {
			userList.clear();
			String[] userIdStr = userId.split(",");
			for (int i = 0; i < userIdStr.length; i++) {
				userList.add(XmppTool.shareConnectionManager(mContext).getUser(
						userIdStr[i]));
			}

		}
		return userList;

	}

	/**
	 * @param flowId
	 *            ����id
	 * @param nodeNo
	 *            �ڵ���
	 * @param type
	 *            1����userid�б�2����username�б�
	 * @return �û��б�
	 */
	public ArrayList<String> getUserInfoList(long flowId, int nodeNo, int type) {
		ArrayList<String> userList = new ArrayList<String>();
		FlowNode flowNode = getNode(flowId, nodeNo);
		String userId = flowNode.getUserId();
		Log.e("user", userId + "");
		if (userId == null) {
			userList = UserDbUtil.shareUserDb(mContext).getAllUserInfo(type);
		} else {
			userList.clear();
			String[] userIdStr = userId.split(",");
			switch (type) {
			case 1:
				for (int i = 0; i < userIdStr.length; i++) {
					userList.add(userIdStr[i]);
				}
				break;

			case 2:
				for (int i = 0; i < userIdStr.length; i++) {
					String userName = UserDbUtil.shareUserDb(mContext)
							.getUserNameById(userIdStr[i]);
					userList.add(userName);
				}
				break;

			}

		}
		return userList;

	}

	/**
	 * @param flowId
	 * @param nodeNo
	 * @param nodeName
	 * @param nextNode
	 * @param userId
	 * @param endTag
	 * @return
	 */
	public long insertDataToFlowNode(long nodeId, long flowId, int nodeNo,
			String nodeName, String nextNode, String userId, int endTag) {
		ContentValues values = new ContentValues();
		values.put(KEY_FLOWNODEID, nodeId);
		values.put(KEY_FLOWOFID, flowId);
		values.put(KEY_FLOWNODENO, nodeNo);
		values.put(KEY_FLOWNODENAME, nodeName);
		values.put(KEY_FLOWNEXTNODE, nextNode);
		values.put(KEY_FLOWNODEUSERID, userId);
		values.put(KEY_FLOWENDTAG, endTag);
		return db.insert(TABLE_FLOWNODE, null, values);
	}

	/**
	 * @param flowId
	 * @param nodeId
	 * @param widgetType
	 * @param widgetTitle
	 * @param item
	 * @param value
	 * @param canNull
	 * @return
	 */
	public long insertDataToWidget(long widgetId, long flowId, long nodeId,
			int widgetType, String widgetTitle, String item, String value,
			int canNull) {
		ContentValues values = new ContentValues();
		values.put(KEY_WIDGETID, widgetId);
		values.put(KEY_ISFLOWID, flowId);
		values.put(KEY_NODEID, nodeId);
		values.put(KEY_WIDGETYPE, widgetType);
		values.put(KEY_WIDGETITLE, widgetTitle);
		values.put(KEY_WIDGEITEM, item);
		values.put(KEY_WIDGEVALUE, value);
		values.put(KEY_WIDGECANNULL, canNull);
		return db.insert(TABLE_FLOWWIDGET, null, values);
	}

	/**
	 * �����������������ݵ�����ʵ���� Administrator 2014��7��7�� ����10:22:42
	 * 
	 * @param projectId
	 * @param flowId
	 * @param nodeId
	 * @param status
	 * @return
	 */
	public long insertDataToProject(long projectId, long flowId, String title,
			long nodeId, int status) {
		ContentValues values = new ContentValues();
		values.put(KEY_PROJECTID, projectId);
		values.put(KEY_PROJECTFLOWID, flowId);
		values.put(KEY_PROJECTTITLE, title);
		values.put(KEY_PROJECTFLOWTYPE, getFlowTypeById(flowId));
		values.put(KEY_PROJECTNODEID, nodeId);
		values.put(KEY_PROJECTSTATUS, status);
		values.put(KEY_PROJECTTIME, System.currentTimeMillis());
		return db.insert(TABLE_PROJECT, null, values);
	}

	/**
	 * ��������������ʵ��������� Administrator 2014��7��7�� ����10:24:16
	 * 
	 * @param status
	 *            ����״̬ ��1δ���� 2�Ѵ���
	 */
	public void updataStatus(long projectId, int status) {
		ContentValues values = new ContentValues();
		values.put(KEY_PROJECTSTATUS, status);
		values.put(KEY_PROJECTTIME, System.currentTimeMillis());
		db.update(TABLE_PROJECT, values, "projectid=" + projectId, null);
	}

	/**
	 * ������������ѯ����ʵ�� Administrator 2014��7��7�� ����10:48:40
	 * 
	 * @param status
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> queryProject(int flowType,
			int status) {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String whereStr = KEY_PROJECTSTATUS + "=" + status;
		if (flowType != 0) {
			whereStr = whereStr + " and flowtype=" + flowType;
		}
		Cursor cursor = db.query(TABLE_PROJECT, PROJECT, whereStr, null, null,
				null, "time desc");
		while (cursor.moveToNext()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("projectid", cursor.getLong(0));
			map.put("flowid", cursor.getLong(1));
			map.put("flowname", cursor.getString(2));
			map.put("nodeid", cursor.getLong(3));
			map.put("time", cursor.getLong(4));
			list.add(map);
			map = null;
		}
		cursor.close();
		return list;

	}
}
