package com.project.easyBuild.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.easyBuild.product.model.Case;
import com.project.easyBuild.product.repository.CaseRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class CaseService {
	@Autowired
	private final CaseRepository caseRepository;
	
	// 생성자를 직접 작성
    public CaseService(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }
    // 전체 case 목록 가져오기
    public List<Case> getAllCases() {
    	return caseRepository.findAll();
    }
    
    public List<Case> filterCases(Map<String, List<String>> filters) {
        // 모든 데이터를 가져옴
        List<Case> cases = caseRepository.findAll();
        System.out.println("Initial CASEs: " + cases); // 초기 데이터 확인
        if (filters.containsKey("manufacturer")) {
            System.out.println("Filtering by manufacturer: " + filters.get("manufacturer"));
        }
        // 출시일을 포맷팅하여 formattedReleaseDate에 저장
        cases.forEach(Case -> {
            if (Case.getReleaseDate() != null) {
            	Case.setFormattedReleaseDate(Case.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

      

        // 제조사 필터
        if (filters.containsKey("manufacturer") && filters.get("manufacturer") instanceof List) {
            List<String> manufacturers = (List<String>) filters.get("manufacturer");
            cases = cases.stream()
                    .filter(Case -> manufacturers.contains(Case.getManufacturer()))
                    .collect(Collectors.toList());
        }
        //제품 분류 필터
        if (filters.containsKey("productClassification") && !filters.get("productClassification").isEmpty()) {
            List<String> productClassificationFilters = filters.get("productClassification");
            cases = cases.stream()
            		.filter(Case -> productClassificationFilters.contains(Case.getProductClassification()))
                    .collect(Collectors.toList());
        }
        //케이스 크기 필터
        if (filters.containsKey("caseSize") && !filters.get("caseSize").isEmpty()) {
            List<String> caseSizeFilters = filters.get("caseSize");
            cases = cases.stream()
            		.filter(Case -> caseSizeFilters.contains(Case.getCaseSize()))
                    .collect(Collectors.toList());
        }
        //13.3cm베이 필터
        if (filters.containsKey("bay13_3CM") && !filters.get("bay13_3CM").isEmpty()) {
            List<String> bay13_3CMFilters = filters.get("bay13_3CM");
            cases = cases.stream()
            		.filter(Case -> bay13_3CMFilters.contains(Case.getBay13_3CM()))
                    .collect(Collectors.toList());
        }
        //8.9cm베이 필터
        if (filters.containsKey("bay8_9CM") && !filters.get("bay8_9CM").isEmpty()) {
            List<String> bay8_9CMFilters = filters.get("bay8_9CM");
            cases = cases.stream()
            		.filter(Case -> bay8_9CMFilters.contains(Case.getBay8_9CM()))
                    .collect(Collectors.toList());
        }
        
        // 탭 정렬 조건
        if (filters.containsKey("sort") && !filters.get("sort").isEmpty()) {
            String sort = filters.get("sort").get(0);
            switch (sort) {
                case "newest":
                	cases.sort(Comparator.comparing(Case::getReleaseDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
                case "low-price":
                	cases.sort(Comparator.comparing(Case::getPrice, Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "high-price":
                	cases.sort(Comparator.comparing(Case::getPrice, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
            }
        }        
        // 가격 포맷팅
        cases.forEach(Case -> Case.setFormattedPrice(String.format("%,d원", Case.getPrice())));
        System.out.println("Filtered Cases: " + cases); // 필터링 결과 확인
        return cases;
               
    }
    public Optional<Case> findById(Long id) {
        return caseRepository.findById(id);
    }
}
