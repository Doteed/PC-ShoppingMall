package com.project.easyBuild.user.dto;

import lombok.Data;

@Data
public class CompatibilityResultDto {
    private String criteria;
    private boolean status;

    public CompatibilityResultDto(String criteria, boolean status) {
        this.criteria = criteria;
        this.status = status;
    }
}