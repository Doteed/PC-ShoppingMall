package com.project.easyBuild.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = (String) request.getSession().getAttribute("userId");

        if (userId == null) { //로그인 안되어있을때
            response.sendRedirect("/member/login"); //로그인 페이지로
            return false;
        }

        request.setAttribute("userId", userId); //userId 저장
        return true;
    }
}


