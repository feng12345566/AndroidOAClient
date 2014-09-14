package com.mct.controls;

import java.util.LinkedList;

import com.mct.client.R;
import com.mct.model.Answer;
import com.mct.model.Question;
import com.mct.util.Common;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ShowQuestionAdapter extends BaseAdapter {
	private LinkedList<Question> qLinkedList;
	private LayoutInflater inflater;
	private Context context;
	private Answer selectedAnswer;
	private static final String[] answerItems = new String[] { "A", "B", "C",
			"D", "E", "F", "G", "H", "I" };

	public ShowQuestionAdapter(Context context) {
		inflater = (LayoutInflater) context
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		this.context = context;

	}

	public LinkedList<Question> getqLinkedList() {
		return qLinkedList;
	}

	public void setqLinkedList(LinkedList<Question> qLinkedList) {
		this.qLinkedList = qLinkedList;
	}

	public Answer getSelectedAnswer() {
		return selectedAnswer;
	}

	public void setSelectedAnswer(Answer selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return qLinkedList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return qLinkedList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int postion, View contentView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		contentView = inflater.inflate(R.layout.showresearchquestionitem, null);
		TextView mustEdit = (TextView) contentView
				.findViewById(R.id.showquestionmustedit);
		TextView num = (TextView) contentView
				.findViewById(R.id.showquestionnum);
		TextView type = (TextView) contentView
				.findViewById(R.id.showquestiontype);
		TextView content = (TextView) contentView
				.findViewById(R.id.showquestioncontent);
		LinearLayout answerLayout = (LinearLayout) contentView
				.findViewById(R.id.showansweritemlayout);
		LinearLayout showQuestionLayout = (LinearLayout) contentView
				.findViewById(R.id.showquestionlayout);
        
		Question question = qLinkedList.get(postion);
		if(postion==qLinkedList.size()-1){
			showQuestionLayout.setBackgroundResource(R.drawable.textbg);
		}else{
			showQuestionLayout.setBackgroundResource(R.drawable.textbg_top);
		}
		if (question.isMustEdit()) {
			mustEdit.setVisibility(View.VISIBLE);
		} else {
			mustEdit.setVisibility(View.GONE);
		}
		num.setText(question.getNum() + "");
		android.widget.LinearLayout.LayoutParams lp2 = new android.widget.LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinkedList<Answer> answerList = question.getAnswerList();
		switch (question.getType()) {
		case 1:
			type.setText("(单选题)");
			RadioGroup rg = new RadioGroup(context);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			rg.setLayoutParams(lp);
			rg.setOrientation(LinearLayout.VERTICAL);
			if (answerList != null && answerList.size() != 0) {
				for (int i = 0; i < answerList.size(); i++) {
					Answer answer = answerList.get(i);
					RadioButton rb = new RadioButton(context);
					rb.setLayoutParams(lp2);
					rb.setTag(answer);
					rb.setText(answerItems[i] + "."
							+ answerList.get(i).getText());
					rb.setId(postion * 10 + i + 100);
					rb.setChecked(answerList.get(i).isChecked());
					rg.addView(rb);
					rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {
							// TODO Auto-generated method stub
							if (arg1) {
								((Answer) ((RadioButton) arg0).getTag())
										.setChecked(true);
							} else {
								((Answer) ((RadioButton) arg0).getTag())
										.setChecked(false);
							}
						}
					});
				}
				answerLayout.addView(rg);
			}

			break;

		case 2:
			type.setText("(多选题)");

			if (answerList != null && answerList.size() != 0) {
				for (int i = 0; i < answerList.size(); i++) {
					CheckBox checkBox = new CheckBox(context);
					Answer answer2 = answerList.get(i);
					checkBox.setLayoutParams(lp2);
					checkBox.setText(answerItems[i] + "." + answer2.getText());
					checkBox.setChecked(answer2.isChecked());
					checkBox.setTag(answer2);
					answerLayout.addView(checkBox);
					checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {
							// TODO Auto-generated method stub
							if (arg1) {
								((Answer) ((CheckBox) arg0).getTag())
										.setChecked(true);
							} else {
								((Answer) ((CheckBox) arg0).getTag())
										.setChecked(false);
							}
						}
					});
				}
			}

			break;
		case 3:
			Answer answer3 = answerList.get(0);
			type.setText("(简答题)");
			EditText editText = new EditText(context);
			editText.setLayoutParams(lp2);
			editText.setText(answer3.getText());
			answerLayout.addView(editText);
			editText.setTag(answer3);
			editText.setInputType(InputType.TYPE_NULL);
			editText.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					selectedAnswer = (Answer) (arg0.getTag());
					Intent intent = new Intent(Common.ACTION_SHOWEDITOR);
					context.sendBroadcast(intent);
				}
			});

			break;
		}
		content.setText(question.getText());

		return contentView;
	}

}
