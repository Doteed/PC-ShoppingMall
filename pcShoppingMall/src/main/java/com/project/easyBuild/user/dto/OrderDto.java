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
public class OrderDto {
	private int orderId;
	private int deliveryId;
	private String userId;
	private int productId;
	private int totalPrice;
	private Boolean cartStatus;
	private String paymentMethod;
	private Date orderDate;
	
	private String productName;
}
