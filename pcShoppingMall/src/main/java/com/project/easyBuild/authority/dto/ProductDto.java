package com.project.easyBuild.authority.dto;

import java.util.Date;

public class ProductDto {
	private int productId;		//상품 아이디
	private String userId;		//유저 아이디
	private int categoryId;		//카테고리 아이디
	private int authId;			//권한 식별자
	private int pPrice;			//가격
	private String pName;		//상품명
	private int pStock;			//재고
	private Date pEnroll;		//등록일
	private String imageUrl;	//이미지 업로드
	
	public ProductDto() {}
	
	public ProductDto(int productId, String userId, int categoryId, int authId, int pPrice, String pName, int pStock,
			Date pEnroll, String imageUrl) {
		super();
		this.productId = productId;
		this.userId = userId;
		this.categoryId = categoryId;
		this.authId = authId;
		this.pPrice = pPrice;
		this.pName = pName;
		this.pStock = pStock;
		this.pEnroll = pEnroll;
		this.imageUrl = imageUrl;
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

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getAuthId() {
		return authId;
	}

	public void setAuthId(int authId) {
		this.authId = authId;
	}

	public int getpPrice() {
		return pPrice;
	}

	public void setpPrice(int pPrice) {
		this.pPrice = pPrice;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public int getpStock() {
		return pStock;
	}

	public void setpStock(int pStock) {
		this.pStock = pStock;
	}

	public Date getpEnroll() {
		return pEnroll;
	}

	public void setpEnroll(Date pEnroll) {
		this.pEnroll = pEnroll;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "ProductDto [productId=" + productId + ", userId=" + userId + ", categoryId=" + categoryId + ", authId="
				+ authId + ", pPrice=" + pPrice + ", pName=" + pName + ", pStock=" + pStock + ", pEnroll=" + pEnroll + imageUrl
				+ "]";
	}
	
}
