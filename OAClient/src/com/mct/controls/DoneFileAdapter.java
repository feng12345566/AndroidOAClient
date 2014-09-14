package com.mct.controls;

import java.util.ArrayList;
import java.util.LinkedList;

import com.mct.client.R;
import com.mct.model.TransferredFile;
import com.mct.util.FTPUtils;
import com.mct.util.FileUtil;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DoneFileAdapter extends BaseAdapter{
	private Context context;
	private LinkedList<TransferredFile> doneFileList;
	private LayoutInflater inflater;
	
	/**
	 * @param context
	 */
	public DoneFileAdapter(Context context) {
		super();
		this.context = context;
		inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
	}

	public LinkedList<TransferredFile> getDoneFileList() {
		return doneFileList;
	}

	public void setDoneFileList(LinkedList<TransferredFile> doneFileList) {
		this.doneFileList = doneFileList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return doneFileList.size(); 
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return doneFileList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(arg1==null){
			arg1=inflater.inflate(R.layout.donefileitem, null);
			viewHolder=new ViewHolder();
			viewHolder.fileName=(TextView) arg1.findViewById(R.id.filenametext);
			viewHolder.fileSize=(TextView) arg1.findViewById(R.id.filesizetext);
			viewHolder.fileTypeImage=(ImageView) arg1.findViewById(R.id.donefiletype);
			arg1.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) arg1.getTag();
		}
		TransferredFile transferredFile=doneFileList.get(arg0);
		String fileName=transferredFile.getFileName();
		viewHolder.fileTypeImage.setImageResource(
				FTPUtils.getFileTypeImageByName(fileName));
		viewHolder.fileName.setText(fileName);
		viewHolder.fileSize.setText(FileUtil.getFileSize(transferredFile.getFileSize()));
		return arg1;
	}
	class ViewHolder{
		ImageView fileTypeImage;
		TextView fileName;
		TextView fileSize;
	}

}
