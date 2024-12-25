package com.project.easyBuild.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.easyBuild.authority.biz.ProductBiz;
import com.project.easyBuild.authority.dao.ProductDao;
import com.project.easyBuild.authority.dto.ProductDto;
import com.project.easyBuild.user.biz.OrderBiz;
import com.project.easyBuild.user.biz.QABiz;
import com.project.easyBuild.user.biz.ReviewBiz;
import com.project.easyBuild.user.dto.OrderDto;
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

	//관리자페이지 관련
	@Autowired
	private ProductBiz productbiz;

	@GetMapping("/auth-index")
	public String authIndex(Model model) {
		List<ProductDto> res = productbiz.listAll();
		model.addAttribute("list", res);
		return "pages/authority/auth-index";
	}

	@GetMapping("/auth-product")
	public String authProduct(Model model, @RequestParam(defaultValue = "0") int page) {
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<ProductDto> productPage = productbiz.listAllPaginated(pageable);

		model.addAttribute("products", productPage.getContent());
		model.addAttribute("currentPage", productPage.getNumber());
		model.addAttribute("totalPages", productPage.getTotalPages());
		model.addAttribute("totalItems", productPage.getTotalElements());

		return "pages/authority/auth-product";
	}

	@GetMapping("/auth-product-insert")
	public String authProductInsert() {
		return "pages/authority/auth-product-insert";
	}

	@GetMapping("/auth-order")
	public String authIndex() {
		return "pages/authority/auth-order";
	}

	//마이페이지 관련
	@Autowired
	private ReviewBiz reviewbiz;

	@GetMapping("/my/review")
	public String myReview(Model model) {
		List<ReviewDto> reviews = reviewbiz.listAll();
		model.addAttribute("reviews", reviews);
		return "pages/mypage/my-review";
	}

	@Autowired
	private QABiz qabiz;

	@GetMapping("/my/qa")
	public String myQA(Model model) {
		List<QADto> qas = qabiz.mylistAll("user01");
		model.addAttribute("qas", qas);
		return "pages/mypage/my-qa";
	}
	
	@Autowired
	private OrderBiz orderbiz;

	@GetMapping("/my/order")
	public String myOrder(Model model) {
		List<OrderDto> orders = orderbiz.mylistAll("user01");
		model.addAttribute("orders", orders);
		return "pages/mypage/my-order";
	}
	
	//제품 카테고리 관련
	@GetMapping("/cpuproducts")
    public String cpuproducts() {
    	return "product/category/cpuproducts";
    }
    
    @GetMapping("/cpu01")
    public String cpu01() {
    	return "product/detail/cpu01";
    }
}