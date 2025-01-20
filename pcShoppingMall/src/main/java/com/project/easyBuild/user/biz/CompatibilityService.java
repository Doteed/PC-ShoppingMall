package com.project.easyBuild.user.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.easyBuild.user.dao.CompatibilityRepository;
import com.project.easyBuild.user.dto.CartDto;
import com.project.easyBuild.user.dto.CompatibilityCheckRequest;
import com.project.easyBuild.user.dto.CompatibilityResultDto;

@Service
public class CompatibilityService {
	@Autowired
    private CompatibilityRepository compatibilityRepository;

    public List<CartDto> getCpuList(String userId) {
        return compatibilityRepository.fetchComponents(userId, "CPU");
    }

    public List<CartDto> getMainboardList(String userId) {
        return compatibilityRepository.fetchComponents(userId, "MAINBOARD");
    }

    public List<CartDto> getMemoryList(String userId) {
        return compatibilityRepository.fetchComponents(userId, "MEMORY");
    }

    public List<CartDto> getPowerList(String userId) {
        return compatibilityRepository.fetchComponents(userId, "POWER");
    }

    public List<CartDto> getGpuList(String userId) {
        return compatibilityRepository.fetchComponents(userId, "GRAPHIC_CARD");
    }
    
    public List<CartDto> getCaseList(String userId) {
        return compatibilityRepository.fetchComponents(userId, "CASE");
    }

    public List<CompatibilityResultDto> checkCompatibility(CompatibilityCheckRequest request) {
        return compatibilityRepository.checkCompatibility(request);
    }
}
