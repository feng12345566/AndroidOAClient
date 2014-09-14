package com.mct.flow;

import com.mct.client.R;

import android.content.Context;
import android.graphics.Color;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Widget {
	public static final int TEXTVIEW = 1;// �ı�
	public static final int EDITTEXT = 2;// �༭��
	public static final int SPINNER = 3;// �����б�
	public static final int RADIOGROUP = 4;// ��ѡѡ��
	private int id;// �ؼ�id
	private long nodeId;//�ɱ༭�ڵ�id
	private String title;// �ؼ�����
	private int widgetType;// �ؼ�����
	private String item;// ��ѡѡ��
	private String value;// ��ǰֵ
	private boolean isEnable=false;// �Ƿ�ɱ༭
	private boolean canBeNull;// �Ƿ��Ϊ��
	private Context context;
	private TextView textView;
	private EditText editText;
	private Spinner spinner;
	private RadioGroup radioGroup;
	private String[] text;

	/**
	 * @param id
	 *            �ؼ�id
	 * @param nodeId
	 *            �ɱ༭�ڵ�id
	 * @param title
	 *            �ؼ�����
	 * @param widgetType
	 *            �ؼ�����
	 * @param item
	 *            ��ѡ�������widgetType=3��4
	 * @param value
	 *            widgetType=1��2��value��ʾ��ǰֵ��widgetType=3��4,value��ʾ��ǰѡ��ڼ���
	 * @param isEnable
	 *            �Ƿ�ɱ༭
	 * @param canBeNull
	 *            �Ƿ��Ϊ��
	 * @param context
	 */
	public Widget(Context context, int id,long nodeId, String title, int widgetType,
			String item, String value,  int canBeNull) {
		super();
		this.id = id;
		this.nodeId=nodeId;
		this.title = title;
		this.widgetType = widgetType;
		this.item = item;
		this.value = value;
		this.canBeNull = (canBeNull==0?false:true);
		this.context = context;
	}

	public LinearLayout initWidget() {
		LinearLayout rl = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(8, 0, 8, 0);
		lp.gravity=Gravity.CENTER_VERTICAL;
		rl.setLayoutParams(lp);
		rl.setMinimumHeight(120);
		rl.setOrientation(LinearLayout.HORIZONTAL);
		rl.setGravity(Gravity.CENTER_VERTICAL);
		rl.setBackgroundResource(R.drawable.textbg);
		ViewGroup.LayoutParams lp2 = new ViewGroup.LayoutParams(200,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		TextView tv = new TextView(context);
		tv.setLayoutParams(lp2);
		tv.setGravity(Gravity.CENTER);
		tv.setText(title);
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(15);
		tv.getPaint().setFakeBoldText(true);
		rl.addView(tv);
		ViewGroup.LayoutParams lp3 = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		switch (widgetType) {
		case 1:
			TextView textView = new TextView(context);
			textView.setLayoutParams(lp3);
			textView.setText(value);
			textView.setTextColor(Color.BLACK);
			textView.setId(id);
			rl.addView(textView);
			break;
		case 2:
			editText = new EditText(context);
			editText.setLayoutParams(lp3);
			editText.setText(value);
			editText.setId(id);
			editText.setTextColor(Color.BLACK);
			editText.setEnabled(isEnable);
			editText.setGravity(Gravity.CENTER_VERTICAL);
			rl.addView(editText);
			break;
		case 3:

			spinner = new Spinner(context);
			spinner.setLayoutParams(lp3);
			String[] data = item.split(",");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_item, data);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setId(id);
			spinner.setEnabled(isEnable);
			if (value != null) {
				spinner.setSelection(Integer.valueOf(value));
			}
			rl.addView(spinner);

			break;
		case 4:
			radioGroup = new RadioGroup(context);
			radioGroup.setLayoutParams(lp3);
			radioGroup.setOrientation(LinearLayout.HORIZONTAL);
			radioGroup.setId(id);

			text = item.split(",");
			for (int i = 0; i < text.length; i++) {
				RadioButton radioButton = new RadioButton(context);
				radioButton.setText(text[i]);
				radioButton.setId(1000 + i);
				radioButton.setEnabled(isEnable);
				if (value != null) {
					if (Integer.valueOf(value) == i) {
						radioButton.setChecked(true);
					}
				}
				radioGroup.addView(radioButton, i);
			}
			rl.addView(radioGroup);
			break;
		}
		return rl;
	}

	

	public int getId() {
		return id;
	}

	public String getValue() {
		switch (widgetType) {
		case 2:
			value=editText.getText().toString();
			break;
		case 3:
			value=String.valueOf(spinner.getSelectedItemPosition());
			break;
		case 4:
			value=String.valueOf(radioGroup.getCheckedRadioButtonId()-1000);
			break;
		}
		return value;
	}

	/**
	 * @param value
	 *            widgetType=1��2��value��ʾ��ǰֵ��widgetType=3��4,value��ʾ��ǰѡ��ڼ���
	 */
	public void setValue(String value) {
		this.value = value;
		switch (widgetType) {
		case 1:
			textView.setText(value);
			break;
		case 2:
			editText.setText(value);
			break;
		case 3:
			if (value != null) {
				if (Integer.valueOf(value) < spinner.getCount()) {
					spinner.setSelection(Integer.valueOf(value));
				}
			}
			break;
		case 4:
			if (value != null) {
				if (Integer.valueOf(value) < radioGroup.getChildCount()) {
					radioGroup.check(Integer.valueOf(value) + 1000);
				}
			}
			break;
		}

	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
		switch (widgetType) {
		case 1:
			break;
		case 2:
			editText.setEnabled(isEnable);
			break;
		case 3:
			spinner.setEnabled(isEnable);
			break;
		case 4:
			for (int i = 0; i < radioGroup.getChildCount(); i++) {
				radioGroup.getChildAt(i).setEnabled(isEnable);
			}
			break;
		}
	}

	public long getNodeId() {
		return nodeId;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public boolean isCanBeNull() {
		return canBeNull;
	}

	public void setCanBeNull(boolean canBeNull) {
		this.canBeNull = canBeNull;
	}
	
	public boolean checkNull(){
		Log.e("widget", title+":"+getValue());
		if((!canBeNull)&&(getValue()==null||getValue().equals(""))){
			Toast.makeText(context, title+"����Ϊ�գ�", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

}
