package com.mct.client;

import com.mct.view.XListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RefreshActivity extends Activity{
    protected LinearLayout backView;
    protected TextView titleText;
    protected TextView nextBtn;
    protected TextView backText;
    protected XListView refreshListView;
    protected LinearLayout nodatahintview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.refreshactivity);
		backView=(LinearLayout) findViewById(R.id.backview);
		titleText=(TextView) findViewById(R.id.titletext);
		nextBtn=(TextView) findViewById(R.id.nextBtn);
		backText=(TextView) findViewById(R.id.backtext);
		refreshListView=(XListView) findViewById(R.id.refreshlistview);
	}
	

}
