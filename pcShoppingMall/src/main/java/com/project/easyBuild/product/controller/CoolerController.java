package com.project.easyBuild.product.controller;

import com.project.easyBuild.product.model.cooler;
import com.project.easyBuild.product.service.CoolerService;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CoolerController {
	
	private final CoolerService coolerService;
    
    // 생성자를 직접 작성
    public CoolerController(CoolerService coolerService) {
        this.coolerService = coolerService;
    }

    // 1) 상품 목록을 반환하는 메서드
    @GetMapping("/coolerproducts")
    public String showCoolerProducts(Model model) {
    	List<cooler> coolers = coolerService.getAllCoolers(); // 5개 데이터
        
    	//가격 포맷팅
        for (cooler cooler : coolers) {
            cooler.setFormattedPrice(String.format("%,d원", cooler.getPrice()));
        }
        
        // 모델에 데이터 추가
        model.addAttribute("coolers", coolers);
        model.addAttribute("pageType", "cooler"); // pageType 설정
        model.addAttribute("manufacturers", List.of("잘만", "맥스엘리트","PCCOOLER","MSI","쿨러마스터","이엠텍","darkFlash","DEEPCOOL"));
        model.addAttribute("coolingMethod", List.of("공랭", "수랭"));
        model.addAttribute("warrantyPeriod", List.of("5년", "4년","3년","2년","1년"));
        return "product/category/coolerproducts"; // templates/product/category/coolerproducts.html
    }
    
    // 2) 필터링 요청 처리 (POST /product/category/coolerproducts/coolerCategoryFilter)
    @PostMapping("/product/category/coolerproducts/coolerCategoryFilter")
    @ResponseBody
    public List<cooler> filterCoolers(@RequestBody Map<String, List<String>> filters) {
    	System.out.println("Received filters: " + filters); // 필터 확인
        List<cooler> filteredCoolers = coolerService.filterCoolers(filters);
        System.out.println("Filtered products: " + filteredCoolers); // 필터링 결과 확인
        
        System.out.println("Received filters: " + filters); // 디버깅용 로그
        return coolerService.filterCoolers(filters);
    }
    // 제품 상세페이지 처리
    @GetMapping("/coolerproducts/{coolerId}")
    public String getCoolerDetail(@PathVariable Long coolerId, Model model) {
        cooler cooler = coolerService.getCoolerById(coolerId);
        if (cooler == null) {
        	model.addAttribute("error", "해당 제품을 찾을 수 없습니다.");
            return "error"; // error.html 템플릿
        }
        // 가격 포맷팅 설정
        cooler.setFormattedPrice(String.format("%,d원", cooler.getPrice()));
        
        model.addAttribute("cooler", cooler);
        return "product/detail/cooler-details"; // templates/product/detail/cooler-details.html
    }

}
