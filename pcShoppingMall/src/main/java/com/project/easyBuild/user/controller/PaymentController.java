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

import com.project.easyBuild.user.biz.PaymentService;
import com.project.easyBuild.user.dto.PaymentRequest;

import org.json.JSONObject;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	private final PaymentService paymentService;

	 public PaymentController(PaymentService paymentService) {
	        this.paymentService = paymentService;
	    }

	    @PostMapping("/request")
	    public ResponseEntity<?> requestPayment(@RequestBody PaymentRequest request) throws Exception {
	    	return paymentService.requestPayment(request);
	    }

	    @PostMapping("/confirm")
	    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, String> paymentData) {
	        try {
	            paymentService.confirmPayment(paymentData);
	            return ResponseEntity.ok("결제 성공");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", e.getMessage()));
	        }
	    }
}