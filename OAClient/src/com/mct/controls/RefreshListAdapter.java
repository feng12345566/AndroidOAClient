package com.mct.controls;

import java.util.ArrayList;

import com.mct.client.R;
import com.mct.model.ChatMessage;
import com.mct.util.TimeRender;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RefreshListAdapter extends BaseAdapter{
private Context context;
private ArrayList<ChatMessage> contentList;
private LayoutInflater inflater;

	/**
 * @param context
 */
public RefreshListAdapter(Context context) {
	super();
	this.context = context;
	
	inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
}

	
    public ArrayList<ChatMessage> getContentList() {
		return contentList;
	}


	public void setContentList(ArrayList<ChatMessage> contentList) {
		if(contentList!=null){
		   this.contentList = contentList;
		}else{
			this.contentList=new ArrayList<ChatMessage>();
		}
	}


	public void addData(ArrayList<ChatMessage> list){
    	contentList.addAll(list);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contentList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return contentList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(contentView==null){
			contentView=inflater.inflate(R.layout.refreshitem, null);
			viewHolder=new ViewHolder();
			viewHolder.notificationTitle=(TextView) contentView.findViewById(R.id.notificationtitletext);
			viewHolder.notificationTime=(TextView) contentView.findViewById(R.id.notificationtimetext);
			contentView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) contentView.getTag();
		}
		String body=contentList.get(position).getBody();
		long time=contentList.get(position).getDate();
		viewHolder.notificationTitle.setText(body.replace("[%news%]", "").split("@")[0]);
		viewHolder.notificationTime.setText(TimeRender.getDate(time,"yy-MM-dd HH:mm"));
		return contentView;
	}

	
	class ViewHolder{
		TextView  notificationTitle;
		TextView  notificationTime;
	}
}
