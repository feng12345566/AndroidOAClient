package com.mct.fragment;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.image.SmartImageView;
import com.mct.client.R;
import com.mct.util.Common;
import com.mct.util.HttpRequestUtil;
import com.mct.util.XmppTool;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfoFragment extends Fragment {
	private SmartImageView myHeadPic;
	private TextView showOrg, showSex, showMobliePhone, showPhone, showPostion,showmyusername;
	private EditText editOrg, editPhone, editMobilePhone, editPostion;
	private RadioGroup editSex;
	private boolean isEdit = false;
	private ProgressDialog dialog;
	private String result;
	private String sex, mobilePhone, phone, org, postion;
	private RadioButton rb1, rb2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.infosettingactivity, null);
		myHeadPic = (SmartImageView) view.findViewById(R.id.myheadpic);
		myHeadPic.setImageUrl(
				Common.HTTPSERVICE + XmppTool.loginUser.getUserId() + ".jpg",
				R.drawable.touxiang);
		showOrg = (TextView) view.findViewById(R.id.showorg);
		showSex = (TextView) view.findViewById(R.id.showsex);
		showMobliePhone = (TextView) view.findViewById(R.id.showmobilephone);
		showPhone = (TextView) view.findViewById(R.id.showphone);
		showPostion = (TextView) view.findViewById(R.id.showposition);
		showmyusername=(TextView) view.findViewById(R.id.showmyusername);
		
		editOrg = (EditText) view.findViewById(R.id.editorg);
		editPhone = (EditText) view.findViewById(R.id.editphone);
		editMobilePhone = (EditText) view.findViewById(R.id.editmobilephone);
		editPostion = (EditText) view.findViewById(R.id.editposition);
		editSex = (RadioGroup) view.findViewById(R.id.editsex);
		rb1 = (RadioButton) view.findViewById(R.id.sex_nan);
		rb2 = (RadioButton) view.findViewById(R.id.sex_nv);
		dialog = new ProgressDialog(getActivity());
		showInfo();
		getData();
		return view;
	}

	private void getData() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
				String httpUrl = "http://nat.nat123.net:14313/oa/message/GetUserinfo.jsp?userid="
						+ XmppTool.loginUser.getUserId();
				result = HttpRequestUtil.getResponsesByGet(httpUrl);
				if (result == null) {
					handler.sendEmptyMessage(-1);
				} else if (result.trim().equals("fail")) {
					handler.sendEmptyMessage(2);
				} else if(result.trim().startsWith("{")){
					handler.sendEmptyMessage(1);
				}
			}
		}).start();
	}

	public void showInfo() {
		showOrg.setVisibility(View.VISIBLE);
		showSex.setVisibility(View.VISIBLE);
		showMobliePhone.setVisibility(View.VISIBLE);
		showPhone.setVisibility(View.VISIBLE);
		showPostion.setVisibility(View.VISIBLE);
		editOrg.setVisibility(View.GONE);
		editPhone.setVisibility(View.GONE);
		editMobilePhone.setVisibility(View.GONE);
		editPostion.setVisibility(View.GONE);
		editSex.setVisibility(View.GONE);
	}

	public void editInfo() {
		showOrg.setVisibility(View.GONE);
		showSex.setVisibility(View.GONE);
		showMobliePhone.setVisibility(View.GONE);
		showPhone.setVisibility(View.GONE);
		showPostion.setVisibility(View.GONE);
		editOrg.setVisibility(View.VISIBLE);
		editPhone.setVisibility(View.VISIBLE);
		editMobilePhone.setVisibility(View.VISIBLE);
		editPostion.setVisibility(View.VISIBLE);
		editSex.setVisibility(View.VISIBLE);
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				dialog.setMessage("������...");
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				break;
			case 1:
				dialog.dismiss();
				Toast.makeText(getActivity(), "���سɹ���", Toast.LENGTH_SHORT)
						.show();
				try {
					JSONObject jsonObject = new JSONObject(result);
					sex = jsonObject.getString("sex");
					mobilePhone = jsonObject.getString("mobilephone");
					phone = jsonObject.getString("phone");
					org = jsonObject.getString("org");
					postion = jsonObject.getString("postion");
					
					showSex.setText(sex);
					showOrg.setText(org);
					showMobliePhone.setText(mobilePhone);
					showPhone.setText(phone==null?"":phone);
					showPostion.setText(postion);
					editOrg.setText(org);
					if (sex.equals("��")) {
						rb1.setChecked(true);
					} else {
						rb2.setChecked(true);
					}
					editPhone.setText(phone==null?"":phone);
					editPostion.setText(postion);
					editMobilePhone.setText(mobilePhone);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case -1:
				dialog.dismiss();
				AlertDialog.Builder builder = new Builder(getActivity());
				builder.setTitle("��ѡ�����");
				builder.setMessage("����ʧ��,�Ƿ����ԣ�");
				builder.setPositiveButton("��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								getData();
							}
						});
				builder.setNegativeButton("��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								getActivity().finish();
							}
						});
				builder.create().show();
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(getActivity(), "��������ݣ�", Toast.LENGTH_SHORT)
						.show();
				break;

			
			case 3:
				dialog.setMessage("�ύ��...");
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				break;
			case 4:
				dialog.dismiss();
				Toast.makeText(getActivity(), "�ύ�ɹ���", Toast.LENGTH_SHORT)
				.show();
				getActivity().finish();
				break;
			case 5:
				dialog.dismiss();
				Toast.makeText(getActivity(), "�ύʧ�ܣ������ԣ�", Toast.LENGTH_SHORT)
				.show();
				break;
			}	
		}

	};

	public void postData() {
		if (checkNull()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					handler.sendEmptyMessage(3);
					String userId = XmppTool.loginUser.getUserId();
					String httpUrl = "http://nat.nat123.net:14313/oa/message/ModifyUserInfo.jsp";
					ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
					list.add(new BasicNameValuePair("userid", userId));
					list.add(new BasicNameValuePair("sex", sex));
					list.add(new BasicNameValuePair("phone", phone));
					list.add(new BasicNameValuePair("postion", postion));
					list.add(new BasicNameValuePair("mobilephone", mobilePhone));
					list.add(new BasicNameValuePair("org", org));
					
					String callback = HttpRequestUtil
							.getResponsesByPost(httpUrl, list);
					if(callback.trim().equals("1")){
						handler.sendEmptyMessage(4);
					}else{
						handler.sendEmptyMessage(5);
					}
				}
			}).start();
		}
	}

	private boolean checkNull() {
		int checkedId = editSex.getCheckedRadioButtonId();
		switch (checkedId) {
		case R.id.sex_nan:
			sex = "��";
			break;
		case R.id.sex_nv:
			sex = "Ů";
			break;

		default:
			sex = "";
			break;
		}
		mobilePhone = editMobilePhone.getText().toString();
		phone = editPhone.getText().toString();
		org = editOrg.getText().toString();
		postion = editPostion.getText().toString();
		if (sex.equals("")) {
			Toast.makeText(getActivity(), "�Ա����ѡ��", Toast.LENGTH_SHORT).show();
			return false;
		} else if (mobilePhone.equals("")) {
			Toast.makeText(getActivity(), "�ֻ��ű�����д��", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (org.equals("")) {
			Toast.makeText(getActivity(), "�������ű�����д��", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (postion.equals("")) {
			Toast.makeText(getActivity(), "ְλ������д��", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences settings=getActivity().getSharedPreferences("setting_info", 0);
		showmyusername.setText(settings.getString("username", ""));
	}
	
}
