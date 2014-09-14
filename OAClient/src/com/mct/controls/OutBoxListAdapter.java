package com.mct.controls;

import java.util.LinkedList;

import com.mct.client.R;
import com.mct.model.CustomMessage;
import com.mct.util.XmppTool;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class OutBoxListAdapter extends BaseAdapter {
    private Context context;
    private LinkedList<CustomMessage> list;
    private LayoutInflater inflater;
    private int isAllChecked=-1;
    
	public OutBoxListAdapter(Context context, LinkedList<CustomMessage> list) {
		super();
		this.context = context;
		this.list = list;
		inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
	}

	public LinkedList<CustomMessage> getList() {
		return list;
	}

	public void setList(LinkedList<CustomMessage> list) {
		this.list = list;
	}

	
	public int getIsAllChecked() {
		return isAllChecked;
	}

	public void setIsAllChecked(int isAllChecked) {
		this.isAllChecked = isAllChecked;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
			convertView=inflater.inflate(R.layout.outbox_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.time=(TextView) convertView.findViewById(R.id.outbox_time);
			viewHolder.title=(TextView) convertView.findViewById(R.id.outbox_title);
			viewHolder.content=(TextView) convertView.findViewById(R.id.outbox_content);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		CustomMessage customMessage=list.get(position);
		viewHolder.time.setText(XmppTool.getSMSDate(customMessage.getTime()));
		viewHolder.title.setText(customMessage.getTitle());
		viewHolder.content.setText(customMessage.getContent());
		return convertView;
	}
	
	class ViewHolder{
		TextView time;
		TextView title;
		TextView content;
		
	}

}
