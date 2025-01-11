package com.project.easyBuild.user.biz;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.MediaType;
import okhttp3.Response;
import com.google.gson.Gson;
import java.util.Base64;

public class PaymentService {
//	private static final String SECRET_KEY = "test_sk_Ba5PzR0ArnOAD76Wx70x8vmYnNeD"; 
//
//    // 결제 요청을 보내는 메서드
//    public void sendPaymentRequest(double totalAmount, String orderId, String orderName) throws Exception {
//        OkHttpClient client = new OkHttpClient();
//
//        // 결제 요청 데이터를 JSON으로 변환
//        PaymentRequestData paymentRequestData = new PaymentRequestData(totalAmount, orderId, orderName);
//        Gson gson = new Gson();
//        String jsonBody = gson.toJson(paymentRequestData); // 객체를 JSON 문자열로 변환
//
//        // 요청 본문
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody);
//
//        // 요청 헤더 설정 (Authorization 헤더에 시크릿 키 추가)
//        Request request = new Request.Builder()
//                .url("https://api.tosspayments.com/v1/payments/confirm") // 실제 결제 API URL
//                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes())) // Base64로 인코딩하여 Authorization 헤더에 추가
//                .post(body)
//                .build();
//
//        // 요청 보내기
//        Response response = client.newCall(request).execute();
//
//        if (response.isSuccessful()) {
//            System.out.println("Payment successful for Order ID: " + orderId);
//        } else {
//            System.out.println("Payment failed for Order ID: " + orderId);
//            throw new Exception("Payment failed: " + response.body().string());
//        }
//    }
//
//    // 결제 요청 데이터 클래스
//    private static class PaymentRequestData {
//        private double amount;
//        private String orderId;
//        private String orderName;
//
//        public PaymentRequestData(double amount, String orderId, String orderName) {
//            this.amount = amount;
//            this.orderId = orderId;
//            this.orderName = orderName;
//        }
//    }
}
