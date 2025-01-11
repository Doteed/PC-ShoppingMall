package com.project.easyBuild.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentViewController {
	@GetMapping("/payment/success")
    public String paymentSuccess() {
        return "pages/mypage/order-success";
    }

    @GetMapping("/payment/fail")
    public String paymentFail() {
        return "pages/mypage/order-fail";
    }
}
