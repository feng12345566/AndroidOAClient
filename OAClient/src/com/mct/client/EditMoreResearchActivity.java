package com.mct.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mct.fragment.EditMoreResearchFragment;
import com.mct.util.HttpRequestUtil;
import com.mct.util.XmppTool;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class EditMoreResearchActivity extends BaseActivity implements OnClickListener{
private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Fragment contentFragment=new EditMoreResearchFragment();
		setParameter("调查设计",contentFragment,null,"上一页","提交");
		nextPage.setOnClickListener(this);
		lastPage.setOnClickListener(this);
	}

	@Override
	public void setParameter(String title, Fragment contentFragment,
			Bundle bundle, String leftTitle, String rightTitle) {
		// TODO Auto-generated method stub
		super.setParameter(title, contentFragment, bundle, leftTitle, rightTitle);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			this.finish();
		}
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.nextpage:
			//获取本地存储的值
			SharedPreferences researchData=getSharedPreferences("research", 0);
			String researchJson=researchData.getString("research", "");
			String questionJson=researchData.getString("question", "");
			Log.e("mtag",researchJson+","+questionJson);
			sendDataToServer(researchJson,questionJson);
			break;

		case R.id.backlayout:
			this.finish();
			break;
		}
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				dialog=new ProgressDialog(EditMoreResearchActivity.this);
				dialog.setMessage("提交中，请稍后...");
				dialog.show();
				break;
			case 1:
				dialog.dismiss();
				Toast.makeText(EditMoreResearchActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
				EditMoreResearchActivity.this.finish();
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(EditMoreResearchActivity.this, "提交失败，请检查网络后重试！", Toast.LENGTH_SHORT).show();
				break;

			}
		}
		
	};
	
	private void sendDataToServer(final String researchJson,final String questionJson){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
				String httpUrl="http://nat.nat123.net:14313/oa/message/postresearchdesgin.jsp";
				List<NameValuePair> list=new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("researchjson",researchJson));
				list.add(new BasicNameValuePair("questionjson",questionJson));
				list.add(new BasicNameValuePair("userid",XmppTool.loginUser.getUserId()));
				String result=HttpRequestUtil.getResponsesByPost(httpUrl, list);
				if(result.trim().startsWith("id")){
					handler.sendEmptyMessage(1);
				}else{
					handler.sendEmptyMessage(2);
				}
			}
		}).start();
		
	}

}
