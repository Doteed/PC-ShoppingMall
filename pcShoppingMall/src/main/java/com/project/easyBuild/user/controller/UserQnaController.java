package com.project.easyBuild.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.project.easyBuild.member.dto.MemberDto;
import com.project.easyBuild.user.biz.QnaBiz;
import com.project.easyBuild.user.dto.QnaDto;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/qna")
public class UserQnaController {
    @Autowired
    private QnaBiz qnaBiz;

    @GetMapping("/detail/{qnaId}")
    public ResponseEntity<?> getDetail(@PathVariable int qnaId, HttpSession session) {
    	MemberDto user = (MemberDto) session.getAttribute("dto");
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}
		
        try {
            QnaDto qna = qnaBiz.listOne(qnaId, user.getUserId());
            
            if (qna != null) {
                return ResponseEntity.ok(qna);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody QnaDto qnaDto, HttpSession session){
    	MemberDto user = (MemberDto) session.getAttribute("dto");
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}
		
        System.out.println("dto: " + qnaDto);
        int result = qnaBiz.update(qnaDto, user.getUserId());
        
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
    public ResponseEntity<?> delete(@RequestParam int qnaId, HttpSession session){
    	MemberDto user = (MemberDto) session.getAttribute("dto");
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}
		
        int result = qnaBiz.delete(qnaId, user.getUserId(), user.getAuthId());
        
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
