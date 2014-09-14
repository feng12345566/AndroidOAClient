package com.mct.client;

import com.mct.fragment.EditResearchFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class EditResearchActivity extends BaseActivity implements OnClickListener{
private EditResearchFragment contentFragment;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		contentFragment =new EditResearchFragment();
        setParameter("调查设计",contentFragment,null,"调查列表","下一页");
        nextPage.setOnClickListener(this);
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
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.nextpage:
			if(contentFragment.getValue()==null){
				Toast.makeText(this, "请完整填写必填项！", Toast.LENGTH_SHORT).show();
			}else{
            Intent intent=new Intent(this,EditMoreResearchActivity.class);
            startActivity(intent);
            this.finish();
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SharedPreferences sharedPreferences=getSharedPreferences("research", 0);
		sharedPreferences.edit().putString("research", contentFragment.getValue()).commit();
	}

}
