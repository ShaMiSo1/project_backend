package com.example.bangyo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("yungju817@naver.com"); // 발신자 이메일 명시적으로 설정
            message.setTo(to); // 사용자가 입력한 이메일로 발송
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message); // 이메일 발송
            System.out.println("이메일 발송 성공");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이메일 발송 실패: " + e.getMessage());
        }
    }

}

