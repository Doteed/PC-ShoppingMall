package com.project.easyBuild.member.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class MailUtils {

    private final JavaMailSender mailSender;
    private MimeMessage message;
    private MimeMessageHelper messageHelper;

    @Autowired
    public MailUtils(JavaMailSender mailSender) throws MessagingException {
        this.mailSender = mailSender;
        this.message = this.mailSender.createMimeMessage();
        this.messageHelper = new MimeMessageHelper(message, true, "UTF-8");
    }

    public void setSubject(String subject) throws MessagingException {
        messageHelper.setSubject(subject);
    }

    public void setText(String htmlContent) throws MessagingException {
        messageHelper.setText(htmlContent, true);
    }

    public void setFrom(String email, String name) throws UnsupportedEncodingException, MessagingException {
        messageHelper.setFrom(email, name);
    }

    public void setTo(String email) throws MessagingException {
        messageHelper.setTo(email);
    }

    public void send() {
        mailSender.send(message);
    }
}
