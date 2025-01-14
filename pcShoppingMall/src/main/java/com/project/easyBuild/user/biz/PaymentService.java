package com.project.easyBuild.user.biz;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.easyBuild.user.dto.PaymentRequest;

import java.util.Base64;

import org.json.JSONObject;

import java.util.Map;
import java.util.HashMap;

@Service
public class PaymentService {
	private static final String SECRET_KEY = "test_sk_Ba5PzR0ArnOAD76Wx70x8vmYnNeD";
    private static final String TOSS_API_URL = "https://api.tosspayments.com/v1/payments";


    public ResponseEntity<?> requestPayment(PaymentRequest request) throws Exception {
    	//httpheaders 설정
        String authorizationValue = "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.set("Authorization", authorizationValue);

        //http 요청 본문
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("method", request.getMethod());
        requestBody.put("amount", request.getAmount());
        requestBody.put("orderId", request.getOrderId());
        requestBody.put("orderName", request.getOrderName());
        requestBody.put("successUrl", request.getSuccessUrl());
        requestBody.put("failUrl", request.getFailUrl());

        //httpentity 설정
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        //toss api 호출
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(TOSS_API_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject responseBody = new JSONObject(response.getBody());
                //ready인지 확인 후 url 반환
                if ("READY".equals(responseBody.getString("status"))) {
                    String checkoutUrl = responseBody.getJSONObject("checkout").getString("url");
                    return ResponseEntity.ok(Map.of("status", "READY", "url", checkoutUrl));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 요청 실패");
                }
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("결제 처리 중 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 요청 중 오류 발생: " + e.getMessage());
        }
    }

    //확인
    public void confirmPayment(Map<String, String> paymentData) {
        String paymentKey = paymentData.get("paymentKey");
        String orderId = paymentData.get("orderId");

        //httpheaders 설정
        String authorizationValue = "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationValue);

        //toss api 결제 확인 url
        String confirmationUrl = String.format("%s/v1/payments/confirm", TOSS_API_URL);

        JSONObject requestBody = new JSONObject();
        requestBody.put("paymentKey", paymentKey);
        requestBody.put("orderId", orderId);

        //httpentity 설정
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        //toss api 호출
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(confirmationUrl, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject responseBody = new JSONObject(response.getBody());
                String paymentStatus = responseBody.getString("status");

                if ("COMPLETED".equals(paymentStatus)) {
                    System.out.println("결제 성공: " + responseBody.getString("orderId"));
                } else {
                    System.out.println("결제 실패: " + responseBody.getString("orderId"));
                }
            } else {
                System.out.println("결제 확인 중 오류 발생: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("결제 확인 중 예외 발생: " + e.getMessage());
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(SECRET_KEY, "");
        return headers;
    }
}