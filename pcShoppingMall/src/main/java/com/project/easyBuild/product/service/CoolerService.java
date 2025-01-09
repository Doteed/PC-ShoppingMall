package com.project.easyBuild.product.service;

import org.springframework.stereotype.Service;

import com.project.easyBuild.product.model.cooler;
import com.project.easyBuild.product.repository.CoolerRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class CoolerService {
	private final CoolerRepository coolerRepository;
	
	// 생성자를 직접 작성
    public CoolerService(CoolerRepository coolerRepository) {
        this.coolerRepository = coolerRepository;
    }
    // 전체 COOLER 목록 가져오기
    public List<cooler> getAllCoolers() {
    	return coolerRepository.findAll();
    }
    
    public List<cooler> filterCoolers(Map<String, List<String>> filters) {
        // 모든 데이터를 가져옴
        List<cooler> coolers = coolerRepository.findAll();
        System.out.println("Initial COOLErs: " + coolers); // 초기 데이터 확인
        if (filters.containsKey("manufacturer")) {
            System.out.println("Filtering by manufacturer: " + filters.get("manufacturer"));
        }
        // 출시일을 포맷팅하여 formattedReleaseDate에 저장
        coolers.forEach(cooler -> {
            if (cooler.getReleaseDate() != null) {
                cooler.setFormattedReleaseDate(cooler.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

      

        // 제조사 필터
        if (filters.containsKey("manufacturer") && filters.get("manufacturer") instanceof List) {
            List<String> manufacturers = (List<String>) filters.get("manufacturer");
            coolers = coolers.stream()
                    .filter(cooler -> manufacturers.contains(cooler.getManufacturer()))
                    .collect(Collectors.toList());
        }
        //냉각방식 필터
        if (filters.containsKey("coolingMethod") && !filters.get("coolingMethod").isEmpty()) {
            List<String> coolingMethoodFilters = filters.get("coolingMethod");
            coolers = coolers.stream()
            		.filter(cooler -> coolingMethoodFilters.contains(cooler.getCoolingMethod()))
                    .collect(Collectors.toList());
        }
        //a/s기간 필터
        if (filters.containsKey("warrantyPeriod") && !filters.get("warrantyPeriod").isEmpty()) {
            List<String> warrantyPeriodFilters = filters.get("warrantyPeriod");
            coolers = coolers.stream()
            		.filter(cooler -> warrantyPeriodFilters.contains(cooler.getWarrantyPeriod()))
                    .collect(Collectors.toList());
        }
        
        // 탭 정렬 조건
        if (filters.containsKey("sort") && !filters.get("sort").isEmpty()) {
            String sort = filters.get("sort").get(0);
            switch (sort) {
                case "newest":
                    coolers.sort(Comparator.comparing(cooler::getReleaseDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
                case "low-price":
                    coolers.sort(Comparator.comparing(cooler::getPrice, Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "high-price":
                    coolers.sort(Comparator.comparing(cooler::getPrice, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
            }
        }        
        // 가격 포맷팅
        coolers.forEach(cooler -> cooler.setFormattedPrice(String.format("%,d원", cooler.getPrice())));
        System.out.println("Filtered COOLERs: " + coolers); // 필터링 결과 확인
        return coolers;
               
    }
    public cooler getCoolerById(Long coolerId) {
    	cooler result = coolerRepository.findById(coolerId).orElse(null);
        System.out.println("Loaded COOLER by ID " + coolerId + ": " + result); // 디버깅 로그
        return result;
    }
}
