package com.project.easyBuild.product.service;

import org.springframework.stereotype.Service;

import com.project.easyBuild.product.model.graphicCard;
import com.project.easyBuild.product.repository.GraphicCardRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class GraphicCardService {
	
	private final GraphicCardRepository graphicCardRepository;
	
	// 생성자를 직접 작성
    public GraphicCardService(GraphicCardRepository graphicCardRepository) {
        this.graphicCardRepository = graphicCardRepository;
    }
    // 전체 GRAPHIC_CARD 목록 가져오기
    public List<graphicCard> getAllGraphicCards() {
    	return graphicCardRepository.findAll();
    }
    
    public List<graphicCard> filterGraphicCards(Map<String, List<String>> filters) {
        // 모든 데이터를 가져옴
        List<graphicCard> graphicCards = graphicCardRepository.findAll();
        System.out.println("Initial GRAPHIC_CARDs: " + graphicCards); // 초기 데이터 확인
        if (filters.containsKey("manufacturer")) {
            System.out.println("Filtering by manufacturer: " + filters.get("manufacturer"));
        }
        // 출시일을 포맷팅하여 formattedReleaseDate에 저장
        graphicCards.forEach(graphicCard -> {
            if (graphicCard.getReleaseDate() != null) {
            	graphicCard.setFormattedReleaseDate(graphicCard.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });

      

        // 제조사 필터
        if (filters.containsKey("manufacturer") && filters.get("manufacturer") instanceof List) {
            List<String> manufacturers = (List<String>) filters.get("manufacturer");
            graphicCards = graphicCards.stream()
                    .filter(graphicCard -> manufacturers.contains(graphicCard.getManufacturer()))
                    .collect(Collectors.toList());
        }
        //칩셋 제조사 필터
        if (filters.containsKey("chipsetManufacturer") && !filters.get("chipsetManufacturer").isEmpty()) {
            List<String> chipsetManufacturerFilters = filters.get("chipsetManufacturer");
            graphicCards = graphicCards.stream()
            		.filter(graphicCard -> chipsetManufacturerFilters.contains(graphicCard.getChipsetManufacturer()))
                    .collect(Collectors.toList());
        }
        //NVIDIA/AMD/인텔 칩셋 필터
        if (filters.containsKey("chipset") && !filters.get("chipset").isEmpty()) {
            List<String> chipsetFilters = filters.get("chipset");
            graphicCards = graphicCards.stream()
            		.filter(graphicCard -> chipsetFilters.contains(graphicCard.getChipset()))
                    .collect(Collectors.toList());
        }
        
        
        // 탭 정렬 조건
        if (filters.containsKey("sort") && !filters.get("sort").isEmpty()) {
            String sort = filters.get("sort").get(0);
            switch (sort) {
                case "newest":
                	graphicCards.sort(Comparator.comparing(graphicCard::getReleaseDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
                case "low-price":
                	graphicCards.sort(Comparator.comparing(graphicCard::getPrice, Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "high-price":
                	graphicCards.sort(Comparator.comparing(graphicCard::getPrice, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
            }
        }        
        // 가격 포맷팅
        graphicCards.forEach(graphicCard -> graphicCard.setFormattedPrice(String.format("%,d원", graphicCard.getPrice())));
        System.out.println("Filtered GRAPHIC_CARDs: " + graphicCards); // 필터링 결과 확인
        return graphicCards;
               
    }
    public graphicCard getGraphicCardById(Long graphicCardId) {
    	graphicCard result = graphicCardRepository.findById(graphicCardId).orElse(null);
        System.out.println("Loaded GRAPHIC_CARD by ID " + graphicCardId + ": " + result); // 디버깅 로그
        return result;
    }
}
