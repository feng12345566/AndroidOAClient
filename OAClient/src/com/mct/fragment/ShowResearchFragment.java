package com.mct.fragment;

import java.util.LinkedList;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mct.client.R;
import com.mct.controls.ShowQuestionAdapter;
import com.mct.model.Answer;
import com.mct.model.Question;
import com.mct.util.Common;
import com.mct.util.HttpRequestUtil;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowResearchFragment extends Fragment implements OnClickListener {
	private TextView showResearchTitle;
	private TextView showResearchContent;
	private ListView showResearchListview;
	private LinkedList<Question> questionList;
	private ShowQuestionAdapter adapter;
	private String jsonStr;
	private String id;
	private String title;
	private String content;
	private ProgressDialog dialog;
	private LinearLayout inputLayout;
	private EditText inputEdit;
	private Button inputSureBtn;
	private Button inputCancleBtn;
	private MyReceiver myReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.showresearchlayout, null);
		id = getArguments().getString("id");
		title = getArguments().getString("title");
		showResearchTitle = (TextView) view
				.findViewById(R.id.showresearchtitle);
		showResearchContent = (TextView) view
				.findViewById(R.id.showresearchcontent);
		showResearchListview = (ListView) view
				.findViewById(R.id.showresearchlistview);
		inputLayout = (LinearLayout) view.findViewById(R.id.inputeditorlayout);
		inputEdit = (EditText) view.findViewById(R.id.inputeditor);
		inputSureBtn = (Button) view.findViewById(R.id.inputsurebtn);
		inputCancleBtn = (Button) view.findViewById(R.id.inputcancelbtn);
		inputSureBtn.setOnClickListener(this);
		inputCancleBtn.setOnClickListener(this);
		showResearchTitle.setText(title);
		questionList = new LinkedList<Question>();
		adapter = new ShowQuestionAdapter(getActivity());
		adapter.setqLinkedList(questionList);
		showResearchListview.setAdapter(adapter);
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Common.ACTION_SHOWEDITOR);
		getActivity().registerReceiver(myReceiver, filter);
		getData();
		return view;
	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(1);
				String httpUrl = "http://nat.nat123.net:14313/oa/message/getQuestion.jsp?id="
						+ id;
				jsonStr = HttpRequestUtil.getResponsesByGet(httpUrl);
				if (jsonStr.trim().contains("@")) {
					String[] str = jsonStr.trim().split("@");
					content = str[0];
					jsonToList(str[1]);
					handler.sendEmptyMessage(2);
				} else {
					handler.sendEmptyMessage(3);
				}
			}
		}).start();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				dialog = new ProgressDialog(getActivity());
				dialog.setMessage("加载中...");
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				break;

			case 2:
				dialog.dismiss();
				Toast.makeText(getActivity(), "加载成功！", Toast.LENGTH_SHORT)
						.show();
				if (content == null || content.equals("null")) {
					showResearchContent.setVisibility(View.GONE);
				} else {
					showResearchContent.setVisibility(View.VISIBLE);
					showResearchContent.setText(content);
				}
				adapter.setqLinkedList(questionList);
				adapter.notifyDataSetChanged();
				break;
			case 3:
				dialog.dismiss();
				Toast.makeText(getActivity(), "加载失败，请检查网络后重试！",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				showResearchListview.setSelection(adapter
						.getSelectedAnswer().getPostion());
				break;
			}
		}

	};

	/**
	 * 功能描述：将从网络获取的json转为list Administrator 2014年7月25日 上午10:20:43
	 * 
	 * @param jsonStr
	 */
	private void jsonToList(String jsonStr) {
		try {
			JSONObject json = new JSONObject(jsonStr);
			JSONArray questionArrary = json.getJSONArray("questions");
			for (int i = 0; i < questionArrary.length(); i++) {
				JSONObject question = questionArrary.getJSONObject(i);
				Question q = new Question();
				q.setMustEdit(question.getBoolean("mustedit"));
				q.setText(question.getString("text"));
				q.setNum(question.getInt("num"));
				q.setType(question.getInt("type"));
				LinkedList<Answer> answerList = new LinkedList<Answer>();
				if (question.isNull("answers")) {
					if (q.getType() == 3) {
						Answer answer = new Answer(0, i, "");
						answerList.add(answer);
						q.setAnswerList(answerList);
					} else {

						q.setAnswerList(null);
					}
				} else {
					JSONArray answerArrary = question.getJSONArray("answers");

					for (int j = 0; j < answerArrary.length(); j++) {
						Answer answer = new Answer(j, i,
								answerArrary.getString(j));
						answerList.add(answer);
					}
					q.setAnswerList(answerList);
				}

				questionList.add(q);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.inputsurebtn:
			String input = inputEdit.getText().toString();
			if (input.equals("")) {
				Toast.makeText(getActivity(), "回答内容不能为空！", Toast.LENGTH_SHORT)
						.show();
			} else {
				adapter.getSelectedAnswer().setText(input);
				inputLayout.setVisibility(View.GONE);
			}
			break;

		case R.id.inputcancelbtn:
			inputLayout.setVisibility(View.GONE);
			break;
		}
	}

	/**
	 * 功能描述：获取输入值，转为json Administrator 2014年7月29日 下午12:38:03
	 * {"questions":[{"num":1,"type":1,"size":4,"answer":2},{"num":2,"type":2,"size":4,"answers":[false,true,true,false]},
	 * {"num":3,"type":3,"answer":"XXXXX"}]}
	 * @return
	 */
	public String getValue() {
		String value = "";
		try {
			JSONObject json = new JSONObject();
			JSONArray questions = new JSONArray();
			for (int i = 0; i < questionList.size(); i++) {
				Question question = questionList.get(i);
				JSONObject questionObject = new JSONObject();
				questionObject.put("num", question.getNum());
				questionObject.put("type", question.getType());
				questionObject.put("size", question.getAnswerList().size());
				
				switch (question.getType()) {
				case 3:
					questionObject.put("answer", question.getAnswerList().get(0).getText());
					break;

				default:
					LinkedList<Answer> answers = question.getAnswerList();
					JSONArray checked = new JSONArray();
					for (int j = 0; j < answers.size(); j++) {

						checked.put(j, answers.get(j).isChecked() ? 1 : 0);
						
					}
					questionObject.put("answer", checked);
					break;
				}
				questions.put(i, questionObject);
				
			}
			json.put("questions", questions);
			value=json.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("value",value);
		return value;

	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (arg1.getAction() == Common.ACTION_SHOWEDITOR) {
				inputLayout.setVisibility(View.VISIBLE);
				inputEdit.setFocusable(true);
				inputEdit.setFocusableInTouchMode(true);
				inputEdit.requestFocus();
				InputMethodManager inputManager =

	                    (InputMethodManager)inputEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

	        inputManager.showSoftInput(inputEdit, 0);
				inputEdit.setText(adapter.getSelectedAnswer().getText());
				inputEdit.setSelection(adapter.getSelectedAnswer().getText()
						.length());
				handler.sendEmptyMessageDelayed(4, 500);
				
			}
		}

	}

}
