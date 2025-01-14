package com.project.easyBuild.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentRequest {
	private int amount;
    private String method;
    private String orderId;
    private String orderName;
    private String customerName;
    private String successUrl;
    private String failUrl;
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
class PaymentResponsen {
    private String status;
    private String url;
    private String orderId;
}