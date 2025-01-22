package com.example.bangyo.controller;

import com.example.bangyo.dto.UserDto;
import com.example.bangyo.entity.User;
import com.example.bangyo.repository.UserRepository;
import com.example.bangyo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 회원가입 페이지 렌더링
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // register.html
    }

    // 로그인 페이지 렌더링
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDto userDto, Model model) {
        String result = userService.registerUser(userDto);
        model.addAttribute("message", result);

        if (result.equals("회원가입이 완료되었습니다. 이메일을 확인하세요.")) {
            // === 회원가입 성공 ===
            // register-success.html 뷰를 렌더링 (템플릿 엔진에서 찾을 수 있게 됨)
            return "register-success";
        } else {
            // === 회원가입 실패 ===
            return "register-failure";
        }
    }


    // 이메일 인증 처리
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token) {
        boolean isVerified = userService.verifyUser(token);

        if (isVerified) {
            // 인증 성공: 로그인 페이지로 리다이렉트
            return "redirect:/api/users/login?verified=true";
        } else {
            // 인증 실패: 로그인 페이지로 리다이렉트하며 에러 메시지 전달
            return "redirect:/api/users/login?error=invalid-token";
        }
    }

}
