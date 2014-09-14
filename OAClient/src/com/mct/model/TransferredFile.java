package com.mct.model;

import com.mct.util.Common;
import com.mct.util.FileDbUtil;

import android.content.Context;
import android.content.Intent;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class TransferredFile {
	private Context context;
	private long id;
	private String fileName;// �ļ���
	private long fileSize;// �ļ���С
	private long length;// ��ǰ�ļ���С
	private String loacalPath;// ����·��
	private String remotePath;// ftp·��
	private int type;// 1 ���� 2 �ϴ�
	private boolean isStart;// �Ƿ�������
	private boolean isStarting;// �Ƿ���������
	private MyFTPDataTransferListener myListener;// ���ݴ������
	private double speed;
	private long startTime = 0;
	private long endTime = 0;
	private int count=1;

	public TransferredFile(long id){
		this.id=id;
	}
	
	/**
	 * @param fileName
	 * @param fileSize
	 * @param length
	 * @param loacalPath
	 * @param remotePath
	 * @param isStart
	 */
	public TransferredFile(Context context, String fileName, long fileSize,
			long length, String loacalPath, String remotePath, int type) {
		super();
		this.context = context;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.length = length;
		this.loacalPath = loacalPath;
		this.remotePath = remotePath;
		this.type = type;
		if (myListener == null) {
			myListener = new MyFTPDataTransferListener();
		}
	}

	public MyFTPDataTransferListener getMyListener() {
		return myListener;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getLoacalPath() {
		return loacalPath;
	}

	public void setLoacalPath(String loacalPath) {
		this.loacalPath = loacalPath;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public boolean isStarting() {
		return isStarting;
	}

	public void setStarting(boolean isStarting) {
		this.isStarting = isStarting;
	}

	/**
	 * �ļ��������
	 * 
	 * @author fengyouchun
	 * @version ����ʱ�䣺2014��7��11�� ����12:05:36
	 */
	class MyFTPDataTransferListener implements FTPDataTransferListener {

		/**
		 * ��������ʱ����
		 */
		@Override
		public void aborted() {
			// TODO Auto-generated method stub
			isStart = false;
			isStarting = false;
			if (context != null) {
				Intent intent = new Intent(Common.ACTION_REMOVELOAD);
				intent.putExtra("id", id);
				intent.putExtra("type", type);
				intent.putExtra("length", length);
				context.sendBroadcast(intent);
			}
		}

		/*
		 * ���ʱ����
		 */
		@Override
		public void completed() {
			// TODO Auto-generated method stub
			// ����������ɹ㲥
			if (context != null) {
				Intent intent = new Intent(Common.ACTION_COMPLETED);
				intent.putExtra("id", id);
				context.sendBroadcast(intent);
			}

		}

		@Override
		public void failed() {
			// TODO Auto-generated method stub
			isStarting = true;
			isStart = false;
			// ��������ʧ�ܹ㲥

			if (context != null) {
				FileDbUtil.getInstance(context).updataFileLoadHistory(id,
						length, 0);
				Intent intent = new Intent(Common.ACTION_REMOVELOAD);
				intent.putExtra("id", id);
				intent.putExtra("isstart", isStart);
				intent.putExtra("isstarting", isStarting);
				context.sendBroadcast(intent);
			}
		}

		@Override
		public void started() {
			// TODO Auto-generated method stub
			startTime=System.currentTimeMillis();
			isStarting = true;
			isStart = true;
			if (context != null) {
				Intent intent = new Intent(Common.ACTION_REFRESH);
				intent.putExtra("id", id);
				intent.putExtra("isstart", isStart);
				intent.putExtra("isstarting", isStarting);
				context.sendBroadcast(intent);
			}
		}

		// ��ʾ�����Ѿ�������ֽ���
		@Override
		public void transferred(int arg0) {
			// TODO Auto-generated method stub
			
			length = length + arg0;
//			FileDbUtil.getInstance(context)
//					.updataFileLoadHistory(id, length, 0);
			if(count++%100==0){
				endTime=System.currentTimeMillis();
			Intent intent = new Intent(Common.ACTION_REFRESH);
			intent.putExtra("id", id);
			intent.putExtra("length", length);
			intent.putExtra("isstart", isStart);
			intent.putExtra("isstarting", isStarting);
			speed=(double)((arg0*1000*100/(1024))/(endTime-startTime));
			intent.putExtra("speed",speed);
			context.sendBroadcast(intent);
			startTime=endTime;
			}
		}

	}

}
