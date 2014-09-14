package com.mct.controls;

import java.util.LinkedList;

import com.loopj.android.image.SmartImageView;
import com.mct.client.R;
import com.mct.model.Conversation;
import com.mct.model.User;
import com.mct.util.Common;
import com.mct.util.ImageUtil;
import com.mct.util.RecordUtil;
import com.mct.util.UserDbUtil;
import com.mct.util.XmppTool;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConversationListAdapter extends BaseAdapter {
	private Context context;
	private LinkedList<Conversation> conversationList;
	private LayoutInflater inflater;

	/**
	 * @param context
	 * @param list
	 */
	public ConversationListAdapter(Context context) {
		super();
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      if(conversationList==null){
    	  conversationList=new LinkedList<Conversation>();
      }
	}

	public LinkedList<Conversation> getList() {
		return conversationList;
	}

	public void setList(LinkedList<Conversation> list) {
		conversationList.clear();
		this.conversationList.addAll(list) ;
	}

	/**
	 * 在头部添加会话
	 * 
	 * @param conversation
	 */
	public void addItem(Conversation conversation) {
		conversationList.addFirst(conversation);
	}

	/**
	 * 更新会话
	 * 
	 * @param position
	 * @param conversation
	 */
	public void updateItem(int position, Conversation conversation) {
		if (position < getCount()) {
			// mList.set(position, conversation);
			conversationList.remove(position);
			conversationList.addFirst(conversation);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		System.out.println("加载" + conversationList.size() + "条数据");
		return conversationList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return conversationList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.convesationitem, null);
			holder = new ViewHolder();
			holder.pic = (SmartImageView) convertView.findViewById(R.id.pic);
			holder.name = (TextView) convertView
					.findViewById(R.id.friend_username);
			holder.msg = (TextView) convertView.findViewById(R.id.lastmsg);
			holder.time = (TextView) convertView.findViewById(R.id.lasttime);
			holder.unReadNews = (TextView) convertView.findViewById(R.id.unreadnews);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		    Conversation conversation=conversationList.get(position);
		    switch (conversation.getType()) {
			case 0:
				String withUserId=conversation.getWith_user();
				Log.e("with", withUserId);
				
				if(conversation.getIsGroup()==1){
					holder.pic.setImageResource(R.drawable.qunzu);
					holder.name.setText(RecordUtil.shareRecordUtil(context).idToName(withUserId.split("@")[0]));
				}else{
					String userName=UserDbUtil.shareUserDb(context).getUserNameById(withUserId);
					holder.name.setText(userName);
				  holder.pic.setImageUrl(Common.HTTPSERVICE+withUserId+".jpg", R.drawable.touxiang);
				}
				
				
				holder.msg.setText((String)conversation.getSnippet());
				
				break;

			case 1:
				holder.pic.setImageUrl(Common.HTTPSERVICE+"email.jpg",R.drawable.email_icon);
				holder.name.setText("邮件提醒");
				//[%email%]
				holder.msg.setText(conversation.getSnippet().split("@")[0].substring(9));
				break;
			case 2:
				holder.pic.setImageUrl(Common.HTTPSERVICE+"news.jpg",R.drawable.news);
				holder.name.setText("企业公告");
				//[%news%]
				holder.msg.setText(conversation.getSnippet().split("@")[0].substring(8));
				break;
			case 3:
				holder.pic.setImageUrl(Common.HTTPSERVICE+"system.jpg",R.drawable.system);
				holder.name.setText("系统通知");
				//[%sysm%]
				holder.msg.setText(conversation.getSnippet().split("@")[0].substring(8));
				break;
			case 4:
				holder.pic.setImageUrl(Common.HTTPSERVICE+"flow.jpg",R.drawable.flow_icon);
				holder.name.setText("流程提醒");
				//[%flow%]
				holder.msg.setText(conversation.getSnippet().split("@")[0].substring(8));
				break;
			}
			
			holder.time.setText(XmppTool.getSMSDate((long) conversationList.get(position)
					.getDate()));
			int num=(int) conversationList.get(position).getUnReadNum();
			if(num>0){
				holder.unReadNews.setVisibility(View.VISIBLE);
				holder.unReadNews.setText(String.valueOf(num));
			}else{
				holder.unReadNews.setVisibility(View.GONE);
			}
		return convertView;
	}

	public class ViewHolder {
		SmartImageView pic;
		TextView name;
		TextView msg;
		TextView time;
		TextView unReadNews;

	}
}
