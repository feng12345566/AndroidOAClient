package com.mct.client;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mct.fragment.ShowResearchFragment;
import com.mct.util.HttpRequestUtil;
import com.mct.util.XmppTool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * 显示问卷调查内容界面
 * 
 * @author fengyouchun
 * @version 创建时间：2014年7月22日 下午1:45:24
 */
public class ShowResearchActivity extends BaseActivity implements
		OnClickListener {
	private ShowResearchFragment contentFragment;
	private String id;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		contentFragment = new ShowResearchFragment();
		Bundle bundle = new Bundle();
		bundle.putString("id", id);
		bundle.putString("title", intent.getStringExtra("title"));
		setParameter("问卷调查", contentFragment, bundle, "调查列表", "提交");
		nextPage.setOnClickListener(this);
	}

	@Override
	public void setParameter(String title, Fragment contentFragment,
			Bundle bundle, String leftTitle, String rightTitle) {
		// TODO Auto-generated method stub
		super.setParameter(title, contentFragment, bundle, leftTitle,
				rightTitle);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.nextpage:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					hanlder.sendEmptyMessage(0);
					String jsonStr = contentFragment.getValue();
					if (jsonStr != null && !jsonStr.equals("{}")) {
						String httpUrl = "http://nat.nat123.net:14313/oa/message/PostVote.jsp";
						ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
						list.add(new BasicNameValuePair("researchid", id));
						list.add(new BasicNameValuePair("json", jsonStr));
						list.add(new BasicNameValuePair("userid",
								XmppTool.loginUser.getUserId()));
						String result = HttpRequestUtil.getResponsesByPost(
								httpUrl, list);
						if (result != null && result.trim().equals("1")) {
							hanlder.sendEmptyMessage(1);
						} else {
							hanlder.sendEmptyMessage(2);
						}

					} else {
						hanlder.sendEmptyMessage(2);
					}
				}
			}).start();

			break;

		default:
			break;
		}
	}

	private Handler hanlder = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				dialog=new ProgressDialog(ShowResearchActivity.this);
				dialog.setMessage("提交中,请稍后...");
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				break;

			case 1:
				dialog.dismiss();
				Toast.makeText(ShowResearchActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
				ShowResearchActivity.this.finish();
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(ShowResearchActivity.this, "提交失败，请重试！", Toast.LENGTH_SHORT).show();
				break;
			}
		}

	};
}
