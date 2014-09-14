package com.mct.fragment;

import com.mct.client.AttendanceSettingActivity;
import com.mct.client.CheckUpdateSettingActivty;
import com.mct.client.DataTransferActivity;
import com.mct.client.MessageSettingActivity;
import com.mct.client.MyInfoActivity;
import com.mct.client.R;
import com.mct.client.SafetySettingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SettingFragment extends Fragment implements OnClickListener{
private LinearLayout locationSetting,myInfoSetting,translationSetting,messageSetting,safeSetting;
private LinearLayout checkUpdataSetting;
@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.setting_list, null);
		locationSetting=(LinearLayout) view.findViewById(R.id.locationsetting);
		myInfoSetting=(LinearLayout) view.findViewById(R.id.myinfosetting);
		translationSetting=(LinearLayout) view.findViewById(R.id.translationsetting);
		messageSetting=(LinearLayout) view.findViewById(R.id.messagesetting);
		safeSetting=(LinearLayout) view.findViewById(R.id.safesetting);
		checkUpdataSetting=(LinearLayout) view.findViewById(R.id.checkupdatasetting);
		locationSetting.setOnClickListener(this);
		myInfoSetting.setOnClickListener(this);
		translationSetting.setOnClickListener(this);
		messageSetting.setOnClickListener(this);
		safeSetting.setOnClickListener(this);
		checkUpdataSetting.setOnClickListener(this);
		
		return view;
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent=null;
		switch (arg0.getId()) {
		
		case R.id.locationsetting:
			intent=new Intent(getActivity(),AttendanceSettingActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;

		case R.id.myinfosetting:
			intent=new Intent(getActivity(),MyInfoActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.translationsetting:
			intent=new Intent(getActivity(),DataTransferActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.messagesetting:
			intent=new Intent(getActivity(),MessageSettingActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.checkupdatasetting:
			intent=new Intent(getActivity(),CheckUpdateSettingActivty.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			break;
		case R.id.safesetting:
			intent=new Intent(getActivity(),SafetySettingActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			getActivity().finish();
			break;
		}
		
	}
	
}
