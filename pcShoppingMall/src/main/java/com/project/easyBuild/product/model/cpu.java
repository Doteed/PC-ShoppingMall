package com.project.easyBuild.product.model;


import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CPU")
public class cpu {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CPU")
    @SequenceGenerator(name = "SEQ_CPU", sequenceName = "SEQ_CPU", allocationSize = 1)
    @Column(name = "CPU_ID")
    private Long cpuId;                              // PK

    @Column(name = "CATEGORY_ID", nullable = false)
    private Long categoryId;                         // 카테고리 아이디

    @Column(name = "MANUFACTURER", nullable = false)
    private String manufacturer;                     // 제조사

    @Column(name = "PRODUCT_NAME", nullable = false)
    private String productName;                      // 제품명
    
    @Column(name = "INTELTYPE", nullable = true)
    private String inteltype;                      // 인텔 타입
    
    @Column(name = "AMDTYPE", nullable = true)
    private String amdtype;                      // amd 타입

    @Column(name = "CORE_TYPES", nullable = false)
    private int coreTypes;                           // 코어 종류

    @Column(name = "SOCKET", nullable = false)
    private String socket;                           // 소켓

    @Column(name = "PRICE", nullable = false)
    private Long price;                              // 가격
    
    @Column(name = "RELEASE_DATE", nullable = false)
    private LocalDate releaseDate;
    @Transient
    private String formattedReleaseDate;
    @Transient
    private String formattedPrice;
    
    @Column(name = "SUPPORTED_MEMORY_STANDARD", nullable = false)
    private String supportedMemoryStandard;          // 메모리 규격

    // 기본 생성자
    public cpu() {
    }

    // 필드 전체를 받는 생성자(필요 시)
    public cpu(Long cpuId, Long categoryId, String manufacturer, String productName, 
               int coreTypes, String socket, Long price, String supportedMemoryStandard) {
        this.cpuId = cpuId;
        this.categoryId = categoryId;
        this.manufacturer = manufacturer;
        this.productName = productName;
        this.inteltype = inteltype;
        this.amdtype = amdtype;
        this.coreTypes = coreTypes;
        this.socket = socket;
        this.price = price;
        this.releaseDate = releaseDate;
        this.formattedReleaseDate = formattedReleaseDate;
        this.supportedMemoryStandard = supportedMemoryStandard;
    }

    // Getter, Setter
    public Long getCpuId() {
        return cpuId;
    }

    public void setCpuId(Long cpuId) {
        this.cpuId = cpuId;
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
    
    public String getIntelType() {
        return inteltype;
    }

    public void setIntelType(String inteltype) {
        this.inteltype = inteltype;
    }
    
    public String getAmdType() {
        return amdtype;
    }

    public void setAmdType(String amdtype) {
        this.amdtype = amdtype;
    }

    public int getCoreTypes() {
        return coreTypes;
    }

    public void setCoreTypes(int coreTypes) {
        this.coreTypes = coreTypes;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
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
    
    
    public String getFormattedReleaseDate() {
        return formattedReleaseDate;
    }

    public void setFormattedReleaseDate(String formattedReleaseDate) {
        this.formattedReleaseDate = formattedReleaseDate;
    }
    
    
    public String getFormattedPrice() {
    	return formattedPrice;
    }
    
    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public String getSupportedMemoryStandard() {
        return supportedMemoryStandard;
    }

    public void setSupportedMemoryStandard(String supportedMemoryStandard) {
        this.supportedMemoryStandard = supportedMemoryStandard;
    }
}
