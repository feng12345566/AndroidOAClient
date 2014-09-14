package com.mct.receiver;

import com.mct.service.LocationService;
import com.mct.util.AlarmUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent locationIntent=new Intent(context,LocationService.class);
		//���ζ�λ
		locationIntent.putExtra("locationType", 1);
		locationIntent.putExtra("useage","attendance");
		locationIntent.putExtra("isAuto",true);
		//�����ٶȶ�λ����
		context.startService(locationIntent);
		AlarmUtils.resetAlarm(context);
	}

}
