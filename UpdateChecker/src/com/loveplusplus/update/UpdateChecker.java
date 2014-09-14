package com.loveplusplus.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.yyxu.download.services.DownloadService;
import com.yyxu.download.utils.MyIntents;

public class UpdateChecker extends Fragment {

	// private static final String NOTIFICATION_ICON_RES_ID_KEY = "resId";
	private static final String NOTICE_TYPE_KEY = "type";
	// private static final String SUCCESSFUL_CHECKS_REQUIRED_KEY = "nChecks";
	private static final int NOTICE_NOTIFICATION = 2;
	private static final int NOTICE_DIALOG = 1;
	private static final String TAG = "UpdateChecker";

	private FragmentActivity mContext;
	private Thread mThread;
	private int mTypeOfNotice;
	private static String updateServerUrl;
	private static Handler handler;
	public static final int FINDUPDATE=1;
	public static final int UNFINDUPDATE=2;

	public static String getUpdateServerUrl() {
		return updateServerUrl;
	}

	
	public static void setUpdateServerUrl(String updateServerUrl) {
		UpdateChecker.updateServerUrl = updateServerUrl;
	}


	/**
	 * Show a Dialog if an update is available for download. Callable in a
	 * FragmentActivity. Number of checks after the dialog will be shown:
	 * default, 5
	 * 
	 * @param fragmentActivity
	 *            Required.
	 */
	public static void checkForDialog(FragmentActivity fragmentActivity,Handler handler) {
		// 获取转换器对象
		FragmentTransaction content = fragmentActivity
				.getSupportFragmentManager().beginTransaction();
		// 实例化对象
		UpdateChecker updateChecker =new UpdateChecker();
		// 设置通知类型为dialog
		Bundle args = new Bundle();
		args.putInt(NOTICE_TYPE_KEY, NOTICE_DIALOG);
		// args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, 5);
		updateChecker.setArguments(args);
		// 提交，运行Fragment生命周期函数onAttach(Activity activity);
		content.add(updateChecker, null).commit();
		UpdateChecker.handler=handler;
	}

	/**
	 * Show a Notification if an update is available for download. Callable in a
	 * FragmentActivity Specify the number of checks after the notification will
	 * be shown.
	 * 
	 * @param fragmentActivity
	 *            Required.
	 * @param notificationIconResId
	 *            R.drawable.* resource to set to Notification Icon.
	 */
	public static void checkForNotification(FragmentActivity fragmentActivity) {
		FragmentTransaction content = fragmentActivity
				.getSupportFragmentManager().beginTransaction();
		UpdateChecker updateChecker =new UpdateChecker();
		Bundle args = new Bundle();
		args.putInt(NOTICE_TYPE_KEY, NOTICE_NOTIFICATION);
		// args.putInt(NOTIFICATION_ICON_RES_ID_KEY, notificationIconResId);
		// args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, 5);
		updateChecker.setArguments(args);
		content.add(updateChecker, null).commit();
	}

	/**
	 * This class is a Fragment. Check for the method you have chosen.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = (FragmentActivity) activity;
		Bundle args = getArguments();
		mTypeOfNotice = args.getInt(NOTICE_TYPE_KEY);
		// mSuccessfulChecksRequired =
		// args.getInt(SUCCESSFUL_CHECKS_REQUIRED_KEY);
		// mNotificationIconResId = args.getInt(NOTIFICATION_ICON_RES_ID_KEY);
		checkForUpdates();
	}

	/**
	 * Heart of the library. Check if an update is available for download
	 * parsing the desktop Play Store page of the app
	 */
	private void checkForUpdates() {
		mThread = new Thread() {
			@Override
			public void run() {
				// if (isNetworkAvailable(mContext)) {

				String json = sendPost();
				if (json != null) {
					parseJson(json);
				} else {
					Log.e(TAG, "can't get app update json");
					if(handler!=null){
						handler.sendEmptyMessage(UNFINDUPDATE);
						}
				}
				// }
			}

		};
		mThread.start();
	}

