package com.mct.fragment;

import com.mct.client.R;
import com.mct.util.SharePeferenceUtils;
import com.mct.view.SwitchButton;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MessageSettingFragment extends Fragment implements OnClickListener {
	private ImageView qxdf;
	private ImageView tssd;
	private LinearLayout qxdf_layout;
	private LinearLayout tssd_layout;
	private SwitchButton voiceNotification;
	private SwitchButton shockNotification;
	private SwitchButton msgNotification;
	private SharedPreferences sharedPreferences;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.msgreceiversetting, null);
		qxdf = (ImageView) view.findViewById(R.id.qxdf_voice);
		tssd = (ImageView) view.findViewById(R.id.tssd_voice);
		qxdf_layout = (LinearLayout) view.findViewById(R.id.qxdf_layout);
		tssd_layout = (LinearLayout) view.findViewById(R.id.tssd_layout);
		qxdf_layout.setOnClickListener(this);
		tssd_layout.setOnClickListener(this);
		msgNotification = (SwitchButton) view
				.findViewById(R.id.msgshownotification);
		shockNotification = (SwitchButton) view
				.findViewById(R.id.msgshocknotification);
		voiceNotification = (SwitchButton) view
				.findViewById(R.id.msgvoicenotification);
		sharedPreferences = getActivity().getSharedPreferences(
				"messagesettings", 0);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.qxdf_layout:
			qxdf.setVisibility(View.VISIBLE);
			tssd.setVisibility(View.GONE);
			SharePeferenceUtils.getInstance(getActivity()).setMessageSettings(
					"qxdf", true);
			break;

		case R.id.tssd_layout:
			qxdf.setVisibility(View.GONE);
			tssd.setVisibility(View.VISIBLE);
			SharePeferenceUtils.getInstance(getActivity()).setMessageSettings(
					"qxdf", false);
			break;
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharePeferenceUtils.getInstance(getActivity()).setMessageSettings(
				"voiceenable", voiceNotification.isChecked());
		SharePeferenceUtils.getInstance(getActivity()).setMessageSettings(
				"shockenable", shockNotification.isChecked());
		SharePeferenceUtils.getInstance(getActivity()).setMessageSettings(
				"notificationenable", msgNotification.isChecked());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		voiceNotification.setChecked(sharedPreferences.getBoolean(
				"voiceenable", false));
		shockNotification.setChecked(sharedPreferences.getBoolean(
				"shockenable", false));
		msgNotification.setChecked(sharedPreferences.getBoolean(
				"notificationenable", false));
		if (sharedPreferences.getBoolean("qxdf", true)) {
			qxdf.setVisibility(View.VISIBLE);
			tssd.setVisibility(View.GONE);
		} else {
			tssd.setVisibility(View.VISIBLE);
			qxdf.setVisibility(View.GONE);
		}
	}

}
