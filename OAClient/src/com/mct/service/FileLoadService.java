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
 * 文件上传下载服务
 * 
 * @author fengyouchun
 * @version 创建时间：2014年7月11日 上午12:03:08
 */
public class FileLoadService extends Service {
	private static final int maxDownloadNum = 2;// 最大同时下载文件数
	private static final int maxUploadNum = 2;// 最大同时上传文件数
	private static ArrayList<TransferredFile> downloadingList;// 正在下载的文件队列
	private static ArrayList<TransferredFile> uploadingList;// 正在上传的文件队列
	private static ArrayList<TransferredFile> downloadList;// 等待下载的文件队列
	private static ArrayList<TransferredFile> uploadList;// 等待上传的文件队列
	private static ArrayList<HashMap<String, Object>> ftpClientList;// 存储正在传输的id与FTPClient的对应关系
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
		// 实例化传输列表，用来存储正在传输的任务
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
		System.out.println("数据传输服务启动");
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
		System.out.println("数据传输服务停止");
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
							// 下载
							Log.e("opt", "加入下载队列");
							fileLoadManager.addDownload(file);
							break;

						case 2:
							// 上传
							fileLoadManager.addUpload(file);
							break;
						}
			

			} else if(arg1.getAction() == Common.ACTION_REMOVELOAD){
				//结束下载
				TransferredFile file = FileDbUtil.getInstance(
						FileLoadService.this).getTransferredFileById(id);
					switch (file.getType()) {
					case 1:
						// 下载
						Log.e("opt", "暂停下载");
						fileLoadManager.removeDownload(0,id);
						break;

					case 2:
						// 上传
						fileLoadManager.removeUpload(id);
						break;

				}
					FileDbUtil.getInstance(FileLoadService.this).updataFileLoadHistory(id, arg1.getIntExtra("length", 0), 0);
			}else if(arg1.getAction() == Common.ACTION_COMPLETED){
				//下载完成
				System.out.println("传输完成");
				TransferredFile file = FileDbUtil.getInstance(
						FileLoadService.this).getTransferredFileById(id);
				if (id > 0) {
					
					switch (file.getType()) {
					case 1:
						// 下载
						fileLoadManager.removeDownload(1,id);
						break;

					case 2:
						// 上传
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
		 * 功能描述：添加一个新的下载任务到下载队列 Administrator 2014年7月11日 下午6:19:39
		 * 
		 * @param transferredFile
		 */
		public void addDownload(TransferredFile transferredFile) {
			
			// 若正在下载的记录数小于最大允许同时下载记录数
			if (downloadingList.size() < maxDownloadNum) {
				Log.e("opt", "开始下载");
				// 将记录加入正在下载的列表中
				downloadingList.add(transferredFile);
				// 开始下载
				startLoading(transferredFile.getId());
			} else {
				Log.e("opt", "下载等待");
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
				//暂停下载
				pauseLoad(id);
			}			
			for(int i=0;i<downloadingList.size();i++){
				TransferredFile mTransferredFile=downloadingList.get(i);
				if(mTransferredFile.getId()==id){
					Log.e("opt","移除"+mTransferredFile.getFileName()+"下载");
					downloadingList.remove(i);
					break;
				}
			}
			// 若有文件等待下载，则移入正在下载队列
			if (downloadList.size() > 0) {
				addDownload(downloadList.get(0));
				downloadList.remove(0);
			}

		}

		/**
		 * 开启指定id的下载 功能描述： Administrator 2014年7月11日 下午6:20:31
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
				Log.e("opt", "开启新线程");
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
		 * 功能描述：暂停指定id的上传/下载 Administrator 2014年7月11日 下午6:28:02
		 * 
		 * @param id
		 */
		public void pauseLoad(long id) {
			// 获取列表中指定id对应的FTPClient对象
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
		 * 功能描述：从队列中移除指定id的上传任务 Administrator 2014年7月11日 下午6:26:42
		 * 
		 * @param id
		 */
		public void removeUpload(long id) {

			pauseLoad(id);
			for(int i=0;i<uploadingList.size();i++){
				TransferredFile mTransferredFile=uploadingList.get(i);
				if(mTransferredFile.getId()==id){
					Log.e("opt","移除"+mTransferredFile.getFileName()+"下载");
					uploadingList.remove(i);
					break;
				}
			}
			// 若有文件等待下载，则移入正在下载队列
			if (uploadList.size() > 0) {
				Log.e("opt", uploadList.get(0).getFileName()+"加入下载");
				addUpload(uploadList.get(0));
				uploadList.remove(0);
			}

		}

		/**
		 * 功能描述：添加一个上传任务到队列 Administrator 2014年7月11日 下午6:41:49
		 * 
		 * @param transferredFile
		 */
		public void addUpload(TransferredFile transferredFile) {
			// 若正在下载的记录数小于最大允许同时下载记录数
			if (uploadingList.size() < maxUploadNum) {
				// 将记录加入正在下载的列表中
				uploadingList.add(transferredFile);
				// 开始上传
				startLoading(transferredFile.getId());
			} else {
				uploadList.add(transferredFile);
			}
		}
	}
	
}
