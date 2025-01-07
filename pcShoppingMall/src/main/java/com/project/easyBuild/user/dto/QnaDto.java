package com.project.easyBuild.user.dto;

import java.util.Date;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QnaDto {
	private int qnaId;
	private int boardId;
	private String userId;
	private int authId;
	private String title;
	private String content;
	private String answer;
	private String password;
	private int isSecret;
	private Date createdDate;
	
	//constructor for updating
	public QnaDto(int qaId, String title, String content) {
		super();
		this.qnaId = qaId;
		this.title = title;
		this.content = content;
	}
}
