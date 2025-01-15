package com.project.easyBuild.user.dto;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
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
public class OrderRequestDto {
    private String userId;
    private int authId;
    private List<Integer> cartIds; //장바구니에서 체크된 cartid
    private String paymentMethod;
    private String addressee;
    private String address;
    private String phone;
    private double amount;
}

