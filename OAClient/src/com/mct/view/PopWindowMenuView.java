package com.mct.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.mct.client.R;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

public class PopWindowMenuView {
	private Context context;
	private PopupWindow pop;
	private ListView listView;
	private ArrayList<HashMap<String, Object>> list;
	private OnPopWindowItemClickListener onPopWindowItemClickListener;

	public PopWindowMenuView(Context context, int[] icons, String[] texts) {
		super();
		this.context = context;
		list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < texts.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("text", texts[i]);
			map.put("icon", icons[i]);
			list.add(map);
		}
	}

	public void showPop(View v) {
		pop.showAsDropDown(v);
	}

	public void getView() {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.listview_popwindow, null);
		pop = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		listView = (ListView) view.findViewById(R.id.pop_listview);
		SimpleAdapter adapter = new SimpleAdapter(context, list,
				R.layout.popwindow_menu_item, new String[] { "text", "icon" },
				new int[] { R.id.popwindow_item_tv, R.id.popwindow_item_iv });
		listView.setAdapter(adapter);
		if (onPopWindowItemClickListener != null) {
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Log.e("click", position + "被点击");
					onPopWindowItemClickListener.onPopWindowItemClick(parent,
							view, position, id);
				}
			});
		}
		pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setTouchable(true);
	}

	public void setOnPopWindowItemClickListener(
			final OnPopWindowItemClickListener onPopWindowItemClickListener) {
		this.onPopWindowItemClickListener = onPopWindowItemClickListener;

	}

	

	public interface OnPopWindowItemClickListener {
		public void onPopWindowItemClick(AdapterView<?> parent, View view,
				int position, long id);
	}
	public void dismiss() {
		pop.dismiss();
	}
}
