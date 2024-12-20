package com.project.easyBuild.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.easyBuild.user.biz.QABiz;
import com.project.easyBuild.user.dto.QADto;

@RestController
@RequestMapping("/qa")
public class QAController {
    @Autowired
    private QABiz qabiz;

    @GetMapping("/detail/{qaId}")
    public ResponseEntity<QADto> getQaDetail(@PathVariable int qaId, @RequestParam String userId){//@RequestAttribute("userId") String userId) {
        QADto qa = qabiz.listOne(qaId, userId);
        if (qa != null) {
            return ResponseEntity.ok(qa);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateQa(@RequestBody QADto qaDto){//@RequestAttribute("userId") String userId) {
        System.out.println("dto: " + qaDto);
        int result = qabiz.update(qaDto);
        
        Map<String, Object> response = new HashMap<>();
        
        if (result > 0) {
            response.put("success", true);
            response.put("message", "해당 게시글이 성공적으로 수정되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "해당 게시글 수정을 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteQa(@RequestParam int qaId, @RequestParam String userId){//@RequestAttribute("userId") String userId) {
        int result = qabiz.delete(qaId, userId);
        
        Map<String, String> response = new HashMap<>();
        if (result > 0) {
            response.put("message", "해당 게시글이 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "해당 게시글 삭제를 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
