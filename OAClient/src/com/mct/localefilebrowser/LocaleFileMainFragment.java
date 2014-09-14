package com.mct.localefilebrowser;

import java.io.File;

import com.mct.client.R;
import com.mct.localefilebrowser.util.FileUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;




/**
 * 本地文件首页
 * @author fengyouchun
 * @version 创建时间：2014年7月9日 下午6:16:24
 */
public class LocaleFileMainFragment extends Fragment{
	private final int REQUEST = 1;
	private String extSdCardPath;  //sd卡路径
	private TextView localefile_bottom_tv;
	private Button localefile_bottom_btn;
	private Button localefile_cancel_btn;
	private BXFileManager bfm;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.localefile_main, null);
		bfm = BXFileManager.getInstance();
		localefile_bottom_btn = (Button) view.findViewById(R.id.localefile_bottom_btn);
		localefile_bottom_tv = (TextView) view.findViewById(R.id.localefile_bottom_tv);
		localefile_bottom_btn.setText(String.format(getString(R.string.bxfile_choosedCnt),0));
		localefile_cancel_btn=(Button) view.findViewById(R.id.localefile_cancel_btn);//清除已选按钮
		TextView picCnt = (TextView) view.findViewById(R.id.localefile_pic_cnt);//本地图片
		picCnt.setText(String.format(getString(R.string.bxfile_media_cnt), bfm.getMediaFilesCnt(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
		TextView musicCnt = (TextView) view.findViewById(R.id.localefile_music_cnt);//本地音乐
		musicCnt.setText(String.format(getString(R.string.bxfile_media_cnt), bfm.getMediaFilesCnt(getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)));
		TextView videoCnt = (TextView) view.findViewById(R.id.localefile_video_cnt);//本地视频
		localefile_cancel_btn.setVisibility(View.VISIBLE);

		videoCnt.setText(String.format(getString(R.string.bxfile_media_cnt), bfm.getMediaFilesCnt(getActivity(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI)));
		
		extSdCardPath = FileUtils.getExtSdCardPath();
		if(!TextUtils.isEmpty(extSdCardPath)){
			//显示存储设备
			View localefile_sdcard = view.findViewById(R.id.localefile_sdcard);
			View localefile_sdcard2 = view.findViewById(R.id.localefile_sdcard2);
			View localefile_extSdcard = view.findViewById(R.id.localefile_extSdcard);
			localefile_sdcard2.setVisibility(View.VISIBLE);
			localefile_extSdcard.setVisibility(View.VISIBLE);
			localefile_sdcard.setVisibility(View.GONE);
		}
		return view;
	}

	//显示底部 已选文件大小 数目
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		localefile_bottom_tv.setText(bfm.getFilesSizes());//已选文件大小
		int cnt = bfm.getFilesCnt();//已选文件数
		localefile_bottom_btn.setText("确认上传("+cnt+")");
		localefile_bottom_btn.setEnabled(cnt>0);
		localefile_cancel_btn.setEnabled(cnt>0);
	}
	
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(REQUEST == requestCode && 1==resultCode){
			bfm.clear();
			getActivity().finish();
		}else if(REQUEST == requestCode && 2==resultCode){
			getActivity().finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.gc();
	}
	
}
