package com.project.easyBuild.authority.dto;

import java.util.Date;

public class OrderDto {
	private int orderId;		//주문아이디
	private int deliveryId;		//배송아이디
	private String userId;			//회원아이디
	private int productId;		//상품아이디
	private int totalPrice;		//총가격
	private String cartstatus;		//카트상태
	private String paymentMethod;	//결제방법
	private Date orderDate;		//주문날짜
	public OrderDto() {}
	public OrderDto(int orderId, int deliveryId, String userId, int productId, int totalPrice, String cartstatus,
			String paymentMethod, Date orderDate) {
		super();
		this.orderId = orderId;
		this.deliveryId = deliveryId;
		this.userId = userId;
		this.productId = productId;
		this.totalPrice = totalPrice;
		this.cartstatus = cartstatus;
		this.paymentMethod = paymentMethod;
		this.orderDate = orderDate;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getCartstatus() {
		return cartstatus;
	}
	public void setCartstatus(String cartstatus) {
		this.cartstatus = cartstatus;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	@Override
	public String toString() {
		return "OrderDto [orderId=" + orderId + ", deliveryId=" + deliveryId + ", userId=" + userId + ", productId="
				+ productId + ", totalPrice=" + totalPrice + ", cartstatus=" + cartstatus + ", paymentMethod="
				+ paymentMethod + ", orderDate=" + orderDate + "]";
	}
}
