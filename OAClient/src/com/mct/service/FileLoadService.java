package com.mct.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Comment;

import com.mct.model.TransferredFile;
import com.mct.util.Common;
import com.mct.util.FTPUtils;
import com.mct.util.FileDbUtil;
import com.mct.util.FileUtil;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * �ļ��ϴ����ط���
 * 
 * @author fengyouchun
 * @version ����ʱ�䣺2014��7��11�� ����12:03:08
 */
public class FileLoadService extends Service {
	private static final int maxDownloadNum = 2;// ���ͬʱ�����ļ���
	private static final int maxUploadNum = 2;// ���ͬʱ�ϴ��ļ���
	private static ArrayList<TransferredFile> downloadingList;// �������ص��ļ�����
	private static ArrayList<TransferredFile> uploadingList;// �����ϴ����ļ�����
	private static ArrayList<TransferredFile> downloadList;// �ȴ����ص��ļ�����
	private static ArrayList<TransferredFile> uploadList;// �ȴ��ϴ����ļ�����
	private static ArrayList<HashMap<String, Object>> ftpClientList;// �洢���ڴ����id��FTPClient�Ķ�Ӧ��ϵ
	private FileLoadManager fileLoadManager;
	private TransferredFile transferredFile;
	private FTPClient ftpClient;
	private MyBroadcastReceiver myBroadcastReceiver;


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// ʵ���������б������洢���ڴ��������
		downloadingList = new ArrayList<TransferredFile>();
		uploadingList = new ArrayList<TransferredFile>();
		downloadList = new ArrayList<TransferredFile>();
		uploadList = new ArrayList<TransferredFile>();
		fileLoadManager=new FileLoadManager();
		ftpClientList=new ArrayList<HashMap<String,Object>>();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		System.out.println("���ݴ����������");
		myBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Common.ACTION_ADDLOAD);
		filter.addAction(Common.ACTION_REMOVELOAD);
		filter.addAction(Common.ACTION_COMPLETED);
		registerReceiver(myBroadcastReceiver, filter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myBroadcastReceiver);
		System.out.println("���ݴ������ֹͣ");
	}

	class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			long id = arg1.getLongExtra("id", -1);
			System.out.println(arg1.getAction() + "," + id);
			if (id <= 0) {
				return ;
			}
			
			if (arg1.getAction() == Common.ACTION_ADDLOAD) {
				TransferredFile file = FileDbUtil.getInstance(
						FileLoadService.this).getTransferredFileById(id);
						switch (file.getType()) {
						case 1:
							// ����
							Log.e("opt", "�������ض���");
							fileLoadManager.addDownload(file);
							break;

						case 2:
							// �ϴ�
							fileLoadManager.addUpload(file);
							break;
						}
			

			} else if(arg1.getAction() == Common.ACTION_REMOVELOAD){
				//��������
				TransferredFile file = FileDbUtil.getInstance(
						FileLoadService.this).getTransferredFileById(id);
					switch (file.getType()) {
					case 1:
						// ����
						Log.e("opt", "��ͣ����");
						fileLoadManager.removeDownload(0,id);
						break;

					case 2:
						// �ϴ�
						fileLoadManager.removeUpload(id);
						break;

				}
					FileDbUtil.getInstance(FileLoadService.this).updataFileLoadHistory(id, arg1.getIntExtra("length", 0), 0);
			}else if(arg1.getAction() == Common.ACTION_COMPLETED){
				//�������
				System.out.println("�������");
				TransferredFile file = FileDbUtil.getInstance(
						FileLoadService.this).getTransferredFileById(id);
				if (id > 0) {
					
					switch (file.getType()) {
					case 1:
						// ����
						fileLoadManager.removeDownload(1,id);
						break;

					case 2:
						// �ϴ�
						fileLoadManager.removeUpload(id);
						break;
					}
					FileDbUtil.getInstance(FileLoadService.this).updataFileLoadHistory(id, file.getFileSize(), 1);
				}
				
			}
		}

	}

	class FileLoadManager {

		/**
		 * �������������һ���µ������������ض��� Administrator 2014��7��11�� ����6:19:39
		 * 
		 * @param transferredFile
		 */
		public void addDownload(TransferredFile transferredFile) {
			
			// ���������صļ�¼��С���������ͬʱ���ؼ�¼��
			if (downloadingList.size() < maxDownloadNum) {
				Log.e("opt", "��ʼ����");
				// ����¼�����������ص��б���
				downloadingList.add(transferredFile);
				// ��ʼ����
				startLoading(transferredFile.getId());
			} else {
				Log.e("opt", "���صȴ�");
				downloadList.add(transferredFile);
				Intent intent = new Intent(Common.ACTION_REFRESH);
				intent.putExtra("id", transferredFile.getId());
				intent.putExtra("isstart", true);
				intent.putExtra("isstarting", false);
				sendBroadcast(intent);
			}
		}

		public void removeDownload(int type,long id) {
			if(type==0){
				//��ͣ����
				pauseLoad(id);
			}			
			for(int i=0;i<downloadingList.size();i++){
				TransferredFile mTransferredFile=downloadingList.get(i);
				if(mTransferredFile.getId()==id){
					Log.e("opt","�Ƴ�"+mTransferredFile.getFileName()+"����");
					downloadingList.remove(i);
					break;
				}
			}
			// �����ļ��ȴ����أ��������������ض���
			if (downloadList.size() > 0) {
				addDownload(downloadList.get(0));
				downloadList.remove(0);
			}

		}

		/**
		 * ����ָ��id������ ���������� Administrator 2014��7��11�� ����6:20:31
		 * 
		 * @param id
		 */
		public void startLoading(long id) {
			transferredFile = FileDbUtil.getInstance(FileLoadService.this)
					.getTransferredFileById(id);
			try {
				ftpClient = FTPUtils.getInstance().getClient();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (ftpClient != null && ftpClient.isAuthenticated()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				map.put("ftpClient", ftpClient);
				ftpClientList.add(map);
				Log.e("opt", "�������߳�");
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							switch (transferredFile.getType()) {
							case 1:

								FTPUtils.getInstance().download(ftpClient,
										transferredFile);
								break;

							case 2:
								FTPUtils.getInstance().upload(ftpClient,
										transferredFile);
								break;
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();

			}
		}

		/**
		 * ������������ָͣ��id���ϴ�/���� Administrator 2014��7��11�� ����6:28:02
		 * 
		 * @param id
		 */
		public void pauseLoad(long id) {
			// ��ȡ�б���ָ��id��Ӧ��FTPClient����
			FTPClient ftpClient = null;
			if (ftpClientList.indexOf(id) != -1) {
				ftpClient = (FTPClient) (ftpClientList.get(ftpClientList
						.indexOf(id)).get("ftpClient"));
				try {
					ftpClient.abortCurrentDataTransfer(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FTPIllegalReplyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		/**
		 * �����������Ӷ������Ƴ�ָ��id���ϴ����� Administrator 2014��7��11�� ����6:26:42
		 * 
		 * @param id
		 */
		public void removeUpload(long id) {

			pauseLoad(id);
			for(int i=0;i<uploadingList.size();i++){
				TransferredFile mTransferredFile=uploadingList.get(i);
				if(mTransferredFile.getId()==id){
					Log.e("opt","�Ƴ�"+mTransferredFile.getFileName()+"����");
					uploadingList.remove(i);
					break;
				}
			}
			// �����ļ��ȴ����أ��������������ض���
			if (uploadList.size() > 0) {
				Log.e("opt", uploadList.get(0).getFileName()+"��������");
				addUpload(uploadList.get(0));
				uploadList.remove(0);
			}

		}

		/**
		 * �������������һ���ϴ����񵽶��� Administrator 2014��7��11�� ����6:41:49
		 * 
		 * @param transferredFile
		 */
		public void addUpload(TransferredFile transferredFile) {
			// ���������صļ�¼��С���������ͬʱ���ؼ�¼��
			if (uploadingList.size() < maxUploadNum) {
				// ����¼�����������ص��б���
				uploadingList.add(transferredFile);
				// ��ʼ�ϴ�
				startLoading(transferredFile.getId());
			} else {
				uploadList.add(transferredFile);
			}
		}
	}
	
}
