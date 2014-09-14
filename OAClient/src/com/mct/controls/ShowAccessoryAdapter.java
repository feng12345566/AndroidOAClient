package com.mct.controls;

import java.util.ArrayList;

import com.mct.client.R;
import com.mct.util.FTPUtils;
import com.mct.util.FileUtil;
import com.mct.view.NumberProgressBar;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowAccessoryAdapter extends BaseAdapter {
	private LayoutInflater inflater;
    private ArrayList<String> accessoryList;
    
	public ShowAccessoryAdapter(Context context) {
		inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
	}

	public void setAccessoryList(ArrayList<String> accessoryList) {
		this.accessoryList = accessoryList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return accessoryList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return accessoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.accessorylistviewitem, null);
			viewHolder=new ViewHolder();
			viewHolder.accessoryPic=(ImageView) convertView.findViewById(R.id.accessorypic);
			viewHolder.accessoryName=(TextView) convertView.findViewById(R.id.accessoryname);
			viewHolder.numberProgressBar=(NumberProgressBar) convertView.findViewById(R.id.donelength);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.accessoryPic.setImageResource(FTPUtils.getFileTypeImageByName(FileUtil.getFileNameByUrl(accessoryList.get(position))));
		viewHolder.accessoryName.setText(FileUtil.getFileNameByUrl(accessoryList.get(position)));
		viewHolder.numberProgressBar.setVisibility(View.GONE);
		return convertView;
	}
	
	class ViewHolder{
		ImageView accessoryPic;
		TextView accessoryName;
		NumberProgressBar numberProgressBar;
	}

}
