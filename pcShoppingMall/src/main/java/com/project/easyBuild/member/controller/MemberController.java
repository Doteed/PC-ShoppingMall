package com.project.easyBuild.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.project.easyBuild.member.biz.MemberBiz;
import com.project.easyBuild.member.dto.MemberDto;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberBiz memberBiz;

    @PostMapping("/login")
    public String login(MemberDto dto) {
        MemberDto result = memberBiz.login(dto);
        if (result != null) {
            return "redirect:/";
        } else {
            return "redirect:/loginform";
        }
    }

    @PostMapping("/register")
    public String register(MemberDto dto) {
        int result = memberBiz.insert(dto);
        if (result > 0) {
            return "redirect:/loginform"; 
        } else {
            return "redirect:/sign_up_email";
        }
    }

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