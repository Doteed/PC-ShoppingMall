package com.project.easyBuild.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.easyBuild.member.biz.MemberBiz;
import com.project.easyBuild.member.dto.MemberDto;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.project.easyBuild.member.biz.MemberBiz;
import com.project.easyBuild.member.dto.MemberDto;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberBiz memberBiz;

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
    @PostMapping("/member/login")
    public String login(HttpSession session, MemberDto dto) {
        MemberDto result = memberBiz.login(dto);
        if (result != null) {
            session.setAttribute("dto", result); // 사용자 정보를 세션에 저장
            return "redirect:/";
        } else {
            return "redirect:/loginform";
        }
    }
    
    

    
    @PostMapping("/find_id")
    public String find_id(Model model, String userName, String email, MemberDto dto) {
        try {
            dto.setUserName(userName);
            dto.setEmail(email);

            MemberDto id = memberBiz.find_id(dto); // 서비스에서 DAO 호출

            if (id == null) {
                model.addAttribute("msg", "아이디를 찾을 수 없습니다.");
            } else {
                model.addAttribute("findId", id); // findId가 null이 아닐 때 객체 전달
            }
        } catch (Exception e) {
            model.addAttribute("msg", "오류가 발생되었습니다.");
            e.printStackTrace();
        }
        return "pages/member/findIdResult"; // Thymeleaf 템플릿을 반환
    }

    @PostMapping("/find_pw")
    public String findPw(MemberDto dto,Model model) throws Exception{
		
		if(memberBiz.findPwCheck(dto)==0) {
			model.addAttribute("msg", "아이디와 이메일를 확인해주세요");
			
			return "/member/findPwView";
		}else {
	
		memberBiz.findPw(dto.getEmail(),dto.getUserId());
		model.addAttribute("member", dto.getEmail());
		
		return "redirect:/loginform";
		}
	}
    
}
