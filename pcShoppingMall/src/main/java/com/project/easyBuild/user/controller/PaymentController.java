package com.project.easyBuild.user.controller;

import org.springframework.http.HttpHeaders;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.http.MediaType;

import com.project.easyBuild.user.dto.PaymentRequest;

import org.json.JSONObject;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	private static final String CLIENT_KEY = "test_ck_kYG57Eba3GjEwRLg6gWQ8pWDOxmA"; // 실제 client key
	private static final String SECRET_KEY = "test_sk_Ba5PzR0ArnOAD76Wx70x8vmYnNeD"; // 실제 secret key
	private static final String TOSS_API_URL = "https://api.tosspayments.com/v1/payments"; // TossPayments API URL

	@PostMapping("/request")
	public ResponseEntity<?> requestPayment(@RequestBody PaymentRequest requestDto) {
		RestTemplate restTemplate = new RestTemplate();

		System.out.println(requestDto);
		
		String authorizationValue = "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes());

		// HTTP 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", authorizationValue);

		// HTTP 요청 생성
		HttpEntity<PaymentRequest> entity = new HttpEntity<>(requestDto, headers);

		try {
			// TossPayments API 호출
			ResponseEntity<String> response = restTemplate.exchange(TOSS_API_URL, HttpMethod.POST, entity,
					String.class);

			System.out.println("응답 상태 코드: " + response.getStatusCode());
			System.out.println("응답 본문: " + response.getBody());

			if (response.getStatusCode() == HttpStatus.OK) {
				JSONObject responseBody = new JSONObject(response.getBody());

				// READY 상태 처리
				if ("READY".equals(responseBody.getString("status"))) {
					String checkoutUrl = responseBody.getJSONObject("checkout").getString("url");
					return ResponseEntity.ok(Map.of("status", "READY", "url", checkoutUrl));
				}

				System.out.println("결제 성공: " + response.getBody());
				return ResponseEntity.ok(responseBody.toString());
			} else {
				return ResponseEntity.status(response.getStatusCode()).body("결제 처리 중 오류가 발생했습니다.");
			}
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			System.err.println("결제 실패 (API 오류): " + e.getResponseBodyAsString());
			System.err.println("응답 상태 코드: " + e.getStatusCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getResponseBodyAsString());
		} catch (Exception e) {
			System.err.println("결제 실패 (예외 발생): " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"message\":\"" + e.getMessage() + "\"}");
		}
	}

	@PostMapping("/payment/callback")
	public ResponseEntity<?> handlePaymentCallback(@RequestParam String paymentKey, @RequestParam String orderId,
			@RequestParam int amount) {
		try {
			// 결제 확인을 위한 요청 데이터 준비
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBasicAuth(SECRET_KEY, ""); // Toss Secret Key 사용

			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("paymentKey", paymentKey);
			requestBody.put("orderId", orderId);
			requestBody.put("amount", amount);

			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

			// Toss Payments API로 결제 확인 요청
			ResponseEntity<String> response = restTemplate.postForEntity(TOSS_API_URL + "/v1/payments/confirm",
					requestEntity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				return ResponseEntity.ok("결제 성공");
			} else {
				return ResponseEntity.status(response.getStatusCode()).body("결제 확인 실패: " + response.getBody());
			}
		} catch (HttpClientErrorException e) {
			return ResponseEntity.status(e.getStatusCode()).body("결제 요청 오류: " + e.getResponseBodyAsString());
		}
	}

	@PostMapping("/api/payment/confirm")
	public ResponseEntity<?> confirmPayment(@RequestBody Map<String, String> paymentData) {
		String paymentKey = paymentData.get("paymentKey");
		String orderId = paymentData.get("orderId");
		String amount = paymentData.get("amount");

		if (paymentKey == null || orderId == null || amount == null) {
			return ResponseEntity.badRequest().body("Invalid payment data.");
		}

		// Toss Payments API 호출
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBasicAuth(SECRET_KEY, ""); // Toss Secret Key 사용

	    // 요청 데이터 준비
	    Map<String, Object> requestBody = new HashMap<>();
	    requestBody.put("paymentKey", paymentKey);
	    requestBody.put("orderId", orderId);
	    requestBody.put("amount", amount);

	    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

	    try {
	        // Toss Payments API로 결제 확인 요청
	        ResponseEntity<String> response = restTemplate.postForEntity(
	            TOSS_API_URL + "/confirm",  // 여기에서 상수 사용
	            requestEntity,
	            String.class
	        );

	        if (response.getStatusCode() == HttpStatus.OK) {
	            return ResponseEntity.ok("결제 성공");
	        } else {
	            return ResponseEntity.status(response.getStatusCode())
	                    .body("결제 확인 실패: " + response.getBody());
	        }
	    } catch (HttpClientErrorException e) {
	        return ResponseEntity.status(e.getStatusCode())
	                .body("결제 요청 오류: " + e.getResponseBodyAsString());
	    }
	}
} 