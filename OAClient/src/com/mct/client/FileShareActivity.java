package com.mct.client;

import java.io.IOException;

import com.mct.fragment.RemoteFileListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class FileShareActivity extends BaseActivity implements OnClickListener {
	private String tag;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		tag=getIntent().getStringExtra("tag");
		RemoteFileListFragment remoteFileListFragment = new RemoteFileListFragment();
		Bundle bundle=new Bundle();
		bundle.putString("tag", tag);
		setParameter("共享文件列表", remoteFileListFragment, bundle, "个人中心", "传输记录");
		lastPage.setOnClickListener(this);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			backToMain();
		}
		return true;
	}

	private void backToMain() {
		this.finish();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.nextpage:
			Intent intent = new Intent(FileShareActivity.this,
					FileLoadHistoryActivity.class);
			startActivity(intent);
			break;

		case R.id.backlayout:
			backToMain();
			break;
		}
	}
	
}
