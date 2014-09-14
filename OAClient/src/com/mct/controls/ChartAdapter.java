package com.mct.controls;

import java.util.ArrayList;

import com.mct.client.R;
import com.mct.model.ChartData;
import com.mct.view.MyBarChart3DView;
import com.mct.view.MyPieChart;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChartAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<ChartData> list;
	private static final String[] answerItems = new String[] { "A", "B", "C",
			"D", "E", "F", "G", "H", "I" };
	private int type;

	/**
	 * @param context
	 */
	public ChartAdapter(Context context,int type) {
		super();
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		this.type=type;
	}

	public ArrayList<ChartData> getList() {
		return list;
	}

	public void setList(ArrayList<ChartData> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.chartitem, null);
			viewHolder.mypiechartview = (MyPieChart) convertView
					.findViewById(R.id.mypiechartview);
			viewHolder.question = (TextView) convertView
					.findViewById(R.id.chartquestion);
			viewHolder.answers = (TextView) convertView
					.findViewById(R.id.chartanswer);
			viewHolder.chartitemlayout=(LinearLayout) convertView
					.findViewById(R.id.chartitemlayout);
			viewHolder.mybarchartview=(MyBarChart3DView) convertView
					.findViewById(R.id.mybarchartview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ChartData chartData = list.get(position);
		viewHolder.question.setText(chartData.getNum() + "."
				+ chartData.getQuestion());
		StringBuffer answer = new StringBuffer();
		for (int i = 0; i < chartData.getAnswerItems().length; i++) {
			if (chartData.getAnswerItems()[i] != null) {
				answer.append(answerItems[i]);
				answer.append(" ");
				answer.append(chartData.getAnswerItems()[i]);
				answer.append("\n");
			} else {
				break;
			}
		}
		viewHolder.answers.setText(answer.toString());
		switch (type) {
		case 1:
			//±ýÍ¼
			viewHolder.mypiechartview.setChData(list.get(position));
			viewHolder.mypiechartview.setVisibility(View.VISIBLE);
			viewHolder.mybarchartview.setVisibility(View.GONE);
			break;

		case 2:
			viewHolder.mybarchartview.setChData(list.get(position));
			viewHolder.mypiechartview.setVisibility(View.GONE);
			viewHolder.mybarchartview.setVisibility(View.VISIBLE);
			break;
		}
		
		
		if(position<list.size()-1){
			viewHolder.chartitemlayout.setBackgroundResource(R.drawable.textbg_top);
		}else{
			viewHolder.chartitemlayout.setBackgroundResource(R.drawable.textbg);
		}
		return convertView;
	}

	class ViewHolder {
		MyPieChart mypiechartview;
		MyBarChart3DView mybarchartview;
		TextView question;
		TextView answers;
		LinearLayout chartitemlayout;
	}

}
