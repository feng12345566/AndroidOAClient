package com.mct.fragment;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.mct.client.R;
import com.mct.controls.FileListAdapter;
import com.mct.localefilebrowser.LocaleFileMainActivity;
import com.mct.service.FileLoadService;
import com.mct.util.Common;
import com.mct.util.FTPUtils;
import com.mct.util.FileDbUtil;
import com.mct.util.FileUtil;
import com.mct.util.HttpRequestUtil;
import com.mct.util.XmppTool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RemoteFileListFragment extends Fragment implements OnClickListener {
	private ListView fileListView;
	private FileListAdapter adapter;
	private ArrayList<FTPFile> fileList;
	private LinearLayout ll;
	private LinearLayout lastDir, openFile, uploadFile, downloadFile,
			refreshForlder;
	private String baseUrl = "/file/";
	private String currUrl;// 当前网络路径
	private ProgressDialog dialog;
	private FTPClient ftpClient;
	private LinearLayout emptyView;
	private ArrayList<String> pathList;
	private String tag;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		tag=getArguments().getString("tag");
		
		View view = inflater.inflate(R.layout.filelist, null);
		fileListView = (ListView) view.findViewById(R.id.filelistview);
		ll = (LinearLayout) view.findViewById(R.id.fileoptlayout);
		lastDir = (LinearLayout) view.findViewById(R.id.lastdirbtn);
		refreshForlder = (LinearLayout) view
				.findViewById(R.id.refreshforlderbtn);
		openFile = (LinearLayout) view.findViewById(R.id.openfilebtn);
		uploadFile = (LinearLayout) view.findViewById(R.id.uploadfilebtn);
		downloadFile = (LinearLayout) view.findViewById(R.id.downloadfilebtn);
		emptyView = (LinearLayout) view.findViewById(R.id.emptyView);
		if(tag.equals("privateFile")){
			baseUrl="/userfile/"+XmppTool.loginUser.getUserId()+"/";
			lastDir.setVisibility(View.GONE);
		}else{
			lastDir.setVisibility(View.VISIBLE);
			lastDir.setOnClickListener(this);
		}
		openFile.setOnClickListener(this);
		uploadFile.setOnClickListener(this);
		downloadFile.setOnClickListener(this);
		refreshForlder.setOnClickListener(this);
		currUrl = baseUrl;
		ll.setVisibility(View.VISIBLE);
		fileList = new ArrayList<FTPFile>();
		adapter = new FileListAdapter(getActivity().getApplicationContext(),
				fileList);
		fileListView.setAdapter(adapter);
		getFileList();
		return view;
	}

	private void getFileList() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
				try {
					ftpClient = null;
					ftpClient = FTPUtils.getInstance().getClient();

					System.out.println("连接ftp服务器成功");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				}
				if (ftpClient != null && ftpClient.isAuthenticated()) {
					try {
						ftpClient.changeDirectory(currUrl);
						System.out.println("打开文件夹" + currUrl);
						FTPFile[] files = ftpClient.list();
						System.out.println("查询到" + files.length + "条记录");
						if (files != null) {
							fileList.clear();
							for (FTPFile file : files) {
								fileList.add(file);
							}
						}
						handler.sendEmptyMessage(1);
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(2);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(2);
					} catch (FTPIllegalReplyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(2);
					} catch (FTPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(2);
					} catch (FTPDataTransferException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(2);
					} catch (FTPAbortedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FTPListParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendEmptyMessage(2);
					}
				} else {
					handler.sendEmptyMessage(2);
				}

			}
		}).start();

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				fileListView.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);
				if (dialog == null) {
					dialog = new ProgressDialog(getActivity());
					dialog.setMessage("加载中...");
					dialog.setCanceledOnTouchOutside(false);
				}
				dialog.show();
				break;

			case 1:
				dialog.dismiss();
				Log.e("dialog", "执行了dismiss");
				adapter.setFileList(fileList);
				adapter.notifyDataSetChanged();
				if (fileList.size() == 0) {
					emptyView.setVisibility(View.VISIBLE);
					fileListView.setVisibility(View.GONE);
				} else {
					emptyView.setVisibility(View.GONE);
					fileListView.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				Toast.makeText(getActivity(), "加载失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}

	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.lastdirbtn:
			if (currUrl.equals(baseUrl)) {
				Toast.makeText(getActivity(), "当前文件夹已是最上层文件夹！",
						Toast.LENGTH_SHORT).show();
			} else {
				currUrl = currUrl.substring(
						0,
						currUrl.substring(0, currUrl.length() - 1).lastIndexOf(
								"/") + 1);
				getFileList();
			}

			break;

		case R.id.openfilebtn:

			ArrayList<FTPFile> selectedFile = adapter.getSelectedFile();
			switch (selectedFile.size()) {
			case 0:
				Toast.makeText(getActivity(), "请选择一个文件夹！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				Log.e("type", selectedFile.get(0).getType() + "");
				switch (selectedFile.get(0).getType()) {
				case FTPFile.TYPE_DIRECTORY:

					currUrl = currUrl + selectedFile.get(0).getName() + "/";

					getFileList();
					break;

				default:
					Toast.makeText(getActivity(), "只能选择一个文件夹！",
							Toast.LENGTH_SHORT).show();
					break;
				}
				break;
			default:
				break;
			}
		case R.id.refreshforlderbtn:
			getFileList();
			break;
		case R.id.downloadfilebtn:
			System.out.println("下载");
			Toast.makeText(getActivity(),
					adapter.getSelectedFile().size() + "个文件加入下载队列",
					Toast.LENGTH_SHORT).show();
			getFileList();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					for (int i = 0; i < adapter.getSelectedFile().size(); i++) {
						// 获取FTP文件对象
						FTPFile ftpFile = fileList.get(i);
						if (ftpFile.getType() == FTPFile.TYPE_FILE) {
							// 循环标记
							boolean frag = true;
							// 文件名标记
							int j = 1;
							// 本地文件对象
							File file = null;
							String baseFilePath = FileUtil.getSDPath()
									+ "/mobileOA/downloadfile/";
							FileUtil.mkdir(baseFilePath);
							String fileName = ftpFile.getName();
							int index = fileName.lastIndexOf(".");
							String localPath = baseFilePath + fileName;
							while (frag) {
								file = new File(localPath);
								System.out.println("检测文件" + localPath);
								if (file.exists()) {
									// 遍历文件名后加编号的文件是否存在
									localPath = baseFilePath
											+ fileName.substring(0, index)
											+ "(" + (j++) + ")"
											+ fileName.substring(index - 1);
								} else {
									// 不存在则退出循环
									frag = false;
								}
							}
							long id = FileDbUtil.getInstance(getActivity())
									.insertFileLoadHistory(fileName, localPath,
											currUrl, 1, 0,
											(int) ftpFile.getSize(), 0);
							Intent intent = new Intent();
							intent.setAction(Common.ACTION_ADDLOAD);
							intent.putExtra("id", id);
							intent.putExtra("type", 1);
							getActivity().sendBroadcast(intent);
						}
					}
					adapter.getSelectedFile().clear();
				}

			}).start();

			break;
		case R.id.uploadfilebtn:
			Intent uploadIntent = new Intent(getActivity(),
					LocaleFileMainActivity.class);
			uploadIntent.putExtra("opt", "upload");
			startActivityForResult(uploadIntent, 0);
			break;
		}

	}

	/** 根据返回选择的文件，来进行上传操作 **/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			pathList = data.getStringArrayListExtra("result");
			if (pathList != null && pathList.size() != 0) {
				Toast.makeText(getActivity(), "上传" + pathList.size() + "个文件",
						Toast.LENGTH_SHORT).show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						for (int i = 0; i < pathList.size(); i++) {
							File file = new File(pathList.get(i));
							long id = FileDbUtil.getInstance(getActivity())
									.insertFileLoadHistory(file.getName(),
											pathList.get(i), currUrl, 2, 0,
											file.length(), 0);
							Intent intent = new Intent(Common.ACTION_ADDLOAD);
							intent.putExtra("id", id);
							getActivity().sendBroadcast(intent);
						}
					}
				}).start();

			}
		}
	}

}
