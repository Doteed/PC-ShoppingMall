package com.project.easyBuild.authority.dto;

import java.util.Date;

public class ProductDto {
    private int productId;      // 상품 아이디
    private String userId;      // 유저 아이디
    private int categoryId;     // 카테고리 아이디
    private int authId;         // 권한 식별자
    private int pPrice;         // 가격
    private String pName;       // 상품명
    private int pStock;         // 재고
    private int pReportstock;   // 통보 수량 (P_REPORTSTOCK으로 변경)
    private Date pEnroll;       // 등록일
    private String imageUrl;    // 이미지 URL
    private String pExplan;     // 상품 설명 (추가됨)
    private String pSale;       // 판매 여부
    private String pSoldout;    // 품절 여부
    private int orderWaiting;   // 대기 중 주문 수량
    private int availableStock; // 가용 재고
	
	public ProductDto() {}

	public ProductDto(int productId, String userId, int categoryId, int authId, int pPrice, String pName, int pStock,
			int pReportstock, Date pEnroll, String imageUrl, String pExplan, String pSale, String pSoldout,
			int orderWaiting, int availableStock) {
		super();
		this.productId = productId;
		this.userId = userId;
		this.categoryId = categoryId;
		this.authId = authId;
		this.pPrice = pPrice;
		this.pName = pName;
		this.pStock = pStock;
		this.pReportstock = pReportstock;
		this.pEnroll = pEnroll;
		this.imageUrl = imageUrl;
		this.pExplan = pExplan;
		this.pSale = pSale;
		this.pSoldout = pSoldout;
		this.orderWaiting = orderWaiting;
		this.availableStock = availableStock;
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

	public int getpReportstock() {
		return pReportstock;
	}

	public void setpReportstock(int pReportstock) {
		this.pReportstock = pReportstock;
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

	public String getpExplan() {
		return pExplan;
	}

	public void setpExplan(String pExplan) {
		this.pExplan = pExplan;
	}

	public String getpSale() {
		return pSale;
	}

	public void setpSale(String pSale) {
		this.pSale = pSale;
	}

	public String getpSoldout() {
		return pSoldout;
	}

	public void setpSoldout(String pSoldout) {
		this.pSoldout = pSoldout;
	}

	public int getOrderWaiting() {
		return orderWaiting;
	}

	public void setOrderWaiting(int orderWaiting) {
		this.orderWaiting = orderWaiting;
	}

	public int getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(int availableStock) {
		this.availableStock = availableStock;
	}

	@Override
	public String toString() {
		return "ProductDto [productId=" + productId + ", userId=" + userId + ", categoryId=" + categoryId + ", authId="
				+ authId + ", pPrice=" + pPrice + ", pName=" + pName + ", pStock=" + pStock + ", pReportstock="
				+ pReportstock + ", pEnroll=" + pEnroll + ", imageUrl=" + imageUrl + ", pExplan=" + pExplan + ", pSale="
				+ pSale + ", pSoldout=" + pSoldout + ", orderWaiting=" + orderWaiting + ", availableStock="
				+ availableStock + "]";
	}
	
}
