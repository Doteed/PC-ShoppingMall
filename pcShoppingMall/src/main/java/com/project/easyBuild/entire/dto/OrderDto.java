package com.project.easyBuild.entire.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
	private int orderId;			//주문아이디
	private int deliveryId;			//배송아이디
	private String userId;			//회원아이디
	private int productId;			//상품아이디
	private int totalPrice;			//총가격
	private String cartstatus;		//카트상태
	private String paymentMethod;	//결제방법
	private Date orderDate;			//주문날짜
	
	private String productName; 	//상품명(product table)
	
	private String deliveryStatus;	//주문상태(delivery table)
	private String addressee;		//수령인(delivery table)
	private String address;			//주소(delivery table)
    private String phone;			//전화번호(delivery table)
    
    //constructor for mypage updating
	public OrderDto(int deliveryId, String userId, String addressee, String address, String phone) {
		super();
		this.deliveryId = deliveryId;
		this.userId = userId;
		this.addressee = addressee;
		this.address = address;
		this.phone = phone;
	}
	
	@Override
    public String toString() {
        return "OrderDto{" +
               "deliveryId=" + deliveryId +
               ", userId='" + userId + '\'' +
               ", addressee='" + addressee + '\'' +
               ", address='" + address + '\'' +
               ", phone='" + phone + '\'' +
               '}';
    }
}