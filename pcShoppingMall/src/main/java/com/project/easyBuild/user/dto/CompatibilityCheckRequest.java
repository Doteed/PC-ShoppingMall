package com.project.easyBuild.user.dto;

import lombok.Data;

@Data
public class CompatibilityCheckRequest {
	private int cpuId;
	private int mainboardId;
	private int memoryId;
	private int powerId;
	private int gpuId;
	private int caseId;
}