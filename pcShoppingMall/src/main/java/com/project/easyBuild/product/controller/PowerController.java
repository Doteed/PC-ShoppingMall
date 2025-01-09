package com.project.easyBuild.product.controller;

import com.project.easyBuild.product.model.power;
import com.project.easyBuild.product.service.PowerService;

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
public class PowerController {
	private final PowerService powerService;
    
    // 생성자를 직접 작성
    public PowerController(PowerService powerService) {
        this.powerService = powerService;
    }

    // 1) 상품 목록을 반환하는 메서드
    @GetMapping("/powerproducts")
    public String showPowerProducts(Model model) {
    	List<power> powers = powerService.getAllPowers(); // 5개 데이터
        
    	//가격 포맷팅
        for (power power : powers) {
        	power.setFormattedPrice(String.format("%,d원", power.getPrice()));
        }
        
        // 모델에 데이터 추가
        model.addAttribute("powers", powers);
        model.addAttribute("pageType", "power"); // pageType 설정
        model.addAttribute("manufacturers", List.of("시소닉", "잘만","앱코","MSI","마이크로닉스","맥스엘리트"));
        model.addAttribute("psuStandard", List.of("ATX", "M-ATX","TFX","서버용 파워","Flex-ATX 파워"));
        model.addAttribute("ratedOutput", List.of("1050W", "850W","700W","650w","600w"));
        model.addAttribute("efficiencyCertification", List.of("80 PLUS 티타늄", "80 PLUS 플래티넘","80 PLUS 골드","80 PLUS 실버","80 PLUS 브론즈"));
        model.addAttribute("etaCertification", List.of("TITANIUM", "PLATINUM","GOLD","SILVER","BRONZE"));
        return "product/category/powerproducts"; // templates/product/category/powerproducts.html
    }
    
    // 2) 필터링 요청 처리 (POST /product/category/powerproducts/powerCategoryFilter)
    @PostMapping("/product/category/powerproducts/powerCategoryFilter")
    @ResponseBody
    public List<power> filterPowers(@RequestBody Map<String, List<String>> filters) {
    	System.out.println("Received filters: " + filters); // 필터 확인
        List<power> filteredPowers = powerService.filterPowers(filters);
        System.out.println("Filtered products: " + filteredPowers); // 필터링 결과 확인
        
        System.out.println("Received filters: " + filters); // 디버깅용 로그
        return powerService.filterPowers(filters);
    }
    // 제품 상세페이지 처리
    @GetMapping("/powerproducts/{powerId}")
    public String getPowerDetail(@PathVariable Long powerId, Model model) {
    	power power = powerService.getPowerById(powerId);
        if (power == null) {
        	model.addAttribute("error", "해당 제품을 찾을 수 없습니다.");
            return "error"; // error.html 템플릿
        }
        // 가격 포맷팅 설정
        power.setFormattedPrice(String.format("%,d원", power.getPrice()));
        
        model.addAttribute("power", power);
        return "product/detail/power-details"; // templates/product/detail/power-details.html
    }
}
