package com.project.easyBuild.product.service;
import org.springframework.stereotype.Service;

import com.project.easyBuild.product.model.power;
import com.project.easyBuild.product.repository.PowerRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class PowerService {
	private final PowerRepository powerRepository;
	
	// 생성자를 직접 작성
    public PowerService(PowerRepository powerRepository) {
        this.powerRepository = powerRepository;
    }
    // 전체 power 목록 가져오기
    public List<power> getAllPowers() {
    	return powerRepository.findAll();
    }
    
    public List<power> filterPowers(Map<String, List<String>> filters) {
        // 모든 데이터를 가져옴
        List<power> powers = powerRepository.findAll();
        System.out.println("Initial POWERs: " + powers); // 초기 데이터 확인
        if (filters.containsKey("manufacturer")) {
            System.out.println("Filtering by manufacturer: " + filters.get("manufacturer"));
        }
        // 출시일을 포맷팅하여 formattedReleaseDate에 저장
        powers.forEach(power -> {
            if (power.getReleaseDate() != null) {
            	power.setFormattedReleaseDate(power.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

      

        // 제조사 필터
        if (filters.containsKey("manufacturer") && filters.get("manufacturer") instanceof List) {
            List<String> manufacturers = (List<String>) filters.get("manufacturer");
            powers = powers.stream()
                    .filter(power -> manufacturers.contains(power.getManufacturer()))
                    .collect(Collectors.toList());
        }
        //제품 분류 필터
        if (filters.containsKey("psuStandard") && !filters.get("psuStandard").isEmpty()) {
            List<String> psuStandardFilters = filters.get("psuStandard");
            powers = powers.stream()
            		.filter(power -> psuStandardFilters.contains(power.getPsuStandard()))
                    .collect(Collectors.toList());
        }
        //정격 출력 필터
        if (filters.containsKey("ratedOutput") && !filters.get("ratedOutput").isEmpty()) {
            List<String> ratedOutputFilters = filters.get("ratedOutput");
            powers = powers.stream()
            		.filter(power -> ratedOutputFilters.contains(power.getRatedOutput()))
                    .collect(Collectors.toList());
        }
        //80PLUS인증 필터
        if (filters.containsKey("efficiencyCertification") && !filters.get("efficiencyCertification").isEmpty()) {
            List<String> efficiencyCertificationFilters = filters.get("efficiencyCertification");
            powers = powers.stream()
            		.filter(power -> efficiencyCertificationFilters.contains(power.getEfficiencyCertification()))
                    .collect(Collectors.toList());
        }
        //ETA인증 필터
        if (filters.containsKey("etaCertification") && !filters.get("etaCertification").isEmpty()) {
            List<String> etaCertificationFilters = filters.get("etaCertification");
            powers = powers.stream()
            		.filter(power -> etaCertificationFilters.contains(power.getEtaCertification()))
                    .collect(Collectors.toList());
        }
        
        // 탭 정렬 조건
        if (filters.containsKey("sort") && !filters.get("sort").isEmpty()) {
            String sort = filters.get("sort").get(0);
            switch (sort) {
                case "newest":
                	powers.sort(Comparator.comparing(power::getReleaseDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
                case "low-price":
                	powers.sort(Comparator.comparing(power::getPrice, Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "high-price":
                	powers.sort(Comparator.comparing(power::getPrice, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
            }
        }        
        // 가격 포맷팅
        powers.forEach(power -> power.setFormattedPrice(String.format("%,d원", power.getPrice())));
        System.out.println("Filtered POWERs: " + powers); // 필터링 결과 확인
        return powers;
               
    }
    public power getPowerById(Long powerId) {
    	power result = powerRepository.findById(powerId).orElse(null);
        System.out.println("Loaded POWER by ID " + powerId + ": " + result); // 디버깅 로그
        return result;
    }
}
