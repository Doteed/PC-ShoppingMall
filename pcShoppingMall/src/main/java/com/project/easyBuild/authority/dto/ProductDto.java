package com.project.easyBuild.authority.dto;

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
public class ProductDto {
    private int productId;      // 상품 아이디
    private String userId;      // 유저 아이디
    private int categoryId1;	// 카테고리 아이디
    private int categoryId2;	// 카테고리 아이디
    private int categoryId3;    // 카테고리 아이디
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
}
