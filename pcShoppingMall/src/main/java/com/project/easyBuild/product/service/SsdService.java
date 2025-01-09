package com.project.easyBuild.product.service;

import org.springframework.stereotype.Service;

import com.project.easyBuild.product.model.ssd;
import com.project.easyBuild.product.repository.SsdRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class SsdService {
	private final SsdRepository ssdRepository;
	
	// 생성자를 직접 작성
    public SsdService(SsdRepository ssdRepository) {
        this.ssdRepository = ssdRepository;
    }
    // 전체 SSD 목록 가져오기
    public List<ssd> getAllSsds() {
    	return ssdRepository.findAll();
    }
    
    public List<ssd> filterSsds(Map<String, List<String>> filters) {
        // 모든 데이터를 가져옴
        List<ssd> ssds = ssdRepository.findAll();
        System.out.println("Initial SSDs: " + ssds); // 초기 데이터 확인
        if (filters.containsKey("manufacturer")) {
            System.out.println("Filtering by manufacturer: " + filters.get("manufacturer"));
        }
        // 출시일을 포맷팅하여 formattedReleaseDate에 저장
        ssds.forEach(ssd -> {
            if (ssd.getReleaseDate() != null) {
            	ssd.setFormattedReleaseDate(ssd.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

      

        // 제조사 필터
        if (filters.containsKey("manufacturer") && filters.get("manufacturer") instanceof List) {
            List<String> manufacturers = (List<String>) filters.get("manufacturer");
            ssds = ssds.stream()
                    .filter(ssd -> manufacturers.contains(ssd.getManufacturer()))
                    .collect(Collectors.toList());
        }
        //폼펙터 필터
        if (filters.containsKey("formFactor") && !filters.get("formFactor").isEmpty()) {
            List<String> formFactorFilters = filters.get("formFactor");
            ssds = ssds.stream()
            		.filter(ssd -> formFactorFilters.contains(ssd.getFormFactor()))
                    .collect(Collectors.toList());
        }
        //용량 필터
        if (filters.containsKey("capacity") && !filters.get("capacity").isEmpty()) {
            List<String> capacityFilters = filters.get("capacity");
            ssds = ssds.stream()
            		.filter(ssd -> capacityFilters.contains(ssd.getCapacity()))
                    .collect(Collectors.toList());
        }
        
        // 탭 정렬 조건
        if (filters.containsKey("sort") && !filters.get("sort").isEmpty()) {
            String sort = filters.get("sort").get(0);
            switch (sort) {
                case "newest":
                	ssds.sort(Comparator.comparing(ssd::getReleaseDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
                case "low-price":
                	ssds.sort(Comparator.comparing(ssd::getPrice, Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "high-price":
                	ssds.sort(Comparator.comparing(ssd::getPrice, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
            }
        }        
        // 가격 포맷팅
        ssds.forEach(ssd -> ssd.setFormattedPrice(String.format("%,d원", ssd.getPrice())));
        System.out.println("Filtered SSDs: " + ssds); // 필터링 결과 확인
        return ssds;
               
    }
    public ssd getSsdById(Long ssdId) {
    	ssd result = ssdRepository.findById(ssdId).orElse(null);
        System.out.println("Loaded SSD by ID " + ssdId + ": " + result); // 디버깅 로그
        return result;
    }
}
