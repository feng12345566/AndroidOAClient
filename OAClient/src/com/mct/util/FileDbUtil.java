package com.mct.util;

import java.util.HashMap;
import java.util.LinkedList;

import com.mct.model.TransferredFile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FileDbUtil {
	private Context context;
	private static final String DBNAME = "fileload";//数据库名称
	private MyDbHelper helper;
	private static FileDbUtil fileDbUtil;
	private SQLiteDatabase db;
	private static final String KEY_ID="_id";//数据id
	private static final String KEY_FILENAME="filename";//上传或下载的文件名
	private static final String KEY_LOCALPATH="loacalpath";//文件的本地路径
	private static final String KEY_REMOTEPATH="remotepath";//文件的ftp路径
	private static final String KEY_TYPE="type";//操作类型 1上传，2下载
	private static final String KEY_SIZE="size";//操作文件的大小
	private static final String KEY_LENGTH="length";//已完成的大小
	private static final String KEY_ISEND="end";//操作是否完成 0未完成 1完成
	private static final String KEY_TIME="time";//插入时间
	private static final String[] FILE=new String[]{KEY_ID,KEY_FILENAME,KEY_LOCALPATH,KEY_REMOTEPATH,
		KEY_TYPE,KEY_SIZE,KEY_LENGTH,KEY_ISEND};
	
	private static final String TABLE_NAME="file";//表名
	
	private static final String TABLE_CREATE="create table file (_id integer primary key autoincrement, "
					+ "filename text not null, loacalpath text not null, remotepath text not null,"
					+ "type integer not null,size integer not null,length integer not null,"
					+ "end integer not null,time integer not null);";

	private FileDbUtil(Context context) {
		this.context = context;
		helper = new MyDbHelper(context);
		openDatabase();
	}

	public static FileDbUtil getInstance(Context context) {
		if (fileDbUtil == null) {
			fileDbUtil = new FileDbUtil(context);
		}
		return fileDbUtil;
	}

	private void openDatabase() {
		// TODO Auto-generated method stub
		db = helper.getWritableDatabase();
	}

	private class MyDbHelper extends SQLiteOpenHelper {

		public MyDbHelper(Context context) {
			super(context, DBNAME, null, 1);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase arg0) {
			// TODO Auto-generated method stub
			arg0.execSQL(TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub

		}

	}
	
	/**
	 * 功能描述：插入新的数据传输记录到数据库
	 * Administrator
	 * 2014年7月11日 下午4:43:20
	 * @param fileName
	 * @param localPath
	 * @param remotePath
	 * @param type
	 * @param length
	 * @param size
	 * @param end
	 * @param rename
	 * @return
	 */
	public long insertFileLoadHistory(String fileName,String localPath,String remotePath,
			int type,long length,long size,int end ){
		ContentValues values=new ContentValues();
		values.put(KEY_FILENAME, fileName);
		values.put(KEY_LOCALPATH, localPath);
		values.put(KEY_REMOTEPATH, remotePath);
		values.put(KEY_TYPE, type);
		values.put(KEY_SIZE, size);
		values.put(KEY_LENGTH, length);
		values.put(KEY_ISEND, end);
		values.put(KEY_TIME, System.currentTimeMillis());
		return db.insert(TABLE_NAME, null, values);
		
	}
	
	public long insertFileLoadHistory(TransferredFile transferredFile){
		long id=insertFileLoadHistory(transferredFile.getFileName(),transferredFile.getLoacalPath(),
				transferredFile.getRemotePath(),transferredFile.getType(),transferredFile.getLength(),transferredFile.getFileSize(),
				0);
		transferredFile.setId(id);
		return id;
	}
	
	/**
	 * 功能描述：删除传输记录
	 * Administrator
	 * 2014年7月11日 下午4:51:26
	 * @param id
	 */
	public void deleteFileLoadHistory(long id){
		db.delete(TABLE_NAME, KEY_ID+"="+id, null);
	}
	
	/**
	 * 功能描述：更新数据传输长度
	 * Administrator
	 * 2014年7月11日 下午5:01:08
	 * @param id
	 * @param length
	 * @param isEnd
	 */
	public void updataFileLoadHistory(long id,long length,int isEnd){
		ContentValues values=new ContentValues();
		values.put(KEY_LENGTH,length);
		values.put(KEY_ISEND,isEnd);	
		db.update(TABLE_NAME, values, KEY_ID+"="+id, null);
	}
	  /**
	 * 功能描述：获取上传获取下载的文件列表
	 * Administrator
	 * 2014年7月14日 上午11:27:54
	 * @param type  1下载 2上传
	 * @param isEnd  传输是否完成
	 * @return
	 */
	public LinkedList<TransferredFile> queryFileLoadHistory(int type,int isEnd,String order){
		  Cursor cursor=db.query(TABLE_NAME, FILE, KEY_TYPE+"="+type+" and "+KEY_ISEND+"="+isEnd, null, null, null, order);
		  LinkedList<TransferredFile> list=new LinkedList<TransferredFile>();
		  while(cursor.moveToNext()){
			  HashMap<String, Object> map=new HashMap<String, Object>();
			  TransferredFile  transferredFile=new TransferredFile(context, cursor.getString(1),
					  cursor.getInt(5), cursor.getInt(6), cursor.getString(2), cursor.getString(3),type);
			  transferredFile.setId(cursor.getLong(0));
			  list.add(transferredFile);
		  }
		  cursor.close();
		  return list;
	  }
	  
	  public TransferredFile getTransferredFileById(long id){
		  TransferredFile transferredFile=null;
		  Cursor cursor=db.query(TABLE_NAME, FILE,KEY_ID+"="+id,null,null,null,null);
		  if(cursor.moveToNext()){
			 transferredFile=new TransferredFile(context,cursor.getString(1),
					  cursor.getInt(5), cursor.getInt(6), cursor.getString(2), cursor.getString(3),cursor.getInt(4));
			 transferredFile.setId(id);
			 System.out.println("id:"+id+",filename:"+cursor.getString(1));
		  }
		  cursor.close();
		  return transferredFile;
		  
	  }
	
}
