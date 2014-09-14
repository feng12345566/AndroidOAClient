package com.mct.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class FileUtil {
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		} else {
			return null;
		}
		return sdDir.toString();

	}

	public static float getEnableSize() {
		long availCount = 0;
		File sdcardDir = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(sdcardDir.toString());
		availCount = sf.getAvailableBlocks();
		Log.e("availCount",availCount / (1024 * 1024)+"M");
		return availCount / (1024 * 1024);
	}
	/**
	 * 创建目录
	 * 
	 * @param dir
	 *            目录
	 */
	public static void mkdir(String dir) {
		File path = new File(dir);
		if(path.exists()){
			return ;
		}
		String[] folder = dir.split("/");
		String dirTemp = getSDPath()+"/";
		for (int i = 4; i < folder.length; i++) {
			dirTemp=dirTemp+folder[i]+"/";
			File dirPath = new File(dirTemp);
			try {
				System.out.println("检查目录"+dirTemp);
				if (!dirPath.exists()) {
					System.out.println("创建目录"+dirTemp);
					dirPath.mkdir();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public static boolean writeToSDFromInput(String path,String fileName,InputStream input){
		if(input==null){
			return false;
		}
		File file = null;
		OutputStream output = null;
		try {
			mkdir(path);
			file=new File(path+fileName);
			file.createNewFile();
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while((input.read(buffer)) != -1){
				output.write(buffer);
			}
			//清缓存，将流中的数据保存到文件中
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally{
			try {
				output.close();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
		return true;
	}
	
	public static String getFileSize(long length){
		DecimalFormat df = new DecimalFormat("0.00");
		if(length<1024*1024){
		    return df.format(length*1.0/1024)+"KB";
		}else{
			return df.format(length*1.0/(1024*1024))+"MB";
		}
		
	}
	
	public static String getFileNameByUrl(String url){
		return url.substring(url.lastIndexOf("/")+1);
	}
	
	
}
