package com.example.bangyo.controller;

import com.example.bangyo.dto.LoginRequest;
import com.example.bangyo.dto.LoginResponse;
import com.example.bangyo.dto.UserDto;
import com.example.bangyo.jwt.JwtTokenProvider;
import com.example.bangyo.service.UserService;
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
    private final UserService userService;

    /**
     * 로그인 처리 (JWT 발급)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new LoginResponse(jwt, "Bearer"));
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        String result = userService.registerUser(userDto);

        if ("회원가입이 완료되었습니다. 이메일을 확인하세요.".equals(result)) {
            return ResponseEntity.ok(Map.of("message", result));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", result));
        }
    }

    /**
     * 현재 사용자 정보 조회 (JWT 인증 필요)
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).body(Map.of("error", "인증 정보가 없습니다."));
        }

        return ResponseEntity.ok(Map.of("username", authentication.getName()));
    }

}
