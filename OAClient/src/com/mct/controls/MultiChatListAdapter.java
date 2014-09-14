package com.mct.controls;

import java.util.HashMap;
import java.util.LinkedList;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.mct.client.R;
import com.mct.util.RecordUtil;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MultiChatListAdapter extends BaseAdapter {
    private Context context;
    private LinkedList<HashMap<String, Object>> list;
    private LayoutInflater inflater;
	public MultiChatListAdapter(Context context,
			LinkedList<HashMap<String, Object>> list) {
		super();
		this.context = context;
		this.list = list;
		inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
	}

	public LinkedList<HashMap<String, Object>> getList() {
		return list;
	}

	public void setList(LinkedList<HashMap<String, Object>> list) {
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
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.groupchatitem,parent,false);
			viewHolder.tv=(TextView) convertView.findViewById(R.id.members);
			viewHolder.ll=(LinearLayout) convertView.findViewById(R.id.back);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		((SwipeListView)parent).recycle(convertView, position);
		viewHolder.tv.setText((String)(list.get(position).get("members")));
		viewHolder.ll.setTag(position);
		viewHolder.ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				long id=(Long) (list.get((Integer) v.getTag()).get("id"));
				Log.e("id", id+"");
				if(RecordUtil.shareRecordUtil(context).deleteConversation(id)){
					list.remove((Integer) v.getTag());
					notifyDataSetChanged();
				}
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		TextView tv;
		LinearLayout ll;
	}

}
