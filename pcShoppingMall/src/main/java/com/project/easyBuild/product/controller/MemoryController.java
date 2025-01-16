package com.project.easyBuild.product.controller;

import com.project.easyBuild.member.dto.MemberDto;
import com.project.easyBuild.product.model.Case;
import com.project.easyBuild.product.model.memory;
import com.project.easyBuild.product.service.MemoryService;

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
public class MemoryController {
	
	private final MemoryService memoryService;
    
    // 생성자를 직접 작성
    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    // 1) 상품 목록을 반환하는 메서드
    @GetMapping("/memoryproducts")
    public String showMemoryProducts(Model model, HttpSession session) {
    	MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
    	Boolean isLoggedIn = loggedInUser != null; // null 여부 확인
    	model.addAttribute("isLoggedIn", isLoggedIn);
    	List<memory> memorys = memoryService.getAllMemorys(); // 5개 데이터
        
    	//가격 포맷팅
        for (memory memory : memorys) {
        	memory.setFormattedPrice(String.format("%,d원", memory.getPrice()));
        }
        
        // 모델에 데이터 추가
        model.addAttribute("memorys", memorys);
        model.addAttribute("pageType", "memory"); // pageType 설정
        model.addAttribute("manufacturers", List.of("삼성전자", "마이크론","G.SKILL","TeamGroup","이메이션"));
        model.addAttribute("productClassification", List.of("DDR5", "DDR4","DDR3","DDR2"));
        model.addAttribute("capacity", List.of("64GB", "32GB","16GB","8GB","4GB"));
        model.addAttribute("operatingClock", List.of("6000MHz (PC5-48000)", "5600MHz (PC5-44800)","4800MHz (PC5-38400)","4133MHz (PC4-33000)","4000MHz (PC4-32000)"));
        return "product/category/memoryproducts"; // templates/product/category/memoryproducts.html
    }
    
    // 2) 필터링 요청 처리 (POST /product/category/memoryproducts/memoryCategoryFilter)
    @PostMapping("/product/category/memoryproducts/memoryCategoryFilter")
    @ResponseBody
    public List<memory> filterMemorys(@RequestBody Map<String, List<String>> filters) {
    	System.out.println("Received filters: " + filters); // 필터 확인
        List<memory> filteredMemorys = memoryService.filterMemorys(filters);
        System.out.println("Filtered products: " + filteredMemorys); // 필터링 결과 확인
        
        System.out.println("Received filters: " + filters); // 디버깅용 로그
        return memoryService.filterMemorys(filters);
    }
    // 제품 상세페이지 처리
    @GetMapping("/memoryproducts/{memoryId}")
    public String getMemoryDetail(@PathVariable Long memoryId, Model model, HttpSession session) {
    	// 상품 정보 로드
        Optional<memory> memory = memoryService.findById(memoryId);;
        if (memory.isEmpty()) {
        	model.addAttribute("error", "해당 제품을 찾을 수 없습니다.");
            return "error"; // error.html 템플릿
        }
        memory memoryItem = memory.get();
        // 가격 포맷팅 설정
        memoryItem.setFormattedPrice(String.format("%,d원", memoryItem.getPrice()));
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
    	Boolean isLoggedIn = loggedInUser != null; // null 여부 확인
    	model.addAttribute("isLoggedIn", isLoggedIn);
        
        model.addAttribute("memory", memoryItem);
        return "product/detail/memory-details"; // templates/product/detail/memory-details.html
    }
}
