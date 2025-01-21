package com.example.bangyo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("인증 객체: " + authentication);
        System.out.println("인증된 사용자 이름: " + authentication.getName());
        if (authentication != null) {
            model.addAttribute("username", authentication.getName()); // 사용자 이름 전달
            model.addAttribute("authorities", authentication.getAuthorities()); // 권한 전달
        } else {
            model.addAttribute("username", "익명 사용자");
            model.addAttribute("authorities", "권한 없음");
        }
        return "index"; // index.html 렌더링
    }
}


