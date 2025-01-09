package com.project.easyBuild.product.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CASE")
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CASE")
    @SequenceGenerator(name = "SEQ_CASE", sequenceName = "SEQ_CASE", allocationSize = 1)
    @Column(name = "CASE")
    private Long caseId; // PK

    @Column(name = "CATEGORY_ID", nullable = false)
    private Long categoryId; // 카테고리 ID

    @Column(name = "MANUFACTURER", nullable = false, length = 230)
    private String manufacturer; // 제조사

    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    private String productName; // 제품명

    @Column(name = "PRODUCT_CLASSIFICATION", nullable = false, length = 230)
    private String productClassification; // 제품 분류

    @Column(name = "CASE_SIZE", nullable = false, length = 230)
    private String caseSize; // 케이스 크기
    
    @Column(name = "POWER_SUPPLY_STANDARD", nullable = false, length = 230)
    private String powerSupplyStandard; // 지원 파워 규격
    
    @Column(name = "SUPPORTED_BOARD_STANDARD", nullable = false, length = 230)
    private String supportedBoardStandard; // 지원 보드 규격
    
    @Column(name = "BAY_13_3CM", nullable = false, length = 230)
    private String bay13_3CM; // 13_3CM_BAY
    
    @Column(name = "BAY_8_9CM", nullable = false, length = 230)
    private String bay8_9CM; // 8_9CM_BAY
    
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
    public Case() {}

    // 모든 필드를 포함한 생성자
    public Case(Long caseId, Long categoryId, String manufacturer, String productName,
                  String productClassification, String caseSize, String powerSupplyStandard, String supportedBoardStandard,
                  String bay13_3CM, String bay8_9CM, Long vgaLength, Long price, LocalDate releaseDate) {
        this.caseId = caseId;
        this.categoryId = categoryId;
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.productClassification = productClassification;
        this.caseSize = caseSize;
        this.powerSupplyStandard = powerSupplyStandard;
        this.supportedBoardStandard = supportedBoardStandard;
        this.bay13_3CM = bay13_3CM;
        this.bay8_9CM = bay8_9CM;
        this.vgaLength = vgaLength;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    // Getter, Setter
    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
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

    public String getProductClassification() {
        return productClassification;
    }

    public void setProductClassification(String productClassification) {
        this.productClassification = productClassification;
    }

    public String getCaseSize() {
        return caseSize;
    }

    public void setCaseSize(String caseSize) {
        this.caseSize = caseSize;
    }

    public String getPowerSupplyStandard() {
        return powerSupplyStandard;
    }

    public void setPowerSupplyStandard(String powerSupplyStandard) {
        this.powerSupplyStandard = powerSupplyStandard;
    }
    
    public String getSupportedBoardStandard() {
        return supportedBoardStandard;
    }

    public void setSupportedBoardStandard(String supportedBoardStandard) {
        this.supportedBoardStandard = supportedBoardStandard;
    }
    
    public String getBay13_3CM() {
        return bay13_3CM;
    }

    public void setBay13_3CM(String bay13_3CM) {
        this.bay13_3CM = bay13_3CM;
    }
    
    public String getBay8_9CM() {
        return bay8_9CM;
    }

    public void setBay8_9CM(String bay8_9CM) {
        this.bay8_9CM = bay8_9CM;
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
