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
public class CartDto {
	private int cartId;
	private String userId;
	private int productId;
	private int quantity;
	private String selected;
	private Date cartDate;
	
	private String productName;
    private int productPrice;
    private String productImageUrl;
    
    private double avgRating;
}
