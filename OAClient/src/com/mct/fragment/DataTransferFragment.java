package com.mct.fragment;

import com.mct.client.R;
import com.mct.util.SharePeferenceUtils;
import com.mct.view.SwitchButton;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DataTransferFragment extends Fragment{
    private SwitchButton loadEnable;
    private SwitchButton notificationEnable;
    private SwitchButton fileNetEnable;
    private SwitchButton fileWifiEnable;
    private SwitchButton mapDownEnable;
    private SwitchButton mapNetEnable;
    private SwitchButton mapWifiEnable;
    private SharedPreferences sharedPreferences;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.transferfilesetting, null);
		loadEnable=(SwitchButton) view.findViewById(R.id.transforenable);
		notificationEnable=(SwitchButton) view.findViewById(R.id.transfornotificationbtn);
		fileNetEnable=(SwitchButton) view.findViewById(R.id.tranfornetenable);
		fileWifiEnable=(SwitchButton) view.findViewById(R.id.tranforwifienable);
		mapDownEnable=(SwitchButton) view.findViewById(R.id.mapdownenable);
		mapNetEnable=(SwitchButton) view.findViewById(R.id.mapdownnetenable);
		mapWifiEnable=(SwitchButton) view.findViewById(R.id.mapdownwifienable);
		return view;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sharedPreferences=getActivity().getSharedPreferences("filesettings", 0);
		loadEnable.setChecked(sharedPreferences.getBoolean(SharePeferenceUtils.FILETRANSFORENABLE, false));
		notificationEnable.setChecked(sharedPreferences.getBoolean(SharePeferenceUtils.FILEDONENOTIFICATION, false));
		fileNetEnable.setChecked(sharedPreferences.getBoolean(SharePeferenceUtils.FILETRANSFORNETENABLE, false));
		fileWifiEnable.setChecked(sharedPreferences.getBoolean(SharePeferenceUtils.FILETRANSFORWIFIENABLE, false));
		mapDownEnable.setChecked(sharedPreferences.getBoolean(SharePeferenceUtils.MAPDOWNLOADENABLE, false));
		mapNetEnable.setChecked(sharedPreferences.getBoolean(SharePeferenceUtils.MAPDOWNLOADNETENABLE, false));
		mapWifiEnable.setChecked(sharedPreferences.getBoolean(SharePeferenceUtils.MAPDOWNLOADWIFIENABLE, false));
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharePeferenceUtils.getInstance(getActivity()).setFileTransfor(SharePeferenceUtils.FILETRANSFORENABLE, loadEnable.isChecked());
		SharePeferenceUtils.getInstance(getActivity()).setFileTransfor(SharePeferenceUtils.FILEDONENOTIFICATION, notificationEnable.isChecked());
		SharePeferenceUtils.getInstance(getActivity()).setFileTransfor(SharePeferenceUtils.FILETRANSFORNETENABLE, fileNetEnable.isChecked());
		SharePeferenceUtils.getInstance(getActivity()).setFileTransfor(SharePeferenceUtils.FILETRANSFORWIFIENABLE, fileWifiEnable.isChecked());
		SharePeferenceUtils.getInstance(getActivity()).setFileTransfor(SharePeferenceUtils.MAPDOWNLOADENABLE, mapDownEnable.isChecked());
		SharePeferenceUtils.getInstance(getActivity()).setFileTransfor(SharePeferenceUtils.MAPDOWNLOADNETENABLE, mapNetEnable.isChecked());
		SharePeferenceUtils.getInstance(getActivity()).setFileTransfor(SharePeferenceUtils.MAPDOWNLOADWIFIENABLE, mapWifiEnable.isChecked());
	}

}
