package com.mct.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.mct.client.NewNoteActivity;
import com.mct.client.R;
import com.mct.util.DrawUtil;
import com.mct.util.MyAnimationUtils;
import com.mct.util.UserDataDbUtil;
import com.mct.util.XmppTool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NoteBookFragment extends Fragment implements OnItemClickListener,
		OnItemLongClickListener {
	private ListView noteListView;
	private TextView nonotetext;
	private ArrayList<HashMap<String, Object>> list;
	private EditText noteSearchEdit;
	private String date;
	private SimpleAdapter adapter;
	private Animation animation;
	private int l;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		date = getArguments().getString("date");
		View view = inflater.inflate(R.layout.fragment_notelist, null);
		noteListView = (ListView) view.findViewById(R.id.notelistview);
		nonotetext = (TextView) view.findViewById(R.id.nonotetext);
		noteSearchEdit = (EditText) view.findViewById(R.id.notesearchedit);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), NewNoteActivity.class);
		intent.putExtra("id", (Long) list.get(position).get("id"));
		intent.putExtra("date", date);
		intent.putExtra("show", true);
		startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		l = position;
		LayoutInflater layoutInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view1 = layoutInflater.inflate(R.layout.note_pop_menu, null);
		
		final PopupWindow pop = new PopupWindow(view1,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		pop.setOutsideTouchable(true);
		pop.setTouchable(true);
		pop.setFocusable(true);
		pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		int[] size = DrawUtil.getViewSize(view1);
		pop.showAtLocation(view, Gravity.NO_GRAVITY,
				location[0] + view.getWidth() / 2 - size[1], location[1]
						- size[0]);
		TextView modifyText = (TextView) view1.findViewById(R.id.modifynotebtn);
		modifyText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), NewNoteActivity.class);
				intent.putExtra("show", false);
				intent.putExtra("date", date);
				intent.putExtra("id", (Long)list.get(l).get("id"));
				startActivity(intent);
				MyAnimationUtils.inActivity(getActivity());
				pop.dismiss();
			}
		});
		TextView deleteText = (TextView) view1.findViewById(R.id.deletenotebtn);
		deleteText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteNote();
				list.remove(l);
		         adapter.notifyDataSetChanged();
                pop.dismiss();
			}
		});
		return true;
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		list = UserDataDbUtil.shareUserDataDb(getActivity()).getNoteList(
				XmppTool.loginUser.getUserId(), date);
		adapter = new SimpleAdapter(getActivity(), list,
				R.layout.notelist_item, new String[] { "dotimestart",
						"dotimeend", "title", "postion" }, new int[] {
						R.id.noteitem_starttime, R.id.noteitem_endtime,
						R.id.noteitem_title, R.id.notelist_postion });
		noteListView.setAdapter(adapter);
		if (list.size() == 0) {
			noteSearchEdit.setVisibility(View.GONE);
			nonotetext.setVisibility(View.VISIBLE);
			animation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.result_nodata_toast);
			animation.setFillAfter(true);
			nonotetext.startAnimation(animation);
		} else {
			if (animation != null) {
				animation.setFillAfter(false);
			}
			noteSearchEdit.setVisibility(View.VISIBLE);
			nonotetext.setVisibility(View.GONE);
			noteListView.setOnItemClickListener(this);
			noteListView.setOnItemLongClickListener(this);
		}
	}
	public void deleteNote(){
		 boolean r= UserDataDbUtil.shareUserDataDb(getActivity()).deleteNote((Long)list.get(l).get("id"));
        Toast.makeText(getActivity(), "…æ≥˝"+(r?"≥…π¶£°":" ß∞‹£¨«Î÷ÿ ‘£°"), Toast.LENGTH_SHORT).show();     
	}
}
