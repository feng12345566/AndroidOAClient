package com.mct.model;

public class Alarm {
	private boolean isActive;
	private String time;
	private boolean[] repeat;

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean[] getRepeat() {
		return repeat;
	}

	public void setRepeat(boolean[] repeat) {
		this.repeat = repeat;
	}

}
