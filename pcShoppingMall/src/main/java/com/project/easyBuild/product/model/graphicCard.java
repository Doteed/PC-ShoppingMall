package com.project.easyBuild.product.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GRAPHIC_CARD")
public class graphicCard {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GRAPHIC_CARD")
    @SequenceGenerator(name = "SEQ_GRAPHIC_CARD", sequenceName = "SEQ_GRAPHIC_CARD", allocationSize = 1)
    @Column(name = "GRAPHIC_CARD_ID")
    private Long graphicCardId; // PK

    @Column(name = "CATEGORY_ID", nullable = false)
    private Long categoryId; // 카테고리 ID
    
    @Column(name = "MANUFACTURER", nullable = false, length = 230)
    private String manufacturer; // 제조사

    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    private String productName; // 제품명
    
    @Column(name = "CHIPSET_MANUFACTURER", nullable = false, length = 230)
    private String chipsetManufacturer; // 칩셋 제조사

    @Column(name = "CHIPSET", nullable = false, length = 230)
    private String chipset; // 칩셋

    @Column(name = "VGA_LENGTH", nullable = false)
    private Long vgaLength; // VGA 길이
    
    @Column(name = "PRICE", nullable = false)
    private Long price; // 가격

    @Column(name = "RELEASE_DATE", nullable = false)
    private LocalDate releaseDate; // 출시일

    @Transient
    private String formattedPrice; // 포맷된 가격 (비영속 필드)

    @Transient
    private String formattedReleaseDate; // 포맷된 출시일 (비영속 필드)

    // 기본 생성자
    public graphicCard() {}

    // 모든 필드를 포함한 생성자
    public graphicCard(Long graphicCardId, Long categoryId, String manufacturer, String productName,
    		String chipsetManufacturer, String chipset, Long vgaLength, Long price, LocalDate releaseDate) {
        this.graphicCardId = graphicCardId;
        this.categoryId = categoryId;
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.chipsetManufacturer = chipsetManufacturer;
        this.chipset = chipset;
        this.vgaLength = vgaLength;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    // Getter, Setter
    public Long getGraphicCardId() {
        return graphicCardId;
    }

    public void setGraphicCardId(Long graphicCardId) {
        this.graphicCardId = graphicCardId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getChipsetManufacturer() {
        return chipsetManufacturer;
    }

    public void setChipsetManufacturer(String chipsetManufacturer) {
        this.chipsetManufacturer = chipsetManufacturer;
    }

    public String getChipset() {
        return chipset;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    public Long getVgaLength() {
        return vgaLength;
    }

    public void setVgaLength(Long vgaLength) {
        this.vgaLength = vgaLength;
    }
    
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public String getFormattedReleaseDate() {
        return formattedReleaseDate;
    }

    public void setFormattedReleaseDate(String formattedReleaseDate) {
        this.formattedReleaseDate = formattedReleaseDate;
    }
}
