package com.mct.controls;

import java.util.ArrayList;
import java.util.HashMap;

import com.mct.client.R;
import com.mct.model.TransferredFile;
import com.mct.util.FTPUtils;
import com.mct.view.ItemPopWindow;
import com.mct.view.NumberProgressBar;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AccessoryListAdapter extends BaseAdapter{
private Context context;
private ArrayList<TransferredFile> list;
private LayoutInflater inflater;
	/**
 * @param context
 */
public AccessoryListAdapter(Context context) {
	super();
	this.context = context;
	inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
}

	public ArrayList<TransferredFile> getList() {
		return list;
	}

	public void setList(ArrayList<TransferredFile> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View contentView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(contentView==null){
			contentView=inflater.inflate(R.layout.accessorylistviewitem, null);
			viewHolder=new ViewHolder();
			viewHolder.iv=(ImageView) contentView.findViewById(R.id.accessorypic);
			viewHolder.progress=(NumberProgressBar) contentView.findViewById(R.id.donelength);
			viewHolder.tv=(TextView) contentView.findViewById(R.id.accessoryname);
			contentView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) contentView.getTag();
		}
		TransferredFile transferredFile=list.get(arg0); 
		String fileName=transferredFile.getFileName();
		viewHolder.iv.setImageResource(FTPUtils.getFileTypeImageByName(fileName));
		if(transferredFile.getLength()<transferredFile.getFileSize()){
			viewHolder.progress.setVisibility(View.VISIBLE);
		viewHolder.progress.setProgress((int) (transferredFile.getLength()*100/transferredFile.getFileSize()));
		}else{
			viewHolder.progress.setVisibility(View.GONE);
		}
		viewHolder.tv.setText(fileName);
		
		return contentView;
	}

	class ViewHolder{
		ImageView iv;
		NumberProgressBar progress;
		TextView tv;
	}
	
}
