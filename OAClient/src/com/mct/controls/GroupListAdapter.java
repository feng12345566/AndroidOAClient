package com.mct.controls;

import java.util.ArrayList;

import com.mct.client.R;
import com.mct.model.GroupInfo;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<GroupInfo> groupInfoList;
    public GroupListAdapter(Context context,ArrayList<GroupInfo> groupInfoList){
    	this.groupInfoList=groupInfoList;
    	inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }
    
    
	public ArrayList<GroupInfo> getGroupInfoList() {
		return groupInfoList;
	}


	public void setGroupInfoList(ArrayList<GroupInfo> groupInfoList) {
		this.groupInfoList = groupInfoList;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return groupInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return groupInfoList.get(position);
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
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.groupnameitem, null);
			viewHolder.tv=(TextView) convertView.findViewById(R.id.list_groupname);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.tv.setText(groupInfoList.get(position).getGroupName());
		return convertView;
	}
	
	class ViewHolder{
		TextView tv;
	}

}
