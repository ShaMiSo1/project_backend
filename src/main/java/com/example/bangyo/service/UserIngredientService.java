// src/main/java/com/example/bangyo/service/UserIngredientService.java
package com.example.bangyo.service;

import com.example.bangyo.dto.IngredientDto;
import com.example.bangyo.entity.User;
import com.example.bangyo.entity.UserIngredient;
import com.example.bangyo.repository.UserIngredientRepository;
import com.example.bangyo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserIngredientService {

    private final UserIngredientRepository userIngredientRepository;
    private final UserRepository userRepository;

    /**
     * 재료 추가
     */
    public void addIngredient(String userEmail, IngredientDto dto) { // IngredientDto 사용
        User user = findUserByEmail(userEmail);
        UserIngredient entity = UserIngredient.builder()
                .user(user)
                .ingredientName(dto.getIngredientName()) // ingredientName 사용
                .quantity(dto.getQuantity() != null ? dto.getQuantity() : 0) // null 체크 및 기본값 설정
                .imageUrl(dto.getImageUrl()) // 이미지도 함께 저장
                .build();
        userIngredientRepository.save(entity);
    }

    /**
     * 사용자 냉장고 재료 조회
     */
    public List<UserIngredient> getUserIngredients(String userEmail) {
        User user = findUserByEmail(userEmail);
        return userIngredientRepository.findByUser(user);
    }

    /**
     * 재료 삭제
     */
    public void removeIngredient(String userEmail, Long ingredientId) {
        // 필요시 "이 재료가 정말 이 user 소유인지" 검증 가능
        User user = findUserByEmail(userEmail);
        UserIngredient ingredient = userIngredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("재료가 존재하지 않습니다. ID: " + ingredientId));

        if (!ingredient.getUser().equals(user)) {
            throw new RuntimeException("해당 재료를 삭제할 권한이 없습니다.");
        }

        userIngredientRepository.delete(ingredient);
    }

    /**
     * email로 User 찾기
     */
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }
}
