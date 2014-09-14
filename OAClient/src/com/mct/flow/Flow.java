package com.mct.flow;

import java.util.ArrayList;

import android.content.Context;

/**
 * ������
 * 
 * @author fengyouchun
 * @version ����ʱ�䣺2014��6��17�� ����9:45:11
 */
public class Flow {
	private long flowId;
	private long flowNo;
	private String flowName;// ��������
	private int flowClass;// �����������
	private ArrayList<FlowNode> nodeList;// �����ڵ��б�
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
