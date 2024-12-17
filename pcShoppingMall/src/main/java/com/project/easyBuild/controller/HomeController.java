package com.project.easyBuild.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.easyBuild.product.biz.ProductBiz;
import com.project.easyBuild.product.dao.ProductDao;
import com.project.easyBuild.product.dto.ProductDto;

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
    
	@Autowired
	private ProductBiz productbiz;
	
    
    @GetMapping("/auth-index")
    public String authIndex(Model model) {
		List<ProductDto> res = productbiz.listAll();
		model.addAttribute("list",res);
    	return "pages/authority/auth-index";
    }

    
    @GetMapping("/auth-product")
    public String authProduct(Model model) {
        List<ProductDto> products = productbiz.listAll();
        model.addAttribute("products", products);
        return "pages/authority/auth-product";
    }

    @GetMapping("/auth-product-insert")
    public String authProductInsert() {
    	return "pages/authority/auth-product-insert";
    }
}