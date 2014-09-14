package com.mct.model;

import java.util.LinkedList;

public class Question {
    private int num;//题号
    private String text;//问题文本
    private int type;//问题类型 1单选   2多选   3编辑文本
    private boolean mustEdit;//是否必填
    private LinkedList<Answer> answerList;//可选项
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public boolean isMustEdit() {
		return mustEdit;
	}
	public void setMustEdit(boolean mustEdit) {
		this.mustEdit = mustEdit;
	}
	public LinkedList<Answer> getAnswerList() {
		return answerList;
	}
	public void setAnswerList(LinkedList<Answer> answerList) {
		this.answerList = answerList;
	}
    
}
