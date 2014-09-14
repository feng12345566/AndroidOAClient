package com.mct.fragment;

import com.loopj.android.image.SmartImageView;
import com.mct.client.R;
import com.mct.model.User;
import com.mct.util.Common;
import com.mct.util.UserDbUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FriendInfoFragment extends Fragment {
	private String userId;
	private SmartImageView friendHeadPic;
	private TextView showFriendUsername;
	private TextView showFriendMobilePhone;
	private TextView showFriendPhone;
	private TextView showFriendSex;
	private TextView showFriendOrg;
	private TextView showFriendPostion;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_friendinfo, null);
		userId = getArguments().getString("userid");
		friendHeadPic = (SmartImageView) view.findViewById(R.id.friendheadpic);
		showFriendUsername = (TextView) view
				.findViewById(R.id.showfriendusername);
		showFriendMobilePhone = (TextView) view
				.findViewById(R.id.showfriendmobilephone);
		showFriendPhone = (TextView) view.findViewById(R.id.showfriendphone);
		showFriendSex = (TextView) view.findViewById(R.id.showfriendsex);
		showFriendOrg = (TextView) view.findViewById(R.id.showfriendorg);
		showFriendPostion = (TextView) view
				.findViewById(R.id.showfriendposition);
		User user = UserDbUtil.shareUserDb(getActivity()).getUserInfo(userId);
		friendHeadPic.setImageUrl(Common.HTTPSERVICE + userId + ".jpg",
				R.drawable.touxiang);
		if (user != null) {
			showFriendUsername.setText(user.getUserName());
			showFriendPhone.setText(user.getPhoneNumber() == null
					|| user.getPhoneNumber().equals("null") ? "Œ¥…Ë÷√" : user
					.getPhoneNumber());
			showFriendMobilePhone.setText(user.getMobilePhone() == null
					|| user.getMobilePhone().equals("null") ? "Œ¥…Ë÷√" : user
					.getMobilePhone());
			showFriendSex.setText(user.getSex() == null
					|| user.getSex().equals("null") ? "Œ¥…Ë÷√" : user.getSex());
			showFriendOrg.setText(user.getOrg() == null
					|| user.getOrg().equals("null") ? "Œ¥…Ë÷√" : user.getOrg());
			showFriendPostion.setText(user.getPosition() == null
					|| user.getPosition().equals("null") ? "Œ¥…Ë÷√" : user
					.getPosition());
		}
		return view;
	}

}
