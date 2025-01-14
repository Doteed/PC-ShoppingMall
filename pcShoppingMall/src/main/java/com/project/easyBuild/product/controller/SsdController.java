package com.project.easyBuild.product.controller;

import com.project.easyBuild.product.model.Case;
import com.project.easyBuild.product.model.ssd;
import com.project.easyBuild.product.service.SsdService;

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
public class SsdController {
	
	private final SsdService ssdService;
    
    // 생성자를 직접 작성
    public SsdController(SsdService ssdService) {
        this.ssdService = ssdService;
    }

    // 1) 상품 목록을 반환하는 메서드
    @GetMapping("/ssdproducts")
    public String showSsdProducts(Model model, HttpSession session) {
    	// 로그인 상태 확인
        Boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);
    	List<ssd> ssds = ssdService.getAllSsds(); // 5개 데이터
        
    	//가격 포맷팅
        for (ssd ssd : ssds) {
        	ssd.setFormattedPrice(String.format("%,d원", ssd.getPrice()));
        }
        
        // 모델에 데이터 추가
        model.addAttribute("ssds", ssds);
        model.addAttribute("pageType", "ssd"); // pageType 설정
        model.addAttribute("manufacturers", List.of("삼성전자", "SK하이닉스","마이크론","Seagate","솔리다임","Western Digital"));
        model.addAttribute("formFactor", List.of("M.2 (2280)", "6.4cm(2.5형)","M.2 (2242)","Mini SATA(mSTATA)"));
        model.addAttribute("capacity", List.of("3TB", "2TB","1TB","500GB","256GB"));       
        return "product/category/ssdproducts"; // templates/product/category/ssdproducts.html
    }
    
    // 2) 필터링 요청 처리 (POST /product/category/ssdproducts/ssdCategoryFilter)
    @PostMapping("/product/category/ssdproducts/ssdCategoryFilter")
    @ResponseBody
    public List<ssd> filterSsds(@RequestBody Map<String, List<String>> filters) {
    	System.out.println("Received filters: " + filters); // 필터 확인
        List<ssd> filteredSsds = ssdService.filterSsds(filters);
        System.out.println("Filtered products: " + filteredSsds); // 필터링 결과 확인
        
        System.out.println("Received filters: " + filters); // 디버깅용 로그
        return ssdService.filterSsds(filters);
    }
    // 제품 상세페이지 처리
    @GetMapping("/ssdproducts/{ssdId}")
    public String getSsdDetail(@PathVariable Long ssdId, Model model, HttpSession session) {
    	// 상품 정보 로드
        Optional<ssd> ssd = ssdService.findById(ssdId);
        if (ssd.isEmpty()) {
        	model.addAttribute("error", "해당 제품을 찾을 수 없습니다.");
            return "error"; // error.html 템플릿
        }
        ssd ssdItem = ssd.get();
        // 가격 포맷팅 설정
        ssdItem.setFormattedPrice(String.format("%,d원", ssdItem.getPrice()));
     // 로그인 상태 확인
        Boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);
        
        model.addAttribute("ssd", ssdItem);
        return "product/detail/ssd-details"; // templates/product/detail/ssd-details.html
    }

}
