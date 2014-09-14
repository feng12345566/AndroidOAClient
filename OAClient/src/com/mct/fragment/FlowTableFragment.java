package com.mct.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.mct.client.R;
import com.mct.flow.FlowNode;
import com.mct.flow.Widget;
import com.mct.flowutil.FlowDbUtil;
import com.mct.util.HttpRequestUtil;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FlowTableFragment extends Fragment {
	private LinearLayout ll;
	private String flowName;
	private long flowId;
	private long nodeId;// 当前节点id
	private Context context;
	private ArrayList<Widget> list;// 所有控件列表
	private ArrayList<Widget> editWidgetList;// 可编辑控件列表
	private String jsonStr;// 从网络上获取的控件值json字符串
	private HashMap<String, String> valueMap;// 从网络上获取的控件值集合

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		/* 通过getArgments()方法获取从Activity传过来的值 */
		Bundle bundle = this.getArguments();
		flowName = bundle.getString("flowname");
		flowId = bundle.getLong("flowid");
		nodeId = bundle.getLong("nodeid");
		jsonStr = bundle.getString("jsonstr");
		Log.e("mtag", "flowName=" + flowName + ",flowid" + flowId + ",nodeid="
				+ nodeId);
		// 布局容器
		View view = inflater.inflate(R.layout.startflowfragment, null);
		context = getActivity();
		ll = (LinearLayout) view.findViewById(R.id.flowtablelayout);
		// 添加标题
		TextView title = new TextView(context);
		title.setText(flowName);
		title.setTextSize(25);
		title.setTextColor(Color.BLACK);
		title.getPaint().setFakeBoldText(true);
		ll.addView(title);
		title.setGravity(Gravity.CENTER);
		// 查询数据库控件列表
		list = FlowDbUtil.shareFlowUtil(context).getWidgetByFlowID(flowId);
		long tempId = 0;
		editWidgetList = new ArrayList<Widget>();
		valueMap = jsonToMap(jsonStr);
		// 遍历可编辑的的控件
		for (int i = 0; i < list.size(); i++) {
			Widget widget = list.get(i);
			Log.e("nodeid", widget.getNodeId() + "");
			FlowNode flowNode = FlowDbUtil.shareFlowUtil(context).getNode(
					widget.getNodeId());
			if (tempId != widget.getNodeId()) {
				// 非结束节点
				if (flowNode.getIsEnd() == 0) {
					TextView nodeName = new TextView(context);
					nodeName.setText(flowNode.getNodeName());
					nodeName.setTextSize(20);
					nodeName.getPaint().setFakeBoldText(true);
					ll.addView(nodeName);
					tempId = widget.getNodeId();
				}
			}
			if (flowNode.getIsEnd() == 0) {
				ll.addView(widget.initWidget());
				String value = getWidgetValueFromData(widget.getId());
				if (value != null) {
					widget.setValue(value);
				}
			}
			if (widget.getNodeId() == nodeId) {
				widget.setEnable(true);
				editWidgetList.add(widget);
			}
		}
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * 获取控件值
	 * 
	 * @return
	 */
	public String getValue() {
		String jsonStr = "{'widget':[";
		for (int i = 0; i < editWidgetList.size(); i++) {
			Widget widget = list.get(i);
			String str = list.get(i).getValue();
			if (str != null) {
				jsonStr = jsonStr + "\"" + list.get(i).getId() + "\":\"" + str
						+ "\"";
			} else {
				jsonStr = jsonStr + "\"" + list.get(i).getId() + "\":" + str;
			}
			if (i == editWidgetList.size() - 1) {
				jsonStr = jsonStr + "]}";
			} else {
				jsonStr = jsonStr + ",";
			}
		}
		Log.e("jsonStr", jsonStr);
		return jsonStr;
	}

	/**
	 * 检查不能为空的控件是否为空
	 * 
	 * @return
	 */
	public boolean checkNull() {
		for (int i = 0; i < list.size(); i++) {
			Widget widget = list.get(i);
			if (widget.getNodeId() == nodeId) {
				if (!widget.checkNull()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 功能描述：从服务器上获取实例id fengyouchun 2014年7月5日 下午6:51:14
	 * 
	 * @param flowId2
	 *            流程id
	 * @param time
	 *            发送时间
	 * @param postUser
	 *            申请人
	 * @return
	 */
	public String getFlowProjectIdFromNet(long flowId2, long time,
			String postUser) {
		String httpUrl = "http://feng1233445566.eicp.net:24009/oa/flow/getprojectid.jsp?flowid="
				+ flowId2 + "&time=" + time + "&postuser=" + postUser;
		Log.e("get", httpUrl);
		String result = HttpRequestUtil.getResponsesByGet(httpUrl);
		return result;

	}

	/**
	 * 功能描述：将获取的json数据转化为集合数据 Administrator 2014年7月7日 下午1:54:18
	 * 
	 * @param jsonStr
	 * @return
	 */
	private HashMap<String, String> jsonToMap(String jsonStr) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (jsonStr != null && !jsonStr.equals("")) {
			try {
				JSONObject data = new JSONObject(jsonStr);
				Iterator<String> iterator = data.keys();
				for (int i = 1; i <= data.length(); i++) {
					map.put(iterator.next(), data.getString(i + ""));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;

	}

	private String getWidgetValueFromData(long widgetId) {
		if (valueMap != null) {
			if (valueMap.containsKey(String.valueOf(widgetId))) {
				return valueMap.get(String.valueOf(widgetId));
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
