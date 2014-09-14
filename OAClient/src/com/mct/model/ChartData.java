package com.mct.model;

public class ChartData {
	private int num;//ÌâºÅ
	private int[] numbers;// Í¶Æ±Êý
	private String question;
	private String[] answerItems;
	
	
	/**
	 * @param num
	 */
	public ChartData(int num) {
		super();
		this.num = num;
	}

	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int[] getNumbers() {
		return numbers;
	}
	public void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}

	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String[] getAnswerItems() {
		return answerItems;
	}
	public void setAnswerItems(String[] answerItems) {
		this.answerItems = answerItems;
	}
	
}
