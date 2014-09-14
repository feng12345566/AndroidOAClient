package com.mct.application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

public class MyApplication extends Application{
	private static MyApplication mInstance = null;
    public boolean m_bKeyRight = true;
    public BMapManager mBMapManager = null;
    private ExecutorService es = Executors.newFixedThreadPool(3);

	// app���� ִ���������
	public void execRunnable(Runnable r) {
		if (!es.isShutdown()) {
			es.execute(r);
		}
	}
	
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
		initEngineManager(this);
	}
	
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(new MyGeneralListener())) {
            Toast.makeText(MyApplication.getInstance().getApplicationContext(), 
                    "BMapManager  ��ʼ������!", Toast.LENGTH_LONG).show();
        }
	}
	
	public static MyApplication getInstance() {
		return mInstance;
	}
	
	
	// �����¼���������������ͨ�������������Ȩ��֤�����
    public static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "���������������",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "������ȷ�ļ���������",
                        Toast.LENGTH_LONG).show();
            }
       
        }

        @Override
        public void onGetPermissionState(int iError) {
        	//����ֵ��ʾkey��֤δͨ��
            if (iError != 0) {
                //��ȨKey����
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), 
                        "���� MyApplication.java�ļ�������ȷ����ȨKey,������������������Ƿ�������error: "+iError, Toast.LENGTH_LONG).show();
                MyApplication.getInstance().m_bKeyRight = false;
            }
            else{
            	MyApplication.getInstance().m_bKeyRight = true;
            	Toast.makeText(MyApplication.getInstance().getApplicationContext(), 
                        "key��֤�ɹ�", Toast.LENGTH_LONG).show();
            }
        }
    }
}
