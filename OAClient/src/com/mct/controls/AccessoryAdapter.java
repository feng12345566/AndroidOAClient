package com.mct.controls;

import java.util.ArrayList;

import com.mct.client.R;
import com.mct.util.FTPUtils;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AccessoryAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private ArrayList<String> pathList;
	public AccessoryAdapter(Context context){
    	 inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
     }
	
	
	public ArrayList<String> getPathList() {
		return pathList;
	}


	public void setPathList(ArrayList<String> pathList) {
		this.pathList = pathList;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pathList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return pathList.get(arg0);
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
			viewHolder.tv=(TextView) contentView.findViewById(R.id.accessoryname);
			contentView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) contentView.getTag();
		}
		String pathNme=pathList.get(arg0); 
		String fileName=pathNme.substring(pathNme.lastIndexOf("/")+1);
		viewHolder.iv.setImageResource(FTPUtils.getFileTypeImageByName(fileName));	
		viewHolder.tv.setText(fileName);
		
		return contentView;
	}

	class ViewHolder{
		ImageView iv;
		TextView tv;
	}
}
