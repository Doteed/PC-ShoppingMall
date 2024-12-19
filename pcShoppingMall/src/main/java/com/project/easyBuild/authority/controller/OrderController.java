package com.project.easyBuild.authority.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/order")
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    @PostMapping("/auth-order-modal")
    public void authOrderModal() {
    	
    }
}
