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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.easyBuild.user.biz.ReviewBiz;
import com.project.easyBuild.user.dto.ReviewDto;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewBiz reviewbiz;

    @GetMapping("/detail/{reviewId}")
    public ResponseEntity<ReviewDto> getDetail(@PathVariable int reviewId, @RequestParam String userId) {
        try {
        	System.out.println(reviewId);
        	ReviewDto review = reviewbiz.listOne(reviewId, userId);
            
            if (review != null) {
                return ResponseEntity.ok(review);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestBody ReviewDto reviewDto){//@RequestAttribute("userId") String userId) {
        int result = reviewbiz.update(reviewDto);
        System.out.println("dto: " + reviewDto.getRating());
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

    @GetMapping("/insert-editor")
    public ResponseEntity<Map<String, Object>> insertEditor(@RequestParam String userId) {
        Map<String, Object> response = new HashMap<>();
        
        //초기화
        response.put("title", "");
        response.put("content", "");
        response.put("rating", 5);

        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/insert")
    public ResponseEntity<Map<String, Object>> insert(@RequestBody ReviewDto reviewDto) {
        int result = reviewbiz.insert(reviewDto);
        System.out.println(reviewDto);
        Map<String, Object> response = new HashMap<>();
        
        if (result > 0) {
            response.put("success", true);
            response.put("message", "해당 게시글이 성공적으로 작성되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "해당 게시글 작성에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    //삭제
    @PutMapping("/delete")
    public ResponseEntity<Map<String, String>> delete(@RequestParam int reviewId, @RequestParam String userId){//@RequestAttribute("userId") String userId) {
        int result = reviewbiz.delete(reviewId, userId);
        
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
