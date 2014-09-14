package com.mct.localefilebrowser;

import java.io.File;







import com.mct.client.R;
import com.mct.localefilebrowser.BXFile.FileState;
import com.mct.localefilebrowser.BXFile.MimeType;
import com.mct.localefilebrowser.util.MIMEUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 单个文件查看器
 * 1、未下载的，提供下载、取消下载
 * 2、已下载的根据分类（图片：直接预览，其他文件：提供打开方式）
 * 
 * @author zhanglei
 *
 */
public class BXFileViewer extends Activity {

	private String tag = "BXFileViewer";
	
	private View otherTypeStub;
	private BXFile bxFile;
	private Button fileOpBtn;
	private ImageView fileType;
	private TextView fileName;
	private TextView fileSize;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bxfile_viewer);
		initViews();
		initData();
	}

	private void initData() {
		bxFile = (BXFile) getIntent().getSerializableExtra("bxFile");
		BXFile.FileState state = bxFile.getFileState();
		if(state.equals(FileState.UNLOAD)){
			
			otherTypeStub.setVisibility(View.VISIBLE);
			fileType = (ImageView) otherTypeStub.findViewById(R.id.fileType);
			fileOpBtn = (Button) otherTypeStub.findViewById(R.id.fileOpBtn);
			fileName = (TextView) otherTypeStub.findViewById(R.id.fileName);
			fileSize = (TextView) otherTypeStub.findViewById(R.id.fileSize);
			fileType.setImageResource(BXFileManager.getInstance().getMimeDrawable(bxFile.getMimeType()));
			fileName.setText(bxFile.getFileName());
			fileSize.setText(bxFile.getFileSizeStr());
			fileOpBtn.setText(getString(R.string.download));
			fileOpBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {//下载
				}
			});
		
		}else if(state.equals(FileState.SENDED) || state.equals(FileState.DOWNLOADED)){
			if(bxFile.getMimeType().equals(MimeType.IMAGE)){
				openFile();
			}else{
				otherTypeStub.setVisibility(View.VISIBLE);
				fileType = (ImageView) otherTypeStub.findViewById(R.id.fileType);
				fileOpBtn = (Button) otherTypeStub.findViewById(R.id.fileOpBtn);
				fileName = (TextView) otherTypeStub.findViewById(R.id.fileName);
				fileSize = (TextView) otherTypeStub.findViewById(R.id.fileSize);
				fileType.setImageResource(BXFileManager.getInstance().getMimeDrawable(bxFile.getMimeType()));
				fileName.setText(bxFile.getFileName());
				fileSize.setText(bxFile.getFileSizeStr());
				fileOpBtn.setOnClickListener(new View.OnClickListener() {//打开
					@Override
					public void onClick(View v) {
						openFile();
					}
				});
			}
		}else if(state.equals(FileState.UNSEND)){
			otherTypeStub.setVisibility(View.VISIBLE);
			fileType = (ImageView) otherTypeStub.findViewById(R.id.fileType);
			fileOpBtn = (Button) otherTypeStub.findViewById(R.id.fileOpBtn);
			fileName = (TextView) otherTypeStub.findViewById(R.id.fileName);
			fileSize = (TextView) otherTypeStub.findViewById(R.id.fileSize);
			fileType.setImageResource(BXFileManager.getInstance().getMimeDrawable(bxFile.getMimeType()));
			fileName.setText(bxFile.getFileName());
			fileSize.setText(bxFile.getFileSizeStr());
			fileOpBtn.setText(getString(R.string.fileReSend));
			fileOpBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {//重新发送
					
				}
			});
		}
	}

	private void initViews() {
		setTitle(getString(R.string.fileViewer));
		otherTypeStub = findViewById(R.id.stub_othertype);
	}
	
	public void onClick(View v){
	}
	
	private void openFile(){
		Intent intent = MIMEUtils.getInstance().getPendingIntent(BXFileViewer.this, new File(bxFile.getFilePath()));
		try{
			startActivity(intent);
			finish();
		}catch(Exception e){
			Log.e(tag, e.toString());
			Toast.makeText(BXFileViewer.this, BXFileViewer.this.getString(R.string.attach_open_err), Toast.LENGTH_SHORT).show();
		}
	}
	
}
