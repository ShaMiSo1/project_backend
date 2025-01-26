package com.example.bangyo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.bangyo.jwt.JwtTokenProvider;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 루트 경로 (/) 요청 처리
     * JWT 토큰 인증 상태에 따라 홈 화면 또는 로그인 페이지로 리다이렉션
     */
    @GetMapping("/")
    public String rootRedirect(HttpServletRequest request) {
        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 인증된 사용자 -> 홈 화면으로 리다이렉트
            return "redirect:/home";
        } else {
            // 인증되지 않은 사용자 -> 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }
    }

    /**
     * 로그인 페이지 요청 처리
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html 반환
    }

    /**
     * Authorization 헤더에서 JWT 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후 토큰 부분만 추출
        }
        return null;
    }
}
