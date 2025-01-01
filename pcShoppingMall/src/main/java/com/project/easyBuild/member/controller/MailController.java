package com.project.easyBuild.member.controller;

import com.project.easyBuild.member.biz.MailBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailBiz mailBiz;

    @ResponseBody
    @PostMapping("/email")  
    public String MailSend(String email) {  

        int number = mailBiz.sendMail(email);  

        String num = "" + number;

        return num;
    }
}
