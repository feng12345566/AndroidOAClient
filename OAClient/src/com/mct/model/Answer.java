package com.mct.model;

public class Answer {
	private int location;// ѡ��λ��
	private int postion;// �����ĸ�����
	private String text;// ѡ���ı�
	private boolean checked=false;

	/**
	 * @param location
	 * @param postion
	 * @param text
	 */
	public Answer(int location, int postion, String text) {
		super();
		this.location = location;
		this.postion = postion;
		this.text = text;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getPostion() {
		return postion;
	}

	public void setPostion(int postion) {
		this.postion = postion;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
