package com.mct.controls;

import java.util.ArrayList;
import java.util.HashMap;

import com.mct.client.R;
import com.mct.client.SortListViewActivity;
import com.mct.flow.FlowNode;
import com.mct.flowutil.FlowDbUtil;
import com.mct.model.User;
import com.mct.util.UserDbUtil;
import com.mct.util.XmppTool;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class FlowsendAdapter extends BaseAdapter{
private Context context;//context对象
private ArrayList<FlowNode> nextNodeList;//节点列表
private LayoutInflater inflater;
private int selected;
private Handler handler;
private ArrayList<ArrayList<User>> selectedUserList;//选中用户列表
private int checkPostion;
private ArrayList<EditText> editTextList;
	/**
 * @param context
 */
public FlowsendAdapter(Context context,Handler handler) {
	this.context = context;
	this.handler=handler;
	nextNodeList=new ArrayList<FlowNode>();
	selectedUserList=new ArrayList<ArrayList<User>>();
	editTextList=new ArrayList<EditText>();
	inflater=(LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
}

	public ArrayList<FlowNode> getNextNodeList() {
		return nextNodeList;
	}

	public void setNextNodeList(ArrayList<FlowNode> nextNodeList) {
		this.nextNodeList = nextNodeList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return nextNodeList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return nextNodeList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		selected=position;
		if(view==null){
			view=inflater.inflate(R.layout.sendpersonitem, null);
		}
		TextView nodeTitle=(TextView) view.findViewById(R.id.nodetitletext);
		EditText selectedPerson=(EditText) view.findViewById(R.id.selectedperson);
		editTextList.add(selectedPerson);
		GridView gridView=(GridView) view.findViewById(R.id.personcheckview);
		FlowNode flowNode=nextNodeList.get(position);
		nodeTitle.setText(flowNode.getNodeName());
		ArrayList<User> userList=FlowDbUtil.shareFlowUtil(context).getUserList(flowNode.getFlowId(), flowNode.getNodeNo());
		PersonCheckAdapter pCheckAdapter=new PersonCheckAdapter(userList);
		gridView.setAdapter(pCheckAdapter);
		return view;
	}

	public ArrayList<ArrayList<User>> getSelectedUserList() {
		return selectedUserList;
	}

	public void setSelectedUserList(ArrayList<ArrayList<User>> selectedUserList) {
		this.selectedUserList = selectedUserList;
		notifyDataSetChanged();
	}

	class PersonCheckAdapter extends BaseAdapter{
        private ArrayList<User> mUserList;//可选用户列表
        private ArrayList<User> mSelectedUserList;//被选中的用户列表
        
		/**
		 * @param mUserList
		 */
		public PersonCheckAdapter(ArrayList<User> mUserList) {
			super();
			this.mUserList = mUserList;
			mSelectedUserList=new ArrayList<User>();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mUserList.size()>8?9:mUserList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mUserList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View contentView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(contentView==null){
				contentView=inflater.inflate(R.layout.personcheckitem, null);
			}
			CheckBox selectUser=(CheckBox) contentView.findViewById(R.id.selectuser);
			TextView moreBtn=(TextView) contentView.findViewById(R.id.moreuserbtn);
			if(arg0==8){
				moreBtn.setVisibility(View.VISIBLE);
				selectUser.setVisibility(View.GONE);
				moreBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Message msg=new Message();
						msg.what=0;
						msg.obj=selected;
						handler.sendMessage(msg);
					}
				});
			}else{
				moreBtn.setVisibility(View.GONE);
				selectUser.setTag(arg0);
				selectUser.setVisibility(View.VISIBLE);
				selectUser.setText(mUserList.get(arg0).getUserName());
				selectUser.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						// TODO Auto-generated method stub
						if(arg1){
							mSelectedUserList.add(mUserList.get((Integer)arg0.getTag()));
						}else{
							mSelectedUserList.remove(mUserList.get((Integer)arg0.getTag()));
						}
						if(selectedUserList.size()<=selected){
							selectedUserList.add(mSelectedUserList);
						}else{
							selectedUserList.set(selected, mSelectedUserList);
						}
						selectedUserList.set(selected, mSelectedUserList);
						StringBuffer sb=new StringBuffer();
						for(int i=0;i<mSelectedUserList.size();i++){
							sb.append(mSelectedUserList.get(i).getUserName());
							if(mSelectedUserList.size()>1&&i<mSelectedUserList.size()-1){
								sb.append(",");
							}
						}
						editTextList.get(selected).setText(sb.toString());
					}
				});
			}
			return contentView;
		}
		
	}

}
