package com.project.easyBuild.product.service;

import org.springframework.stereotype.Service;

import com.project.easyBuild.product.model.memory;
import com.project.easyBuild.product.repository.MemoryRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class MemoryService {
	private final MemoryRepository memoryRepository;
	
	// 생성자를 직접 작성
    public MemoryService(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }
    // 전체 MEMORY 목록 가져오기
    public List<memory> getAllMemorys() {
    	return memoryRepository.findAll();
    }
    
    public List<memory> filterMemorys(Map<String, List<String>> filters) {
        // 모든 데이터를 가져옴
        List<memory> memorys = memoryRepository.findAll();
        System.out.println("Initial MEMORYs: " + memorys); // 초기 데이터 확인
        if (filters.containsKey("manufacturer")) {
            System.out.println("Filtering by manufacturer: " + filters.get("manufacturer"));
        }
        // 출시일을 포맷팅하여 formattedReleaseDate에 저장
        memorys.forEach(memory -> {
            if (memory.getReleaseDate() != null) {
            	memory.setFormattedReleaseDate(memory.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

      

        // 제조사 필터
        if (filters.containsKey("manufacturer") && filters.get("manufacturer") instanceof List) {
            List<String> manufacturers = (List<String>) filters.get("manufacturer");
            memorys = memorys.stream()
                    .filter(memory -> manufacturers.contains(memory.getManufacturer()))
                    .collect(Collectors.toList());
        }
        //제품 분류 필터
        if (filters.containsKey("productClassification") && !filters.get("productClassification").isEmpty()) {
            List<String> productClassificationFilters = filters.get("productClassification");
            memorys = memorys.stream()
            		.filter(memory -> productClassificationFilters.contains(memory.getProductClassification()))
                    .collect(Collectors.toList());
        }
        //메모리 용량 필터
        if (filters.containsKey("capacity") && !filters.get("capacity").isEmpty()) {
            List<String> capacityFilters = filters.get("capacity");
            memorys = memorys.stream()
            		.filter(memory -> capacityFilters.contains(memory.getCapacity()))
                    .collect(Collectors.toList());
        }
        //동작클럭 필터
        if (filters.containsKey("operatingClock") && !filters.get("operatingClock").isEmpty()) {
            List<String> operatingClockFilters = filters.get("operatingClock");
            memorys = memorys.stream()
            		.filter(memory -> operatingClockFilters.contains(memory.getOperatingClock()))
                    .collect(Collectors.toList());
        }
        
        // 탭 정렬 조건
        if (filters.containsKey("sort") && !filters.get("sort").isEmpty()) {
            String sort = filters.get("sort").get(0);
            switch (sort) {
                case "newest":
                	memorys.sort(Comparator.comparing(memory::getReleaseDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
                case "low-price":
                	memorys.sort(Comparator.comparing(memory::getPrice, Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "high-price":
                	memorys.sort(Comparator.comparing(memory::getPrice, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
            }
        }        
        // 가격 포맷팅
        memorys.forEach(memory -> memory.setFormattedPrice(String.format("%,d원", memory.getPrice())));
        System.out.println("Filtered MEMORYs: " + memorys); // 필터링 결과 확인
        return memorys;
               
    }
    public memory getMemoryById(Long memoryId) {
    	memory result = memoryRepository.findById(memoryId).orElse(null);
        System.out.println("Loaded MEMORY by ID " + memoryId + ": " + result); // 디버깅 로그
        return result;
    }
}
