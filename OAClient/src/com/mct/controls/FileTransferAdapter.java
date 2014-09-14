package com.mct.controls;

import java.text.DecimalFormat;
import java.util.LinkedList;

import com.mct.client.R;
import com.mct.model.TransferredFile;
import com.mct.util.Common;
import com.mct.util.FTPUtils;
import com.mct.util.FileUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FileTransferAdapter extends BaseAdapter{
    private LinkedList<TransferredFile> transferredFileList;
    private Context context;
    private LayoutInflater inflater;
    
	/**
	 * @param context
	 */
	public FileTransferAdapter(Context context) {
		super();
		this.context = context;
		inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		transferredFileList=new LinkedList<TransferredFile>();
	}

	
	public LinkedList<TransferredFile> getTransferredFileList() {
		return transferredFileList;
	}


	public void setTransferredFileList(
			LinkedList<TransferredFile> transferredFileList) {
		this.transferredFileList = transferredFileList;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return transferredFileList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return transferredFileList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(contentView==null){
			contentView=inflater.inflate(R.layout.loadingitem, null);
			holder=new ViewHolder();
			holder.fileTypeImage=(ImageView) contentView.findViewById(R.id.filetypeimage);
			holder.loadingFileName=(TextView) contentView.findViewById(R.id.loadingfilename);
			holder.loadingFileProgress=(ProgressBar) contentView.findViewById(R.id.loadingfileprogress);
			holder.loadingFileLength=(TextView) contentView.findViewById(R.id.loadingfilelength);
			holder.loadingFileSpeed=(TextView) contentView.findViewById(R.id.loadingfilespeed);
			holder.loadingFileOptBtn=(ImageView) contentView.findViewById(R.id.loadingfileoptbtn);
			contentView.setTag(holder);
		}else{
			holder=(ViewHolder) contentView.getTag();
		}
		TransferredFile transferredFile=(TransferredFile) transferredFileList.get(position);
		String fileName=transferredFile.getFileName();
		long length=transferredFile.getLength();
		long fileSize=transferredFile.getFileSize();
		holder.fileTypeImage.setImageResource(FTPUtils.getFileTypeImageByName(fileName));
		holder.loadingFileName.setText(fileName);
		holder.loadingFileProgress.setProgress((int) ((length*100)/fileSize));
		holder.loadingFileLength.setText(FileUtil.getFileSize(length)+"/"+FileUtil.getFileSize(fileSize));
		holder.loadingFileSpeed.setText(transferredFile.isStart()?(transferredFile.isStarting()?getSpeed(transferredFile.getSpeed()):"µÈ´ýÖÐ"):(transferredFile.isStarting()?"Ê§°Ü":"ÔÝÍ£"));
		holder.loadingFileOptBtn.setImageResource(transferredFile.isStart()?R.drawable.pause:(transferredFile.isStarting()?R.drawable.restart:R.drawable.startdown));
		holder.loadingFileOptBtn.setTag(new int[]{transferredFile.isStart()?R.drawable.pause:(transferredFile.isStarting()?R.drawable.restart:R.drawable.startdown),(int) transferredFile.getId(),transferredFile.getType()});
		holder.loadingFileOptBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int [] value=(int[])arg0.getTag();
				switch (value[0]) {
				case R.drawable.pause:
					System.out.println("ÔÝÍ£");
					((ImageView)arg0).setImageResource(R.drawable.startdown);
					Intent intent=new Intent(Common.ACTION_REMOVELOAD);
					intent.putExtra("id", (long)value[1]);
					intent.putExtra("type", value[2]);
					context.sendBroadcast(intent);
					break;

				default:
					System.out.println("ÏÂÔØ");
					((ImageView)arg0).setImageResource(R.drawable.pause);
					Intent intent2=new Intent(Common.ACTION_ADDLOAD);
					intent2.putExtra("id", (long)value[1]);
					intent2.putExtra("type", value[2]);
					context.sendBroadcast(intent2);
					break;
				}
			}
		});
		return contentView;
	}
	
	class ViewHolder{
		ImageView fileTypeImage;
		TextView loadingFileName;
		ProgressBar loadingFileProgress;
		TextView loadingFileLength;
		TextView loadingFileSpeed;
		ImageView loadingFileOptBtn;
	}
private String getSpeed(double speed){
	DecimalFormat df = new DecimalFormat("0.00");
	if(speed>=1024){
		return df.format(speed/1024)+"MB/s";
	}else{
	    return df.format(speed)+"KB/s";
	}
	
}
}
