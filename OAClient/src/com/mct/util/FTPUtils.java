package com.mct.util;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

import com.mct.client.R;
import com.mct.model.TransferredFile;

import android.util.Log;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;

public class FTPUtils {
	private static FTPUtils ftp;
	/**
	 * FTP服务地址
	 */
//	 public static final String ADDRESS = "ftp://n.nat123.net:14855";
	public static final String ADDRESS = "ftp://192.168.0.46:21";
	/**
	 * FTP登录用户名
	 */
	private static final String USERNAME = "administrator";
	/**
	 * FTP登录密码
	 */
	private static final String PASSWORD = "qwerty";

	/**
	 * 构造方法
	 */
	protected FTPUtils() {
	}

	/**
	 * 实例化对象
	 * 
	 * @return
	 */
	public static FTPUtils getInstance() {
		if (ftp == null) {
			ftp = new FTPUtils();
		}
		return ftp;
	}

	/**
	 * 获取FTP客户端对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public FTPClient getClient() throws Exception {
		FTPClient client = new FTPClient();
		client.setCharset("utf-8");
		client.setType(FTPClient.TYPE_BINARY);
		URL url = new URL(FTPUtils.ADDRESS);
		int port = url.getPort() < 1 ? 21 : url.getPort();
		client.connect(url.getHost(), port);
		Log.e("host", url.getHost());
		Log.e("port", port + "");
		client.login(FTPUtils.USERNAME, FTPUtils.PASSWORD);
		return client;
	}

	/**
	 * 注销客户端连接
	 * 
	 * @param client
	 *            FTP客户端对象
	 * @throws Exception
	 */
	public void logout(FTPClient client) throws Exception {
		if (client != null) {
			try {
				// 有些FTP服务器未实现此功能，若未实现则会出错
				client.logout(); // 退出登录
			} catch (FTPException fe) {
			} catch (Exception e) {
				throw e;
			} finally {
				if (client.isConnected()) { // 断开连接
					client.disconnect(true);
				}
			}
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param client
	 *            FTP客户端对象
	 * @param dir
	 *            目录
	 * @throws Exception
	 */
	public void mkdirs(FTPClient client, String dir) throws Exception {
		if (dir == null) {
			return;
		}
		dir = dir.replace("//", "/");
		String[] dirs = dir.split("/");

		if (!exists(client, dir)) {
			client.createDirectory(dir);
		}
		client.changeDirectory(dir);

	}

	/**
	 * 获取FTP目录
	 * 
	 * @param url
	 *            原FTP目录
	 * @param dir
	 *            目录
	 * @return
	 * @throws Exception
	 */
	public URL getURL(URL url, String dir) throws Exception {
		String path = url.getPath();
		if (!path.endsWith("/") && !path.endsWith("//")) {
			path += "/";
		}
		dir = dir.replace("//", "/");
		if (dir.startsWith("/")) {
			dir = dir.substring(1);
		}
		path += dir;
		return new URL(url, path);
	}

	/**
	 * 获取FTP目录
	 * 
	 * @param dir
	 *            目录
	 * @return
	 * @throws Exception
	 */
	public URL getURL(String dir) throws Exception {
		return getURL(new URL(FTPUtils.ADDRESS), dir);
	}

	/**
	 * 判断文件或目录是否存在
	 * 
	 * @param client
	 *            FTP客户端对象
	 * @param dir
	 *            文件或目录
	 * @return
	 * @throws Exception
	 */
	public boolean exists(FTPClient client, String dir) throws Exception {
		return getFileType(client, dir) != -1;
	}

	/**
	 * 判断当前为文件还是目录
	 * 
	 * @param client
	 *            FTP客户端对象
	 * @param dir
	 *            文件或目录
	 * @return -1、文件或目录不存在 0、文件 1、目录
	 * @throws Exception
	 */
	public int getFileType(FTPClient client, String dir) throws Exception {
		FTPFile[] files = null;
		try {
			files = client.list(dir);
		} catch (Exception e) {
			return -1;
		}
		if (files.length > 1) {
			return FTPFile.TYPE_DIRECTORY;
		} else if (files.length == 1) {
			FTPFile f = files[0];
			if (f.getType() == FTPFile.TYPE_DIRECTORY) {
				return FTPFile.TYPE_DIRECTORY;
			}
			String path = dir + "/" + f.getName();
			try {
				int len = client.list(path).length;
				if (len == 1) {
					return FTPFile.TYPE_DIRECTORY;
				} else {
					return FTPFile.TYPE_FILE;
				}
			} catch (Exception e) {
				return FTPFile.TYPE_FILE;
			}
		} else {
			try {
				client.changeDirectory(dir);
				client.changeDirectoryUp();
				return FTPFile.TYPE_DIRECTORY;
			} catch (Exception e) {
				return -1;
			}
		}
	}

	/**
	 * 上传文件或目录
	 * 
	 * @param dir
	 *            目标文件
	 * @param del
	 *            是否删除源文件，默认为false
	 * @param file
	 *            文件
	 * @throws Exception
	 */
	public void upload(FTPClient ftpClient, TransferredFile file)
			throws Exception {
		String dir = file.getRemotePath();
		if (dir == null || file == null) {
			return;
		}
		ftpClient = null;
		try {
			ftpClient = getClient();
			mkdirs(ftpClient, dir); // 创建文件
			File localFile = new File(file.getLoacalPath());
			ftpClient.upload(localFile, file.getLength(), file.getMyListener()); // 上传文件

		} finally {
			logout(ftpClient);
		}
	}

	// /**
	// * 删除文件或目录
	// *
	// * @param dir
	// * 文件或目录数组
	// * @throws Exception
	// */
	// public void delete(String dirs) throws Exception {
	// if (StringUtils.isEmpty(dirs)) {
	// return;
	// }
	// FTPClient client = null;
	// try {
	// client = getClient();
	// int type = -1;
	// client.changeDirectory("/"); // 切换至根目录
	// type = getFileType(client, dir); // 获取当前类型
	// if (type == 0) { // 删除文件
	// client.deleteFile(dir);
	// } else if (type == 1) { // 删除目录
	// deleteFolder(client, getURL(dir));
	// }
	//
	// } finally {
	// logout(client);
	// }
	// }
	// /**
	// * 删除目录
	// *
	// * @param client
	// * FTP客户端对象
	// * @param url
	// * FTP URL
	// * @throws Exception
	// */
	public void deleteFolder(FTPClient client, URL url) throws Exception {
		String path = url.getPath();
		client.changeDirectory(path);
		FTPFile[] files = client.list();
		String name = null;
		for (FTPFile file : files) {
			name = file.getName();
			// 排除隐藏目录
			if (".".equals(name) || "..".equals(name)) {
				continue;
			}
			if (file.getType() == FTPFile.TYPE_DIRECTORY) { // 递归删除子目录
				deleteFolder(client, getURL(url, file.getName()));
			} else if (file.getType() == FTPFile.TYPE_FILE) { // 删除文件
				client.deleteFile(file.getName());
			}
		}
		client.changeDirectoryUp();
		client.deleteDirectory(url.getPath()); // 删除当前目录
	}

	public void download(FTPClient client, TransferredFile transferredFile)
			throws Exception {
		String remoteDir = transferredFile.getRemotePath();// FTP文件夹
		String localDir = transferredFile.getLoacalPath();// 本地文件路径
		try {
			File file = new File(localDir);
			Log.e("opt", "下载中...");
			client.download(remoteDir + transferredFile.getFileName(), file,
					transferredFile.getLength(),
					transferredFile.getMyListener());

		} finally {
			logout(client);
		}
	}

	/**
	 * 获取目录下所有文件
	 * 
	 * @param dir
	 *            目录
	 * @return
	 * @throws Exception
	 */
	public String[] list(String dir) throws Exception {
		FTPClient client = null;
		try {
			client = getClient();
			client.changeDirectory(dir);
			String[] values = client.listNames();
			if (values != null) {
				// 将文件排序(忽略大小写)
				Arrays.sort(values, new Comparator<String>() {
					public int compare(String val1, String val2) {
						return val1.compareToIgnoreCase(val2);
					}
				});
			}
			return values;
		} catch (FTPException fe) {
			// 忽略文件夹不存在的情况
			String mark = "code=550";
			if (fe.toString().indexOf(mark) == -1) {
				throw fe;
			}
		} finally {
			logout(client);
		}
		return new String[0];
	}

	/**
	 * 功能描述：根据文件类型获取图标资源id
	 * Administrator
	 * 2014年7月14日 上午11:48:15
	 * @param fileType
	 * @return
	 */
	public static int getFileTypeImage(int fileType) {
		// TODO Auto-generated method stub
		int imageId = 0;
		switch (fileType) {
		case 1:
			// 文件夹
			imageId = R.drawable.bxfile_file_dir;
			break;
		case 2:
			// text
			imageId = R.drawable.bxfile_file_txt;
			break;
		case 3:
			// rar
			imageId = R.drawable.bxfile_file_zip;
			break;
		case 4:
			// zip
			imageId = R.drawable.bxfile_file_zip;
			break;
		case 5:
			// xls
			imageId = R.drawable.bxfile_file_xls;
			break;
		case 6:
			// html
			imageId = R.drawable.bxfile_file_html;
			break;
		case 7:
			// doc
			imageId = R.drawable.bxfile_file_doc;
			break;
		case 8:
			// ppt
			imageId = R.drawable.bxfile_file_ppt;
			break;
		case 9:
			// jpg
			imageId = R.drawable.bxfile_file_jpg;
			break;
		case 10:
			// pdf
			imageId = R.drawable.bxfile_file_pdf;
			break;
		case 11:
			// png
			imageId = R.drawable.bxfile_file_jpg;
			break;

		case 12:
			imageId = R.drawable.bxfile_file_unknow;
			break;
		}
		return imageId;
	}

	public static int getFileType(FTPFile file) {
		if (file.getType() == FTPFile.TYPE_DIRECTORY) {
			return 1;
		} else {
			String type = file.getName().substring(
					file.getName().lastIndexOf(".") + 1);
			if (type.equals("txt")) {
				return 2;
			} else if (type.equals("rar")) {
				return 3;
			} else if (type.equals("zip")) {
				return 4;
			} else if (type.equals("xls")) {
				return 5;
			} else if (type.equals("html")) {
				return 6;
			} else if (type.equals("doc")) {
				return 7;
			} else if (type.equals("ppt")) {
				return 8;
			} else if (type.equals("jpg")) {
				return 9;
			} else if (type.equals("pdf")) {
				return 10;
			} else if (type.equals("png")) {
				return 11;
			} else {
				return 12;
			}
		}
	}

	private static int getFileType(String type) {
		if (type.equals("txt")) {
			return 2;
		} else if (type.equals("rar")) {
			return 3;
		} else if (type.equals("zip")) {
			return 4;
		} else if (type.equals("xls")) {
			return 5;
		} else if (type.equals("html")) {
			return 6;
		} else if (type.equals("doc")) {
			return 7;
		} else if (type.equals("ppt")) {
			return 8;
		} else if (type.equals("jpg")) {
			return 9;
		} else if (type.equals("pdf")) {
			return 10;
		} else if (type.equals("png")) {
			return 11;
		} else {
			return 12;
		}
	}
	public static int getFileTypeImageByName(String name){
		if(name.indexOf(".")==-1){
			return getFileTypeImage(1);
		}else{
			return getFileTypeImage(getFileType(name.substring(name.indexOf(".")+1)));
		}
		
	}
}
