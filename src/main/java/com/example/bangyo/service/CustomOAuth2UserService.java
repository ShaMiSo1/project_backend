package com.example.bangyo.service;

import com.example.bangyo.entity.User;
import com.example.bangyo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * 네이버 OAuth2 로그인 시, 사용자 정보를 받아 DB에 저장하거나 조회하는 서비스
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository; // DB 접근용

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 1) 네이버에서 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2) 로그 찍어보기 (디버깅용)
        System.out.println("네이버 사용자 정보: " + oAuth2User.getAttributes());

        // 3) attributes에서 이메일, 이름 등 추출
        //    네이버는 "response" 안에 email, name 등이 들어있음
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        String email = (String) response.get("email");
        String name  = (String) response.get("name");   // (키 이름이 "name"일 수도 있고, "nickname"일 수도 있음)

        // 4) DB에서 해당 이메일을 가진 사용자 조회
        //    없으면 새로 생성 (orElseGet)
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .username(name != null ? name : "네이버사용자")
                                .enabled(true) // 소셜 로그인은 바로 활성화 시키거나, 추가 절차 사용
                                .build()
                ));

        // 5) 실제 SecurityContext에 등록할 UserDetails(혹은 OAuth2User) 생성
        //    여기서는 DefaultOAuth2User를 사용
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "response" // user-name-attribute (네이버의 경우 "response")
        );
    }
}
