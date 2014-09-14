package com.mct.fragment;

import com.mct.client.GestureLockSettingActivity;
import com.mct.client.LoginActivity;
import com.mct.client.R;
import com.mct.util.AESUtil;
import com.mct.util.MyAnimationUtils;
import com.mct.util.SharePeferenceUtils;
import com.mct.util.XmppTool;
import com.mct.view.SwitchButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class SafetySettingFragment extends Fragment implements OnClickListener,OnCheckedChangeListener{
    private LinearLayout modifyPasswordLayout;
    private LinearLayout modifingPasswordLayout;
    private ImageView modifyStatus;
    private EditText editOldPassword,editNewPassword,reEditNewPassword;
    private Button sureModify,cancelModify;
    private SwitchButton gestureSwitch;
    private LinearLayout gestureSettingLayout;
    private TextView isGestureSetted;
    private SharedPreferences sharedPreferences;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.safetysetting_fragment, null);
		modifyPasswordLayout=(LinearLayout) view.findViewById(R.id.modifypasswordlayout);
		modifingPasswordLayout=(LinearLayout) view.findViewById(R.id.modifingpasswordlayout);
		modifyStatus=(ImageView) view.findViewById(R.id.modifystatus);
		editOldPassword=(EditText) view.findViewById(R.id.editoldpassword);
		editNewPassword=(EditText) view.findViewById(R.id.editnewpassword);
		reEditNewPassword=(EditText) view.findViewById(R.id.reeditnewpassword);
		sureModify=(Button) view.findViewById(R.id.suremodifypassword);
		cancelModify=(Button) view.findViewById(R.id.cancelmodifypassword);
		gestureSwitch=(SwitchButton) view.findViewById(R.id.gestureswitch);
		gestureSettingLayout=(LinearLayout) view.findViewById(R.id.gesturesettinglayout);
		modifyPasswordLayout.setOnClickListener(this);
		gestureSettingLayout.setOnClickListener(this);
		sureModify.setOnClickListener(this);
		cancelModify.setOnClickListener(this);
		gestureSwitch.setOnCheckedChangeListener(this);
		isGestureSetted=(TextView) view.findViewById(R.id.isgesturesetted);
		sharedPreferences=getActivity().getSharedPreferences("safetysettings", 0);
		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.gesturesettinglayout:
			Intent intent=new Intent(getActivity(),GestureLockSettingActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.suremodifypassword:
			String oldPassword=editOldPassword.getText().toString();
			String newPassword=editNewPassword.getText().toString();
			String newPassword2=reEditNewPassword.getText().toString();
			
			try {
				if(!oldPassword.equals(AESUtil.decryptDES(sharedPreferences.getString("password",""),AESUtil.KEY))){
					Toast.makeText(getActivity(), "原密码不正确！", Toast.LENGTH_SHORT).show();
				}else if(newPassword.equals("")){
					Toast.makeText(getActivity(), "新密码不能为空！", Toast.LENGTH_SHORT).show();
				}else if(newPassword.length()<6){
					Toast.makeText(getActivity(), "密码不能少于6位字符！", Toast.LENGTH_SHORT).show();
				}else if(!newPassword.equals(newPassword2)){
					Toast.makeText(getActivity(), "两次输入新密码不一致！", Toast.LENGTH_SHORT).show();
				}else{
					boolean result=XmppTool.shareConnectionManager(getActivity()).changePassword(newPassword);
					if(result){
						Toast.makeText(getActivity(), "密码修改成功，请重新登陆！", Toast.LENGTH_SHORT).show();
						Intent intent1=new Intent(getActivity(),LoginActivity.class);
						startActivity(intent1);
						getActivity().finish();
						MyAnimationUtils.outActivity(getActivity());
					}else{
						Toast.makeText(getActivity(), "密码修改失败，请重试！", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		case R.id.cancelmodifypassword:
			editOldPassword.setText("");
			editNewPassword.setText("");
			reEditNewPassword.setText("");
			modifingPasswordLayout.setVisibility(View.GONE);
			modifyStatus.setImageResource(R.drawable.unexpanded);
			break;
		case R.id.modifypasswordlayout:
			switch (modifingPasswordLayout.getVisibility()) {
			case View.VISIBLE:
				modifingPasswordLayout.setVisibility(View.GONE);
				modifyStatus.setImageResource(R.drawable.unexpanded);
				break;

			case View.GONE:
				modifingPasswordLayout.setVisibility(View.VISIBLE);
				modifyStatus.setImageResource(R.drawable.expanded);
				break;
			}
			break;
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		SharePeferenceUtils.getInstance(getActivity()).setGestrueLockEnable(gestureSwitch.isChecked());
		if(isChecked){
			
			gestureSettingLayout.setVisibility(View.VISIBLE);
		}else{
			
			gestureSettingLayout.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		gestureSwitch.setChecked(SharePeferenceUtils.getInstance(getActivity()).getGestrueLockEnable());
		try {
			if(SharePeferenceUtils.getInstance(getActivity()).getGestrueLockPass()!=null){
				isGestureSetted.setText("未设置");
			}else{
				isGestureSetted.setText("已设置");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==1){
			isGestureSetted.setText("已设置");
		}
	}
	

}
