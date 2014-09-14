package com.mct.controls;

import java.util.ArrayList;

import com.loopj.android.image.SmartImageView;
import com.mct.client.R;
import com.mct.model.CustomMessage;
import com.mct.util.Common;
import com.mct.util.RecordUtil;
import com.mct.util.UserDbUtil;
import com.mct.util.XmppTool;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InboxAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
    private ArrayList<CustomMessage> customtMessagesList;
	public InboxAdapter(Context context) {
		this.context=context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	

	public ArrayList<CustomMessage> getCustomtMessagesList() {
		return customtMessagesList;
	}



	public void setCustomtMessagesList(ArrayList<CustomMessage> customtMessagesList) {
		this.customtMessagesList = customtMessagesList;
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return customtMessagesList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return customtMessagesList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.inbox_list_item, null);
			holder = new ViewHolder();
			holder.pic = (SmartImageView) convertView.findViewById(R.id.sender_pic);
			holder.name = (TextView) convertView
					.findViewById(R.id.sender_name);
			holder.title = (TextView) convertView.findViewById(R.id.inbox_title);
			holder.time = (TextView) convertView.findViewById(R.id.inbox_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CustomMessage customMessage=customtMessagesList.get(arg0);
		holder.pic.setImageUrl(Common.HTTPSERVICE+customMessage.getSender()+".jpg", R.drawable.touxiang);
		if(customMessage.getSender().equals(XmppTool.loginUser.getUserId())){
			SharedPreferences settings=context.getSharedPreferences("setting_info", 0);
			holder.name.setText(settings.getString("username", ""));
		}else{
		 holder.name.setText(UserDbUtil.shareUserDb(context).getUserNameById(customMessage.getSender()));
		}
		holder.title.setText("Ö÷Ìâ£º"+customMessage.getTitle());
		holder.time.setText(XmppTool.getSMSDate(customMessage.getTime()));
		return convertView;
	}

	class ViewHolder {
		SmartImageView pic;
		TextView name;
		TextView title;
		TextView time;
	}
}
