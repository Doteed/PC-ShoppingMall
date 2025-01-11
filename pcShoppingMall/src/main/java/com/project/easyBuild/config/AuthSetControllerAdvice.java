package com.project.easyBuild.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.project.easyBuild.member.dto.MemberDto;

@ControllerAdvice
public class AuthSetControllerAdvice {

    @ModelAttribute("isAdmin")
    public boolean isAdmin(HttpSession session) {
        MemberDto user = (MemberDto) session.getAttribute("dto");
        return user != null && user.getAuthId() == 2; // 관리자 권한 확인
    }
    
    @ModelAttribute("loggedInUser")
    public MemberDto getLoggedInUser(HttpSession session) {
        return (MemberDto) session.getAttribute("dto");
    }
}
