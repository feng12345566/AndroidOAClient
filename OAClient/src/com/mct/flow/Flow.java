package com.mct.flow;

import java.util.ArrayList;

import android.content.Context;

/**
 * 流程类
 * 
 * @author fengyouchun
 * @version 创建时间：2014年6月17日 上午9:45:11
 */
public class Flow {
	private long flowId;
	private long flowNo;
	private String flowName;// 流程名称
	private int flowClass;// 所属流程类别
	private ArrayList<FlowNode> nodeList;// 包含节点列表
	private Context context;

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public int getFlowClass() {
		return flowClass;
	}

	public void setFlowClass(int typeId) {
		this.flowClass = typeId;
	}

	public ArrayList<FlowNode> getNodeList() {
		return nodeList;
	}

	public void setNodeList(ArrayList<FlowNode> nodeList) {
		this.nodeList = nodeList;
	}

	public long getFlowNo() {
		return flowNo;
	}

	public void setFlowNo(long flowNo) {
		this.flowNo = flowNo;
	}

	public long getFlowId() {
		return flowId;
	}

	public void setFlowId(long flowId) {
		this.flowId = flowId;
	}

}
