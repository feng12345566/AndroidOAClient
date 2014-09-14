package com.mct.client;


import java.lang.reflect.Method;

import com.mct.fragment.ShowMessageFragment;
import com.mct.util.Common;
import com.mct.util.HttpRequestUtil;
import com.mct.util.MyAnimationUtils;
import com.mct.util.XmppTool;
import com.mct.view.CustomClipLoading;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ShowMessageActivity extends FragmentActivity implements OnClickListener{
   private TextView backText;//����
   private TextView menu_layout_title;//����
   private LinearLayout backLayout;
   private ImageView moreApk;
   private String sender;//�������ķ�����
   private ShowMessageFragment fragment;
   private long id;
   private AlertDialog dialog;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_showmessage);
		Intent intent=getIntent();
		sender=intent.getStringExtra("sender");
		backText=(TextView) findViewById(R.id.backmycenter);
		backText.setText("����");
		menu_layout_title=(TextView) findViewById(R.id.menu_layout_title);
		menu_layout_title.setText("���ʼ�");
		backLayout=(LinearLayout) findViewById(R.id.backlayout);
		moreApk=(ImageView) findViewById(R.id.moreapplicationbtn);
		moreApk.setOnClickListener(this);
		FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
		Bundle bundle=new Bundle();
		id=intent.getLongExtra("id", -1);
		bundle.putLong("id",id );
		bundle.putLong("time", intent.getLongExtra("time", -1));
		bundle.putString("title", intent.getStringExtra("title"));
		fragment=new ShowMessageFragment();
		fragment.setArguments(bundle);
		transaction.replace(R.id.showmessageFrame, fragment);
		transaction.commit();
		backLayout.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.moreapplicationbtn:
			PopupMenu pop=new PopupMenu(this, v);
			pop.getMenuInflater().inflate(R.menu.menu_showmessage, pop.getMenu());
			setIconEnable(pop.getMenu(),true);
			pop.show();
			pop.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					// TODO Auto-generated method stub
					switch (item.getItemId()) {
					case R.id.showmessage_replyemail:
						//�����ʼ��༭����
						Intent intent=new Intent(ShowMessageActivity.this,PostMessageActivity.class);
						//��ʾΪ�༭�ʼ�
						intent.putExtra("opt", 1);
						//���ݷ�����id��Ϊ�ʼ�������
						intent.putExtra("receiver", sender);
						startActivity(intent);
						MyAnimationUtils.inActivity(ShowMessageActivity.this);
						break;
					case R.id.showmessage_transpondemail:
						//�����ʼ��༭����
						Intent intent2=new Intent(ShowMessageActivity.this,PostMessageActivity.class);
						intent2.putExtra("opt", 1);
						intent2.putExtra("title", fragment.getTitle());
						intent2.putExtra("content", fragment.getContent());
						intent2.putExtra("accessory", fragment.getAccessory());
						startActivity(intent2);
						MyAnimationUtils.inActivity(ShowMessageActivity.this);
						break;
					case R.id.showmessage_deleteemail:
				        new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								handler.sendEmptyMessage(0);
								String result=HttpRequestUtil.getResponsesByGet(Common.HTTPJSPSERVICE+"DeleteMessage.jsp?id="+id+"&userid='"+XmppTool.loginUser.getUserId()+"'");
								if(result!=null&&result.trim().equals("success")){
									handler.sendEmptyMessage(1);
								}else{
									handler.sendEmptyMessage(-1);
								}
							}
						}).start();
						break;
				
					}
					return true;
				}
			});
			break;

		case R.id.backlayout:
			finish();
			MyAnimationUtils.outActivity(this);
			break;
		}
		
	}
	//enableΪtrueʱ���˵����ͼ����Ч��enableΪfalseʱ��Ч��4.0ϵͳĬ����Ч
    private void setIconEnable(Menu menu, boolean enable)
    {
    	try 
    	{
			Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
			Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
			m.setAccessible(true);
			
			//MenuBuilderʵ��Menu�ӿڣ������˵�ʱ����������menu��ʵ����MenuBuilder����(java�Ķ�̬����)
			m.invoke(menu, enable);
    		
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
    }
	
    
    private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				AlertDialog.Builder builder=new Builder(ShowMessageActivity.this);
				CustomClipLoading view=new CustomClipLoading(ShowMessageActivity.this);
				view.setMessage("�ύ��...");
				builder.setView(view);
				dialog=builder.create();
				dialog.show();
				break;

			case 1:
				dialog.dismiss();
				Toast.makeText(ShowMessageActivity.this, "ɾ���ɹ������ڻ���վ�һأ�", Toast.LENGTH_SHORT).show();
				break;
				
			case -1:
				dialog.dismiss();
				Toast.makeText(ShowMessageActivity.this, "ɾ��ʧ�ܣ������ԣ�", Toast.LENGTH_SHORT).show();
				break;
			}
		}
    	
    };
}
