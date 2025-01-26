package com.example.bangyo.controller;

import com.example.bangyo.dto.UserDto;
import com.example.bangyo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 처리 (JSON 요청/응답)
     * 예: POST /api/users/register
     *     Body { "username": "...", "email": "...", "password": "..." }
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
     * 이메일 인증 처리
     * 예: GET /api/users/verify?token=xxxx
     */
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        boolean isVerified = userService.verifyUser(token);

        if (isVerified) {
            // 인증 성공
            return ResponseEntity.ok("이메일 인증 성공. 이제 로그인 가능합니다.");
        } else {
            // 인증 실패
            return ResponseEntity.badRequest().body("이메일 인증 실패. 유효하지 않은 토큰.");
        }
    }

}
