package com.project.easyBuild.product.controller;

import com.project.easyBuild.product.model.Case;
import com.project.easyBuild.product.model.cpu;
import com.project.easyBuild.product.service.CpuProductService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller

public class CpuProductController {

    private final CpuProductService productService;
    
    // 생성자를 직접 작성
    public CpuProductController(CpuProductService productService) {
        this.productService = productService;
    }

    // 1) 상품 목록을 반환하는 메서드
    @GetMapping("/cpuproducts")
    public String showProducts(Model model, HttpSession session) {
    	// 로그인 상태 확인
        Boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);
        
    	List<cpu> cpus = productService.getAllCpus(); // 5개 데이터
        
    	//가격 포맷팅
        for (cpu cpu : cpus) {
            cpu.setFormattedPrice(String.format("%,d원", cpu.getPrice()));
        }
        
        // 모델에 데이터 추가
        model.addAttribute("cpus", cpus);
        model.addAttribute("manufacturers", List.of("인텔", "AMD"));
        model.addAttribute("intelCpuTypes", List.of("울트라 9", "코어 i7"));
        model.addAttribute("amdCpuTypes", List.of("라이젠9", "라이젠7"));
        model.addAttribute("sockets", List.of("인텔(소켓1851)", "인텔(소켓1700)"));
        return "product/category/cpuproducts"; // templates/product/category/cpuproducts.html
    }
    
    // 2) 필터링 요청 처리 (POST /product/category/cpuproducts/cpuCategoryFilter)
    @PostMapping("/product/category/cpuproducts/cpuCategoryFilter")
    @ResponseBody
    public List<cpu> filterProducts(@RequestBody Map<String, List<String>> filters) {
    	System.out.println("Received filters: " + filters); // 필터 확인
        List<cpu> filteredProducts = productService.filterProducts(filters);
        System.out.println("Filtered products: " + filteredProducts); // 필터링 결과 확인
        
        System.out.println("Received filters: " + filters); // 디버깅용 로그
        return productService.filterProducts(filters);
    }
    // 제품 상세페이지 처리
    @GetMapping("/cpuproducts/{cpuId}")
    public String getProductDetail(@PathVariable Long cpuId, Model model, HttpSession session) {
    	// 상품 정보 로드
        Optional<cpu> product = productService.findById(cpuId);
        if (product.isEmpty()) {
        	model.addAttribute("error", "해당 제품을 찾을 수 없습니다.");
            return "error"; // error.html 템플릿
        }
        cpu cpuItem = product.get();
        // 가격 포맷팅 설정
        cpuItem.setFormattedPrice(String.format("%,d원", cpuItem.getPrice()));
        
        // 로그인 상태 확인
        Boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);
        
        model.addAttribute("product", cpuItem);
        return "product/detail/cpu-details"; // templates/product/detail/cpu-details.html
    }
}
