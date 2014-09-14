package com.mct.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.mct.client.FlowmanagerActivity;
import com.mct.client.GroupChatListActivity;
import com.mct.client.R;
import com.mct.client.WeatherSearchActivity;
import com.mct.controls.MyCenterAdapter;
import com.mct.util.MyAnimationUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ToolsBoxFragment extends Fragment implements OnItemClickListener{
    private GridView gridView;
    
    private Intent[] intents;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.mycenterfragment, null);	
		gridView=(GridView) view.findViewById(R.id.mycenter);
		MyCenterAdapter adapter = new MyCenterAdapter(getActivity());
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		int[] iv = new int[] { R.drawable.weathericon,R.drawable.calculator};
		String[] tv = new String[] { "天气查询", "计算器"};
		for (int i = 0; i < iv.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("iv", iv[i]);
			map.put("tv", tv[i]);
			list.add(map);
			map = null;
		}
		adapter.setList(list);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		intents = new Intent[] {
				new Intent(getActivity(), WeatherSearchActivity.class),
				new Intent()};
		intents[1].setClassName("com.android.calculator2", "com.android.calculator2.Calculator");
		return view;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		startActivity(intents[position]);
		MyAnimationUtils.inActivity(getActivity());
	}

}
