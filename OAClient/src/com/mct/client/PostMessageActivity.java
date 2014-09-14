package com.mct.client;


import com.mct.fragment.PostMessageFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

public class PostMessageActivity extends BaseActivity implements
		OnClickListener {
	private PostMessageFragment fragment;
	private int opt;
	private static final int EMAIL=1;
	private static final int NOTIFICATION=2;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		opt=getIntent().getIntExtra("opt", 0);
		fragment=new PostMessageFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("opt", opt);
		if(getIntent().hasExtra("receiver")){
			String receiver=getIntent().getStringExtra("receiver");
			bundle.putString("receiver", receiver);
		}
		if(getIntent().hasExtra("title")){
			String title=getIntent().getStringExtra("title");
			bundle.putString("title", title);
		}
		if(getIntent().hasExtra("content")){
			String content=getIntent().getStringExtra("content");
			bundle.putString("content", content);
		}
		if(getIntent().hasExtra("accessory")){
			String accessory=getIntent().getStringExtra("accessory");
			bundle.putString("accessory", accessory);
		}
		switch (opt) {
		case EMAIL:
			setParameter("д�ʼ�",fragment,bundle,"����","����");
			break;

		case NOTIFICATION:
			setParameter("֪ͨ����",fragment,bundle,"֪ͨ�б�","����");
			break;
		}
		nextPage.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.nextpage:
			fragment.sendMessage();
			break;
		
		}

	}

	
}
