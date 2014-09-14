package com.mct.util;

public class Common {
	public static final String HTTPSERVICE = "http://nat.nat123.net:15414/headimage/";
	public static final String HTTPJSPSERVICE = "http://nat.nat123.net:14313/oa/message/";
	public static final int MSG_IN = 1;
	public static final int MSG_OUT = 2;
	// ==========================操作码==========================//
	// 连接服务器
	public static final int OPT_CONNECT_SERVER = 100;
	// 连接服务器成功
	public static final int CONNECT_SERVER = 101;

	// 登录操作
	public static final int OPT_LOGIN = 1;

	// 获取离线消息操作
	public static final int OPT_GET_OFFLINE_MSG = 3;

	// 发送消息操作
	public static final int OPT_SEND_MSG = 4;

	// 创建房间操作
	public static final int OPT_CREATE_ROOM = 5;

	// 加入房间
	public static final int OPT_JOIN_ROOM = 6;

	// 设置头像
	public static final int OPT_SET_HEAD_PHO = 7;

	// ============================广播消息=============================//

	// 服务Action
	public static final String ACTION_CONNECTION_SERVICE = "com.mct.ACTION_CONNECTION_SERVICE";

	// 发消息广播
	public static final String ACTION_SEND_MSG = "com.mct.ACTION_SEND_MSG";

	// 登录广播
	public static final String ACTION_LOGIN = "com.mct.ACTION_LOGIN";
	// 下载头像广播
	public static final String ACTION_HEADIMAGE = "com.mct.ACTION_HEADIMAGE";
	// 加载头像广播
	public static final String ACTION_IMAGELOAD = "com.mct.ACTION_IMAGELOAD";
	public static final String ACTION_NOSDCARD = "com.mct.ACTION_NOSDCARD";
	public static final String ACTION_SDCARDSIZE = "com.mct.ACTION_SDCARDSIZE";
	public static final String ACTION_LOADFAIL = "com.mct.ACTION_LOADFAIL";
	public static final String ACTION_ABORTED = "com.mct.ACTION_ABORTED";
	
	
	public static final String ACTION_SHOWEDITOR = "com.mct.ACTION_SHOWEDITOR";
	public static final String ACTION_EDITCONTENT = "com.mct.ACTION_EDITCONTENT";

	// 获取离线消息广播
	public static final String ACTION_GET_OFFLINE_MSG = "com.mct.ACTION_GET_OFFLINE_MSG";

	// 获取在线消息广播
	public static final String ACTION_GET_ONLINE_MSG = "com.mct.ACTION_GET_ONLINE_MSG";

	// 更新消息
	public static final String ACTION_UPDATE_MSG = "com.mct.ACTION_UPDATE_MSG";

	// 好友状态有更新
	public static final String ACTION_UPDATE_USER_STATUS = "com.mct.ACTION_UPDATE_USER_STATUS";

	// 连接成功
	public static final String ACTION_CONNECTION_OK = "com.mct.ACTION_CONNECT_OK";

	// 连接失败
	public static final String ACTION_CONNECTION_FAIL = "com.mct.ACTION_CONNECT_FAIL";

	public static final String ACTION_ADDLOAD = "com.mct.ACTION_ADDLOAD";
	public static final String ACTION_REMOVELOAD = "com.mct.ACTION_REMOVELOAD";
	public static final String ACTION_REFRESH = "com.mct.ACTION_REFRESH";
	public static final String ACTION_COMPLETED = "com.mct.ACTION_COMPLETED";
	public final static String WEATHERSEARCHSUCCESS="com.mct.action_weathersearchsuccess";
	public final static String WEATHERSEARCHFAIL="com.mct.action_weathersearchfail";
	public final static String LOCSUCCESS="com.mct.action_locsuccess";
	public final static String LOCFAIL="com.mct.action_locfail";
	public final static String WEATHERICONSUCCESS="com.mct.action_weathericonsuccess";
	// ==========================传递键================================//

	// 操作键
	public static final String KEY_OPT = "key_opt";

	// 用户名
	public static final String KEY_USER_NAME = "user_name";

	// 密码
	public static final String KEY_PASSWORD = "password";

	// 登录结果
	public static final String KEY_LOGIN_RESULT = "login_result";

	// 会话id
	public static final String KEY_THREAD_ID = "thread_id";

	// 对方帐号
	public static final String KEY_WITH_USER = "with_user";
	// 对方帐号
	public static final String KEY_WITH_NAME = "with_name";

	// 发送的消息类型（单聊消息或者群聊消息）
	public static final String KEY_SEND_TYPE = "send_type";

	// ========================传递值====================================//

	// 成功
	public static final int VALUE_OK = 1;

	// 失败
	public static final int VALUE_FAIL = 0;

	// 加载成功
	public static final int MSG_LOAD_OK = 2;

	// 加载失败
	public static final int MSG_LOAF_FAIL = 3;

	// 进入房间成功
	public static final int MSG_JOIN_OK = 4;

	// 进入房间失败
	public static final int MSG_JOIN_FAIL = 5;
	/*--------------------会话类型-------------------*/
	// 发送单聊消息
	public static final int TYPE_SEND_SINGLE = 0;

	// 发送群聊消息
	public static final int TYPE_SEND_CHATROOM = 1;

	// 发送通知
	public static final int TYPE_SEND_NOTIFACATION = -1;

	/*--------------------消息类型-------------------*/
	// 文字
	public static final int TYPE_SEND_TEXT = 0;
	// 文件
	public static final int TYPE_SEND_FILE = 1;
	// 照片
	public static final int TYPE_SEND_PHOTO = 2;
	// 语音
	public static final int TYPE_SEND_VOICE = 3;
	// 位置
	public static final int TYPE_SEND_LOCATION = 4;
	// 系统通知
	public static final int TYPE_SEND_SYSTEM = 5;
	// 企业通知
	public static final int TYPE_SEND_NEWS = 6;
	// 流程提醒
	public static final int TYPE_SEND_FLOW = 7;
	// 邮件提醒
	public static final int TYPE_SEND_EMAIL = 8;

}
