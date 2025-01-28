package com.example.bangyo.controller;

import com.example.bangyo.dto.RegisterResult;
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

    // (회원가입 API가 AuthController에도 있으므로, 여기선 필요 없다면 지워도 됩니다)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        RegisterResult result = userService.registerUser(userDto);
        if (result.isSuccess()) {
            return ResponseEntity.ok(Map.of("message", result.getMessage()));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", result.getMessage()));
        }
    }

    // 이메일 인증
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        boolean isVerified = userService.verifyUser(token);
        if (isVerified) {
            return ResponseEntity.ok("이메일 인증 성공. 이제 로그인 가능합니다.");
        } else {
            return ResponseEntity.badRequest().body("이메일 인증 실패. 유효하지 않은 토큰.");
        }
    }
}
