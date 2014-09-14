package com.mct.client;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ForumActivity extends Activity implements OnClickListener{
   private LinearLayout backLayout,nodatamsgview;
   private TextView titleTextView,sureTextView;
   private ListView forumListView;
   private Button addContentbtn;
   private ArrayList<String> arrayList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forumactivity);
		backLayout=(LinearLayout) findViewById(R.id.backlayout);
		titleTextView=(TextView) findViewById(R.id.toptext);
		sureTextView=(TextView) findViewById(R.id.sureBtn);
		forumListView=(ListView) findViewById(R.id.forumlistview);
		nodatamsgview=(LinearLayout) findViewById(R.id.nodatamsgview);
		addContentbtn=(Button) findViewById(R.id.addcontentbtn);
		sureTextView.setOnClickListener(this);
		backLayout.setOnClickListener(this);
		addContentbtn.setOnClickListener(this);
		arrayList=new ArrayList<String>();
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
		forumListView.setAdapter(adapter);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	

}
