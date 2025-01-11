package com.project.easyBuild.product.service;

import org.springframework.stereotype.Service;

import com.project.easyBuild.product.model.mainboard;
import com.project.easyBuild.product.repository.MainboardRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Service
public class MainboardService {
	private final MainboardRepository mainboardRepository;
	
	// 생성자를 직접 작성
    public MainboardService(MainboardRepository mainboardRepository) {
        this.mainboardRepository = mainboardRepository;
    }
    // 전체 MAINBOARD 목록 가져오기
    public List<mainboard> getAllMainboards() {
    	return mainboardRepository.findAll();
    }
    
    public List<mainboard> filterMainboards(Map<String, List<String>> filters) {
        // 모든 데이터를 가져옴
        List<mainboard> mainboards = mainboardRepository.findAll();
        System.out.println("Initial MAINBOARDs: " + mainboards); // 초기 데이터 확인
        if (filters.containsKey("manufacturer")) {
            System.out.println("Filtering by manufacturer: " + filters.get("manufacturer"));
        }
        // 출시일을 포맷팅하여 formattedReleaseDate에 저장
        mainboards.forEach(mainboard -> {
            if (mainboard.getReleaseDate() != null) {
            	mainboard.setFormattedReleaseDate(mainboard.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

      

        // 제조사 필터
        if (filters.containsKey("manufacturer") && filters.get("manufacturer") instanceof List) {
            List<String> manufacturers = (List<String>) filters.get("manufacturer");
            mainboards = mainboards.stream()
                    .filter(mainboard -> manufacturers.contains(mainboard.getManufacturer()))
                    .collect(Collectors.toList());
        }
        //제품 분류 필터
        if (filters.containsKey("productClassification") && !filters.get("productClassification").isEmpty()) {
            List<String> productClassificationFilters = filters.get("productClassification");
            mainboards = mainboards.stream()
            		.filter(mainboard -> productClassificationFilters.contains(mainboard.getProductClassification()))
                    .collect(Collectors.toList());
        }
        //CPU 소켓 필터
        if (filters.containsKey("socket") && !filters.get("socket").isEmpty()) {
            List<String> socketFilters = filters.get("socket");
            mainboards = mainboards.stream()
            		.filter(mainboard -> socketFilters.contains(mainboard.getSocket()))
                    .collect(Collectors.toList());
        }
        //세부 칩셋 필터
        if (filters.containsKey("detailedChipset") && !filters.get("detailedChipset").isEmpty()) {
            List<String> detailedChipsetFilters = filters.get("detailedChipset");
            mainboards = mainboards.stream()
            		.filter(mainboard -> detailedChipsetFilters.contains(mainboard.getDetailedChipset()))
                    .collect(Collectors.toList());
        }
        //폼펙터 필터
        if (filters.containsKey("formFactor") && !filters.get("formFactor").isEmpty()) {
            List<String> formFactorFilters = filters.get("formFactor");
            mainboards = mainboards.stream()
            		.filter(mainboard -> formFactorFilters.contains(mainboard.getFormFactor()))
                    .collect(Collectors.toList());
        }
        //Vcore 출력합계 필터
        if (filters.containsKey("vcoreOutputTotal") && !filters.get("vcoreOutputTotal").isEmpty()) {
            List<String> vcoreOutputTotalFilters = filters.get("vcoreOutputTotal");
            mainboards = mainboards.stream()
            		.filter(mainboard -> vcoreOutputTotalFilters.contains(mainboard.getVcoreOutputTotal()))
                    .collect(Collectors.toList());
        }
        
        // 탭 정렬 조건
        if (filters.containsKey("sort") && !filters.get("sort").isEmpty()) {
            String sort = filters.get("sort").get(0);
            switch (sort) {
                case "newest":
                	mainboards.sort(Comparator.comparing(mainboard::getReleaseDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
                case "low-price":
                	mainboards.sort(Comparator.comparing(mainboard::getPrice, Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "high-price":
                	mainboards.sort(Comparator.comparing(mainboard::getPrice, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
            }
        }        
        // 가격 포맷팅
        mainboards.forEach(mainboard -> mainboard.setFormattedPrice(String.format("%,d원", mainboard.getPrice())));
        System.out.println("Filtered MAINBOARDs: " + mainboards); // 필터링 결과 확인
        return mainboards;
               
    }
    public mainboard getMainboardById(Long mainboardId) {
    	mainboard result = mainboardRepository.findById(mainboardId).orElse(null);
        System.out.println("Loaded MAINBOARD by ID " + mainboardId + ": " + result); // 디버깅 로그
        return result;
    }
}
