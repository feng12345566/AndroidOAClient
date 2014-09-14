package com.mct.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GestureLockView extends View{
	
	public static final int MODE_NORMAL = 0x100;
	public static final int MODE_SELECTED = 0x200;
	public static final int MODE_ERROR = 0x400;
	public static final int MODE_DONESETTING = 0x800;
	
	public static final int ARROW_TOP = 0x1;
	public static final int ARROW_TOP_RIGHT = 0x2;
	public static final int ARROW_RIGHT = 0x4;
	public static final int ARROW_RIGHT_BOTTOM = 0x8;
	public static final int ARROW_BOTTOM = 0x10;
	public static final int ARROW_BOTTOM_LEFT = 0x20;
	public static final int ARROW_LEFT = 0x40;
	public static final int ARROW_LEFT_TOP = 0x80;
	
	private int mode = MODE_NORMAL;
	
	private int width;
	private int height;
	
	private int centerX;
	private int centerY;
	
	private Paint paint;
	
	private static final int COLOR_NORMAL =Color.WHITE;
	private static final int COLOR_ERROR = Color.RED;
	private static final int COLOR_DONE = Color.GREEN;
	public static final int COLOR_SELECTED =Color.argb(200, 0, 255, 255);
	
	private static int radius;
	private float innerRate = 0.2F;
	private float outerWidthRate = 0.03F;//外环宽度
	private float outerRate = 0.65F;//外环半径
	private float arrowRate = 0.20F;
	private float arrowDistanceRate = 0.3F;
	private int arrowDistance;
	
	private Path arrow;

	public GestureLockView(Context context){
		this(context, null);
	}
	
	public GestureLockView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public GestureLockView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//获取屏幕宽高
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		//屏幕获取中心坐标
		centerX = width / 2;
		centerY = height / 2;
		//取宽高较短的作为半径
		radius = width > height ? height : width;
		//取短边的一般
		radius /= 2;
		
		if(arrow == null){
			arrowDistance = (int) (radius * arrowDistanceRate);
			//绘制三角形？
			int length = (int) (radius * arrowRate);
			arrow = new Path();
			arrow.moveTo(-length + centerX, length + centerY - arrowDistance);
			arrow.lineTo(centerX, centerY - arrowDistance);
			arrow.lineTo(length + centerX, length + centerY - arrowDistance);
			arrow.close();
			
		}
	}
	
	public void setMode(int mode){
		this.mode = mode;
		invalidate();
	}
	
	public int getMode(){
		return mode;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		switch(mode & 0xF00){
		case MODE_NORMAL:
			//画外圈
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(radius * outerWidthRate);
			paint.setColor(COLOR_NORMAL);
			canvas.drawCircle(centerX, centerY, radius * outerRate, paint);
			Log.e("radius",  radius * outerRate+"");
			break;
		case MODE_SELECTED:
			//画外圈
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(COLOR_SELECTED);
			paint.setStrokeWidth(radius * outerWidthRate);
			canvas.drawCircle(centerX, centerY, radius * outerRate, paint);
            RectF rectF=new RectF((float)(centerX-radius * (outerRate-0.1)), (float)(centerY-radius * (outerRate-0.1)),
        		  (float)(centerX+radius * (outerRate-0.1)), (float)(centerY+radius * (outerRate-0.1)));
			canvas.drawArc(rectF, 2.0f, 86f, false, paint);
			canvas.drawArc(rectF, 92.0f, 86f, false, paint);
			canvas.drawArc(rectF, 182.0f, 86f, false, paint);
			canvas.drawArc(rectF, 272.0f, 86f, false, paint);
			paint.setColor(Color.rgb(12, 12, 12));
			paint.setStrokeWidth((float) (radius * outerRate));
			canvas.drawCircle(centerX, centerY, (float) (radius *innerRate), paint);
			//画内圈  
			paint.setColor(COLOR_SELECTED);
			paint.setStyle(Style.FILL);
			canvas.drawCircle(centerX, centerY, radius * innerRate, paint);
			break;
		case MODE_ERROR:
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(COLOR_ERROR);
			paint.setStrokeWidth(radius * outerWidthRate);
			canvas.drawCircle(centerX, centerY, radius * outerRate, paint);
			paint.setStrokeWidth(2);
			canvas.drawCircle(centerX, centerY, radius * innerRate, paint);
			break;
		case MODE_DONESETTING:
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(COLOR_DONE);
			paint.setStrokeWidth(radius * outerWidthRate);
			canvas.drawCircle(centerX, centerY, radius * outerRate, paint);
			paint.setStrokeWidth(2);
			canvas.drawCircle(centerX, centerY, radius * innerRate, paint);
			break;
		}
		
		if((mode & 0xFF) > 0){
			paint.setStyle(Paint.Style.FILL);
			//paint.setColor(COLOR_ERROR);
			
			canvas.save();
			//canvas.translate(-centerX, -arrowDistance);
			switch(mode & 0xFF){
			case ARROW_TOP:
				break;
			case ARROW_TOP_RIGHT:
				canvas.rotate(45, centerX, centerY);
				break;
			case ARROW_RIGHT:
				canvas.rotate(90, centerX, centerY);
				break;
			case ARROW_RIGHT_BOTTOM:
				canvas.rotate(135, centerX, centerY);
				break;
			case ARROW_BOTTOM:
				canvas.rotate(180, centerX, centerY);
				break;
			case ARROW_BOTTOM_LEFT:
				canvas.rotate(-135, centerX, centerY);
				break;
			case ARROW_LEFT:
				canvas.rotate(-90, centerX, centerY);
				break;
			case ARROW_LEFT_TOP:
				canvas.rotate(-45, centerX, centerY);
				break;
			}
			
			canvas.drawPath(arrow, paint);
			
			canvas.restore();
		}
		
	}
	
}
