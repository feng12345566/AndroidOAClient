package com.mct.fragment;

import com.mct.client.R;
import com.mct.util.SharePeferenceUtils;
import com.mct.view.SwitchButton;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AttendanceSettingFragment extends Fragment implements
		OnClickListener, OnCheckedChangeListener {
	private Button[] repeatBtns = new Button[4];
	private Button[] textViews = new Button[4];
	private boolean[][] xq = new boolean[4][7];
	private String[] times = new String[4];
	private SwitchButton[] switchButtons = new SwitchButton[4];
	private SharePeferenceUtils sharePeferenceUtils;
	private static final String[] items = new String[] { "星期日", "星期一", "星期二",
			"星期三", "星期四", "星期五", "星期六" };
	private boolean onResume = false;

	private SwitchButton autoAttendanceBtn;// 自动签到开关
	private SwitchButton locationNotificationBtn;// 通知提醒开关

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.attendancesetting, null);
		repeatBtns[0] = (Button) view.findViewById(R.id.repeat1);
		repeatBtns[1] = (Button) view.findViewById(R.id.repeat2);
		repeatBtns[2] = (Button) view.findViewById(R.id.repeat3);
		repeatBtns[3] = (Button) view.findViewById(R.id.repeat4);
		textViews[0] = (Button) view.findViewById(R.id.timesettingbtn1);
		textViews[1] = (Button) view.findViewById(R.id.timesettingbtn2);
		textViews[2] = (Button) view.findViewById(R.id.timesettingbtn3);
		textViews[3] = (Button) view.findViewById(R.id.timesettingbtn4);
		switchButtons[0] = (SwitchButton) view
				.findViewById(R.id.autoattendancebtn1);
		switchButtons[1] = (SwitchButton) view
				.findViewById(R.id.autoattendancebtn2);
		switchButtons[2] = (SwitchButton) view
				.findViewById(R.id.autoattendancebtn3);
		switchButtons[3] = (SwitchButton) view
				.findViewById(R.id.autoattendancebtn4);
		autoAttendanceBtn = (SwitchButton) view
				.findViewById(R.id.autoattendancebtn);
		locationNotificationBtn = (SwitchButton) view
				.findViewById(R.id.locationnotificationbtn);
		autoAttendanceBtn.setOnCheckedChangeListener(this);

		for (int i = 0; i < 4; i++) {
			repeatBtns[i].setOnClickListener(this);
			textViews[i].setOnClickListener(this);
			switchButtons[i].setOnCheckedChangeListener(this);
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.repeat1:
			showWeekSelctDialog(0);
			break;
		case R.id.repeat2:
			showWeekSelctDialog(1);
			break;
		case R.id.repeat3:
			showWeekSelctDialog(2);
			break;
		case R.id.repeat4:
			showWeekSelctDialog(3);
			break;
		case R.id.timesettingbtn1:
			showTimeSelectDialog(0);
			break;
		case R.id.timesettingbtn2:
			showTimeSelectDialog(1);
			break;
		case R.id.timesettingbtn3:
			showTimeSelectDialog(2);
			break;
		case R.id.timesettingbtn4:
			showTimeSelectDialog(3);
			break;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sharePeferenceUtils = SharePeferenceUtils.getInstance(getActivity());
		for (int i = 0; i < 4; i++) {
			xq[i] = sharePeferenceUtils.getRepeat(i);
			repeatBtns[i].setText(getRepeatString(xq[i]));
			times[i] = sharePeferenceUtils.getTime(i);
			textViews[i].setText(times[i]);
			switchButtons[i].setChecked(sharePeferenceUtils.getAlarmActive(i));
		}
		onResume = true;
	}

	private void showTimeSelectDialog(final int i) {
		String[] t = times[i].split(":");
		int hourOfDay = Integer.valueOf(t[0]);
		int minute = Integer.valueOf(t[1].startsWith("0") ? t[1].substring(1)
				: t[1]);
		new TimePickerDialog(getActivity(), new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				times[i] = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":"
						+ (minute < 10 ? "0" + minute : minute);
				textViews[i].setText(times[i]);
				sharePeferenceUtils.modifyTime(i, times[i]);
			}
		}, hourOfDay, minute, true).show();
	}

	private void showWeekSelctDialog(final int i) {

		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("选择星期");
		builder.setMultiChoiceItems(items, xq[i],
				new OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						// TODO Auto-generated method stub
						xq[i][which] = isChecked;

					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				repeatBtns[i].setText(getRepeatString(xq[i]));
				sharePeferenceUtils.modifyRepeat(i, xq[i]);
			}

		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}

	private String getRepeatString(boolean[] repeat) {
		String[] zhou = new String[] { "日", "一", "二", "三", "四", "五", "六" };
		StringBuffer str = new StringBuffer();
		str.append("周");
		for (int i = 0; i < 7; i++) {
			if (repeat[i]) {
				str.append(zhou[i]);
				str.append("、");
			}
		}
		str.deleteCharAt(str.length() - 1);
		String repeats = str.toString();
		System.out.println(repeats);
		if (repeats.equals("")) {
			return "不重复";
		} else if (repeats.equals("周一、二、三、四、五")) {
			return "工作日";
		} else if (repeats.equals("周日、一、二、三、四、五、六")) {
			return "每天";
		} else {
			return repeats;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (onResume) {
			switch (buttonView.getId()) {

			case R.id.autoattendancebtn1:
				sharePeferenceUtils.getInstance(getActivity()).setSwitch(
						sharePeferenceUtils.ALARM0, isChecked);
				break;
			case R.id.autoattendancebtn2:
				sharePeferenceUtils.getInstance(getActivity()).setSwitch(
						sharePeferenceUtils.ALARM1, isChecked);
				break;
			case R.id.autoattendancebtn3:
				sharePeferenceUtils.getInstance(getActivity()).setSwitch(
						sharePeferenceUtils.ALARM2, isChecked);
				break;
			case R.id.autoattendancebtn4:
				sharePeferenceUtils.getInstance(getActivity()).setSwitch(
						sharePeferenceUtils.ALARM3, isChecked);
				break;

			case R.id.autoattendancebtn:
				sharePeferenceUtils.getInstance(getActivity()).setSwitch(
						sharePeferenceUtils.AUTOATTENDANCE, isChecked);
				if (isChecked) {

					locationNotificationBtn.setEnabled(true);
				} else {
					locationNotificationBtn.setChecked(false);
					locationNotificationBtn.setEnabled(false);
					for (int i = 0; i < 4; i++) {
						switchButtons[i].setChecked(false);
					}
				}
				for (int i = 0; i < 4; i++) {
					switchButtons[i].setEnabled(isChecked);
				}
				break;

			case R.id.locationnotificationbtn:
				sharePeferenceUtils.getInstance(getActivity()).setSwitch(
						sharePeferenceUtils.ATTENDANCENOTIFICATION, isChecked);
break;
			}
		}
	}
}
