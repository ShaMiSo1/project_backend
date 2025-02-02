// src/main/java/com/example/bangyo/controller/FridgeController.java
package com.example.bangyo.controller;

import com.example.bangyo.dto.IngredientDto; // IngredientDto 임포트 추가
import com.example.bangyo.entity.UserIngredient;
import com.example.bangyo.service.UserIngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 냉장고(재료) 관련 CRUD
 */
@RestController
@RequestMapping("/api/fridge")
@RequiredArgsConstructor
public class FridgeController {

    private final UserIngredientService userIngredientService;

    // (1) 냉장고 목록
    @GetMapping
    public ResponseEntity<?> getAllIngredients(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("인증 실패");
        }
        String email = authentication.getName();
        List<UserIngredient> list = userIngredientService.getUserIngredients(email);
        return ResponseEntity.ok(list);
    }

    // (2) 냉장고에 재료 추가
    @PostMapping("/add")
    public ResponseEntity<?> addIngredient(
            @RequestBody IngredientDto dto, // IngredientDto로 변경
            Authentication authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("인증 실패");
        }
        String email = authentication.getName();
        userIngredientService.addIngredient(email, dto); // IngredientDto 전달
        return ResponseEntity.ok("재료 추가 완료");
    }

    // (3) 재료 삭제
    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<?> removeIngredient(
            @PathVariable Long ingredientId,
            Authentication authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("인증 실패");
        }
        String email = authentication.getName();
        userIngredientService.removeIngredient(email, ingredientId);
        return ResponseEntity.ok("재료 삭제 완료");
    }
}
