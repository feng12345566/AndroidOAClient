package com.mct.view;

import com.mct.util.DrawUtil;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

public class ItemPopWindow extends PopupWindow{
  private Context context;
	/**
	 * @param context
	 */
	public ItemPopWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		// TODO Auto-generated method stub
		super.showAtLocation(parent, gravity, x, y);
		int viewWidth=parent.getMeasuredWidth();
		int viewHeight=parent.getMeasuredHeight();
		Log.e("Viewsize",viewWidth+","+viewHeight);
		if(x+viewWidth>=DrawUtil.getScreenSize(context)[0]-5){
			x=x-viewWidth;
		}else if(x-viewWidth<=5){
			x=5;
		}
		if(y+viewHeight>=DrawUtil.getScreenSize(context)[1]-5){
			x=x-viewHeight;
		}else if(x-viewHeight<=5){
			x=5;
		}
	}
	
	public void setItem(String[] items,int x,int y){
		ListView lv=new ListView(context);
		LayoutParams lp=new LayoutParams(200, LayoutParams.WRAP_CONTENT);
		lv.setLayoutParams(lp);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);
		lv.setAdapter(adapter);
		showAtLocation(lv,Gravity.CENTER,x,y);
	}

}
