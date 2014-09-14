package com.mct.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.mct.client.R;
import com.mct.model.Answer;
import com.mct.model.Question;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuestionAapter extends BaseAdapter {
	private LinkedList<Question> qArrayList;
	private LayoutInflater inflater;
	private int index;
	private ArrayList<RadioButton> radioButtons;
	private static final String[] answerItems = new String[] { "A", "B", "C",
			"D", "E", "F", "G", "H", "I" };

	public QuestionAapter(Context context) {
		inflater = (LayoutInflater) context
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		radioButtons = new ArrayList<RadioButton>();
	}

	public LinkedList<Question> getqArrayList() {
		return qArrayList;
	}

	public void setqArrayList(LinkedList<Question> qArrayList) {
		this.qArrayList = qArrayList;

	}

	public ArrayList<RadioButton> getRadioButtons() {
		return radioButtons;
	}

	public void setRadioButtons(ArrayList<RadioButton> radioButtons) {
		this.radioButtons = radioButtons;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return qArrayList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return qArrayList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int postion, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (view == null) {
			view = inflater.inflate(R.layout.questionedititem, null);
			holder = new ViewHolder();
			holder.numText = (TextView) view.findViewById(R.id.editquestionnum);
			holder.radioButton = (RadioButton) view
					.findViewById(R.id.questioneditbtn);

			holder.quesetionEdit = (EditText) view
					.findViewById(R.id.editingquestionedit);
			holder.typeText = (TextView) view
					.findViewById(R.id.editquestiontype);
			holder.answerEditLayout = (LinearLayout) view
					.findViewById(R.id.editansweritemlayout);
			holder.questionEditingItemLayout = (LinearLayout) view
					.findViewById(R.id.questioneditingitemlayout);
			holder.nullcheckTextview = (CheckBox) view
					.findViewById(R.id.nullcheckTextview);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.radioButton.setTag(postion);
		radioButtons.add(holder.radioButton);
		Question question = qArrayList.get(postion);
		holder.numText.setText(String.valueOf(question.getNum()));
		holder.quesetionEdit.setText(question.getText());
		holder.quesetionEdit.setTag(question);
		// holder.quesetionEdit.addTextChangedListener(new
		// MyTextChangedListenser(question,1,0));
		holder.numText.setText(String.valueOf(question.getNum()));
		holder.nullcheckTextview.setChecked(question.isMustEdit());
		holder.nullcheckTextview.setTag(question);
		holder.nullcheckTextview
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						Question q = (Question) ((CheckBox) arg0).getTag();
						q.setMustEdit(arg1);
						qArrayList.set(q.getNum()-1, q);
					}
				});
		switch (question.getType()) {
		case 1:
			// 单选题
			holder.typeText.setText("(单选题)");
			break;

		case 2:
			// 多选题
			holder.typeText.setText("(多选题)");
			break;

		case 3:
			// 简答题
			holder.typeText.setText("(简答题)");
			break;
		}

		holder.radioButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1) {
							for (RadioButton rb : radioButtons) {
								if (rb.getTag() != arg0.getTag()) {
									rb.setChecked(false);
								}
							}
						}
					}
				});

		if (postion == qArrayList.size() - 1) {
			holder.questionEditingItemLayout
					.setBackgroundResource(R.drawable.textbg);
		} else {
			holder.questionEditingItemLayout
					.setBackgroundResource(R.drawable.textbg_top);
		}
		LinkedList<Answer> answerList = question.getAnswerList();
		if (answerList == null || answerList.size() == 0) {
			holder.answerEditLayout.setVisibility(View.GONE);
		} else {
			holder.answerEditLayout.removeAllViewsInLayout();
			for (int i = 0; i < answerList.size(); i++) {
				holder.answerEditLayout.setVisibility(View.VISIBLE);
				LinearLayout ll = (LinearLayout) inflater.inflate(
						R.layout.editansweritem, null);
				TextView tv = (TextView) ll
						.findViewById(R.id.editansweritemnum);
				EditText et = (EditText) ll
						.findViewById(R.id.editansweritemtext);
				et.setText(answerList.get(i).getText());
				et.setTag(answerList.get(i));
				et.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						// TODO Auto-generated method stub
						if (!arg1) {
							Answer a = (Answer) arg0.getTag();
							Question qt = qArrayList.get(a.getPostion());
							a.setText(((EditText) arg0).getText().toString());
							LinkedList<Answer> answers=qt.getAnswerList();
							if(answers==null){
								qt.setAnswerList(new LinkedList<Answer>());
							}else{
								answers.set(a.getLocation(), a);
							}
							qArrayList.set(a.getPostion(), qt);
						}
					}
				});
				// et.addTextChangedListener(new
				// MyTextChangedListenser(question, 2,i));
				tv.setText(answerItems[i]);
				holder.answerEditLayout.addView(ll);
			}
		}
		holder.quesetionEdit
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						// TODO Auto-generated method stub
						Question q = (Question) arg0.getTag();
						if (arg1) {						
							index=q.getNum()-1;						
						}else{
							q.setText(((EditText) arg0).getText().toString());
                            qArrayList.set(q.getNum()-1, q);
						}
					}
				});
		holder.quesetionEdit.clearFocus();
		if (index == postion) {
			holder.quesetionEdit.requestFocus();
			holder.quesetionEdit.setSelection(holder.quesetionEdit.getText()
					.length());

		}

		return view;
	}

	class ViewHolder {
		TextView numText;
		RadioButton radioButton;
		TextView typeText;
		LinearLayout answerEditLayout, questionEditingItemLayout;
		EditText quesetionEdit;
		CheckBox nullcheckTextview;
	}

}