	/**
	 * 获取服务器上的版本信息
	 * 
	 * @return
	 */
	protected String sendPost() {
		if (updateServerUrl == null || updateServerUrl.equals("")) {
			Log.e("err", "请先通过setUpdateServerUrl方法设置更新信息的请求地址");
			return null;
		}
		HttpURLConnection uRLConnection = null;
		InputStream is = null;
		BufferedReader buffer = null;
		String result = null;
		try {
			URL url = new URL(updateServerUrl);
			uRLConnection = (HttpURLConnection) url.openConnection();
			uRLConnection.setDoInput(true);
			uRLConnection.setDoOutput(true);
			uRLConnection.setRequestMethod("POST");
			uRLConnection.setUseCaches(false);
			uRLConnection.setConnectTimeout(10 * 1000);
			uRLConnection.setReadTimeout(10 * 1000);
			uRLConnection.setInstanceFollowRedirects(false);
			uRLConnection.setRequestProperty("Connection", "Keep-Alive");
			uRLConnection.setRequestProperty("Charset", "UTF-8");
			uRLConnection
					.setRequestProperty("Accept-Encoding", "gzip, deflate");
			uRLConnection
					.setRequestProperty("Content-Type", "application/json");

			uRLConnection.connect();

			is = uRLConnection.getInputStream();

			String content_encode = uRLConnection.getContentEncoding();

			if (null != content_encode && !"".equals(content_encode)
					&& content_encode.equals("gzip")) {
				is = new GZIPInputStream(is);
			}

			buffer = new BufferedReader(new InputStreamReader(is));
			StringBuilder strBuilder = new StringBuilder();
			String line;
			while ((line = buffer.readLine()) != null) {
				strBuilder.append(line);
			}
			result = strBuilder.toString();
		} catch (Exception e) {
			Log.e(TAG, "http post error", e);
		} finally {
			if (buffer != null) {
				try {
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (uRLConnection != null) {
				uRLConnection.disconnect();
			}
		}
		return result;
	}

	/**
	 * 解析json
	 * 
	 * @param json
	 */
	private void parseJson(String json) {
		mThread.interrupt();
		Looper.prepare();
		try {

			JSONObject obj = new JSONObject(json);
			String updateMessage = obj.getString(Constants.APK_UPDATE_CONTENT);
			String apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL);
			int apkCode = obj.getInt(Constants.APK_VERSION_CODE);

			int versionCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;

			if (apkCode > versionCode) {
				
				if (mTypeOfNotice == NOTICE_NOTIFICATION) {
					showNotification(updateMessage, apkUrl);
				} else if (mTypeOfNotice == NOTICE_DIALOG) {
					showDialog(updateMessage, apkUrl);
				}
				if(handler!=null){
				   handler.sendEmptyMessage(FINDUPDATE);
				}
			} else {
				if(handler!=null){
					handler.sendEmptyMessage(UNFINDUPDATE);
					}
			
			}

		} catch (PackageManager.NameNotFoundException ignored) {
		} catch (JSONException e) {
			Log.e(TAG, "parse json error", e);
			if(handler!=null){
			handler.sendEmptyMessage(UNFINDUPDATE);
			}
		}
	}

	/**
	 * Show dialog
	 * 
	 * @see Dialog#show(android.support.v4.app.FragmentActivity)
	 */
	public void showDialog(String content, String apkUrl) {
		// 实例化dialog对象
		Dialog d = new Dialog();
		// 设置参数
		Bundle args = new Bundle();
		args.putString(Constants.APK_UPDATE_CONTENT, content);
		args.putString(Constants.APK_DOWNLOAD_URL, apkUrl);
		d.setArguments(args);
		d.show(mContext.getSupportFragmentManager(), null);
	}

	/**
	 * Show Notification
	 * 
	 * @param info
	 * 
	 * @see Notification#show(android.content.Context, int)
	 */
	public void showNotification(String content, String apkUrl) {
		android.app.Notification noti;
		Intent myIntent = new Intent(mContext, DownloadService.class);
		myIntent.putExtra(MyIntents.URL, apkUrl);
		myIntent.putExtra(MyIntents.TYPE, MyIntents.Types.ADD);
		PendingIntent pendingIntent = PendingIntent.getService(mContext, 0,
				myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		int smallIcon = mContext.getApplicationInfo().icon;
		noti = new NotificationCompat.Builder(mContext)
				.setTicker(getString(R.string.newUpdateAvailable))
				.setContentTitle(getString(R.string.newUpdateAvailable))
				.setContentText(content).setSmallIcon(smallIcon)
				.setContentIntent(pendingIntent).build();

		noti.flags = android.app.Notification.FLAG_AUTO_CANCEL;
		NotificationManager notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, noti);
	}

	/**
	 * Check if a network available
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean connected = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				connected = ni.isConnected();
			}
		}
		return connected;
	}
	

}
