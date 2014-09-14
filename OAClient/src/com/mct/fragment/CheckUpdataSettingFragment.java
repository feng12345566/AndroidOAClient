package com.mct.fragment;

import com.loveplusplus.update.UpdateChecker;
import com.mct.client.R;
import com.mct.util.SharePeferenceUtils;
import com.mct.view.CustomClipLoading;
import com.mct.view.SwitchButton;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckUpdataSettingFragment extends Fragment implements
		OnClickListener {
	private TextView currentVersion;
	private LinearLayout checkDateupNow;
	private SwitchButton autoUpdataBtn;
	private AlertDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.checkupdatasetting, null);
		currentVersion = (TextView) view.findViewById(R.id.currentversion);
		checkDateupNow = (LinearLayout) view.findViewById(R.id.checkDateupNow);
		autoUpdataBtn = (SwitchButton) view.findViewById(R.id.autoupdatabtn);
		checkDateupNow.setOnClickListener(this);
		PackageManager manager = getActivity().getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(getActivity().getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (info != null) {
			currentVersion.setText(info.versionName);
		}
		return view;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				AlertDialog.Builder builder = new Builder(getActivity());
				builder.setView(new CustomClipLoading(getActivity()));
				dialog = builder.create();
				dialog.show();
				break;

			case 1:
				dialog.dismiss();
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.checkDateupNow:
			handler.sendEmptyMessage(0);
			UpdateChecker
					.setUpdateServerUrl("http://nat.nat123.net:14313/oa/UpdateChecker.jsp");
			UpdateChecker.checkForDialog(getActivity(), handler);
			break;

		default:
			break;
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharePeferenceUtils.getInstance(getActivity()).setSwitch(
				SharePeferenceUtils.AUTOUPDATECHECK, autoUpdataBtn.isChecked());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		autoUpdataBtn.setChecked(SharePeferenceUtils.getInstance(getActivity())
				.getSwitch(SharePeferenceUtils.AUTOUPDATECHECK));
	}

}
