package com.mct.util;

import java.io.File;
import java.io.IOException;

import org.jivesoftware.smack.XMPPException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView.CommaTokenizer;

import com.mct.model.ChatRoom;

/**
 * 提交信息任务
 * 
 * @author huajian.zhang
 * 
 */
public class PostMsgTask extends AsyncTask<Object, Void, Object> {
	private int opt;
	private Context context;
	private Handler handler;

	public PostMsgTask(Context context, int opt, Handler handler) {
		this.opt = opt;
		this.context = context;
		this.handler = handler;
	}

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		switch (opt) {
		case Common.OPT_LOGIN:
			// 登录
			Log.e("progress", "登陆");
			return XmppTool.shareConnectionManager(context).login(
					(String) params[0], (String) params[1]);
		
		case Common.OPT_SEND_MSG:
			//单聊还是群聊
			int sendType = (Integer) params[0];
			String with_user = (String) params[1];
			String content = (String) params[2];

			boolean r = sendMsg(sendType,with_user, content);
			//发送成功
			if (r) {
			   //更新会话数据表
				RecordUtil.shareRecordUtil(context).updateConversation(
						XmppTool.loginUser.getUserId(), with_user,
						content, sendType,Common.MSG_OUT);
				Intent intent=new Intent(Common.ACTION_UPDATE_MSG);
				intent.putExtra(Common.KEY_THREAD_ID, RecordUtil.shareRecordUtil(context).getConversationID(XmppTool.loginUser.getUserId(), with_user,0));
				context.sendBroadcast(intent);
			}
			return r;
		case Common.OPT_CREATE_ROOM:
			// 创建房间
			String roomName = (String) params[0];
			String password = (String) params[1];
			String roomdesc = (String) params[2];
			return XmppTool.shareConnectionManager(context)
					.createRoom(roomName, password, roomdesc);
		case Common.OPT_JOIN_ROOM:
			String psw = (String) params[0];
			ChatRoom room = (ChatRoom) params[1];
			// 加入房间
			return XmppTool.shareConnectionManager(context)
					.joinMultiUserChat(psw, room);
		case Common.OPT_SET_HEAD_PHO:
			// 设置头像
			File f = (File) params[0];
			try {
				XmppTool.shareConnectionManager(context).changeImage(f);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		switch (opt) {
		case Common.OPT_LOGIN:
			// 登录
			boolean loginResult = (Boolean) result;
			if (loginResult) {
				handler.sendEmptyMessage(Common.VALUE_OK);
				// 获取离线消息
				Intent intent = new Intent(Common.ACTION_CONNECTION_SERVICE);
				intent.putExtra(Common.KEY_OPT, Common.OPT_GET_OFFLINE_MSG);
				context.startService(intent);
			} else {
				handler.sendEmptyMessage(Common.VALUE_FAIL);
			}
			break;
		
		case Common.OPT_JOIN_ROOM:
			// 加入房间
			if ((Boolean) result) {
				handler.sendEmptyMessage(Common.MSG_JOIN_OK);
			} else {
				handler.sendEmptyMessage(Common.MSG_JOIN_FAIL);
			}
			break;
		case Common.OPT_SEND_MSG:
			// 发送消息
			boolean bResult = (Boolean) result;
			if (bResult) {
				handler.sendEmptyMessage(Common.VALUE_OK);
				
			} else {
				handler.sendEmptyMessage(Common.VALUE_FAIL);
			}
			break;
		case Common.OPT_CREATE_ROOM:
			// 创建房间
			if (result != null) {
				Message msg = handler.obtainMessage(Common.VALUE_OK, result);
				msg.sendToTarget();
			} else {
				handler.sendEmptyMessage(Common.VALUE_FAIL);
			}
			break;
		}
	}

	/**
	 * 根据类型发送消息
	 * 
	 * @param type
	 * @param with_user
	 * @param content
	 * @return
	 */
	private boolean sendMsg(int type,String with_user, String content) {
		switch (type) {
		case Common.TYPE_SEND_CHATROOM:
			return XmppTool.shareConnectionManager(context)
					.sendMessageToChatRoom(with_user, content);
		case Common.TYPE_SEND_SINGLE:
			return XmppTool.shareConnectionManager(context)
					.sendMessage(with_user, content);
		}
		return false;
	}

}
