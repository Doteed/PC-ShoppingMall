package com.project.easyBuild.member.biz;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailBiz {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "rgs0631@gmail.com";
    private static int number;

    public static void createNumber() {
        number = (int) (Math.random() * (900000)) + 100000; // 100000~999999
    }

    public MimeMessage createMail(String email) {  // mail -> email
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);  // mail -> email
            message.setSubject("이메일 인증");
            String body = "<h3>요청하신 인증 번호입니다.</h3>"
                    + "<h1>" + number + "</h1>"
                    + "<h3>감사합니다.</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String email) {  // mail -> email
        MimeMessage message = createMail(email);  // mail -> email
        javaMailSender.send(message);
        return number;
    }
}
