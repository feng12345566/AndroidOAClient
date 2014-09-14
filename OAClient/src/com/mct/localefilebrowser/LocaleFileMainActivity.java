package com.mct.localefilebrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mct.client.BaseActivity;
import com.mct.client.FileShareActivity;
import com.mct.client.PostMessageActivity;
import com.mct.client.R;
import com.mct.localefilebrowser.util.FileUtils;

public class LocaleFileMainActivity extends BaseActivity {
	private final int REQUEST = 1;
	private String extSdCardPath; // sd卡路径
	private BXFileManager bfm;
	private String opt;// 从哪个页面跳转而来
	private Button sureBtn;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Fragment localeFileMainFragment = new LocaleFileMainFragment();
		extSdCardPath = FileUtils.getExtSdCardPath();
		bfm = BXFileManager.getInstance();
		setParameter("本地文件", localeFileMainFragment, null, "文件共享", null);
		opt = getIntent().getStringExtra("opt");
		sureBtn=(Button)findViewById(R.id.localefile_bottom_btn);
	}

	@Override
	public void setParameter(String title, Fragment contentFragment,
			Bundle bundle, String leftTitle, String rightTitle) {
		// TODO Auto-generated method stub
		super.setParameter(title, contentFragment, bundle, leftTitle,
				rightTitle);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.localefile_music:
			Intent intent5 = new Intent(this, LocaleMediaFileBrowser.class);
			intent5.putExtra("title", getString(R.string.bxfile_music));
			intent5.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent5, REQUEST);
			break;
		case R.id.localefile_video:
			Intent intent6 = new Intent(this, LocaleMediaFileBrowser.class);
			intent6.putExtra("title", getString(R.string.bxfile_video));
			intent6.setData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent6, REQUEST);
			break;
		case R.id.localefile_picture:
			Intent intent7 = new Intent(this, LocaleFileGallery.class);
			intent7.putExtra("title", getString(R.string.bxfile_image));
			startActivityForResult(intent7, REQUEST);
			break;
		case R.id.localefile_download:
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				Intent intent1 = new Intent(this, LocaleFileBrowser.class);
				intent1.putExtra("title", getString(R.string.bxfile_download));
				String base = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + File.separator;
				String downloadPath = base + "download";
				File file = new File(downloadPath);
				if (!file.exists())
					file.mkdir();
				intent1.putExtra("startPath", downloadPath);
				startActivityForResult(intent1, REQUEST);
			} else {
				Toast.makeText(this, getString(R.string.SDCardNotMounted),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.localefile_ram:
			Intent intent2 = new Intent(this, LocaleFileBrowser.class);
			intent2.putExtra("title", getString(R.string.bxfile_ram));
			intent2.putExtra("startPath", "/");
			startActivityForResult(intent2, REQUEST);
			break;
		case R.id.localefile_sdcard:
		case R.id.localefile_sdcard2:
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				Intent intent3 = new Intent(this, LocaleFileBrowser.class);
				intent3.putExtra("title", getString(R.string.bxfile_sdcard));
				intent3.putExtra("startPath", Environment
						.getExternalStorageDirectory().getAbsolutePath());
				startActivityForResult(intent3, REQUEST);
			} else {
				Toast.makeText(this, getString(R.string.SDCardNotMounted),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.localefile_extSdcard:
			Intent intent4 = new Intent(this, LocaleFileBrowser.class);
			intent4.putExtra("title", getString(R.string.bxfile_extsdcard));
			intent4.putExtra("startPath", extSdCardPath);
			startActivityForResult(intent4, REQUEST);
			break;

		case R.id.localefile_bottom_btn:
			List<BXFile> list = bfm.getChoosedFiles();
			ArrayList<String> pathList = null;
			if (list != null) {
				pathList = new ArrayList<String>();
				for (int i = 0; i < list.size(); i++) {
					pathList.add(list.get(i).getFilePath());
				}
			}
			Intent intent9 = new Intent();
			intent9.putStringArrayListExtra("result", pathList);
			if (opt.equals("upload")) {
				intent9.setClass(this,FileShareActivity.class);	
				setResult(0, intent9);
			}else if(opt.equals("addaccessory")){
				intent9.setClass(this,PostMessageActivity.class);
				setResult(1, intent9);
			}
			bfm.clear();
			this.finish();
			break;
		case R.id.localefile_cancel_btn:
			bfm.clear();			
			sureBtn.setText("确定上传("+bfm.getFilesSizes()+")");
			sureBtn.setEnabled(false);
			break;
		}
	}
	
	
}
