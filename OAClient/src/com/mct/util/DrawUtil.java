package com.mct.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;

public class DrawUtil {
	public static double getTextHeight(float frontSize) {
		Paint paint = new Paint();
		paint.setTextSize(frontSize);
		FontMetrics fm = paint.getFontMetrics();
		return Math.ceil(fm.descent - fm.top) + 2;
	}

	public static float getTextWidth(String text) {
		Paint paint = new Paint();
		return paint.measureText(text);

	}
	public static int[] getScreenSize(Context context){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		 int displayWidth = dm.widthPixels;
		int  displayHeight = dm.heightPixels;
		Log.i("Main", "Width = " + displayWidth);
		Log.i("Main", "Height = " + displayHeight);
		return new int[]{displayWidth,displayHeight};
	}
	public static int getStatusBarHeight(Context context){
		Rect frame = new Rect();
		((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		Log.i("Main", "statusBarHeight = " + statusBarHeight);
		return statusBarHeight;
	}
	public static int getTtitleBarHeight(Context context){
		int contentTop = ((Activity) context).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		//statusBarHeight是上面所求的状态栏的高度
		int titleBarHeight = contentTop - getStatusBarHeight(context);
		Log.i("Main", "titleBarHeight = " + titleBarHeight);
		return titleBarHeight;
	}
	
	/**
	 * 获取视图的长宽,用于视图未显示之前
	 * @param view
	 * @return  [长，宽]
	 */
	public static int[] getViewSize(View view){
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h); 
		int height=view.getMeasuredHeight(); 
		int width =view.getMeasuredHeight(); 
		return new int[]{height,width};
	}
	
	public static float dp2px(Context context,float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }
}
