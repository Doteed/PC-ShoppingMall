package com.project.easyBuild.user.dto;

import java.util.Date;

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
	
	public ReviewDto() {}
	public ReviewDto(int reviewId, int productId, String userId, int authId, String title, String content, Date date,
			int rating, String productName) {
		super();
		this.reviewId = reviewId;
		this.productId = productId;
		this.userId = userId;
		this.authId = authId;
		this.title = title;
		this.content = content;
		this.date = date;
		this.rating = rating;
		this.productName = productName;
	}
	public int getReviewId() {
		return reviewId;
	}
	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
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
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
}


