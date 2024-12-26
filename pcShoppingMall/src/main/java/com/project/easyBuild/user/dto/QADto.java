package com.project.easyBuild.user.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QADto {
	private int qaId;
	private int boardId;
	private String userId;
	private int authId;
	private String title;
	private String content;
	private Date date;
	
	//update용 생성자
	public QADto(int qaId, String title, String content, String userId) {
		super();
		this.qaId = qaId;
		this.userId = userId;
		this.title = title;
		this.content = content;
	}
}
