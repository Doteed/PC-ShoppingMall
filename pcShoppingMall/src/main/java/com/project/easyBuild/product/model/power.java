package com.project.easyBuild.product.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "POWER")
public class power {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POWER")
    @SequenceGenerator(name = "SEQ_POWER", sequenceName = "SEQ_POWER", allocationSize = 1)
    @Column(name = "POWER_ID")
    private Long powerId; // PK

    @Column(name = "CATEGORY_ID", nullable = false)
    private Long categoryId; // 카테고리 ID

    @Column(name = "MANUFACTURER", nullable = false, length = 230)
    private String manufacturer; // 제조사

    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    private String productName; // 제품명

    @Column(name = "RATED_OUTPUT", nullable = false, length = 230)
    private String ratedOutput; // 정격 출력

    @Column(name = "EFFICIENCY_CERTIFICATION", nullable = false, length = 230)
    private String efficiencyCertification; // 80PLUS인증
    
    @Column(name = "PSU_STANDARD", nullable = false, length = 230)
    private String psuStandard; // 제품 분류
    
    @Column(name = "ETA_CERTIFICATION", length = 230)
    private String etaCertification; // ETA인증

    @Column(name = "PRICE", nullable = false)
    private Long price; // 가격

    @Column(name = "RELEASE_DATE", nullable = false)
    private LocalDate releaseDate; // 출시일

    @Transient
    private String formattedPrice; // 포맷된 가격 (비영속 필드)

    @Transient
    private String formattedReleaseDate; // 포맷된 출시일 (비영속 필드)

    // 기본 생성자
    public power() {}

    // 모든 필드를 포함한 생성자
    public power(Long powerId, Long categoryId, String manufacturer, String productName,
                  String ratedOutput, String efficiencyCertification, String psuStandard, String etaCertification, Long price, LocalDate releaseDate) {
        this.powerId = powerId;
        this.categoryId = categoryId;
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.ratedOutput = ratedOutput;
        this.efficiencyCertification = efficiencyCertification;
        this.psuStandard = psuStandard;
        this.etaCertification = etaCertification;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    // Getter, Setter
    public Long getPowerId() {
        return powerId;
    }

    public void setPowerId(Long powerId) {
        this.powerId = powerId;
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

    public String getRatedOutput() {
        return ratedOutput;
    }

    public void setRatedOutput(String ratedOutput) {
        this.ratedOutput = ratedOutput;
    }

    public String getEfficiencyCertification() {
        return efficiencyCertification;
    }

    public void setEfficiencyCertification(String efficiencyCertification) {
        this.efficiencyCertification = efficiencyCertification;
    }

    public String getPsuStandard() {
        return psuStandard;
    }

    public void setPsuStandard(String psuStandard) {
        this.psuStandard = psuStandard;
    }
    
    public String getEtaCertification() {
        return etaCertification;
    }

    public void setEtaCertification(String etaCertification) {
        this.etaCertification = etaCertification;
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
