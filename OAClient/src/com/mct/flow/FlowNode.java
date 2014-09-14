package com.mct.flow;

import android.content.Context;

/**
 * 流程节点类
 * 
 * @author fengyouchun
 * @version 创建时间：2014年6月17日 上午9:45:34
 */
public class FlowNode {
	private Context context;
	private long nodeId;
	private long flowId;// 流程id
	private int nodeNo;// 节点编号
	private String nodeName;// 节点名称
	private String nextNodeNo;
	private String userId;
	private int isEnd;

	/**
	 * @param context
	 * @param nodeId
	 * @param flowId
	 * @param nodeNo
	 * @param nodeName
	 * @param nextNodeNo
	 * @param isEnd
	 */
	public FlowNode(Context context, long nodeId,long flowId, int nodeNo, String nodeName,
			String nextNodeNo,String userId, int isEnd) {
		super();
		this.context = context;
		this.nodeId=nodeId;
		this.flowId = flowId;
		this.nodeNo = nodeNo;
		this.nodeName = nodeName;
		this.nextNodeNo = nextNodeNo;
		this.userId=userId;
		this.isEnd = isEnd;
	}

	

	public long getNodeId() {
		return nodeId;
	}



	public long getFlowId() {
		return flowId;
	}



	public String getUserId() {
		return userId;
	}



	public int getNodeNo() {
		return nodeNo;
	}

	public void setNodeNo(int nodeNo) {
		this.nodeNo = nodeNo;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNextNodeNo() {
		return nextNodeNo;
	}

	public void setNextNodeNo(String nextNodeNo) {
		this.nextNodeNo = nextNodeNo;
	}

	public int getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(int isEnd) {
		this.isEnd = isEnd;
	}

}
