package com.mct.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mct.calendar.CalendarPickerView;
import com.mct.calendar.CalendarPickerView.OnDateSelectedListener;
import com.mct.calendar.CalendarPickerView.SelectionMode;
import com.mct.client.NoteBookActivity;
import com.mct.client.R;
import com.mct.util.TimeRender;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScheduleManagerFragment extends Fragment{
    private CalendarPickerView calendar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.schedulefragment, null);
		final Calendar nextYear = Calendar.getInstance();
	    nextYear.add(Calendar.YEAR, 1);

	    final Calendar lastYear = Calendar.getInstance();
	    lastYear.add(Calendar.YEAR, -1);

	    calendar = (CalendarPickerView) view.findViewById(R.id.calendar_view);
	    calendar.init(lastYear.getTime(), nextYear.getTime()) //
	        .inMode(SelectionMode.SINGLE) //
	        .withSelectedDate(new Date());
	    calendar.setOnDateSelectedListener(new OnDateSelectedListener() {
			
			@Override
			public void onDateUnselected(Date date) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDateSelected(Date date) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),NoteBookActivity.class);
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
				String dateStr=simpleDateFormat.format(date);
				intent.putExtra("date", dateStr);
				startActivity(intent);
			}
		});
		return view;
	}

}
