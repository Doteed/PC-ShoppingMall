package com.project.easyBuild.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.easyBuild.member.biz.MemberBiz;
import com.project.easyBuild.member.dto.MemberDto;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberBiz memberBiz;

    // Handle login requests
    @PostMapping("/login")
    public ResponseEntity<MemberDto> login(@RequestBody MemberDto dto) {
        MemberDto result = memberBiz.login(dto);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(401).body(null);  // Unauthorized
        }
    }

    // 아이디 중복 확인
    @GetMapping("/check-user-id")
    public ResponseEntity<String> checkUserId(@RequestParam String userId) {
        boolean isExist = memberBiz.checkUserId(userId);
        if (isExist) {
            return ResponseEntity.ok("아이디가 이미 존재합니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        }
    }

    // Handle insert requests
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody MemberDto dto) {
        int result = memberBiz.insert(dto);
        if (result > 0) {
            return ResponseEntity.ok("회원가입 성공.");
        } else {
            return ResponseEntity.status(400).body("아이디가 이미 존재합니다."); // 중복 아이디
        }
    }

    // Handle delete requests
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> delete(@PathVariable String userId) {
        int result = memberBiz.delete(userId);
        if (result > 0) {
            return ResponseEntity.ok("회원 삭제 성공.");
        } else {
            return ResponseEntity.status(404).body("회원이 존재하지 않습니다.");
        }
    }
}
