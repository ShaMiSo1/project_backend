package com.example.bangyo.controller;

import com.example.bangyo.dto.LoginRequest;
import com.example.bangyo.dto.LoginResponse;
import com.example.bangyo.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인 처리
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        // 인증 수행
        Authentication authentication = authenticationManager.authenticate(authToken);

        // JWT 토큰 생성
        String jwt = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new LoginResponse(jwt, "Bearer"));
    }

    /**
     * 현재 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("인증 정보가 없습니다.");
        }

        String username = authentication.getName();
        return ResponseEntity.ok(Map.of("username", username));
    }
}
