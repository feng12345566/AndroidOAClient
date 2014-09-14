package com.mct.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import com.mct.model.ChartData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

public class MyPieChart extends GraphicalView{

	private String TAG = "MyPieChart";
	 private PieChart chart = new PieChart();	
	 private LinkedList<PieData> chartData = new LinkedList<PieData>();
	 private Context context;
	 private ChartData chData;
	
	 /**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyPieChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		 this.context=context;
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public MyPieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		 this.context=context;
	}

	/**
	 * @param context
	 */
	public MyPieChart(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		 this.context=context;
	}




	public void setChData(ChartData chData) {
		this.chData = chData;
		initView();
	}

	private void initView()
	 {
		 chartDataSet();	
		 chartRender();
	 }	 		 	
	
	
	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //图所占范围大小
        chart.setChartRange(w,h);
    }  	

	private void chartRender()
	{
		try {										
			//标签显示(隐藏，显示在中间，显示在扇区外面,折线注释方式)
			chart.setLabelPosition(XEnum.SliceLabelPosition.LINE);			
			
			//图的内边距
			//注释折线较长，缩进要多些
			int [] ltrb = new int[4];
			ltrb[0] = DensityUtil.dip2px(context, 55); //top	
			ltrb[1] = DensityUtil.dip2px(context, 50); //bottom	
			ltrb[2] = DensityUtil.dip2px(context, 60); //left		
			ltrb[3] = DensityUtil.dip2px(context, 60); //right				
			chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
			
			//设定数据源
			chart.setDataSource(chartData);												
		
//			//标题
//			chart.setTitle("");
//			chart.addSubtitle("(XCL-Charts Demo)");
//			chart.getPlotTitle().setTitlePosition(XEnum.Position.CENTER);
				
			//隐藏渲染效果
			chart.hideGradient();
			//显示边框
			//chart.showRoundBorder();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}

	private void chartDataSet()
	{
		String[] items=new String[]{"A","B","C","D","E","F","G","H"};
		int[] colors=new int[]{(int)Color.rgb(77, 83, 97),(int)Color.rgb(227, 207, 87),
				(int)Color.rgb(225, 153, 18),(int)Color.rgb(176, 224, 230),
				(int)Color.rgb(0, 255, 255),(int)Color.rgb(8, 46, 84),
				(int)Color.rgb(0, 255, 0),(int)Color.rgb(160, 32, 240)};
		//设置图表数据源
		if(chData!=null){
			int[] numbers=chData.getNumbers();
			int total=0;
			for(int i=0;i<8;i++){
				total=total+numbers[i];
			}
			if(total==0){
				return  ;
			}
			for(int i=0;i<8;i++){
				if(numbers[i]!=0){
				chartData.add(new PieData(items[i],items[i]+"-"+numbers[i]*100.0/total+"%",numbers[i]*100.0/total,colors[i]));
				}
			}
					
		}
		
	}
	
	public void render(Canvas canvas) {
		// TODO Auto-generated method stub
		 try{
	            chart.render(canvas);
	        } catch (Exception e){
	        	Log.e(TAG, e.toString());
	        }
	}


	public List<XChart> bindChart() {
		// TODO Auto-generated method stub
		List<XChart> lst = new ArrayList<XChart>();
		lst.add(chart);		
		return lst;
	}
}

