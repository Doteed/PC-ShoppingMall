package com.project.easyBuild.user.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
	private int reviewId;
	private int productId;
	private String userId;
	private int authId;
	private String title;
	private String content;
	private Date date;
	private int rating;
	
	private String productName;
}


