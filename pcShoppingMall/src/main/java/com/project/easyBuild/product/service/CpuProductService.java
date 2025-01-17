package com.project.easyBuild.product.service;

import com.project.easyBuild.product.model.cpu;
import com.project.easyBuild.product.repository.CpuRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class CpuProductService {
	@Autowired
    private final CpuRepository cpuRepository;
    
    // 생성자를 직접 작성
    public CpuProductService(CpuRepository cpuRepository) {
        this.cpuRepository = cpuRepository;
    }

    // 전체 CPU 목록 가져오기
    public List<cpu> getAllCpus() {
    	List<cpu> cpus = cpuRepository.findAll();
    	System.out.println("Loaded CPUs: " + cpus); // 디버깅 로그
        return cpus;
    }
    
    public List<cpu> filterProducts(Map<String, List<String>> filters) {
        // 모든 데이터를 가져옴
        List<cpu> cpus = cpuRepository.findAll();
        System.out.println("Initial CPUs: " + cpus); // 초기 데이터 확인
        if (filters.containsKey("manufacturer")) {
            System.out.println("Filtering by manufacturer: " + filters.get("manufacturer"));
        }
        
     // 출시일을 포맷팅하여 formattedReleaseDate에 저장
        cpus.forEach(cpu -> {
            if (cpu.getReleaseDate() != null) {
                cpu.setFormattedReleaseDate(cpu.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

      

     // 제조사 필터
        if (filters.containsKey("manufacturer") && filters.get("manufacturer") instanceof List) {
            List<String> manufacturers = (List<String>) filters.get("manufacturer");
            System.out.println("Before manufacturer filter: " + cpus);
            cpus = cpus.stream()
                    .filter(cpu -> manufacturers.contains(cpu.getManufacturer()))
                    .collect(Collectors.toList());
            System.out.println("After manufacturer filter: " + cpus);
        }
        //인텔
        if (filters.containsKey("intelCpu") && !filters.get("intelCpu").isEmpty()) {
            List<String> intelFilters = filters.get("intelCpu");
            System.out.println("Before Intel filter: " + cpus);
            cpus = cpus.stream()
                    .filter(cpu -> cpu.getIntelType() != null && intelFilters.contains(cpu.getIntelType()))
                    .collect(Collectors.toList());
            System.out.println("After Intel filter: " + cpus);
        }
        //AMD
        if (filters.containsKey("amdCpu") && !filters.get("amdCpu").isEmpty()) {
            List<String> amdFilters = filters.get("amdCpu");
            System.out.println("Before AMD filter: " + cpus);
            cpus = cpus.stream()
                    .filter(cpu -> cpu.getAmdType() != null && amdFilters.contains(cpu.getAmdType()))
                    .collect(Collectors.toList());
            System.out.println("After AMD filter: " + cpus);
        }

        // 소켓 필터
        if (filters.containsKey("socket") && !filters.get("socket").isEmpty()) {
        	System.out.println("Before socket filter: " + cpus);
        	cpus = cpus.stream()
                    .filter(cpu -> filters.get("socket").contains(cpu.getSocket()))
                    .collect(Collectors.toList());
        	System.out.println("After socket filter: " + cpus);
        }
        
        // 탭 정렬 조건
        if (!cpus.isEmpty() && filters.containsKey("sort") && filters.get("sort") != null) {
            String sort = filters.get("sort").get(0);
            switch (sort) {
                case "newest":
                    cpus.sort(Comparator.comparing(cpu::getReleaseDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
                case "low-price":
                    cpus.sort(Comparator.comparing(cpu::getPrice, Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "high-price":
                    cpus.sort(Comparator.comparing(cpu::getPrice, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
            }
        }        
        // 가격 포맷팅
        cpus.forEach(cpu -> cpu.setFormattedPrice(String.format("%,d원", cpu.getPrice())));
        System.out.println("Filtered CPUs: " + cpus); // 필터링 결과 확인
        return cpus;
               
    }
    
    public Optional<cpu> findById(Long id) {
        return cpuRepository.findById(id);
    }
    
}
