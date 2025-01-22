package com.example.bangyo.service;

import com.example.bangyo.dto.UserDto;
import com.example.bangyo.entity.User;
import com.example.bangyo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev") // 개발용 H2 설정 사용
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registerUser() {
        // given
        UserDto userDto = new UserDto("testuser", "testpassword", "test@example.com");

        // when
        String result = userService.registerUser(userDto);

        // then
        assertEquals("회원가입이 완료되었습니다. 이메일을 확인하세요.", result);

        // 추가 확인: 데이터베이스에 저장된 사용자 확인
        Optional<User> savedUser = userRepository.findByEmail("test@example.com");
        assertTrue(savedUser.isPresent());
        assertEquals("testuser", savedUser.get().getUsername());
        assertTrue(passwordEncoder.matches("testpassword", savedUser.get().getPassword()));
    }

    @Test
    void verifyUser() {
        // given
        User user = User.builder()
                .username("testuser")
                .password(passwordEncoder.encode("testpassword"))
                .email("test@example.com")
                .enabled(false)
                .verificationToken("test-token")
                .build();
        userRepository.save(user);

        // when
        boolean isVerified = userService.verifyUser("test-token");

        // then
        assertTrue(isVerified);

        // 추가 확인: 사용자 상태가 활성화되었는지 확인
        Optional<User> verifiedUser = userRepository.findByEmail("test@example.com");
        assertTrue(verifiedUser.isPresent());
        assertTrue(verifiedUser.get().isEnabled());
        assertNull(verifiedUser.get().getVerificationToken());
    }

    @Test
    void loginUser() {
        // given
        User user = User.builder()
                .username("testuser")
                .password(passwordEncoder.encode("testpassword"))
                .email("test@example.com")
                .enabled(true)
                .build();
        userRepository.save(user);

        // when
        boolean loginSuccess = userService.loginUser("test@example.com", "testpassword");
        boolean loginFail = userService.loginUser("test@example.com", "wrongpassword");

        // then
        assertTrue(loginSuccess);
        assertFalse(loginFail);
    }
}
