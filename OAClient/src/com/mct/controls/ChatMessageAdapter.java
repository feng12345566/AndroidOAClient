package com.mct.controls;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageTask;
import com.loopj.android.image.SmartImageView;
import com.mct.client.FriendInfoActivity;
import com.mct.client.R;
import com.mct.fragment.FriendInfoFragment;
import com.mct.model.ChatMessage;
import com.mct.model.User;
import com.mct.util.Common;
import com.mct.util.SharePeferenceUtils;
import com.mct.util.UserDbUtil;
import com.mct.util.XmppTool;

public class ChatMessageAdapter extends BaseAdapter {
	private ArrayList<ChatMessage> list;
private Context context;
	private LayoutInflater mInflater;
	private String loginUserName;

	public ChatMessageAdapter(Context context) {
		this.context=context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		loginUserName=SharePeferenceUtils.getInstance(context).getLoginUserName();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == list ? 0 : list.size();
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
    public void addMsg(ChatMessage msg){
    	if(list==null){
    		list=new ArrayList<ChatMessage>();
    	}
    	list.add(msg);
    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.message_item_layout, null);
			holder = new ViewHolder();
			holder.tvInBody = (TextView) convertView
					.findViewById(R.id.tv_in_sms_body);
			holder.tvInUser = (TextView) convertView
					.findViewById(R.id.tv_in_sms_user_name);
			holder.tvOutBody = (TextView) convertView
					.findViewById(R.id.tv_out_sms_body);
			holder.tvOutUser = (TextView) convertView
					.findViewById(R.id.tv_out_sms_user_name);
			holder.ivInIcon = (SmartImageView) convertView
					.findViewById(R.id.in_user_icon);
			holder.ivOutIcon = (SmartImageView) convertView
					.findViewById(R.id.out_user_icon);
			holder.msgTime=(TextView) convertView.findViewById(R.id.msgoftime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ChatMessage msg = list.get(position);
		String body = msg.getBody();
		 XmppTool.shareConnectionManager(context);
		String time = XmppTool.getSMSDate(msg.getDate());
		 holder.msgTime.setText(time);
		if (msg.getType() == Common.MSG_IN) {
			// 收到的信息
			convertView.findViewById(R.id.in_item_layout).setVisibility(
					View.VISIBLE);
			convertView.findViewById(R.id.out_item_layout).setVisibility(
					View.GONE);
			holder.tvInBody.setText(body);
			String address = msg.getAddress();
			if (address.contains("@")) {
				address = address.substring(0, address.indexOf("@"));
			}
			holder.tvInUser.setText(UserDbUtil.shareUserDb(context).getUserNameById(address));
			holder.ivInIcon.setImageUrl(Common.HTTPSERVICE+address+".jpg", R.drawable.touxiang);
		} else if (msg.getType() == Common.MSG_OUT) {
			// 已发出
			convertView.findViewById(R.id.in_item_layout).setVisibility(
					View.GONE);
			convertView.findViewById(R.id.out_item_layout).setVisibility(
					View.VISIBLE);
			holder.tvOutBody.setText(body);
			
			holder.tvOutUser.setText(loginUserName);
			String loginUserId=XmppTool.loginUser.getUserId();
			holder.ivOutIcon.setImageUrl(Common.HTTPSERVICE+loginUserId+".jpg", R.drawable.touxiang);
			holder.ivOutIcon.setTag(loginUserId);
			holder.ivOutIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,FriendInfoActivity.class);
					intent.putExtra("userid",(String)(v.getTag()));
					context.startActivity(intent);
				}
			});
		} else {
			convertView.findViewById(R.id.in_item_layout).setVisibility(
					View.GONE);
			convertView.findViewById(R.id.out_item_layout).setVisibility(
					View.GONE);
		}
		return convertView;
	}

	public class ViewHolder {
		TextView tvInBody;
	    TextView tvInUser;
		TextView tvOutBody;
		TextView tvOutUser;
		SmartImageView ivInIcon;
		SmartImageView ivOutIcon;
		TextView msgTime;
		
		public SmartImageView getIvInIcon() {
			return ivInIcon;
		}


		public SmartImageView getIvOutIcon() {
			return ivOutIcon;
		}

		public TextView getTvInUser() {
			return tvInUser;
		}
		
	}

	public ArrayList<ChatMessage> getList() {
		return list;
	}

	public void setList(ArrayList<ChatMessage> list) {
		this.list = list;
	}

}
