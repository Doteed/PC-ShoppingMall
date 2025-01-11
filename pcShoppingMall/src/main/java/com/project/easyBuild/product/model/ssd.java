package com.project.easyBuild.product.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SSD")
public class ssd {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSD")
    @SequenceGenerator(name = "SEQ_SSD", sequenceName = "SEQ_SSD", allocationSize = 1)
    @Column(name = "SSD_ID")
    private Long ssdId; // PK

    @Column(name = "CATEGORY_ID", nullable = false)
    private Long categoryId; // 카테고리 ID

    @Column(name = "MANUFACTURER", nullable = false, length = 230)
    private String manufacturer; // 제조사

    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    private String productName; // 제품명

    @Column(name = "FORM_FACTOR", nullable = false, length = 230)
    private String formFactor; // 폼펙터

    @Column(name = "CAPACITY", nullable = false, length = 230)
    private String capacity; // 용량
    
    @Column(name = "INTERFACE", nullable = false, length = 230)
    private String interFace; // 인터페이스
    
    @Column(name = "PROTOCOL", nullable = false, length = 230)
    private String protocol; // 프로토콜

    @Column(name = "PRICE", nullable = false)
    private Long price; // 가격

    @Column(name = "RELEASE_DATE", nullable = false)
    private LocalDate releaseDate; // 출시일

    @Transient
    private String formattedPrice; // 포맷된 가격 (비영속 필드)

    @Transient
    private String formattedReleaseDate; // 포맷된 출시일 (비영속 필드)

    // 기본 생성자
    public ssd() {}

    // 모든 필드를 포함한 생성자
    public ssd(Long ssdId, Long categoryId, String manufacturer, String productName,
                  String formFactor, String capacity, String interFace, String protocol, Long price, LocalDate releaseDate) {
        this.ssdId = ssdId;
        this.categoryId = categoryId;
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.formFactor = formFactor;
        this.capacity = capacity;
        this.interFace = interFace;
        this.protocol = protocol;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    // Getter, Setter
    public Long getSsdId() {
        return ssdId;
    }

    public void setSsdId(Long ssdId) {
        this.ssdId = ssdId;
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

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getInterFace() {
        return interFace;
    }

    public void setInterFace(String interFace) {
        this.interFace = interFace;
    }
    
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
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
