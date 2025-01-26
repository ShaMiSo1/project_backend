package com.example.bangyo.config;

import com.example.bangyo.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 로그인 성공 후, JWT 발급 및 리다이렉트 처리
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 인증 성공 시점에 호출
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        // 1) 여기서 'authentication'에는 네이버에서 받은 사용자 정보가 들어있음
        //    (CustomOAuth2UserService에서 loadUser 한 정보가 세팅됨)

        // 2) JWT 발급
        String jwtToken = jwtTokenProvider.generateToken(authentication);
        // generateToken 내부에서 authentication.getName() (보통 email)로 토큰 생성

        // 3) 클라이언트(React)로 어떻게 전달할지 결정
        //    - 예: 프런트엔드 주소로 리다이렉트, 쿼리 파라미터에 토큰 포함
        response.sendRedirect("http://localhost:8080/social-login?token=" + jwtToken);


    }
}

