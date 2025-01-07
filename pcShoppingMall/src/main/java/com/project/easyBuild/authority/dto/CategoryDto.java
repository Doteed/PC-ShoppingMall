package com.project.easyBuild.authority.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

}
