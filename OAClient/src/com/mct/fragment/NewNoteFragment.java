package com.mct.fragment;

import java.util.Calendar;

import com.mct.client.R;
import com.mct.model.Note;
import com.mct.util.MyAnimationUtils;
import com.mct.util.TimeRender;
import com.mct.util.UserDataDbUtil;
import com.mct.util.XmppTool;
import com.mct.view.SwitchButton;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewNoteFragment extends Fragment implements OnClickListener{
    private EditText notetitleedit;//编辑标题控件
    private TextView notetitleshowText;//显示标题
    private EditText notecontenteditText;//编辑内容控件
    private TextView notecontentshowText;//显示内容
    private EditText notestarttimeedit;
    private EditText noteendtimeedit;
    private SwitchButton notenotificationenable;
    private CheckBox notevoiceenable;
    private CheckBox noteshockenable;
    private Button notestarttimeselect;
    private Button noteendtimeselect;
    private String date;
    private boolean isNew;
    private long id;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_editnote, null);
		Bundle bundle=getArguments();
		date=bundle.getString("date");
		notetitleedit=(EditText) view.findViewById(R.id.notetitleedit);
		notetitleshowText=(TextView) view.findViewById(R.id.notetitleshowText);
		notecontenteditText=(EditText) view.findViewById(R.id.notecontenteditText);
		notecontentshowText=(TextView) view.findViewById(R.id.notecontentshowText);
		notestarttimeedit=(EditText) view.findViewById(R.id.notestarttimeedit);
		noteendtimeedit=(EditText) view.findViewById(R.id.noteendtimeedit);
		notenotificationenable=(SwitchButton) view.findViewById(R.id.notenotificationenable);
		notevoiceenable=(CheckBox) view.findViewById(R.id.notevoiceenable);
		noteshockenable=(CheckBox) view.findViewById(R.id.noteshockenable);
		notestarttimeselect=(Button) view.findViewById(R.id.notestarttimeselect);
		noteendtimeselect=(Button) view.findViewById(R.id.noteendtimeselect);
		notestarttimeselect.setOnClickListener(this);
		noteendtimeselect.setOnClickListener(this);
		boolean isShow=bundle.getBoolean("show", false);
		//传递参数包括id则为查看或修改，否则为新建
		isNew=!bundle.containsKey("id");
		if(!isNew){
		    id=bundle.getLong("id");
			Note note=UserDataDbUtil.shareUserDataDb(getActivity()).getNote(id);
			if(isShow){
				notetitleshowText.setText(note.getTitle());
				notecontentshowText.setText(note.getContent());
				
			}else{
				notetitleedit.setText(note.getTitle());
				notecontenteditText.setText(note.getContent());
			}
			noteendtimeedit.setText(TimeRender.getDate(note.getDotimeEnd(), "HH:mm"));
			notestarttimeedit.setText(TimeRender.getDate(note.getDotimeStart(), "HH:mm"));
			notenotificationenable.setChecked(note.isNotification());
			notevoiceenable.setChecked(note.isVoice());
			noteshockenable.setChecked(note.isShock());
		}
		if(isShow){
			show();
		}else{
			edit();
		}
		return view;
	}
	
	
	public void edit(){
		notetitleedit.setVisibility(View.VISIBLE);
		notecontenteditText.setVisibility(View.VISIBLE);
		notetitleshowText.setVisibility(View.GONE);
		notecontentshowText.setVisibility(View.GONE);
		notestarttimeselect.setVisibility(View.VISIBLE);
		noteendtimeselect.setVisibility(View.VISIBLE);
		notestarttimeedit.setEnabled(true);
		noteendtimeedit.setEnabled(true);
		notevoiceenable.setClickable(true);
		noteshockenable.setClickable(true);
		notenotificationenable.setEnabled(true);
	}
	 
	public void show(){
		notetitleshowText.setVisibility(View.VISIBLE);
		notecontentshowText.setVisibility(View.VISIBLE);
		notetitleedit.setVisibility(View.GONE);
		notecontenteditText.setVisibility(View.GONE);
		notestarttimeselect.setVisibility(View.GONE);
		noteendtimeselect.setVisibility(View.GONE);
		notestarttimeedit.setEnabled(false);
		noteendtimeedit.setEnabled(false);
		notevoiceenable.setClickable(false);
		noteshockenable.setClickable(false);
		notenotificationenable.setEnabled(false);
	}
	
	public void showToEdit(){
		edit();
		notetitleedit.setText(notetitleshowText.getText());
		notecontenteditText.setText(notecontentshowText.getText());
	}
	public  void sureValue(){
		String title=notetitleedit.getText().toString();
		if(title.equals("")){
			Toast.makeText(getActivity(), "代办事项不能为空！", Toast.LENGTH_SHORT).show();
			return ;
		}
		String content=notecontenteditText.getText().toString();
		String startTime=notestarttimeedit.getText().toString();
		String endTime=noteendtimeedit.getText().toString();
		if(startTime.equals("")){
			Toast.makeText(getActivity(), "开始时间不能为空！", Toast.LENGTH_SHORT).show();
			return ;
		}
		if(endTime.equals("")){
			Toast.makeText(getActivity(), "结束时间不能为空！", Toast.LENGTH_SHORT).show();
			return ;
		}
		Log.e("time", date+" "+startTime);
		long start=TimeRender.string2Date(date+" "+startTime, "yyyy-MM-dd HH:mm").getTime();
		long end=TimeRender.string2Date(date+" "+endTime, "yyyy-MM-dd HH:mm").getTime();
		if(end<=start){
			Toast.makeText(getActivity(), "结束时间必须大于开始时间！", Toast.LENGTH_SHORT).show();
			return ;
		}
		if(isNew){
		long newId=UserDataDbUtil.shareUserDataDb(getActivity()).insertNewNote(XmppTool.loginUser.getUserId(),
				title, content, start, end, notenotificationenable.isChecked(), 
				notevoiceenable.isChecked(), noteshockenable.isChecked());
		if(newId>0){
			Toast.makeText(getActivity(), "创建成功！", Toast.LENGTH_SHORT).show();
			getActivity().finish();
			MyAnimationUtils.outActivity(getActivity());
		}else{
			Toast.makeText(getActivity(), "创建失败，请重试！", Toast.LENGTH_SHORT).show();
		}
		}else{
			boolean isS=UserDataDbUtil.shareUserDataDb(getActivity()).modifyNote(id,
 title,
					content, start, end, notenotificationenable.isChecked(), 
					notevoiceenable.isChecked(), noteshockenable.isChecked());
			if(isS){
				Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
				getActivity().finish();
				MyAnimationUtils.outActivity(getActivity());
			}else{
				Toast.makeText(getActivity(), "修改失败，请重试！", Toast.LENGTH_SHORT).show();
			}
		}
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		switch (v.getId()) {
		
		case R.id.notestarttimeselect:
			new TimePickerDialog(getActivity(), new OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub
					notestarttimeedit.setText(timeFormat(arg1)+":"+timeFormat(arg2));
				}

			}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
			break;

		case R.id.noteendtimeselect:
			new TimePickerDialog(getActivity(), new OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub
					noteendtimeedit.setText(timeFormat(arg1)+":"+timeFormat(arg2));
				}

			}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
			break;
		}
	}
	
	private String timeFormat(int num){
		if(num<10){
			return "0"+num;
		}else{
			return num+"";
		}
	}
	
	public void deleteNote(){
		 boolean r= UserDataDbUtil.shareUserDataDb(getActivity()).deleteNote(id);
         Toast.makeText(getActivity(), "删除"+(r?"成功！":"失败，请重试！"), Toast.LENGTH_SHORT).show();     
	}

}
