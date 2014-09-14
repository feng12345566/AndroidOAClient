package com.mct.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mct.client.R;
import com.mct.controls.ChartAdapter;
import com.mct.model.ChartData;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class PieChartFragment extends Fragment {
	private ListView pieChartListView;
	private ArrayList<ChartData> list;
	private TextView chartTitle;
	private ChartAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		String title = getArguments().getString("title");
		View view = inflater.inflate(R.layout.piechartlistview, null);
		pieChartListView = (ListView) view.findViewById(R.id.piechartlistview);
		chartTitle = (TextView) view.findViewById(R.id.charttitle);
		adapter = new ChartAdapter(getActivity(),1);
		list = new ArrayList<ChartData>();
		adapter.setList(list);
		pieChartListView.setAdapter(adapter);
		chartTitle.setText(title);
		return view;
	}
private Handler handler=new Handler(){

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		switch (msg.what) {
		case 0:
			
			break;

		case 1:
			adapter.setList(list);
			adapter.notifyDataSetChanged();
			break;
		}
	}
	
};
	public void setResult(final String result) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
				list.clear();
				String[] jsons=result.trim().split("@");
				try {
					JSONObject json = new JSONObject(jsons[0]);
					JSONArray votes = json.getJSONArray("votes");
					for (int i = 0; i < votes.length(); i++) {
						ChartData chartData = new ChartData(votes.getJSONObject(i)
								.getInt("num"));
						String[] items = new String[] { "a", "b", "c", "d", "e", "f",
								"g", "h" };
						int[] numbers = new int[8];
						for (int j = 0; j < numbers.length; j++) {
							numbers[j] = votes.getJSONObject(i).getInt(items[j]);
						}
						chartData.setNumbers(numbers);
						list.add(chartData);
					}
					JSONObject object=new JSONObject(jsons[1]);
					JSONArray questions = object.getJSONArray("questions");
					for (int i = 0; i < questions.length(); i++) {
						JSONObject question = questions.getJSONObject(i);
						int num = question.getInt("num");
						for (int j = 0; j < list.size(); j++) {
							if (list.get(j).getNum() == num) {
								list.get(j).setQuestion(question.getString("text"));
								String[] answerItems = new String[8];
								JSONArray answers = question.getJSONArray("answers");
								for (int m = 0; m < answers.length(); m++) {
									answerItems[m] = answers.getString(m);
								}
								list.get(j).setAnswerItems(answerItems);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			handler.sendEmptyMessage(1);
			}
		}).start();
		
		
	}
}
