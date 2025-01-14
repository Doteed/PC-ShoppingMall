package com.project.easyBuild.product.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.easyBuild.product.model.Case;
import com.project.easyBuild.product.model.hdd;
import com.project.easyBuild.product.repository.HddRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class HddService {
	@Autowired
	private final HddRepository hddRepository;
	
	// 생성자를 직접 작성
    public HddService(HddRepository hddRepository) {
        this.hddRepository = hddRepository;
    }
    // 전체 hdd 목록 가져오기
    public List<hdd> getAllHdds() {
    	return hddRepository.findAll();
    }
    
    public List<hdd> filterHdds(Map<String, List<String>> filters) {
        // 모든 데이터를 가져옴
        List<hdd> hdds = hddRepository.findAll();
        System.out.println("Initial HDDs: " + hdds); // 초기 데이터 확인
        if (filters.containsKey("manufacturer")) {
            System.out.println("Filtering by manufacturer: " + filters.get("manufacturer"));
        }
        // 출시일을 포맷팅하여 formattedReleaseDate에 저장
        hdds.forEach(hdd -> {
            if (hdd.getReleaseDate() != null) {
            	hdd.setFormattedReleaseDate(hdd.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

      

        // 제조사 필터
        if (filters.containsKey("manufacturer") && filters.get("manufacturer") instanceof List) {
            List<String> manufacturers = (List<String>) filters.get("manufacturer");
            hdds = hdds.stream()
                    .filter(hdd -> manufacturers.contains(hdd.getManufacturer()))
                    .collect(Collectors.toList());
        }
        //제품 분류 필터
        if (filters.containsKey("productClassification") && !filters.get("productClassification").isEmpty()) {
            List<String> productClassificationFilters = filters.get("productClassification");
            hdds = hdds.stream()
            		.filter(hdd -> productClassificationFilters.contains(hdd.getProductClassification()))
                    .collect(Collectors.toList());
        }
        //디스크 크기 필터
        if (filters.containsKey("diskSize") && !filters.get("diskSize").isEmpty()) {
            List<String> diskSizeFilters = filters.get("diskSize");
            hdds = hdds.stream()
            		.filter(hdd -> diskSizeFilters.contains(hdd.getDiskSize()))
                    .collect(Collectors.toList());
        }
        //디스크 용량 필터
        if (filters.containsKey("capacity") && !filters.get("capacity").isEmpty()) {
            List<String> capacityFilters = filters.get("capacity");
            hdds = hdds.stream()
            		.filter(hdd -> capacityFilters.contains(hdd.getCapacity()))
                    .collect(Collectors.toList());
        }
        
        // 탭 정렬 조건
        if (filters.containsKey("sort") && !filters.get("sort").isEmpty()) {
            String sort = filters.get("sort").get(0);
            switch (sort) {
                case "newest":
                	hdds.sort(Comparator.comparing(hdd::getReleaseDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
                case "low-price":
                	hdds.sort(Comparator.comparing(hdd::getPrice, Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "high-price":
                	hdds.sort(Comparator.comparing(hdd::getPrice, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
            }
        }        
        // 가격 포맷팅
        hdds.forEach(hdd -> hdd.setFormattedPrice(String.format("%,d원", hdd.getPrice())));
        System.out.println("Filtered HDDs: " + hdds); // 필터링 결과 확인
        return hdds;
               
    }
    public Optional<hdd> findById(Long id) {
        return hddRepository.findById(id);
    }
}
