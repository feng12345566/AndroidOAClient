package com.mct.model;

public class Note {
	private long id;
	private String title;
	private String content;
	private long dotimeStart;
	private long dotimeEnd;
	private long time;
	private boolean isNotification;
   private boolean voice;
   private boolean shock;
   
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	

	public long getDotimeStart() {
		return dotimeStart;
	}

	public void setDotimeStart(long dotimeStart) {
		this.dotimeStart = dotimeStart;
	}

	public long getDotimeEnd() {
		return dotimeEnd;
	}

	public void setDotimeEnd(long dotimeEnd) {
		this.dotimeEnd = dotimeEnd;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isNotification() {
		return isNotification;
	}

	public void setNotification(boolean isNotification) {
		this.isNotification = isNotification;
	}

	public boolean isVoice() {
		return voice;
	}

	public void setVoice(boolean voice) {
		this.voice = voice;
	}

	public boolean isShock() {
		return shock;
	}

	public void setShock(boolean shock) {
		this.shock = shock;
	}

}
