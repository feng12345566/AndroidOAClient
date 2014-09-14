package com.mct.fragment;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mct.client.R;
import com.mct.controls.QuestionAapter;
import com.mct.model.Answer;
import com.mct.model.Question;
import com.mct.view.CustomSpinner;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mct.view.CustomSpinner.OnItemSeletedListener;

public class EditMoreResearchFragment extends Fragment implements
		OnClickListener, OnItemSeletedListener {
	private CustomSpinner spinner, questionNum;
	private ListView editingQuestionListView;
	private LinkedList<Question> questionList;
	private ArrayAdapter<String> numAdapter;
	private QuestionAapter questionAapter;
	private Button sureEditOptBtn;
	private LinkedList<String> numList;
	private int num = 1;// ��ǰѡ�е����
	private int opt = 0;// ѡ��Ĳ���
	private SharedPreferences preferences;

	private static final String[] opts = new String[] { "�������", "ɾ������", "���ѡ��",
			"ɾ��ѡ��" };
	private static final String[] items = new String[] { "��ѡ��", "��ѡ��", "�����" };
	private int questionType = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.questionlistview, null);
		spinner = (CustomSpinner) view.findViewById(R.id.editingquestionopt);
		questionNum = (CustomSpinner) view
				.findViewById(R.id.editingquestionnum);
		editingQuestionListView = (ListView) view
				.findViewById(R.id.editingquestionlistview);
		sureEditOptBtn = (Button) view.findViewById(R.id.sureeditoptbtn);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.select_dialog_item, opts);
		spinner.setAdapter(adapter);
		numList = new LinkedList<String>();
		numList.add("1");
		numAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.select_dialog_item, numList);
		questionNum.setAdapter(numAdapter);
		questionList = new LinkedList<Question>();
		preferences = getActivity().getSharedPreferences("research", 0);
		jsonToList();
		questionAapter = new QuestionAapter(getActivity());
		questionAapter.setqArrayList(questionList);
		editingQuestionListView.setAdapter(questionAapter);
		sureEditOptBtn.setOnClickListener(this);
		spinner.setOnItemSeletedListener(this);
		questionNum.setOnItemSeletedListener(this);
		return view;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.sureeditoptbtn:
			switch (opt) {
			case 0:
               //�������
				AlertDialog.Builder builder = new Builder(getActivity());
				builder.setTitle("��ѡ�����������");
				builder.setIcon(R.drawable.icon_hint);
				builder.setSingleChoiceItems(items, 0,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								questionType = arg1 + 1;
							}

						});
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								// ��num�⴦���һ��
								Question question = new Question();
								question.setNum(num);
								question.setType(questionType);
								questionList.add(num - 1, question);
								for (int i = num; i < questionList.size(); i++) {
									questionList.get(i).setNum(i + 1);
								}
								questionAapter.notifyDataSetChanged();
								numList.add(String.valueOf(questionList.size() + 1));
								numAdapter.notifyDataSetChanged();
							}
						});
				builder.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
							}
						});
				builder.create().show();

				break;

			case 1:
				questionList.remove(num - 1);
				for (int i = num - 1; i < questionList.size(); i++) {
					questionList.get(i).setNum(i + 1);
				}
				questionAapter.notifyDataSetChanged();
				numList.removeLast();
				numAdapter.notifyDataSetChanged();
				Toast.makeText(getActivity(), "��" + num + "�ⱻ�ɹ�ɾ��",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				LinkedList<Answer> answerList = questionList.get(num - 1)
						.getAnswerList();
				if (answerList == null) {
					answerList = new LinkedList<Answer>();
				}
					answerList.add(new Answer(answerList.size(), num - 1, ""));
					questionList.get(num - 1).setAnswerList(answerList);
				
				questionAapter.notifyDataSetChanged();
				break;
			case 3:
				questionList.get(num - 1).getAnswerList().removeLast();
				questionAapter.notifyDataSetChanged();
				break;
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onItemSeleted(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.editingquestionopt:
			opt = position;
			break;

		case R.id.editingquestionnum:
			num = position + 1;
			break;
		}
	}

	/*
	 * {"questions":[{"num":1,"type":1,"mustedit":true,"text":"question1",
	 * "answers":["ѡ��1","ѡ��2","ѡ��3","ѡ��4"]},
	 * {"num":2,"type":2,"mustedit":false,"text":"question2",
	 * "answers":["ѡ��1","ѡ��2","ѡ��3","ѡ��4"]}]}
	 */
	private String getValue() {
		LinkedList<Question> qList = questionAapter.getqArrayList();
		JSONObject jsonObject = new JSONObject();
		JSONArray questions = new JSONArray();
		for (int i = 0; i < qList.size(); i++) {
			Question question = qList.get(i);
			JSONArray answeritem = null;
			if (question.getAnswerList() != null
					&& question.getAnswerList().size() != 0) {
				answeritem = new JSONArray();
				for (int j=0;j<question.getAnswerList().size();j++) {
					answeritem.put(question.getAnswerList().get(j).getText());
				}
			}
			JSONObject questionObject = new JSONObject();
			try {
				questionObject.put("num", question.getNum())
						.put("type", question.getType())
						.put("mustedit", question.isMustEdit())
						.put("text", question.getText())
				         .put("answers", answeritem);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			questions.put(questionObject);
		}
		try {
			jsonObject.put("questions", questions);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	private void jsonToList() {
		String jsonStr = preferences.getString("question", "");
		Log.e("jsonStr", jsonStr);
		if (!jsonStr.equals("")) {
			try {
				JSONObject jo = new JSONObject(jsonStr);
				JSONArray questions = jo.getJSONArray("questions");
				for (int i = 0; i < questions.length(); i++) {
					Question question = new Question();
					JSONObject questionObj = questions.getJSONObject(i);
					question.setNum(questionObj.getInt("num"));
					question.setText(questionObj.getString("text"));
					question.setType(questionObj.getInt("type"));
					question.setMustEdit(questionObj.getBoolean("mustedit"));
					if (questionObj.has("answers")) {
						JSONArray items = questionObj.getJSONArray("answers");
						if (items != null && items.length() != 0) {
							LinkedList<Answer> answers = new LinkedList<Answer>();
							for (int j = 0; j < items.length(); j++) {
								answers.add(new Answer(j, i, items.getString(j)));
							}
							question.setAnswerList(answers);
						}
						
					}else{
						question.setAnswerList(null);
					}
					questionList.add(question);
					Log.e("question", questionList.size() + "");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.e("json", getValue());
		preferences.edit().putString("question", getValue()).commit();
	}

}
