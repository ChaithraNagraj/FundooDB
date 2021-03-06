package com.bridgelabz.fundoonotes.dto;

public class NoteDto {

	private String title;
	private String content;
	private String color;
	private boolean isArchived;
	private boolean isDeleted;
	private boolean isPinned;
	private String reminder;
	private boolean reminderStatus;
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isArchived() {
		return isArchived;
	}
	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public boolean isPinned() {
		return isPinned;
	}
	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}
	public String getReminder() {
		return reminder;
	}
	public void setReminder(String reminder) {
		this.reminder = reminder;
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
	public boolean isReminderStatus() {
		return reminderStatus;
	}
	public void setReminderStatus(boolean reminderStatus) {
		this.reminderStatus = reminderStatus;
	}

}
