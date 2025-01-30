package com.example.bangyo.controller;

import com.example.bangyo.dto.LoginRequest;
import com.example.bangyo.dto.LoginResponse;
import com.example.bangyo.dto.RegisterResult;
import com.example.bangyo.dto.UserDto;
import com.example.bangyo.entity.User;
import com.example.bangyo.jwt.JwtTokenProvider;
import com.example.bangyo.repository.UserRepository;
import com.example.bangyo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
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
    private final UserRepository userRepository; // UserRepository 주입

    /**
     * 이메일/비밀번호 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // AuthenticationManager에 이메일/비밀번호 전달
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 인증 성공 시 JWT 생성
        String jwt = jwtTokenProvider.generateToken(authentication);

        // JWT 반환
        return ResponseEntity.ok(new LoginResponse(jwt, "Bearer"));
    }

    /**
     * 회원가입
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        RegisterResult result = userService.registerUser(userDto);
        if (result.isSuccess()) {
            return ResponseEntity.ok(Map.of("message", result.getMessage()));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", result.getMessage()));
        }
    }

    /**
     * JWT 인증이 필요한 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).body(Map.of("error", "인증 정보가 없습니다."));
        }
        // authentication.getName() == 이메일
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String email = user.getEmail();
        String username = user.getUsername() != null ? user.getUsername() : "사용자 이름 없음";

        return ResponseEntity.ok(Map.of("email", email, "username", username));
    }
}
