package com.project.easyBuild.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class PaymentViewController {
    @GetMapping("/success")
    public String paymentSuccess() {
        return "pages/mypage/order-success";
    }

    @GetMapping("/fail")
    public String paymentFail() {
        return "pages/mypage/order-fail";
    }
}