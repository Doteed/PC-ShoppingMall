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
import com.project.easyBuild.user.biz.QABiz;
import com.project.easyBuild.user.biz.ReviewBiz;
import com.project.easyBuild.user.dto.QADto;
import com.project.easyBuild.user.dto.ReviewDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
    
    @Autowired
	private ReviewBiz reviewbiz;
    
    @GetMapping("/my-review")
    public String myReview(Model model) { //, HttpServletRequest request) {
    	List<ReviewDto> reviews = reviewbiz.listAll();
        model.addAttribute("reviews", reviews);
        return "pages/mypage/my-reviews";
        
        /*
    	HttpSession session = request.getSession(false); //세션에서 userId 가져오기
        if (session != null) {
            String userId = (String) session.getAttribute("userId");
            if (userId != null) {
                List<ReviewDto> reviews = reviewbiz.mylistAll(userId);
                model.addAttribute("reviews", reviews);
                return "pages/mypage/my-reviews";
            }
        }
        */
        
        //return "redirect:/"; //로그아웃 상태일 때 로그인 페이지로
    }
    
    @Autowired
	private QABiz qabiz;
    
    @GetMapping("/my-qa")
    public String myQA(Model model) { //, HttpServletRequest request) {
        	List<QADto> qas = qabiz.listAll();
            model.addAttribute("qas", qas);
            return "pages/mypage/my-qas";
            
            /*
        	HttpSession session = request.getSession(false); //세션에서 userId 가져오기
            if (session != null) {
                String userId = (String) session.getAttribute("userId");
                if (userId != null) {
                    List<QADto> qas = reviewbiz.mylistAll(userId);
                    model.addAttribute("qas", qas);
                    return "pages/mypage/my-qas";
                }
            }
            */
            
            //return "redirect:/"; //로그아웃 상태일 때 로그인 페이지로
        }
}