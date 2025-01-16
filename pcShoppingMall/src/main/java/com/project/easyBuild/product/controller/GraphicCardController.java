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

import com.project.easyBuild.member.dto.MemberDto;
import com.project.easyBuild.product.model.Case;
import com.project.easyBuild.product.model.graphicCard;
import com.project.easyBuild.product.service.GraphicCardService;

import jakarta.servlet.http.HttpSession;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class GraphicCardController {
	
	private final GraphicCardService graphicCardService;
    
    // 생성자를 직접 작성
    public GraphicCardController(GraphicCardService graphicCardService) {
        this.graphicCardService = graphicCardService;
    }

    // 1) 상품 목록을 반환하는 메서드
    @GetMapping("/graphicCardproducts")
    public String showGraphicCardProducts(Model model, HttpSession session) {
    	MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
    	Boolean isLoggedIn = loggedInUser != null; // null 여부 확인
    	model.addAttribute("isLoggedIn", isLoggedIn);
        
    	List<graphicCard> graphicCards = graphicCardService.getAllGraphicCards(); // 5개 데이터
        
    	//가격 포맷팅
        for (graphicCard graphicCard : graphicCards) {
        	graphicCard.setFormattedPrice(String.format("%,d원", graphicCard.getPrice()));
        }
        
        // 모델에 데이터 추가
        model.addAttribute("graphicCards", graphicCards);
        model.addAttribute("pageType", "graphicCard"); // pageType 설정
        model.addAttribute("manufacturers", List.of("MSI", "ASUS","이엠텍","GIGABYTE","GAINWARD","COLORFUL"));
        model.addAttribute("chipsetManufacturer", List.of("NVIDIA", "AMD","Intel"));
        model.addAttribute("chipset", List.of("RTX 4080 SUPER", "RTX 4070","RTX 4060 Ti","RTX 4060","RX 7800 XT","RX 7600"));
        return "product/category/graphicCardproducts"; // templates/product/category/graphicCardproducts.html
    }
    
    // 2) 필터링 요청 처리 (POST /product/category/graphicCardproducts/graphicCardCategoryFilter)
    @PostMapping("/product/category/graphicCardproducts/graphicCardCategoryFilter")
    @ResponseBody
    public List<graphicCard> filterGraphicCards(@RequestBody Map<String, List<String>> filters) {
    	System.out.println("Received filters: " + filters); // 필터 확인
        List<graphicCard> filteredGraphicCards = graphicCardService.filterGraphicCards(filters);
        System.out.println("Filtered products: " + filteredGraphicCards); // 필터링 결과 확인
        
        System.out.println("Received filters: " + filters); // 디버깅용 로그
        return graphicCardService.filterGraphicCards(filters);
    }
    // 제품 상세페이지 처리
    @GetMapping("/graphicCardproducts/{graphicCardId}")
    public String getGraphicCardDetail(@PathVariable Long graphicCardId, Model model, HttpSession session) {
    	// 상품 정보 로드
        Optional<graphicCard> graphicCard = graphicCardService.findById(graphicCardId);
        if (graphicCard.isEmpty()) {
        	model.addAttribute("error", "해당 제품을 찾을 수 없습니다.");
            return "error"; // error.html 템플릿
        }
        graphicCard graphicCardItem = graphicCard.get();
        // 가격 포맷팅 설정
        graphicCardItem.setFormattedPrice(String.format("%,d원", graphicCardItem.getPrice()));
        MemberDto loggedInUser = (MemberDto) session.getAttribute("dto");
    	Boolean isLoggedIn = loggedInUser != null; // null 여부 확인
    	model.addAttribute("isLoggedIn", isLoggedIn);
        
        model.addAttribute("graphicCard", graphicCardItem);
        return "product/detail/graphicCard-details"; // templates/product/detail/graphicCard-details.html
    }
}
