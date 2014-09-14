package com.mct.client;

import com.mct.fragment.NoteBookFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NoteBookActivity extends BaseActivity {
    private NoteBookFragment fragment;
    private String date;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Intent intent=getIntent();
		date=intent.getStringExtra("date");
		fragment=new NoteBookFragment();
		Bundle bundle=new Bundle();
		bundle.putString("date", date);
		setParameter(date, fragment, bundle, "日程管理", "新建事项");
		nextPage.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.nextpage:
			Intent intent=new Intent(this,NewNoteActivity.class);
			intent.putExtra("date", date);
			intent.putExtra("show", false);
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
	}
	

}
