package com.mct.client;

import com.mct.fragment.DataTransferFragment;

import android.os.Bundle;

public class DataTransferActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		DataTransferFragment dataTransferFragment=new DataTransferFragment();
		setParameter("�ļ�����", dataTransferFragment, null, "����", null);
	}
   
}
