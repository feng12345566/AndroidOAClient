package com.mct.model;

import java.util.LinkedList;

public class Question {
    private int num;//���
    private String text;//�����ı�
    private int type;//�������� 1��ѡ   2��ѡ   3�༭�ı�
    private boolean mustEdit;//�Ƿ����
    private LinkedList<Answer> answerList;//��ѡ��
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
