package com.mct.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.BarChart3D;
import org.xclcharts.chart.BarData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import com.mct.model.ChartData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

public class MyBarChart3DView extends GraphicalView{
	private String TAG = "Bar3DChart01View";	
	private BarChart3D chart = new BarChart3D();
	private ChartData chData;
	//标签轴
	private List<String> chartLabels = new LinkedList<String>();
	//数据轴
	private List<BarData> BarDataset = new LinkedList<BarData>();	
	private int size;
	public MyBarChart3DView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub				
	}
		
	
	public MyBarChart3DView(Context context, AttributeSet attrs){   
        super(context, attrs);   
	 }
	 
	 public MyBarChart3DView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		
	 }
	 
	 public void setChData(ChartData chData) {
		this.chData = chData;
		size=getSize();
		initView();
	}


	private void initView()
	 {
		 	chartLabels();
			chartDataSet();	
			chartRender();
	 }
	 
	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       // Log.d("mDebug", "onSizeChanged,w="+w+",h="+h+",oldw="+oldw+",oldh="+oldh);  
       //图所占范围大小
        chart.setChartRange(w,h);
    }  
	
	
	private void chartRender()
	{
		try {						
			
			//设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....	
			int [] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(ltrb[0], ltrb[1], DensityUtil.dip2px(getContext(), 50), ltrb[3]);
			
			//显示边框
			chart.showRoundBorder();
			
			
			//数据源			
			chart.setDataSource(BarDataset);
			chart.setCategories(chartLabels);	
			
			//坐标系
			int[] numbers=chData.getNumbers();
			int max=0;
			for(int i=0;i<size;i++){
				if(numbers[i]>max){
					max=numbers[i];
				}
			}
			chart.getDataAxis().setAxisMax(2*max);
			chart.getDataAxis().setAxisMin(0);
			chart.getDataAxis().setAxisSteps(max/5<=1?1:max/5);
			//chart.getCategoryAxis().setAxisTickLabelsRotateAgent(-45f);
			
			
		
			
			//背景网格
			chart.getPlotGrid().showHorizontalLines();
			chart.getPlotGrid().showVerticalLines();
			chart.getPlotGrid().showEvenRowBgColor();
			chart.getPlotGrid().showOddRowBgColor();
			
			
			//定义数据轴标签显示格式		
			chart.getDataAxis().setTickLabelRotateAgent(-45);
			chart.getDataAxis().getTickMarksPaint().
					setColor((int)Color.rgb(186, 20, 26));
			chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack(){
	
				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub							
					Double tmp = Double.parseDouble(value);
					DecimalFormat df=new DecimalFormat("#0");
					String label = df.format(tmp).toString();				
					return (label +"人次");
				}
				
			});
			
			//定义标签轴标签显示格式
			chart.getCategoryAxis().setLabelFormatter(new IFormatterTextCallBack(){
	
				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub									
					return value;
				}
				
			});
			//定义柱形上标签显示格式
			chart.getBar().setItemLabelVisible(true);
			chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
				@Override
				public String doubleFormatter(Double value) {
					// TODO Auto-generated method stub
					DecimalFormat df=new DecimalFormat("#0");					 
					String label = df.format(value).toString();
					return label;
				}});	        
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void chartDataSet()
	{
		//标签对应的柱形数据集
		List<Double> dataSeriesA= new LinkedList<Double>();
		
		for(int i=0;i<size;i++){
		dataSeriesA.add((double) chData.getNumbers()[i]);
		}
		BarDataset.add(new BarData("",dataSeriesA,(int)Color.rgb(55, 144, 206)));
	}
	
	private void chartLabels()
	{
		String[] items=new String[]{"A","B","C","D","E","F","G","H"};
		for(int i=0;i<size;i++){
		chartLabels.add(items[i]);
		}
	}


	@Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }

	
	private int getSize(){
		String[] items=chData.getAnswerItems();
		for(int i=0;i<items.length;i++){
			if(items[i]==null){
				return i;
			}
		}
		return items.length;
	}

}
