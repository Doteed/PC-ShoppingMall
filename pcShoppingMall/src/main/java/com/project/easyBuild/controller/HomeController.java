package com.project.easyBuild.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/example")
    public String example() {
    	return "example/example";
    }
    
    @GetMapping("/auth-index")
    public String authIndex() {
    	return "pages/authority/auth-index";
    }
}