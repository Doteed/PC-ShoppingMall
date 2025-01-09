package com.project.easyBuild.product.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MAINBOARD")
public class mainboard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MAINBOARD")
    @SequenceGenerator(name = "SEQ_MAINBOARD", sequenceName = "SEQ_MAINBOARD", allocationSize = 1)
    @Column(name = "MAINBOARD_ID")
    private Long mainboardId; // PK

    @Column(name = "CATEGORY_ID", nullable = false)
    private Long categoryId; // 카테고리 ID

    @Column(name = "PRODUCT_CLASSIFICATION", nullable = false, length = 230)
    private String productClassification; // 제품 분류
    
    @Column(name = "MANUFACTURER", nullable = false, length = 230)
    private String manufacturer; // 제조사

    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    private String productName; // 제품명

    @Column(name = "SOCKET", nullable = false, length = 230)
    private String socket; // 소켓

    @Column(name = "DETAILED_CHIPSET", nullable = false, length = 230)
    private String detailedChipset; // 세부 칩셋

    @Column(name = "FORM_FACTOR", nullable = false, length = 230)
    private String formFactor; // 폼펙터
    
    @Column(name = "VCORE_OUTPUT_TOTAL", nullable = false, length = 230)
    private String vcoreOutputTotal; // vcore 출력합계
    
    @Column(name = "SUPPORTED_MEMORY_STANDARD", nullable = false, length = 50)
    private String supportedMemoryStandard; // 지원 메모리 규격
    
    @Column(name = "PRICE", nullable = false)
    private Long price; // 가격

    @Column(name = "RELEASE_DATE", nullable = false)
    private LocalDate releaseDate; // 출시일

    @Transient
    private String formattedPrice; // 포맷된 가격 (비영속 필드)

    @Transient
    private String formattedReleaseDate; // 포맷된 출시일 (비영속 필드)

    // 기본 생성자
    public mainboard() {}

    // 모든 필드를 포함한 생성자
    public mainboard(Long mainboardId, Long categoryId, String productclassification, String manufacturer, String productName,
    		String socket, String detailedchipset, String formfactor, String vcoreoutputtotal, String supportedmemorystandard,
    		Long price, LocalDate releaseDate) {
        this.mainboardId = mainboardId;
        this.categoryId = categoryId;
        this.productClassification = productClassification;
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.socket = socket;
        this.detailedChipset = detailedChipset;
        this.formFactor = formFactor;
        this.vcoreOutputTotal = vcoreOutputTotal;
        this.supportedMemoryStandard = supportedMemoryStandard;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    // Getter, Setter
    public Long getMainboardId() {
        return mainboardId;
    }

    public void setMainboardId(Long mainboardId) {
        this.mainboardId = mainboardId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductClassification() {
    	return productClassification;
    }
    
    public void setProductClassification(String productClassification) {
    	this.productClassification = productClassification;
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

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public String getDetailedChipset() {
        return detailedChipset;
    }

    public void setDetailedChipset(String detailedChipset) {
        this.detailedChipset = detailedChipset;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public String getVcoreOutputTotal() {
        return vcoreOutputTotal;
    }

    public void setVcoreOutputTotal(String vcoreOutputTotal) {
        this.vcoreOutputTotal = vcoreOutputTotal;
    }
    
    public String getSupportedMemoryStandard() {
        return supportedMemoryStandard;
    }

    public void setSupportedMemoryStandard(String supportedMemoryStandard) {
        this.supportedMemoryStandard = supportedMemoryStandard;
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
