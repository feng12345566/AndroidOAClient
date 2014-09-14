package com.mct.service;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;

import com.mct.client.R;
import com.mct.client.UserActivity;
import com.mct.fragment.MsgFragment;
import com.mct.util.Common;
import com.mct.util.RecordUtil;
import com.mct.util.UserDbUtil;
import com.mct.util.XmppTool;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

public class MsgService extends Service {
	private XmppTool connectionManager;
	private int soundId;
	private SoundPool soundPool;
	private static final String TAG = "mtag";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("spark", "启动连接服务");
		new ConnectTask(Common.OPT_CONNECT_SERVER).execute();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		if (intent != null) {
			int opt = intent.getIntExtra(Common.KEY_OPT, -1);
			switch (opt) {
			case Common.OPT_GET_OFFLINE_MSG:
				new ConnectTask(Common.OPT_GET_OFFLINE_MSG).execute();
				break;

			}
		}
	}

	class ConnectTask extends AsyncTask<Object, Integer, Object> {
		private int opt;

		ConnectTask(int opt) {
			this.opt = opt;
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			switch (opt) {
			case Common.OPT_GET_OFFLINE_MSG:
				// 获取离线消息
				try {
					return connectionManager.getOfflineMesssage();
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			case Common.OPT_CONNECT_SERVER:
				connectionManager = XmppTool
						.shareConnectionManager(MsgService.this);
				// 设置数据包监听
				connectionManager
						.setConnectionPacketListener(connectionPacketListener);
				// 设置数据包过滤
				connectionManager
						.setConnectionPacketFilter(connectionPacketFilter);
				// 设置单聊消息监听
				connectionManager.setMessageListener(msgListener);
				// 设置群聊信息监听
				connectionManager
						.setMulChatPacketListener(mulChatPacketListener);
				// 设置群成员个人状态监听
				connectionManager.setMyPacketListener(myPacketListener);
				// 设置群成员动态监听
				connectionManager
						.setParticipantStatusListener(participantStatusListener);
				RecordUtil.shareRecordUtil(MsgService.this).openDatabase();
				// 链接服务器
				return connectionManager.getConnection();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			switch (opt) {
			case Common.OPT_GET_OFFLINE_MSG:
				// 获取离线消息
				Intent offIntent = new Intent(Common.ACTION_GET_OFFLINE_MSG);
				sendBroadcast(offIntent);
				break;
			case Common.OPT_CONNECT_SERVER:
				Intent intent;
				if (null != result) {
					intent = new Intent(Common.ACTION_CONNECTION_OK);
				} else {
					intent = new Intent(Common.ACTION_CONNECTION_FAIL);
				}
				sendBroadcast(intent);
				break;
			}
		}

		/**
		 * 链接的数据包监听
		 */
		private PacketListener connectionPacketListener = new PacketListener() {

			@Override
			public void processPacket(Packet arg0) {
				// TODO Auto-generated method stub
				Log.i("m_tag", "----processPacket---" + arg0.getFrom() + ":"
						+ arg0.getXmlns());
				if (arg0 instanceof Presence) {
					Presence p = (Presence) arg0;
					String from = p.getFrom();
					Log.e("m_tag", "==Presence==" + p.getFrom() + " status:"
							+ p.getStatus() + " " + p.isAvailable() + " type:"
							+ p.getType());
					Presence.Type type = p.getType();

				} else if (arg0 instanceof Message) {
					Message m = (Message) arg0;
					Log.e("m_tag",
							"===Message==" + m.getFrom() + " " + m.getBody());
				}
				// 其他个人发来的消息
				connectionManager.createChat(arg0.getFrom());
			}
		};

		// 链接的数据包过滤器
		private PacketFilter connectionPacketFilter = new PacketFilter() {

			@Override
			public boolean accept(Packet arg0) {
				String from = arg0.getFrom();
				if (from != null && !from.equals("null") && !"".equals(from)) {
					return true;
				} else {
					return false;
				}
			}
		};

		/**
		 * 聊天消息监听器
		 */
		private MessageListener msgListener = new MessageListener() {

			@Override
			public void processMessage(Chat chat, Message msg) {
				// TODO Auto-generated method stub
				Log.e("m_tag", "=========接收到消息=============" + msg.toString());
				String from = connectionManager.getUserAccount(msg.getFrom());
				if (from.contains("@")) {
					from = from.substring(0, from.indexOf("@"));
				}
				String login_user = connectionManager.loginUser.getUserId();
				if (!login_user.equals(from)) {
					SharedPreferences sharedPreferences = getSharedPreferences(
							"messagesettings", 0);
					//播放声音
					if (sharedPreferences.getBoolean("voiceenable", false)) {
						soundPool = new SoundPool(1,
								AudioManager.STREAM_SYSTEM, 5);
						soundId = soundPool
								.load(MsgService.this,sharedPreferences.getBoolean("qxdf", false)?R.raw.qxdf:R.raw.tssd, 1);
						soundPool
								.setOnLoadCompleteListener(new OnLoadCompleteListener() {

									@Override
									public void onLoadComplete(SoundPool arg0,
											int arg1, int arg2) {
										// TODO Auto-generated method stub
										soundPool.play(soundId, 1, 1, 0, 0, 1);
									}
								});
					}
					if(sharedPreferences.getBoolean("shockenable", false)){
						Vibrator vibrator=(Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
						vibrator.vibrate(1000);
					}
					if(sharedPreferences.getBoolean("notificationenable", false)){
						NotificationManager manager=(NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
						int smallIcon =getApplicationInfo().icon;
						Intent intent=new Intent(MsgService.this,UserActivity.class);
						intent.putExtra("tag", 0);
						PendingIntent pendingIntent=PendingIntent.getActivity(MsgService.this, 0,intent , 0);
						NotificationCompat.Builder builder=new Builder(MsgService.this);
						builder.setSmallIcon(smallIcon)
						.setContentText(UserDbUtil.shareUserDb(MsgService.this).getUserNameById(from)+":"+msg.getBody())
						.setContentTitle("新消息")
						.setContentIntent(pendingIntent);
						manager.notify(0,builder.build());
					}
				}
                
				long[] id = RecordUtil.shareRecordUtil(MsgService.this)
						.updateConversation(login_user, from, msg.getBody(),
								Common.TYPE_SEND_SINGLE, Common.MSG_IN);
				long thread_id = id[0];
				long msg_id = id[1];
				System.out.println("msg_id:" + msg_id);
				// 更新聊天消息列表广播
				Intent intent = new Intent(Common.ACTION_GET_ONLINE_MSG);
				intent.putExtra(Common.KEY_THREAD_ID, thread_id);
				intent.putExtra("msg_id", msg_id);
				// 更新会话列表广播
				Intent intent2 = new Intent(Common.ACTION_UPDATE_MSG);
				intent2.putExtra(Common.KEY_THREAD_ID, thread_id);
				intent2.putExtra("msg_id", msg_id);
				Log.e("send", "发送消息广播");
				sendBroadcast(intent);
				sendBroadcast(intent2);

			}
		};

		/**
		 * 会议室聊天信息监听
		 */
		private PacketListener mulChatPacketListener = new PacketListener() {

			@Override
			public void processPacket(Packet packet) {
				// TODO Auto-generated method stub
				Message message = (Message) packet; // 接收来自聊天室的聊天信息
				// String from =
				// connectionServer.getUserName(message.getFrom());
				// String login_user = connectionServer.loginUser.getUserName();
				// from:needpsw@conference.aji/user002
				// body:消息
				Log.e("m_tag",
						"会议室：" + message.getFrom() + ":" + message.getBody());
				String from = message.getFrom();

				if (!from.contains(connectionManager.loginUser.getUserId())) {
					int index = from.lastIndexOf("/");
					String with_user = from.substring(0, index);
					String address = from.substring(index + 1);
					RecordUtil.shareRecordUtil(MsgService.this)
							.updateConversation(
									connectionManager.loginUser.getUserId(),
									with_user, message.getBody(),
									Common.TYPE_SEND_CHATROOM, 1);
				}
			}
		};

		/**
		 * 自身状态监听
		 */
		private PacketListener myPacketListener = new PacketListener() {

			@Override
			public void processPacket(Packet arg0) {
				// 线上--------------chat
				// 忙碌--------------dnd
				// 离开--------------away
				// 隐藏--------------xa
				Presence presence = (Presence) arg0;
				// PacketExtension pe = presence.getExtension("x",
				// "http://jabber.org/protocol/muc#user");
				String LogKineName = presence.getFrom().toString();
				String kineName = LogKineName.substring(LogKineName
						.indexOf("/") + 1);
				String stats = "";
				if ("chat".equals(presence.getMode().toString())) {
					stats = "[线上]";
				}
				if ("dnd".equals(presence.getMode().toString())) {
					stats = "[忙碌]";
				}
				if ("away".equals(presence.getMode().toString())) {
					stats = "[离开]";
				}
				if ("xa".equals(presence.getMode().toString())) {
					stats = "[隐藏]";
				}

				// for (int i = 0; i < affiliates.size(); i++) {
				// String name = affiliates.get(i);
				// if (kineName.equals(name.substring(name.indexOf("]") + 1))) {
				// affiliates.set(i, stats + kineName);
				// System.out.println("状态改变成：" + affiliates.get(i));
				// android.os.Message msg = new android.os.Message();
				// msg.what = MEMBER;
				// handler.sendMessage(msg);
				// break;
				// }
				// }

			}
		};

		/**
		 * 群成员状态监听
		 */
		private ParticipantStatusListener participantStatusListener = new ParticipantStatusListener() {

			@Override
			public void adminGranted(String arg0) {
				Log.i(TAG, "执行了adminGranted方法:" + arg0);
			}

			@Override
			public void adminRevoked(String arg0) {
				Log.i(TAG, "执行了adminRevoked方法:" + arg0);
			}

			@Override
			public void banned(String arg0, String arg1, String arg2) {
				Log.i(TAG, "执行了banned方法:" + arg0);
			}

			@Override
			public void joined(String arg0) {
				Log.i(TAG, "执行了joined方法:" + arg0 + "加入了房间");
				// getAllMember();
				// android.os.Message msg = new android.os.Message();
				// msg.what = MEMBER;
				// handler.sendMessage(msg);
			}

			@Override
			public void kicked(String arg0, String arg1, String arg2) {
				Log.i(TAG, "执行了kicked方法:" + arg0 + "被踢出房间");
			}

			@Override
			public void left(String arg0) {
				String lefter = arg0.substring(arg0.indexOf("/") + 1);
				Log.i(TAG, "执行了left方法:" + lefter + "离开的房间");
				// getAllMember();
				// android.os.Message msg = new android.os.Message();
				// msg.what = MEMBER;
				// handler.sendMessage(msg);
			}

			@Override
			public void membershipGranted(String arg0) {
				Log.i(TAG, "执行了membershipGranted方法:" + arg0);
			}

			@Override
			public void membershipRevoked(String arg0) {
				Log.i(TAG, "执行了membershipRevoked方法:" + arg0);
			}

			@Override
			public void moderatorGranted(String arg0) {
				Log.i(TAG, "执行了moderatorGranted方法:" + arg0);
			}

			@Override
			public void moderatorRevoked(String arg0) {
				Log.i(TAG, "执行了moderatorRevoked方法:" + arg0);
			}

			@Override
			public void nicknameChanged(String arg0, String arg1) {
				Log.i(TAG, "执行了nicknameChanged方法:" + arg0);
			}

			@Override
			public void ownershipGranted(String arg0) {
				Log.i(TAG, "执行了ownershipGranted方法:" + arg0);
			}

			@Override
			public void ownershipRevoked(String arg0) {
				Log.i(TAG, "执行了ownershipRevoked方法:" + arg0);
			}

			@Override
			public void voiceGranted(String arg0) {
				Log.i(TAG, "执行了voiceGranted方法:" + arg0);
			}

			@Override
			public void voiceRevoked(String arg0) {
				Log.i(TAG, "执行了voiceRevoked方法:" + arg0);
			}
		};
	}

}
