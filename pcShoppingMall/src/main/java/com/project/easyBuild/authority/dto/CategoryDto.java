package com.project.easyBuild.authority.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) // JSON에 없는 필드는 무시
public class CategoryDto {

    @JsonProperty("categoryId") // JSON 필드 매핑
    private Long categoryId;

    @JsonProperty("parentId")
    private Long parentId;

    @JsonProperty("categoryName")
    private String categoryName;

    @JsonProperty("categoryCode")
    private String categoryCode;

    @JsonProperty("categoryLevel")
    private Integer categoryLevel;

    @JsonProperty("sortOrder")
    private Integer sortOrder;

    public CategoryDto() {}

    public CategoryDto(Long categoryId, Long parentId, String categoryName, 
                       String categoryCode, Integer categoryLevel, Integer sortOrder) {
        this.categoryId = categoryId;
        this.parentId = parentId;
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
        this.categoryLevel = categoryLevel;
        this.sortOrder = sortOrder;
    }

    // Getters and Setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Integer getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(Integer categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public Integer getSortOrder() {
        return sortOrder != null ? sortOrder : 0; // 기본값 0 설정
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
