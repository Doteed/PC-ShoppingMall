package com.project.easyBuild.user.dto;

import java.util.Date;

public class QADto {
	private int qaId;
	private int boardId;
	private String userId;
	private int authId;
	private String title;
	private String content;
	private Date date;
	
	public QADto() {}
	public QADto(int qaId, int boardId, String userId, int authId, String title, String content, Date date) {
		super();
		this.qaId = qaId;
		this.boardId = boardId;
		this.userId = userId;
		this.authId = authId;
		this.title = title;
		this.content = content;
		this.date = date;
	}
	public int getQaId() {
		return qaId;
	}
	public void setQaId(int qaId) {
		this.qaId = qaId;
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getAuthId() {
		return authId;
	}
	public void setAuthId(int authId) {
		this.authId = authId;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
