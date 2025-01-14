package com.project.easyBuild.product.controller;

import com.project.easyBuild.product.model.Case;
import com.project.easyBuild.product.model.hdd;
import com.project.easyBuild.product.service.HddService;

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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class HddController {
	
	private final HddService hddService;
    
    // 생성자를 직접 작성
    public HddController(HddService hddService) {
        this.hddService = hddService;
    }

    // 1) 상품 목록을 반환하는 메서드
    @GetMapping("/hddproducts")
    public String showHddProducts(Model model, HttpSession session) {
    	// 로그인 상태 확인
        Boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);
    	List<hdd> hdds = hddService.getAllHdds(); // 5개 데이터
        
    	//가격 포맷팅
        for (hdd hdd : hdds) {
        	hdd.setFormattedPrice(String.format("%,d원", hdd.getPrice()));
        }
        
        // 모델에 데이터 추가
        model.addAttribute("hdds", hdds);
        model.addAttribute("pageType", "hdd"); // pageType 설정
        model.addAttribute("manufacturers", List.of("Seagate", "Western Digital","도시바","CENTURY","DELL"));
        model.addAttribute("productClassification", List.of("HDD (PC용)", "HDD (노트북용)","HDD (NAS용)","HDD (CCTV용)","HDD (기업용)"));
        model.addAttribute("diskSize", List.of("8.9cm(3.5인치)", "6.4cm(2.5인치)","4.6cm(1.8인치)"));
        model.addAttribute("capacity", List.of("8TB", "4TB","3TB","2TB","1TB"));
        return "product/category/hddproducts"; // templates/product/category/hddproducts.html
    }
    
    // 2) 필터링 요청 처리 (POST /product/category/hddproducts/hddCategoryFilter)
    @PostMapping("/product/category/hddproducts/hddCategoryFilter")
    @ResponseBody
    public List<hdd> filterHdds(@RequestBody Map<String, List<String>> filters) {
    	System.out.println("Received filters: " + filters); // 필터 확인
        List<hdd> filteredHdds = hddService.filterHdds(filters);
        System.out.println("Filtered products: " + filteredHdds); // 필터링 결과 확인
        
        System.out.println("Received filters: " + filters); // 디버깅용 로그
        return hddService.filterHdds(filters);
    }
    // 제품 상세페이지 처리
    @GetMapping("/hddproducts/{hddId}")
    public String getHddDetail(@PathVariable Long hddId, Model model, HttpSession session) {
    	// 상품 정보 로드
        Optional<hdd> hdd = hddService.findById(hddId);
        if (hdd.isEmpty()) {
        	model.addAttribute("error", "해당 제품을 찾을 수 없습니다.");
            return "error"; // error.html 템플릿
        }
        hdd hddItem = hdd.get();
        // 가격 포맷팅 설정
        hddItem.setFormattedPrice(String.format("%,d원", hddItem.getPrice()));
        // 로그인 상태 확인
        Boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);
        
        model.addAttribute("hdd", hddItem);
        return "product/detail/hdd-details"; // templates/product/detail/hdd-details.html
    }
}
