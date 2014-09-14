package com.mct.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MessageListActvity extends Activity implements OnClickListener{
	private LinearLayout backLayout;
	private TextView titleTextView, sureTextView;
	private ListView forumListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forumactivity);
		backLayout = (LinearLayout) findViewById(R.id.backlayout);
		titleTextView = (TextView) findViewById(R.id.toptext);
		sureTextView = (TextView) findViewById(R.id.sureBtn);
		forumListView = (ListView) findViewById(R.id.forumlistview);
		backLayout.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
