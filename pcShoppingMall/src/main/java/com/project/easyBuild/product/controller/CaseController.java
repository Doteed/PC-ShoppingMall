package com.project.easyBuild.product.controller;

import com.project.easyBuild.product.model.Case;
import com.project.easyBuild.product.service.CaseService;

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
public class CaseController {
	private final CaseService caseService;
    
    // 생성자를 직접 작성
    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    // 1) 상품 목록을 반환하는 메서드
    @GetMapping("/caseproducts")
    public String showCaseProducts(Model model) {
    	List<Case> cases = caseService.getAllCases(); // 5개 데이터
        
    	//가격 포맷팅
        for (Case Case : cases) {
        	Case.setFormattedPrice(String.format("%,d원", Case.getPrice()));
        }
        
        // 모델에 데이터 추가
        model.addAttribute("cases", cases);
        model.addAttribute("pageType", "case"); // pageType 설정
        model.addAttribute("manufacturers", List.of("앱코", "마이크로닉스","darkFlash","Fractal Design","잘만","CHENBRO","아이구주","COX"));
        model.addAttribute("productClassification", List.of("PC케이스(ATX)", "PC케이스(M-ATX)","미니ITX","HTPC케이스","튜닝 케이스"));
        model.addAttribute("caseSize", List.of("빅타워", "미들타워","미니타워","미니타워(LP)","미니ITX(리틀밸리)"));
        model.addAttribute("bay13_3CM", List.of("5", "4","3","2","1"));
        model.addAttribute("bay8_9CM", List.of("8", "7","6","5","4","3","2","1"));
        return "product/category/caseproducts"; // templates/product/category/caseproducts.html
    }
    
    // 2) 필터링 요청 처리 (POST /product/category/caseproducts/caseCategoryFilter)
    @PostMapping("/product/category/caseproducts/caseCategoryFilter")
    @ResponseBody
    public List<Case> filterCases(@RequestBody Map<String, List<String>> filters) {
    	System.out.println("Received filters: " + filters); // 필터 확인
        List<Case> filteredCases = caseService.filterCases(filters);
        System.out.println("Filtered products: " + filteredCases); // 필터링 결과 확인
        
        System.out.println("Received filters: " + filters); // 디버깅용 로그
        return caseService.filterCases(filters);
    }
    // 제품 상세페이지 처리
    @GetMapping("/caseproducts/{caseId}")
    public String getCaseDetail(@PathVariable Long caseId, Model model) {
    	Case Case = caseService.getCaseById(caseId);
        if (Case == null) {
        	model.addAttribute("error", "해당 제품을 찾을 수 없습니다.");
            return "error"; // error.html 템플릿
        }
        // 가격 포맷팅 설정
        Case.setFormattedPrice(String.format("%,d원", Case.getPrice()));
        
        model.addAttribute("Case", Case);
        return "product/detail/case-details"; // templates/product/detail/case-details.html
    }
}
