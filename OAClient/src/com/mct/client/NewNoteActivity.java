package com.mct.client;

import com.mct.fragment.NewNoteFragment;
import com.mct.util.MyAnimationUtils;
import com.mct.util.UserDataDbUtil;
import com.mct.view.PopWindowMenuView;
import com.mct.view.PopWindowMenuView.OnPopWindowItemClickListener;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class NewNoteActivity extends FragmentActivity implements
		OnClickListener {
	private boolean isShow;
	private long id;
	private Bundle bundle;
	private NewNoteFragment fragment;
	private TextView backmycenter;
	private LinearLayout backlayout;
	private TextView menu_layout_title;
	private ImageView moreapplicationbtn;
	private TextView sureposttext;
	private PopWindowMenuView pop;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_newnote);
		backmycenter = (TextView) findViewById(R.id.backmycenter);
		backlayout = (LinearLayout) findViewById(R.id.backlayout);
		menu_layout_title = (TextView) findViewById(R.id.menu_layout_title);
		moreapplicationbtn = (ImageView) findViewById(R.id.moreapplicationbtn);
		sureposttext=(TextView) findViewById(R.id.sureposttext);
		sureposttext.setOnClickListener(this);
		backmycenter.setText("返回");
		moreapplicationbtn.setOnClickListener(this);
		backlayout.setOnClickListener(this);
		Intent intent = getIntent();
		isShow = intent.getBooleanExtra("show", false);
		bundle = new Bundle();
		bundle.putBoolean("show", isShow);
		bundle.putString("date", intent.getStringExtra("date"));
		if (intent.hasExtra("id")) {
			id = intent.getLongExtra("id", 0);
			bundle.putLong("id", id);
		}
		if (isShow) {
			menu_layout_title.setText("查看事项");
			sureposttext.setVisibility(View.GONE);
			moreapplicationbtn.setVisibility(View.VISIBLE);
		} else {
			if(id==0){
				menu_layout_title.setText("新建事项");
			}else{
			   menu_layout_title.setText("修改事项");
			}
			sureposttext.setVisibility(View.VISIBLE);
			moreapplicationbtn.setVisibility(View.GONE);
		}
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		fragment = new NewNoteFragment();
		fragment.setArguments(bundle);
		transaction.replace(R.id.newnotefragmentlayout, fragment);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.moreapplicationbtn:
			   pop= new PopWindowMenuView(this,
						new int[] { R.drawable.ic_action_edit_dark,
								R.drawable.ic_action_discard_dark },
						new String[] { "修改", "删除" });
				
				pop.setOnPopWindowItemClickListener(new OnPopWindowItemClickListener(){

					@Override
					public void onPopWindowItemClick(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						System.out.println("position："+position);
						switch (position) {
						case 0:
							fragment.showToEdit();
							menu_layout_title.setText("修改事项");
							isShow = false;
							sureposttext.setVisibility(View.VISIBLE);
							moreapplicationbtn.setVisibility(View.GONE);
							pop.dismiss();
							break;

						case 1:
							fragment.deleteNote();
							pop.dismiss();
							NewNoteActivity.this.finish();
							MyAnimationUtils.outActivity(NewNoteActivity.this);
							break;
						}
					}
					
				});
				pop.getView();
				pop.showPop(v);	
			
			break;
		case R.id.sureposttext:
			fragment.sureValue();
			break;
		case R.id.backlayout:
			this.finish();
			MyAnimationUtils.outActivity(NewNoteActivity.this);
			break;
		}
	}

}
