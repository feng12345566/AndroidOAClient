package com.mct.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.util.Log;

/*
 * 
 */
/**
 * @author fengyouchun
 * @version 创建时间：2014年5月18日 下午4:38:51
 */
public class HttpRequestUtil {
	/*
	 * 通过HttpClient发送请求，返回字符串 httpUrl 请求地址,params 请求参数键值对列表,通过new
	 * BasicNameValuePair(key,value)添加
	 */
	public static String getResponsesByPost(String httpUrl,
			List<? extends NameValuePair> params) {
		String result = "";
		HttpPost httpRequest = new HttpPost(httpUrl);
		try {
			HttpEntity httpEntity = new UrlEncodedFormEntity(params,"utf8");
			httpRequest.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			// 请求超时
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            // 读取超时
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			try {
				HttpResponse httpResponse = httpClient.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					result = EntityUtils.toString(httpResponse.getEntity());
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("result", result);
		return result;

	}

	/**
	 * @param httpUrl
	 * @return
	 */
	public static String getResponsesByGet(String httpUrl) {
		String resultData = "";// 获得的数据
		try {
			// 构造一个URL对象
			URL url = new URL(httpUrl);
			// 使用HttpURLConnection打开连接
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			urlConn.setConnectTimeout(10000);
			if (urlConn.getResponseCode() == 200) {
				// 得到读取的内容(流)
				InputStreamReader in = new InputStreamReader(
						urlConn.getInputStream());
				// 为输出创建BufferedReader
				BufferedReader buffer = new BufferedReader(in);
				String inputLine = null;
				while ((inputLine = buffer.readLine()) != null) {
					resultData += inputLine.trim();
				}
				Log.e("resultData", resultData + "");
				// 关闭InputStreamReader
				in.close();
				// 关闭http连接
				urlConn.disconnect();

			}
		} catch (IOException e) {

		}
		return resultData;
	}

}
