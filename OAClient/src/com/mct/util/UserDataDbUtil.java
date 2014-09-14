package com.mct.util;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.mct.model.CustomMessage;
import com.mct.model.Note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDataDbUtil {
	private SQLiteDatabase db;
	private SQLiteOpenHelper helper;
	private static UserDataDbUtil userDataDbUtil;
	
	private static final String DB_NAME = "userdata";
	private static final String TABLE_LOCATION_NAME = "location";
	
	/* ----------------------------email表--------------------------*/
	private static final String TABLE_EMAIL = "email";
	private static final String KEY_EMAILID = "id";
	private static final String KEY_EMAILTITLE = "title";
	private static final String KEY_EMAILRECEIVER = "receiver";
	private static final String KEY_EMAILCONTENT = "content";
	private static final String KEY_EMAILACCESSORY = "accessory";
	private static final String KEY_EMAILTIME = "time";
	private static final String KEY_EMAIUSERID = "userid";
	private static final String KEY_EMAIISESENDED = "issended";
	
	/* ----------------------------记事本--------------------------*/
	private static final String TABLE_NOTE = "note";
	private static final String KEY_NOTEID = "id";
	private static final String KEY_NOTEUSERID = "userid";
	private static final String KEY_NOTETITLE = "title";
	private static final String KEY_NOTECONTENT = "content";
	private static final String KEY_NOTETIME = "time";
	private static final String KEY_NOTEDOTIMESTART = "dotimestart";
	private static final String KEY_NOTEDOTIMEEND = "dotimeend";
	private static final String KEY_NOTENOTIFICATION = "notification";
	private UserDataDbUtil(Context context) {
		helper = new DbHelper(context);
		openDatabase();
	}

	public static UserDataDbUtil shareUserDataDb(Context context) {
		if (userDataDbUtil == null) {
			userDataDbUtil = new UserDataDbUtil(context);
		}
		return userDataDbUtil;
	}

	private class DbHelper extends SQLiteOpenHelper {
		public DbHelper(Context context) {
			super(context, DB_NAME, null, 1);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase mdb) {
			// TODO Auto-generated method stub
			final String TABLE_LOCATION_CREATE = "create table location (_id integer primary key autoincrement, "
					+ "userid text not null, loc text not null, coordinate text not null,time text not null);";
			final String TABLE_EMAIL_CREATE = "create table email (id integer primary key autoincrement, "
					+ " sender text not null,title text not null, receiver text not null,content text not null,"
					+ "accessory text,time integer not null,issended integer not null);";
			final String TABLE_NOTE_CREATE = "create table note (id integer primary key autoincrement, "
					+ " userid text not null,title text not null, content text not null,time integer not null,"
					+ "dotimestart integer not null,dotimeend integer not null,notification integer not null,"
					+ "voice integer not null,shock integer not null);";
			mdb.execSQL(TABLE_LOCATION_CREATE);
			mdb.execSQL(TABLE_EMAIL_CREATE);
			mdb.execSQL(TABLE_NOTE_CREATE);
			
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
	 * 功能描述：插入定位数据到数据表
	 * Administrator
	 * 2014年7月20日 下午9:16:50
	 * @param userId
	 * @param loc
	 * @param coordinate
	 * @param time
	 * @return
	 */
	public long insertLocation(String userId,String loc,String  coordinate,String time){
		ContentValues values=new ContentValues();
		values.put("userid", userId);
		values.put("loc", loc);
		values.put("coordinate", coordinate);
		values.put("time", time);
		return db.insert(TABLE_LOCATION_NAME, null, values);
	}
	
	
	public LinkedList<HashMap<String, String>> getLocationList(String userId,int page){
		Cursor cursor=db.query(TABLE_LOCATION_NAME, new String[]{"loc","coordinate","time"},
				"userid='"+userId+"'", null, null, null, "time desc",(page-1)*10+",10");
		LinkedList<HashMap<String, String>> list=new LinkedList<HashMap<String,String>>();
		while(cursor.moveToNext()){
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("loc", cursor.getString(0));
			map.put("coordinate", cursor.getString(1));
			map.put("time", cursor.getString(2));
			list.add(map);
		}
		return list;
	}
	
	public long insertEmail(String sender,String title,String content,String accessory,long time,String receiver,int issended){
		ContentValues values=new ContentValues();
		values.put("sender", sender);
		values.put("title", title);
		values.put("content", content);
		values.put("accessory", accessory);
		values.put("time", time);
		values.put("receiver", receiver);
		values.put("issended", issended);
		return db.insert(TABLE_EMAIL, null, values);
	}
	
	public void update(long id,String title,String content,String accessory,long time,String receiver,int issended){
		ContentValues values=new ContentValues();
		values.put("title", title);
		values.put("content", content);
		values.put("accessory", accessory);
		values.put("time", time);
		values.put("receiver", receiver);
		values.put("issended", issended);
		db.update(TABLE_EMAIL, values, KEY_EMAILID+"="+id, null);
	}
	
	
	public LinkedList<CustomMessage> queryMessageList(String userid,int isSended,int page){
		LinkedList<CustomMessage> list=new LinkedList<CustomMessage>();
		Cursor cursor=db.query(TABLE_EMAIL, new String[]{KEY_EMAILID,KEY_EMAILRECEIVER,KEY_EMAILTITLE,KEY_EMAILCONTENT,KEY_EMAILACCESSORY,KEY_EMAILTIME},
				"sender='"+userid+"' and issended="+isSended,null, null, null, "time desc",10*(page-1)+",10");
		while(cursor.moveToNext()){
			CustomMessage customMessage=new CustomMessage(); 
			customMessage.setId(cursor.getLong(0));
//			customMessage.setReceiver(cursor.getString(1));
			customMessage.setTitle(cursor.getString(2));
			customMessage.setContent(cursor.getString(3));
//			customMessage.setAccessory(cursor.getString(4));
			customMessage.setTime(cursor.getLong(5));
			list.add(customMessage);
		}
		return list;
		
	}
	
	public  CustomMessage getCustomMessage(long id){
		CustomMessage customMessage=new CustomMessage(); 
		Cursor cursor=db.query(TABLE_EMAIL, new String[]{KEY_EMAILRECEIVER,KEY_EMAILACCESSORY},
				"id="+id,null, null, null, null);
		if(cursor.moveToNext()){
			customMessage.setReceiver(cursor.getString(0));
			customMessage.setAccessory(cursor.getString(1));
		}
		return customMessage;
	}
	
	public  int deleteCustomMessage(long id){
		
		return db.delete(TABLE_EMAIL, "id="+id, null);
	}
	
	
	/**
	 * 插入新的记事
	 * @param title 标题
	 * @param content 内容
	 * @param dotime 执行时间
	 * @param isNotification 是否通知提醒
	 * @return
	 */
	public long insertNewNote(String userId,String title,String content,long dotimeStart,long dotimeEnd,boolean isNotification,
			boolean voice,boolean shock){
		ContentValues values=new ContentValues();
		values.put(KEY_NOTEUSERID, userId);
		values.put(KEY_NOTETITLE, title);
		values.put(KEY_NOTECONTENT, content);
		values.put(KEY_NOTETIME, System.currentTimeMillis());
		values.put(KEY_NOTEDOTIMESTART, dotimeStart);
		values.put(KEY_NOTEDOTIMEEND, dotimeEnd);
		values.put(KEY_NOTENOTIFICATION, isNotification?1:0);
		values.put("voice", voice?1:0);
		values.put("shock", shock?1:0);
		return db.insert(TABLE_NOTE, null, values);
		
	}
	
	
	public boolean modifyNote(long id,String title,String content,long dotimeStart,long dotimeEnd,boolean isNotification,
			boolean voice,boolean shock){
		ContentValues values=new ContentValues();
		values.put(KEY_NOTETITLE, title);
		values.put(KEY_NOTECONTENT, content);
		values.put(KEY_NOTETIME, System.currentTimeMillis());
		values.put(KEY_NOTEDOTIMESTART, dotimeStart);
		values.put(KEY_NOTEDOTIMEEND, dotimeEnd);
		values.put(KEY_NOTENOTIFICATION, isNotification?1:0);
		values.put("voice", voice?1:0);
		values.put("shock", shock?1:0);
		return db.update(TABLE_NOTE, values, "id="+id, null)>0;
	}
	
	/**
	 * 删除指定id的记事
	 * @param id
	 * @return
	 */
	public boolean deleteNote(long id){
		return db.delete(TABLE_NOTE,"id="+id, null)>0;		
	}
	
	public Note getNote(long id){
		Note note=new Note();
		Cursor cursor=db.query(TABLE_NOTE, new String[]{KEY_NOTEID,KEY_NOTETITLE,KEY_NOTECONTENT,
				KEY_NOTETIME,KEY_NOTEDOTIMESTART,KEY_NOTEDOTIMEEND,KEY_NOTENOTIFICATION,"voice","shock"},"id="+id, null, null, null, null);
		if(cursor.moveToNext()){
			note.setId(cursor.getLong(0));
			note.setTitle(cursor.getString(1));
			note.setContent(cursor.getString(2));
			note.setTime(cursor.getLong(3));
			note.setDotimeStart(cursor.getLong(4));
			note.setDotimeEnd(cursor.getLong(5));
			note.setNotification(cursor.getInt(6)==1?true:false);
			note.setVoice(cursor.getInt(7)==1?true:false);
			note.setShock(cursor.getInt(8)==1?true:false);
		}
		return note;
	}
	
	
	public ArrayList<HashMap<String, Object>> getNoteList(String userId,String date){
		ArrayList<HashMap<String, Object>> noteList=new ArrayList<HashMap<String, Object>>();	
		long[] between=TimeRender.getDayBetween(date);
		Log.e("between", between[0]+","+between[1]);
		Cursor cursor=db.query(TABLE_NOTE,new String[]{KEY_NOTEID,KEY_NOTEDOTIMESTART,KEY_NOTEDOTIMEEND,KEY_NOTETITLE},"userid='"+userId+"' and dotimestart>="+between[0]+" and dotimestart<="+between[1],
				null,null,null,null);
		int i=1;
		while(cursor.moveToNext()){
			HashMap<String, Object> note=new HashMap<String, Object>();
			note.put("id",cursor.getLong(0));
			note.put("title",cursor.getString(3));
			note.put("dotimestart",TimeRender.getDate(cursor.getLong(1), "HH:mm"));
			note.put("dotimeend",TimeRender.getDate(cursor.getLong(2), "HH:mm"));
			note.put("postion",i++);
			noteList.add(note);
			
		}
		return noteList;
	}
}
