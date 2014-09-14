package com.mct.sortlistview;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.image.SmartImageView;
import com.mct.client.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SortGroupMemberAdapter extends BaseAdapter implements SectionIndexer {
	private List<GroupMemberBean> list = null;
	private Context mContext;
	private Handler handler;
	private ArrayList<String> selectedList;

	public SortGroupMemberAdapter(Context mContext, List<GroupMemberBean> list,Handler handler) {
		this.mContext = mContext;
		this.list = list;
		this.handler=handler;
	}

	public void setSelectedList(ArrayList<String> selectedList) {
		this.selectedList = selectedList;
	}

	/**
	 * ��ListView���ݷ����仯ʱ,���ô˷���������ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<GroupMemberBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final GroupMemberBean mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.activity_group_member_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.pic = (SmartImageView) view.findViewById(R.id.receiver_pic);
			viewHolder.cb = (CheckBox) view.findViewById(R.id.receiver_check);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.cb.setTag(mContent.getUserId());
		
		if(selectedList.contains(mContent.getUserId())){
			viewHolder.cb.setChecked(true);
		}else{
			viewHolder.cb.setChecked(false);
		}
		viewHolder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					//������ѡ�б�
					Message msg=new Message();
					msg.what=7;
					msg.obj=(String)(buttonView.getTag());
					handler.sendMessage(msg);
				}else{
					//�Ƴ���ѡ�б�
					Message msg=new Message();
					msg.what=8;
					msg.obj=(String)(buttonView.getTag());
					handler.sendMessage(msg);
				}
			}
		});
		// ����position��ȡ���������ĸ��Char asciiֵ
		int section = getSectionForPosition(position);

		// �����ǰλ�õ��ڸ÷�������ĸ��Char��λ�� ������Ϊ�ǵ�һ�γ���
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.pic.setImageUrl("http://nat.nat123.net:15414/headimage/"+list.get(position).getUserId()+".jpg", R.drawable.touxiang);
		viewHolder.tvTitle.setText(this.list.get(position).getName());

		return view;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		SmartImageView pic;
		CheckBox cb;
	}

	/**
	 * ����ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#���档
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// �������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}