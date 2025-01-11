package com.project.easyBuild.product.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.easyBuild.product.model.mainboard;
import com.project.easyBuild.product.service.MainboardService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MainboardController {
	
	private final MainboardService mainboardService;
    
    // 생성자를 직접 작성
    public MainboardController(MainboardService mainboardService) {
        this.mainboardService = mainboardService;
    }

    // 1) 상품 목록을 반환하는 메서드
    @GetMapping("/mainboardproducts")
    public String showMainboardProducts(Model model) {
    	List<mainboard> mainboards = mainboardService.getAllMainboards(); // 5개 데이터
        
    	//가격 포맷팅
        for (mainboard mainboard : mainboards) {
            mainboard.setFormattedPrice(String.format("%,d원", mainboard.getPrice()));
        }
        
        // 모델에 데이터 추가
        model.addAttribute("mainboards", mainboards);
        model.addAttribute("pageType", "mainboard"); // pageType 설정
        model.addAttribute("manufacturers", List.of("MSI", "ASUS","ASRock","GIGABYTE","ECS"));
        model.addAttribute("productClassification", List.of("인텔 CPU용", "AMD CPU용","임베디드","주변기기"));
        model.addAttribute("socket", List.of("인텔(소켓1851)", "인텔(소켓1700)","인텔(소켓1200)","AMD(소켓AM5)","AMD(소켓AM4)"));
        model.addAttribute("detailedChipset", List.of("인텔 Z890", "AMD X870E","AMD X870","AMD X670E","AMD B650"));
        model.addAttribute("formFactor", List.of("ATX", "M-ATX","M-iTX","E-ATX"));
        model.addAttribute("vcoreOutputTotal", List.of("1440A", "1280A","1260A","1120A","960A"));
        return "product/category/mainboardproducts"; // templates/product/category/mainboardproducts.html
    }
    
    // 2) 필터링 요청 처리 (POST /product/category/mainboardproducts/mainboardCategoryFilter)
    @PostMapping("/product/category/mainboardproducts/mainboardCategoryFilter")
    @ResponseBody
    public List<mainboard> filterMainboards(@RequestBody Map<String, List<String>> filters) {
    	System.out.println("Received filters: " + filters); // 필터 확인
        List<mainboard> filteredMainboards = mainboardService.filterMainboards(filters);
        System.out.println("Filtered products: " + filteredMainboards); // 필터링 결과 확인
        
        System.out.println("Received filters: " + filters); // 디버깅용 로그
        return mainboardService.filterMainboards(filters);
    }
    // 제품 상세페이지 처리
    @GetMapping("/mainboardproducts/{mainboardId}")
    public String getMainboardDetail(@PathVariable Long mainboardId, Model model) {
    	mainboard mainboard = mainboardService.getMainboardById(mainboardId);
        if (mainboard == null) {
        	model.addAttribute("error", "해당 제품을 찾을 수 없습니다.");
            return "error"; // error.html 템플릿
        }
        // 가격 포맷팅 설정
        mainboard.setFormattedPrice(String.format("%,d원", mainboard.getPrice()));
        
        model.addAttribute("mainboard", mainboard);
        return "product/detail/mainboard-details"; // templates/product/detail/mainboard-details.html
    }

}
