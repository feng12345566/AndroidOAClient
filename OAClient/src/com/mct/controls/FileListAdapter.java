package com.mct.controls;

import it.sauronsoftware.ftp4j.FTPFile;

import java.util.ArrayList;

import com.mct.client.R;
import com.mct.controls.ChatMessageAdapter.ViewHolder;
import com.mct.util.FTPUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class FileListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<FTPFile> fileList;
	private LayoutInflater inflater;
	private ArrayList<FTPFile> selectedFile;
	private int postion;

	/**
	 * @param context
	 * @param fileList
	 */
	public FileListAdapter(Context context, ArrayList<FTPFile> fileList) {
		super();
		this.context = context;
		this.fileList = fileList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		selectedFile = new ArrayList<FTPFile>();
	}

	public ArrayList<FTPFile> getFileList() {
		return fileList;
	}

	public void setFileList(ArrayList<FTPFile> fileList) {
		this.fileList = fileList;
	}

	public ArrayList<FTPFile> getSelectedFile() {
		return selectedFile;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fileList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return fileList.get(arg0);
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
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.fileitem, null);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) arg1
					.findViewById(R.id.remotefiletype);
			viewHolder.fileName = (TextView) arg1
					.findViewById(R.id.remotefilename);
			viewHolder.upLoadTime = (TextView) arg1
					.findViewById(R.id.uploadtime);
			viewHolder.checkBox = (CheckBox) arg1
					.findViewById(R.id.fileischeck);
			arg1.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		FTPFile file = fileList.get(arg0);

		viewHolder.image.setImageResource(FTPUtils.getFileTypeImage(FTPUtils.getFileType(file)));
		viewHolder.fileName.setText(file.getName());
		viewHolder.upLoadTime.setText(file.getModifiedDate().toLocaleString());
		viewHolder.checkBox.setTag(arg0);
		viewHolder.checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton checkBox,
							boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1) {
							System.out.println("位置" + checkBox.getTag() + "被选中");
							selectedFile.add(fileList.get((Integer)checkBox.getTag()));

						} else {
							System.out.println("位置" + checkBox.getTag()
									+ "被取消选中");
							selectedFile.add(fileList.get((Integer)checkBox.getTag()));

						}
					}
				});
		return arg1;
	}

	class ViewHolder {
		ImageView image;
		TextView fileName;
		TextView upLoadTime;
		CheckBox checkBox;
	}

}
