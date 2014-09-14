package com.mct.localefilebrowser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mct.client.R;
import com.mct.localefilebrowser.BXFile.MimeType;
import com.mct.localefilebrowser.util.FileUtils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


/***
 * 本地文件管理器
 * 
 * @author zhanglei
 *
 */
public class BXFileManager {
	
	private static BXFileManager instance;
	private final Map<String ,MimeType> map;//mimeType集合
	private final Map<MimeType,Integer> resMap;//mimeType对应图片资源集合
	private final List<BXFile> choosedFiles;//已选择文件集合
	public static BXFileManager getInstance(){
		if(null == instance){
			instance = new BXFileManager();
		}
		return instance;
	}
	
	//初始化数据
	private BXFileManager(){
		map = new HashMap<String,MimeType>();
		map.put(".amr", MimeType.MUSIC);
		map.put(".mp3", MimeType.MUSIC);
		map.put(".ogg", MimeType.MUSIC);
		map.put(".wav", MimeType.MUSIC);
		map.put(".3gp", MimeType.VIDEO);
		map.put(".mp4", MimeType.VIDEO);
		map.put(".rmvb", MimeType.VIDEO);
		map.put(".mpeg", MimeType.VIDEO);
		map.put(".mpg", MimeType.VIDEO);
		map.put(".asf", MimeType.VIDEO);
		map.put(".avi", MimeType.VIDEO);
		map.put(".wmv", MimeType.VIDEO);
		map.put(".apk", MimeType.APK);
		map.put(".bmp", MimeType.IMAGE);
		map.put(".gif", MimeType.IMAGE);
		map.put(".jpeg", MimeType.IMAGE);
		map.put(".jpg", MimeType.IMAGE);
		map.put(".png", MimeType.IMAGE);
		map.put(".doc", MimeType.DOC);
		map.put(".docx", MimeType.DOC);
		map.put(".rtf", MimeType.DOC);
		map.put(".wps", MimeType.DOC);
		map.put(".xls", MimeType.XLS);
		map.put(".xlsx", MimeType.XLS);
		map.put(".gtar", MimeType.RAR);
		map.put(".gz", MimeType.RAR);
		map.put(".zip", MimeType.RAR);
		map.put(".tar", MimeType.RAR);
		map.put(".rar", MimeType.RAR);
		map.put(".jar", MimeType.RAR);
		map.put(".htm", MimeType.HTML);
		map.put(".html", MimeType.HTML);
		map.put(".xhtml", MimeType.HTML);
		map.put(".java", MimeType.TXT);
		map.put(".txt", MimeType.TXT);
		map.put(".xml", MimeType.TXT);
		map.put(".log", MimeType.TXT);
		map.put(".pdf", MimeType.PDF);
		map.put(".ppt", MimeType.PPT);
		map.put(".pptx", MimeType.PPT);
		
		resMap = new HashMap<MimeType,Integer>();
		resMap.put(MimeType.APK, R.drawable.bxfile_file_apk);
		resMap.put(MimeType.DOC, R.drawable.bxfile_file_doc);
		resMap.put(MimeType.HTML, R.drawable.bxfile_file_html);
		resMap.put(MimeType.IMAGE, R.drawable.bxfile_file_unknow);
		resMap.put(MimeType.MUSIC, R.drawable.bxfile_file_mp3);
		resMap.put(MimeType.VIDEO, R.drawable.bxfile_file_video);
		resMap.put(MimeType.PDF, R.drawable.bxfile_file_pdf);
		resMap.put(MimeType.PPT, R.drawable.bxfile_file_ppt);
		resMap.put(MimeType.RAR, R.drawable.bxfile_file_zip);
		resMap.put(MimeType.TXT, R.drawable.bxfile_file_txt);
		resMap.put(MimeType.XLS, R.drawable.bxfile_file_xls);
		resMap.put(MimeType.UNKNOWN, R.drawable.bxfile_file_unknow);
		
		choosedFiles = new ArrayList<BXFile>();
	}
	
	public MimeType getMimeType(String exspansion){
		return map.get(exspansion.toLowerCase());
	}
	public Integer getMimeDrawable(MimeType type){
		return resMap.get(type);
	}

	//已选择文件集合
	public List<BXFile> getChoosedFiles() {
		return choosedFiles;
	}
	
	//已选择文件大小 
	public String getFilesSizes(){
		long sum = 0;
		for(BXFile f:choosedFiles){
			sum+=f.getFileSize();
		}
		return FileUtils.getFileSizeStr(sum);
	}
	
	//已选择文件数
	public int getFilesCnt(){
		return choosedFiles.size();
	}
	
	public void clear(){
		choosedFiles.clear();
	}
	
	//查找external多媒体文件
	public synchronized List<BXFile> getMediaFiles(Activity cxt , Uri uri) {
		Cursor mCursor = cxt.managedQuery(
				uri,
				new String[] {MediaStore.Audio.Media.DATA}, null,
				null, " date_modified desc");
		cxt.startManagingCursor(mCursor);
		int count = mCursor.getCount();
		if(count>0){
			List<BXFile> data = new ArrayList<BXFile>();
			if (mCursor.moveToFirst()) {
				do {
					BXFile.Builder builder = new BXFile.Builder(mCursor.getString(0));
					BXFile bxfile = builder.build();
					if(null != bxfile)
						data.add(bxfile);
				} while (mCursor.moveToNext());
			}
			return data;
		}else{
			return null;
		}
	}
	
	//external多媒体文件计数
	public synchronized int getMediaFilesCnt(Activity cxt , Uri uri) {
		Cursor mCursor = cxt.managedQuery(
				uri,
				new String[] {MediaStore.Audio.Media.DATA}, null,
				null, null);
		cxt.startManagingCursor(mCursor);
		int cnt = mCursor.getCount();
		return cnt;
	}

}
