package com.mct.controls;

import java.util.ArrayList;
import java.util.HashMap;

import com.mct.client.R;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCenterAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, Object>> list;
private LayoutInflater inflater;
	/**
	 * @param context
	 */
	public MyCenterAdapter(Context context) {
		super();
		this.context = context;
		inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
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
			contentView=inflater.inflate(R.layout.mycenteritem, null);
			viewHolder=new ViewHolder();			
			contentView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) contentView.getTag();
		}
		viewHolder.iv=(ImageView) contentView.findViewById(R.id.imgbtn);
		viewHolder.tv=(TextView) contentView.findViewById(R.id.txtbtn);
		viewHolder.iv.setImageResource((Integer)list.get(arg0).get("iv"));
		viewHolder.tv.setText((String)list.get(arg0).get("tv"));
		return contentView;
	}

	public ArrayList<HashMap<String, Object>> getList() {
		return list;
	}

	public void setList(ArrayList<HashMap<String, Object>> list) {
		this.list = list;
	}

	class ViewHolder {
		ImageView iv;
		TextView tv;
	}
}
