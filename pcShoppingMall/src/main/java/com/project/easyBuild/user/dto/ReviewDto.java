package com.project.easyBuild.user.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDto {
	private int reviewId;
	private int productId;
	private int orderId;
	private String userId;
	private int authId;
	private String title;
	private String content;
	private Date date;
	private int rating;
	private String isDeleted;

	private String productName;
	
	// constructor for inserting
	public ReviewDto(int orderId, String userId, String title, String content, int rating) {
		this.orderId = orderId;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.rating = rating;
	}

	// constructor for updating
	public ReviewDto(Integer reviewId, String userId, String title, String content, int rating) {
		super();
		if(reviewId != null) this.reviewId = reviewId;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.rating = rating;
	}
}
