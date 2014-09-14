package com.mct.client;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mct.controls.MyPagerAdapter;
import com.mct.fragment.BarChartFragment;
import com.mct.fragment.PieChartFragment;
import com.mct.model.ChartData;
import com.mct.transform.CubeOutTransformer;
import com.mct.util.HttpRequestUtil;
import com.mct.view.XListView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 显示调查统计界面
 * 
 * @author fengyouchun
 * @version 创建时间：2014年7月25日 上午9:54:45
 */
public class ShowStatisticsActivity extends FragmentActivity implements
		OnClickListener,OnPageChangeListener {
	private String id;// 调查的id
	private String title;
	private TextView topText, backText, sureBtn;
	private LinearLayout backLayout;
	private ViewPager mPager;
	private RadioGroup chartTypeCheck, questionTypeCheck;
	private XListView editQuestionAnswerListview;
	private ArrayList<ChartData> list;
	private Fragment[] fragments;
	private ProgressDialog dialog;
	private RadioButton[] buttons;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		title = intent.getStringExtra("title");
		setContentView(R.layout.showstatistics_activity);
		backLayout = (LinearLayout) findViewById(R.id.backlayout);
		editQuestionAnswerListview = (XListView) findViewById(R.id.editquestionanswerlistview);
		topText = (TextView) findViewById(R.id.toptext);
		backText = (TextView) findViewById(R.id.backText);
		sureBtn = (TextView) findViewById(R.id.sureBtn);
		mPager = (ViewPager) findViewById(R.id.chartviewpager);
		chartTypeCheck = (RadioGroup) findViewById(R.id.charttypecheck);
		questionTypeCheck = (RadioGroup) findViewById(R.id.questiontypecheck);
		topText.setText("调查统计");
		backText.setText("返回");
		Bundle bundle = new Bundle();
		bundle.putString("id", id);
		bundle.putString("title", title);
		fragments = new Fragment[] { new PieChartFragment(),
				new BarChartFragment() };
		fragments[0].setArguments(bundle);
		fragments[1].setArguments(bundle);
		MyPagerAdapter adapter = new MyPagerAdapter(
				getSupportFragmentManager(), fragments);
		mPager.setAdapter(adapter);
		mPager.setOnPageChangeListener(this);
		list = new ArrayList<ChartData>();
		if (VERSION.SDK_INT >= 11) {
			mPager.setPageTransformer(true, new CubeOutTransformer());
		}
		chartTypeCheck
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						switch (checkedId) {
						case R.id.piechart:
							mPager.setCurrentItem(0);
							
							break;

						case R.id.barchart:
							mPager.setCurrentItem(1);
							break;
						}
					}
				});
		questionTypeCheck
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						switch (checkedId) {
						case R.id.selectedquestion:
							chartTypeCheck.setVisibility(View.VISIBLE);
							mPager.setVisibility(View.VISIBLE);
							editQuestionAnswerListview.setVisibility(View.GONE);
							break;

						case R.id.editedquestion:
							chartTypeCheck.setVisibility(View.INVISIBLE);
							mPager.setVisibility(View.GONE);
							editQuestionAnswerListview.setVisibility(View.VISIBLE);
							break;
						}
					}
				});
		((RadioButton) findViewById(R.id.selectedquestion)).setChecked(true);
		buttons=new RadioButton[]{(RadioButton) findViewById(R.id.piechart),
		(RadioButton) findViewById(R.id.barchart)};
		buttons[0].setChecked(true);
		backLayout.setOnClickListener(this);
		getData();
	}

	/**
	 * 功能描述：从网络上获取对应id的json数据 Administrator 2014年7月25日 上午9:58:29
	 * 
	 * @return
	 */
	private String getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);// 加载
				String httpUrl = "http://nat.nat123.net:14313/oa/message/getVote.jsp?id="
						+ id;
				String result = HttpRequestUtil.getResponsesByGet(httpUrl);
				Log.e("result", result + "");
				// 若加载成功，则解析json字符串
				if (result.trim().startsWith("{\"votes\":[")) {
					((PieChartFragment) fragments[0]).setResult(result);
					((BarChartFragment) fragments[1]).setResult(result);

					handler.sendEmptyMessage(1);// 成功
				} else {
					handler.sendEmptyMessage(-1);// 失败
				}
			}
		}).start();
		return null;

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				dialog = new ProgressDialog(ShowStatisticsActivity.this);
				dialog.setMessage("加载中...");
				dialog.show();
				break;

			case -1:
				dialog.dismiss();
				AlertDialog.Builder builder = new Builder(
						ShowStatisticsActivity.this);
				builder.setTitle("请选择操作");
				builder.setMessage("加载失败，是否需要重试？");
				builder.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								getData();
							}
						});
				builder.setNegativeButton("否",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								ShowStatisticsActivity.this.finish();
							}
						});
				builder.create().show();
				break;
			case 1:
				dialog.dismiss();
				Toast.makeText(ShowStatisticsActivity.this,"加载成功！",Toast.LENGTH_SHORT).show();
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		buttons[arg0].setChecked(true);
	}

}
