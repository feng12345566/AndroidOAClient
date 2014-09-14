package com.mct.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;
import org.json.JSONException;
import org.json.JSONObject;

import com.mct.model.ChatRoom;
import com.mct.model.ChatRoomGroup;
import com.mct.model.GroupInfo;
import com.mct.model.User;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class XmppTool {
	private static String servername = "192.168.0.46";
	// private static String servername = "211.149.208.125";
	private static XMPPConnection con = null;
	protected XMPPConnection connection;
	public static XmppTool server;
	private ConnectionConfiguration config;
	private Context context;
	private ChatManager cm;
	private static boolean isStop = false;
	public static User loginUser = null;

	// 单聊消息监听
	private MessageListener messageListener;

	// 链接的数据包监听
	private PacketListener connectionPacketListener;

	// 链接的数据包过滤器
	private PacketFilter connectionPacketFilter;

	// 群聊消息监听
	private PacketListener mulChatPacketListener;

	// 群成员自身状态监听
	private PacketListener myPacketListener;

	// 群成员动态监听（哪个走了哪个来了哪个被踢了）
	private ParticipantStatusListener participantStatusListener;

	// 聊天对象记录
	private HashMap<String, Object> chatMap = new HashMap<String, Object>();

	public XmppTool(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		config = new ConnectionConfiguration("192.168.0.46", 5222);
		if (Build.VERSION.SDK_INT >= 14) {
			// Build.VERSION_CODES.ICE_CREAM_SANDWICH == 14
			config.setTruststoreType("AndroidCAStore");
			config.setTruststorePassword(null);
			config.setTruststorePath(null);
		} else {
			config.setTruststoreType("BKS");
			String path = System.getProperty("javax.net.ssl.trustStore");
			if (path == null)
				path = System.getProperty("java.home") + File.separator + "etc"
						+ File.separator + "security" + File.separator
						+ "cacerts.bks";
			config.setTruststorePath(path);
		}
		config.setSendPresence(false);
	}

	public static boolean isStop() {
		return isStop;
	}

	public static void setStop(boolean isStop) {
		XmppTool.isStop = isStop;
	}

	/**
	 * 连接服务器
	 * 
	 * @return
	 */
	public XMPPConnection getConnection() {
		if (null == connection) {
			configureConnection(ProviderManager.getInstance());
			try {

				/** 创建connection链接 */
				connection = new XMPPConnection(config);
				// connection.addPacketListener(connectionPacketListener,
				// connectionPacketFilter);
				/** 建立连接 */
				Log.e("m_tag", "================connect==============");
				connection.connect();
				Log.e("m_tag", "================connected==============");
				if (connection.isConnected()) {
					Intent intent = new Intent();
					intent.setAction("com.mct.xmpp.loading");
					context.sendBroadcast(intent);
				} else {
					getConnection();
				}
			} catch (XMPPException e) {
				if (isStop) {
					getConnection();
				}
			}
		}
		return connection;
	}

	public static XmppTool shareConnectionManager(Context context) {
		if (null == server) {
			server = new XmppTool(context);
		}
		return server;
	}

	public static void closeConnection() {
		if (con != null) {
			con.disconnect();
			con = null;
		}
	}

	public static String getIp() {
		return servername;
	}

	public static void setIp(String ip) {
		XmppTool.servername = ip;
	}

	public void setUserList() {
		// 删除所有员工信息
		UserDbUtil.shareUserDb(context).deleteAllData();
		// 获取员工分组列表
		Roster roster = XmppTool.shareConnectionManager(context)
				.getConnection().getRoster();
		Collection<RosterGroup> entriesGroup = roster.getGroups();
		for (RosterGroup group : entriesGroup) {
			String groupName = group.getName();
			if (groupName.equals("")) {
				groupName = "实习员工";
			}
			long groupId = UserDbUtil.shareUserDb(context).insertDataToGroup(
					groupName);
			Collection<RosterEntry> entries = group.getEntries();
			for (RosterEntry entry : entries) {
				// Presence presence = roster.getPresence(entry.getUser());
				Log.e("mtag", "用户名：" + entry.getName());
				UserDbUtil.shareUserDb(context).insertDataToUser(groupId,
						entry.getUser().split("@")[0], entry.getName(), null,
						null, null, null, null);
			}
		}
		
		
		// 更新员工信息

		ArrayList<String> userList = UserDbUtil.shareUserDb(context)
				.getAllUserInfo(1);
		for (String user : userList) {
			String httpUrl = "http://nat.nat123.net:14313/oa/message/GetUserinfo.jsp?userid="
					+ user;
			String result = HttpRequestUtil.getResponsesByGet(httpUrl);
			if (result != null) {

				try {
					JSONObject json = new JSONObject(result);
					if (!json.isNull("sex")) {
						UserDbUtil.shareUserDb(context).updateUserInfo(user,
								json.getString("sex"),
								json.getString("mobilephone"),
								json.getString("phone"), json.getString("org"),
								json.getString("postion"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					continue;
				}
			}
		}

	}

	/**
	 * 获取离线消息
	 * 
	 * @return
	 * @throws XMPPException
	 */
	public boolean getOfflineMesssage() throws XMPPException {
		if (connection != null && connection.isConnected()) {
			System.out.println("获取离线消息");
			OfflineMessageManager offlineManager = new OfflineMessageManager(
					connection);
			// boolean b = offlineManager.supportsFlexibleRetrieval();
			// System.out.println("============="+b);
			// //获取支持灵活的检索状态，正常应该是为true，个人理解为服务器的离线消息功能支持开关

			boolean result = false;
			Iterator<Message> it = offlineManager.getMessages();
			while (it.hasNext()) {
				Message message = it.next();
				// System.out.println(message.getBody());
				String from = message.getFrom();
				String body = message.getBody();
				if (body!=null&&!RecordUtil.shareRecordUtil(context).checkMsgExist(body)) {
					createChat(from);

					Log.e("msg", "获得离线消息" + body);

					int isGroup = isChatRoomUser(from) == true ? 1 : 0;
					RecordUtil.shareRecordUtil(context).updateConversation(
							message.getTo().split("@")[0], from.split("@")[0],
							body, isGroup, Common.MSG_IN);
					long id = RecordUtil.shareRecordUtil(context)
							.getConversationID(message.getTo().split("@")[0],
									from.split("@")[0], 0);
					Intent intent = new Intent(Common.ACTION_UPDATE_MSG);
					intent.getLongExtra(Common.KEY_THREAD_ID, id);
					context.sendBroadcast(intent);
					if (!result)
						result = true;
				}
			}
			offlineManager.deleteMessages(); // 上报服务器已获取，需删除服务器备份，不然下次登录会重新获取
			// Presence presence = new Presence(Presence.Type.available);//
			// 此时再上报用户状态
			// connection.sendPacket(presence);
			setPresence(1);
			return result;
		} else {
			return false;
		}
	}

	/**
	 * 登录
	 * 
	 * @param userName
	 *            登录帐号
	 * @param password
	 *            登录密码
	 * @return
	 */
	public boolean login(String userName, String password) {
		try {
			if (connection == null)
				return false;
			/** 登录 */
			// connection.login(userName, password);
			// 用户名、密码、资源名“域名和资源名”完全相同的两个用户才可以互发文件，
			// 否则永远都没反应，如果不清楚自己所用的客户端的资源名，
			// 可以借助前面提到的SmackDebug工具查看往返信息完整报文，在to和from中一定可以看到
			connection.login(userName, password, connection.getServiceName());
			loginUser = getUser(userName);
			cm = connection.getChatManager();
			cm.addChatListener(new ChatManagerListener() {

				@Override
				public void chatCreated(Chat chat, boolean arg1) {
					// TODO Auto-generated method stub
					chat.addMessageListener(messageListener);
				}
			});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据用户名获取用户具体信息
	 * 
	 * @param userName用户名
	 *            ，'@'前面部分
	 * @return
	 */
	public User getUser(String userId) {
		String account = userId + "@" + connection.getServiceName();
		return getUser(userId, account);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param entry
	 * @return
	 */
	private User getUser(RosterEntry entry) {
		String userID = entry.getUser();
		String userName = entry.getName();
		return getUser(userName, userID);
	}

	/**
	 * 根据用户名和用户id获得用户
	 * 
	 * @param userName
	 * @param userID
	 * @return
	 */
	public User getUser(String userId, String userJid) {
		Presence presence = connection.getRoster().getPresence(userJid);
		String type = presence.getType().toString();
		User user = new User(userId, userJid, type);
		user.setUserName(UserDbUtil.shareUserDb(context)
				.getUserNameById(userId));
		return user;
	}

	/**
	 * 修改密码
	 * 
	 * @param connection
	 * @return
	 */
	public boolean changePassword(String pwd) {
		try {
			connection.getAccountManager().changePassword(pwd);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 删除当前用户
	 * 
	 * @param connection
	 * @return
	 */
	public boolean logout() {
		try {
			setPresence(5);
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		connection = null;
		server = null;
		loginUser = null;
		Iterator<Map.Entry<String, Object>> it = chatMap.entrySet().iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj instanceof Chat) {
				((Chat) obj).removeMessageListener(messageListener);
			} else if (obj instanceof MultiUserChat) {
				((MultiUserChat) obj)
						.removeMessageListener(mulChatPacketListener);
				((MultiUserChat) obj)
						.removeParticipantListener(myPacketListener);
				((MultiUserChat) obj)
						.removeParticipantStatusListener(participantStatusListener);
			}
		}
		chatMap.clear();
		// UserManager.shareFriendManager().clearAllUser();
		return true;
	}

	/**
	 * 更改用户状态
	 */
	public void setPresence(int code) {
		if (connection == null)
			return;
		Presence presence;
		switch (code) {
		case 0:
			presence = new Presence(Presence.Type.available);
			connection.sendPacket(presence);
			Log.v("state", "设置在线");
			break;
		case 1:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.chat);
			connection.sendPacket(presence);
			Log.v("state", "设置Q我吧");
			System.out.println(presence.toXML());
			break;
		case 2:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.dnd);
			connection.sendPacket(presence);
			Log.v("state", "设置忙碌");
			System.out.println(presence.toXML());
			break;
		case 3:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.away);
			connection.sendPacket(presence);
			Log.v("state", "设置离开");
			System.out.println(presence.toXML());
			break;
		case 4:
			Roster roster = connection.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				presence = new Presence(Presence.Type.unavailable);
				presence.setPacketID(Packet.ID_NOT_AVAILABLE);
				presence.setFrom(connection.getUser());
				presence.setTo(entry.getUser());
				connection.sendPacket(presence);
				System.out.println(presence.toXML());
			}
			// 向同一用户的其他客户端发送隐身状态
			presence = new Presence(Presence.Type.unavailable);
			presence.setPacketID(Packet.ID_NOT_AVAILABLE);
			presence.setFrom(connection.getUser());
			presence.setTo(StringUtils.parseBareAddress(connection.getUser()));
			connection.sendPacket(presence);
			Log.v("state", "设置隐身");
			break;
		case 5:
			presence = new Presence(Presence.Type.unavailable);
			connection.sendPacket(presence);
			Log.v("state", "设置离线");
			break;
		default:
			break;
		}
	}

	/**
	 * 创建聊天室
	 * 
	 * @param roomName
	 *            房间名称
	 */
	public MultiUserChat createRoom(String roomName, String password,
			String roomdesc) {
		if (connection == null) {
			return null;
		}
		MultiUserChat muc = null;
		try {
			String roomID = roomName + "@conference."
					+ connection.getServiceName();
			// 创建一个MultiUserChat
			muc = getMultiUserChat(roomID);
			// 创建聊天室
			muc.create(roomName); // roomName房间的名字
			// 获得聊天室的配置表单
			Form form = muc.getConfigurationForm();
			// 根据原始表单创建一个要提交的新表单。
			Form submitForm = form.createAnswerForm();
			// 向要提交的表单添加默认答复
			for (Iterator<FormField> fields = form.getFields(); fields
					.hasNext();) {
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null) {
					// 设置默认值作为答复
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}
			// 设置聊天室的新拥有者
			List<String> owners = new ArrayList<String>();
			owners.add(connection.getUser());// 用户JID
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			// 可以更改主题
			submitForm.setAnswer("muc#roomconfig_changesubject", true);
			// 设置最大人数
			// submitForm.setAnswer("muc#roomconfig_maxusers", maxNum);

			// 房间描述
			submitForm.setAnswer("muc#roomconfig_roomdesc", roomdesc);
			// 设置聊天室是持久聊天室，即将要被保存下来
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			// 房间仅对成员开放
			submitForm.setAnswer("muc#roomconfig_membersonly", false);
			// 允许占有者邀请其他人
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			if (password != null) {
				// 进入是否需要密码
				submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
						true);
				// 设置进入密码
				submitForm.setAnswer("muc#roomconfig_roomsecret", password);
			} else {
				// 进入是否需要密码
				submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
						false);
			}
			// 能够发现占有者真实 JID 的角色
			// submitForm.setAnswer("muc#roomconfig_whois", "anyone");
			// 登录房间对话
			submitForm.setAnswer("muc#roomconfig_enablelogging", true);
			// 仅允许注册的昵称登录
			submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
			// 允许使用者修改昵称
			submitForm.setAnswer("x-muc#roomconfig_canchangenick", true);
			// 允许用户注册房间
			submitForm.setAnswer("x-muc#roomconfig_registration", false);
			// 发送已完成的表单（有默认值）到服务器来配置聊天室
			muc.sendConfigurationForm(submitForm);
			if (password != null) {
				muc.join(connection.getUser(), password);
			} else {
				muc.join(connection.getUser());
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return muc;
	}

	public void check() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (null != connection && !connection.isConnected()) {
					connection.disconnect();
					connection = null;
					while (getConnection() == null) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					// login(loginUser.getUserName(), password)
				}
			}
		}).start();

	}

	/**
	 * 当前用户加入会议室
	 * 
	 * @param roomPassword
	 *            会议室密码
	 * @param roomsName
	 *            会议室名称
	 */
	public boolean joinMultiUserChat(String roomPassword, ChatRoom room) {
		try {
			// 使用XMPPConnection创建一个MultiUserChat窗口
			MultiUserChat muc = getMultiUserChat(room.getRoomId());
			// 聊天室服务将会决定要接受的历史记录数量
			DiscussionHistory history = new DiscussionHistory();
			history.setMaxStanzas(0);
			// history.setSince(new Date());
			// 用户加入聊天室
			muc.join(connection.getUser(), roomPassword, history,
					SmackConfiguration.getPacketReplyTimeout());
			System.out.println("会议室加入成功........");
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("会议室加入失败........");
			return false;
		}
	}

	/**
	 * 判断用户是否加入该房间
	 * 
	 * @param roomID房间id
	 * @return
	 */
	public boolean isUserJoinRoom(String roomID) {
		String key = loginUser.getUserId() + "-" + roomID;
		if (chatMap.containsKey(key)) {
			return true;
		}
		return false;
	}

	/**
	 * 查询会议室成员名字
	 * 
	 * @param roomID房间ID
	 * @return
	 */
	public List<String> findMulitUser(String roomID) {
		List<String> listUser = new ArrayList<String>();
		Iterator<String> it = getMultiUserChat(roomID).getOccupants();
		// 遍历出聊天室人员名称
		while (it.hasNext()) {
			// 聊天室成员名字
			String name = StringUtils.parseResource(it.next());
			listUser.add(name);
		}
		return listUser;
	}

	/**
	 * 根据房间名称获取MultiUserChat对象
	 * 
	 * @param roomID房间ID
	 * @return
	 */
	public MultiUserChat getMultiUserChat(String roomID) {
		String key = loginUser.getUserId() + "-" + roomID;
		MultiUserChat muc;
		if (chatMap.containsKey(key)) {
			muc = (MultiUserChat) chatMap.get(key);
		} else {
			// 使用XMPPConnection创建一个MultiUserChat窗口
			// muc = new MultiUserChat(connection, roomsName + "@conference."
			// + connection.getServiceName());
			muc = new MultiUserChat(connection, roomID);
			// 群聊信息监听
			muc.addMessageListener(mulChatPacketListener);
			// 个人状态监听
			muc.addParticipantListener(myPacketListener);
			// 群组成员状态监听
			muc.addParticipantStatusListener(participantStatusListener);
			chatMap.put(key, muc);
		}
		return muc;
	}

	/**
	 * 获取服务器上所有的聊天室以及房间
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<ChatRoomGroup> getRoomList() throws Exception {
		// 查找服务
		List<String> col = getConferenceServices(connection.getServiceName(),
				connection);
		ArrayList<ChatRoomGroup> list = new ArrayList<ChatRoomGroup>();
		for (String aCol : col) {
			String service = aCol;
			// 查询服务器上的聊天室
			Collection<HostedRoom> rooms = MultiUserChat.getHostedRooms(
					connection, service);
			ArrayList<ChatRoom> roomlist = new ArrayList<ChatRoom>();
			for (HostedRoom room : rooms) {
				// 查看Room消息
				// System.out.println(room.getName() + "-" + room.getJid());
				// 记录MultiUserChat对象
				// getMultiUserChat(room.getJid());
				// 获取房间信息
				RoomInfo roomInfo = MultiUserChat.getRoomInfo(connection,
						room.getJid());
				ChatRoom chatroom = new ChatRoom();
				chatroom.setRoomName(room.getName()).setRoomId(room.getJid());
				if (roomInfo != null) {
					chatroom.setSubjection(roomInfo.getSubject())
							.setDescription(roomInfo.getDescription())
							.setPasswordProtected(
									roomInfo.isPasswordProtected())
							.setMemberNum(roomInfo.getOccupantsCount());
					Log.e("m_tag",
							roomInfo.getDescription() + " "
									+ roomInfo.getOccupantsCount());

				}
				roomlist.add(chatroom);
			}
			list.add(new ChatRoomGroup(aCol, roomlist));
		}
		return list;
	}

	/**
	 * 获取聊天室服务器
	 * 
	 * @param server
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public List<String> getConferenceServices(String server,
			XMPPConnection connection) throws Exception {
		List<String> answer = new ArrayList<String>();
		ServiceDiscoveryManager discoManager = ServiceDiscoveryManager
				.getInstanceFor(connection);
		DiscoverItems items = discoManager.discoverItems(server);
		for (Iterator<DiscoverItems.Item> it = items.getItems(); it.hasNext();) {
			DiscoverItems.Item item = (DiscoverItems.Item) it.next();
			if (item.getEntityID().startsWith("conference")
					|| item.getEntityID().startsWith("private")) {
				answer.add(item.getEntityID());
			} else {
				try {
					DiscoverInfo info = discoManager.discoverInfo(item
							.getEntityID());
					if (info.containsFeature("http://jabber.org/protocol/muc")) {
						answer.add(item.getEntityID());
					}
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		}
		return answer;
	}

	/**
	 * 第一次接到消息时创建会话
	 * 
	 * @param from
	 */
	public void createChat(String from) {
		Log.e("m_tag", "========创建聊天createChat==========");
		if (from.contains("@" + connection.getServiceName())) {
			// String with_user = getUserName(from);
			String with_user = getUserAccount(from);
			if (with_user != connection.getServiceName()) {
				String key = loginUser.getUserId() + "-" + with_user;
				if (!chatMap.containsKey(key)) {
					chatMap.put(key, createChat(from, messageListener));
				}
			}
		}
	}

	/**
	 * 创建聊天
	 * 
	 * @param login_user登录的用户
	 * @param account
	 * @param listener
	 * @return
	 */
	public Chat createChat(String account, final MessageListener listener) {
		cm.addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(Chat chat, boolean arg1) {
				// TODO Auto-generated method stub
				chat.addMessageListener(listener);
			}
		});
		// String account = with_user + "@" + connection.getServiceName();
		Chat chat = cm.createChat(account, listener);
		chat.addMessageListener(listener);
		return chat;
	}

	/**
	 * 发消息到聊天室
	 * 
	 * @param roomID房间id
	 * @param msg
	 */
	public boolean sendMessageToChatRoom(String roomID, String msg) {
		MultiUserChat muc = getMultiUserChat(roomID);
		// MultiUserChat muc = new MultiUserChat(connection, roomID);
		// // 群聊信息监听
		// muc.addMessageListener(mulChatPacketListener);
		// // 个人状态监听
		// muc.addParticipantListener(myPacketListener);
		// // 群组成员状态监听
		// muc.addParticipantStatusListener(participantStatusListener);
		try {
			// Message m = new Message();
			// m.setBody(msg);
			muc.sendMessage(msg);
			return true;
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断用户名是否是群组聊天的用户名结构，如：needpsw@conference.aji/user002
	 * 
	 * @param from
	 * @return
	 */
	public boolean isChatRoomUser(String from) {
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z0-9]{1,}/[a-zA-Z0-9-_]{1,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(from);
		return matcher.matches();
	}

	/**
	 * 发消息给好友
	 * 
	 * @param login_user登录用户
	 * @param with_user对方账号
	 * @param content内容
	 * @return
	 */
	public boolean sendMessage(String with_user, String content) {
		String key = loginUser.getUserId() + "-" + with_user;
		Chat chat;
		if (chatMap.containsKey(key)) {
			chat = (Chat) chatMap.get(key);
		} else {
			String account = with_user + "@" + connection.getServiceName();
			chat = createChat(account, messageListener);
			chatMap.put(key, chat);
		}
		try {
			Message m = new Message();
			m.setBody(content);
			chat.sendMessage(m);
			// sendBroadcast(content);
			return true;
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	// 在聊天室中发消息
	// multiChat.sendMessage(str);

	// private String getUTFString(String str) {
	// byte[] ary;
	// String newString = str;
	// try {
	// ary = str.getBytes("ISO-8859-1");
	// newString = new String(ary, "UTF-8");
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return newString;
	// }

	public void sendBroadcast(final String msg) {
		Packet packet = new Packet() {

			@Override
			public String toXML() {
				// TODO Auto-generated method stub
				return "<message from='" + loginUser.getUserJid() + "' id='"
						+ loginUser.getUserId() + "' to='all@broadcast."
						+ connection.getServiceName() + "' type='chat'>/n"
						+ "<body>" + msg + "</body>/n" + "<thread>"
						+ loginUser.getUserId() + "</thread>/n"
						+ "<x xmlns='jabber:x:event'>/n"
						+ "<offline/><composing/>/n" + "</x>/n" + "</message>";
			}
		};
		System.out.println("发送广播"+msg);
		connection.sendPacket(packet);
	}

	/**
	 * xmpp配置
	 */
	private void configureConnection(ProviderManager pm) {
		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());
		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());
		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());
		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());
		// Service Discovery # Items //解析房间列表
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		// Service Discovery # Info //某一个房间的信息
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());
		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());
		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
		pm.addIQProvider("command", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());
	}

	/**
	 * 获取用户信息
	 * 
	 * @param connection
	 * @param user
	 * @return
	 */
	public VCard getVCard(String userId) {
		// ByteArrayInputStream bais = null;
		VCard vcard = new VCard();
		try {

			// 加入这句代码，解决No VCard for
			ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
					new org.jivesoftware.smackx.provider.VCardProvider());
			vcard.load(connection, userId + "@" + connection.getServiceName());

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return vcard;

	}

	/**
	 * 修改用户头像
	 * 
	 * @param connection
	 * @param f
	 * @throws XMPPException
	 * @throws IOException
	 */
	public void changeImage(File f) throws IOException, XMPPException {

		VCard vcard = new VCard();
		vcard.load(connection);

		byte[] bytes;

		bytes = getBytes(f);
		String encodedImage = StringUtils.encodeBase64(bytes);
		vcard.setAvatar(bytes, encodedImage);
		// vcard.setEncodedImage(encodedImage);
		vcard.setField("PHOTO", "<TYPE>image/jpg</TYPE><BINVAL>" + encodedImage
				+ "</BINVAL>", true);
		ByteArrayInputStream bais = new ByteArrayInputStream(vcard.getAvatar());
		BitmapFactory.decodeStream(bais);

		vcard.save(connection);
	}



	/**
	 * 获得指定文件的byte数组
	 */
	public byte[] getBytes(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	// 转换时间格式
	public static String getSMSDate(long date) {
		// SimpleDateFormat dateformat = new SimpleDateFormat(
		// "yy/MM/dd HH:mm:ss E");
		SimpleDateFormat dateformat = new SimpleDateFormat("yy/MM/dd");
		Date d = new Date(date);
		Date now = new Date();
		long t = now.getTime() - d.getTime();
		if (t <= 86400000) {
			// 在今天
			dateformat = new SimpleDateFormat("HH:mm:ss");
		} else if (t > 86400000 && t < 5 * 86400000) {
			dateformat = new SimpleDateFormat("E");
		}
		String dateStr = dateformat.format(d);
		return dateStr;
	}

	/**
	 * 将账号'@'后面部分截掉
	 * 
	 * @param account
	 * @return
	 */
	public String getUserName(String account) {
		if (account.contains("@")) {
			String name = account.substring(0, account.indexOf("@"));
			return name;
		}
		return account;
	}

	public String getNormalNameByUserName(String userName) {
		String normalName = "";

		return userName;

	}

	/**
	 * 将账号‘/’后面部分截掉
	 * 
	 * @param account
	 * @return
	 */
	public String getUserAccount(String account) {
		if (account.contains("/")) {
			String name = account.substring(0, account.indexOf("/"));
			return name;
		}
		return account;
	}

	public MessageListener getMessageListener() {
		return messageListener;
	}

	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}

	public PacketListener getMulChatPacketListener() {
		return mulChatPacketListener;
	}

	public void setMulChatPacketListener(PacketListener mulChatPacketListener) {
		this.mulChatPacketListener = mulChatPacketListener;
	}

	public PacketListener getMyPacketListener() {
		return myPacketListener;
	}

	public void setMyPacketListener(PacketListener myPacketListener) {
		this.myPacketListener = myPacketListener;
	}

	public ParticipantStatusListener getParticipantStatusListener() {
		return participantStatusListener;
	}

	public void setParticipantStatusListener(
			ParticipantStatusListener participantStatusListener) {
		this.participantStatusListener = participantStatusListener;
	}

	public PacketListener getConnectionPacketListener() {
		return connectionPacketListener;
	}

	public void setConnectionPacketListener(
			PacketListener connectionPacketListener) {
		this.connectionPacketListener = connectionPacketListener;
	}

	public PacketFilter getConnectionPacketFilter() {
		return connectionPacketFilter;
	}

	public void setConnectionPacketFilter(PacketFilter connectionPacketFilter) {
		this.connectionPacketFilter = connectionPacketFilter;
	}
}
