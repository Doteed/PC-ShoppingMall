package com.project.easyBuild.product.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "HDD")
public class hdd {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HDD")
    @SequenceGenerator(name = "SEQ_HDD", sequenceName = "SEQ_HDD", allocationSize = 1)
    @Column(name = "HDD_ID")
    private Long hddId; // PK

    @Column(name = "CATEGORY_ID", nullable = false)
    private Long categoryId; // 카테고리 ID

    @Column(name = "MANUFACTURER", nullable = false, length = 230)
    private String manufacturer; // 제조사

    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    private String productName; // 제품명

    @Column(name = "PRODUCT_CLASSIFICATION", nullable = false, length = 230)
    private String productClassification; // 제품 분류

    @Column(name = "DISK_SIZE", nullable = false, length = 230)
    private String diskSize; // 디스크 크기
    
    @Column(name = "CAPACITY", nullable = false, length = 230)
    private String capacity; // 디스크 용량
    
    @Column(name = "INTERFACE", nullable = false, length = 230)
    private String interFace; // 인터페이스

    @Column(name = "PRICE", nullable = false)
    private Long price; // 가격

    @Column(name = "RELEASE_DATE", nullable = false)
    private LocalDate releaseDate; // 출시일

    @Transient
    private String formattedPrice; // 포맷된 가격 (비영속 필드)

    @Transient
    private String formattedReleaseDate; // 포맷된 출시일 (비영속 필드)

    // 기본 생성자
    public hdd() {}

    // 모든 필드를 포함한 생성자
    public hdd(Long hddId, Long categoryId, String manufacturer, String productName,
                  String productClassification, String diskSize, String capacity, String interFace, Long price, LocalDate releaseDate) {
        this.hddId = hddId;
        this.categoryId = categoryId;
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.productClassification = productClassification;
        this.diskSize = diskSize;
        this.capacity = capacity;
        this.interFace = interFace;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    // Getter, Setter
    public Long getHddId() {
        return hddId;
    }

    public void setHddId(Long hddId) {
        this.hddId = hddId;
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

    public String getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(String diskSize) {
        this.diskSize = diskSize;
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
