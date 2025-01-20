package com.project.easyBuild.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.easyBuild.member.dto.MemberDto;
import com.project.easyBuild.user.biz.CompatibilityService;
import com.project.easyBuild.user.dto.CompatibilityCheckRequest;
import com.project.easyBuild.user.dto.CompatibilityResultDto;
import com.project.easyBuild.user.dto.ComponentDto;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class CompatibilityController {

    @Autowired
    private CompatibilityService compatibilityService;

    @GetMapping("/cpu/list")
    public ResponseEntity<?> getCpuList(HttpSession session) {
    	MemberDto user = (MemberDto) session.getAttribute("dto");
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
	    }
	    
        return ResponseEntity.ok(compatibilityService.getCpuList(user.getUserId()));
    }

    @GetMapping("/mainboard/list")
    public ResponseEntity<?> getMainboardList(HttpSession session) {
    	MemberDto user = (MemberDto) session.getAttribute("dto");
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
	    }
        return ResponseEntity.ok(compatibilityService.getMainboardList(user.getUserId()));
    }

    @GetMapping("/memory/list")
    public ResponseEntity<?> getMemoryList(HttpSession session) {
    	MemberDto user = (MemberDto) session.getAttribute("dto");
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
	    }
        return ResponseEntity.ok(compatibilityService.getMemoryList(user.getUserId()));
    }

    @GetMapping("/power/list")
    public ResponseEntity<?> getPowerList(HttpSession session) {
    	MemberDto user = (MemberDto) session.getAttribute("dto");
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
	    }
        return ResponseEntity.ok(compatibilityService.getPowerList(user.getUserId()));
    }

    @GetMapping("/gpu/list")
    public ResponseEntity<?> getGpuList(HttpSession session) {
    	MemberDto user = (MemberDto) session.getAttribute("dto");
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
	    }
        return ResponseEntity.ok(compatibilityService.getGpuList(user.getUserId()));
    }

    @GetMapping("/case/list")
    public ResponseEntity<?> getCaseList(HttpSession session) {
    	MemberDto user = (MemberDto) session.getAttribute("dto");
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
	    }
        return ResponseEntity.ok(compatibilityService.getCaseList(user.getUserId()));
    }

    
    @PostMapping("/compatibility/check")
    public ResponseEntity<List<CompatibilityResultDto>> checkCompatibility(@RequestBody CompatibilityCheckRequest request) {
        List<CompatibilityResultDto> results = compatibilityService.checkCompatibility(request);
        return ResponseEntity.ok(results);
    }
}

