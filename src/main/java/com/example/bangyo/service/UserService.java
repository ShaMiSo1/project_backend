package com.example.bangyo.service;

import com.example.bangyo.dto.UserDto;
import com.example.bangyo.entity.User;
import com.example.bangyo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    // 회원가입 로직
    public String registerUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return "이미 존재하는 사용자 이름입니다.";
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return "이미 존재하는 이메일입니다.";
        }

        String token = UUID.randomUUID().toString(); // 인증 토큰 생성

        // HTML 버튼을 포함한 이메일 발송
        emailService.sendEmail(userDto.getEmail(), "Email Verification",
                "<h1>이메일 인증</h1>" +
                        "<p>아래 버튼을 클릭하여 이메일 인증을 완료하세요:</p>" +
                        "<a href='http://localhost:8080/api/users/verify?token=" + token + "' " +
                        "style='padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px;'>이메일 인증</a>" +
                        "<p>만약 버튼이 보이지 않는다면 아래 링크를 복사해서 브라우저에 붙여넣으세요:</p>" +
                        "<p>http://localhost:8080/api/users/verify?token=" + token + "</p>");

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .enabled(false) // 이메일 인증 전까지 비활성화 상태
                .verificationToken(token)
                .build();

        userRepository.save(user);
        return "회원가입이 완료되었습니다. 이메일을 확인하세요.";
    }

    @Transactional
    public boolean verifyUser(String token) {
        System.out.println("인증 요청 토큰: " + token);
        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("인증된 사용자: " + user.getUsername());

            user.setEnabled(true); // 사용자 활성화
            user.setVerificationToken(null); // 인증 토큰 제거

            userRepository.save(user);
            System.out.println("사용자 enabled 상태: " + user.isEnabled());
            return true;
        }
        System.out.println("인증 실패: 유효하지 않은 토큰");
        return false;
    }




    // 로그인 로직
    public boolean loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }
}
