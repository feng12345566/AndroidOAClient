package com.mct.controls;

import java.util.ArrayList;

import com.loopj.android.image.SmartImageView;
import com.mct.client.R;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SelectedMemberImageAdapter extends BaseAdapter{
private ArrayList<String> list;
private Context context;
private LayoutInflater inflater;

	public SelectedMemberImageAdapter(Context context, ArrayList<String> list) {
	super();
	this.list = list;
	this.context = context;
	inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
}

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
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
			convertView=inflater.inflate(R.layout.selectedpersonimagelauout, null);
			viewHolder=new ViewHolder();
			viewHolder.siv=(SmartImageView) convertView.findViewById(R.id.selectedpersonpic);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.siv.setImageUrl("http://nat.nat123.net:15414/headimage/"+list.get(position)+".jpg", R.drawable.touxiang);
		return convertView;
	}
	class ViewHolder{
		SmartImageView siv;
	}

}
