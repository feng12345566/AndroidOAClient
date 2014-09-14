package com.mct.flowview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class FlowView extends View {
	private int tableId;
	private Canvas canvas;
	private Context context;
	private int width;
	private int height;

	public FlowView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		DisplayMetrics dm = new DisplayMetrics();
		Rect rect = new Rect();
		this.getWindowVisibleDisplayFrame(rect);
		width = rect.width();
		height = rect.height();
		Log.e("size", "width=" + width + ",height=" + height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);

	}

	/**
	 * ���ƽڵ�
	 * 
	 * @param id
	 *            �ڵ�id
	 * @param nadeName
	 *            �ڵ�����
	 */
	private void drawNode(Canvas canvas, int id, String nadeName) {

	}
	/**
	 * �������̱���
	 * 
	 * @param canvas
	 * @param title
	 *            ����
	 */
	// private void drawTitle(Canvas canvas,String title){
	// canvas.drawText(startX-, arg1, arg2, arg3);
	// }
}
