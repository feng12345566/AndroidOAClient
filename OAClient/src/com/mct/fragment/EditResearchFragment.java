package com.mct.fragment;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mct.client.R;
import com.mct.util.TimeRender;

public class EditResearchFragment extends Fragment implements OnClickListener {
	private String TAG="EditResearchActivity";
	private EditText dateEditText;
	private EditText timeEditText;
	private Button datepickerbtn, timepickerbtn;
private EditText researchTitleEditText,researchcontentedittext;
private RadioGroup publicRadioGroup;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.editresearchlayout, null);
		dateEditText = (EditText) view.findViewById(R.id.dataeditor);
		timeEditText = (EditText) view.findViewById(R.id.timeeditor);
		datepickerbtn = (Button) view.findViewById(R.id.datepickerbtn);
		timepickerbtn = (Button) view.findViewById(R.id.timepickerbtn);
		researchTitleEditText=(EditText) view.findViewById(R.id.researchtitleedittext);
		researchcontentedittext=(EditText) view.findViewById(R.id.researchcontentedittext);
		publicRadioGroup=(RadioGroup) view.findViewById(R.id.publicradiogroup);
		datepickerbtn.setOnClickListener(this);
		timepickerbtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		switch (arg0.getId()) {
		case R.id.datepickerbtn:
         Log.e("tag","datepickerbtn");
     
			new DatePickerDialog(getActivity(), new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker arg0, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					dateEditText.setText(arg1 + "-" + (arg2+1) + "-" + arg3);
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
			c.get(Calendar.DAY_OF_MONTH)).show();
			break;

		case R.id.timepickerbtn:
			Log.e("tag","timepickerbtn");
			new TimePickerDialog(getActivity(), new OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub
					timeEditText.setText(arg1+":"+arg2);
				}

			}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
			break;

		}
	}
	
	public String getValue(){
		String title=researchTitleEditText.getText().toString();
		String content=researchcontentedittext.getText().toString();
		String date=dateEditText.getText().toString();
		String time=timeEditText.getText().toString();
		int isPublic=-1;
		switch (publicRadioGroup.getCheckedRadioButtonId()) {
		case R.id.publicradiobtn1:
			isPublic=0;
			break;
		case R.id.publicradiobtn2:
			isPublic=1;
			break;
		case R.id.publicradiobtn3:
			isPublic=2;
			break;
		}
		if(title.equals("")||date.equals("")||time.equals("")||isPublic==-1){
			return null;
		}else{
		    JSONObject js=new JSONObject();
		    try {
				js.put("title", title);
				if(!content.equals("")){
				 js.put("content", content);
				}else{
					js.put("content", null);
				}
				    js.put("endtime", TimeRender.string2Date(date+" "+time, "yyyy-MM-dd HH:mm").getTime());
				    js.put("ispublic", isPublic);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   System.out.println(js.toString());
		    return js.toString();
		}
	
	}
	
}
