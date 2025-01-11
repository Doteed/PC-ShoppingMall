package com.project.easyBuild.product.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "COOLER")
public class cooler {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COOLER")
    @SequenceGenerator(name = "SEQ_COOLER", sequenceName = "SEQ_COOLER", allocationSize = 1)
    @Column(name = "COOLER_ID")
    private Long coolerId; // PK

    @Column(name = "CATEGORY_ID", nullable = false)
    private Long categoryId; // 카테고리 ID

    @Column(name = "MANUFACTURER", nullable = false, length = 230)
    private String manufacturer; // 제조사

    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    private String productName; // 제품명

    @Column(name = "COOLING_METHOD", nullable = false, length = 230)
    private String coolingMethod; // 냉각 방식

    @Column(name = "WARRANTY_PERIOD", nullable = false, length = 230)
    private String warrantyPeriod; // 보증 기간

    @Column(name = "PRICE", nullable = false)
    private Long price; // 가격

    @Column(name = "RELEASE_DATE", nullable = false)
    private LocalDate releaseDate; // 출시일

    @Transient
    private String formattedPrice; // 포맷된 가격 (비영속 필드)

    @Transient
    private String formattedReleaseDate; // 포맷된 출시일 (비영속 필드)

    // 기본 생성자
    public cooler() {}

    // 모든 필드를 포함한 생성자
    public cooler(Long coolerId, Long categoryId, String manufacturer, String productName,
                  String coolingMethod, String warrantyPeriod, Long price, LocalDate releaseDate) {
        this.coolerId = coolerId;
        this.categoryId = categoryId;
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.coolingMethod = coolingMethod;
        this.warrantyPeriod = warrantyPeriod;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    // Getter, Setter
    public Long getCoolerId() {
        return coolerId;
    }

    public void setCoolerId(Long coolerId) {
        this.coolerId = coolerId;
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

    public String getCoolingMethod() {
        return coolingMethod;
    }

    public void setCoolingMethod(String coolingMethod) {
        this.coolingMethod = coolingMethod;
    }

    public String getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(String warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
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
