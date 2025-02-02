// src/main/java/com/example/bangyo/controller/RecommendedIngredientsController.java
package com.example.bangyo.controller;

import com.example.bangyo.dto.IngredientDto;
import com.example.bangyo.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ChatGPT를 활용하여 추천 재료를 제공하는 컨트롤러
 */
@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class RecommendedIngredientsController {

    private final ChatGPTService chatGPTService;

    /**
     * 추천 재료 목록 API
     * @return 추천 재료 목록
     */
    @GetMapping("/recommend")
    public ResponseEntity<List<IngredientDto>> recommendIngredients() {
        List<IngredientDto> recommended = chatGPTService.getPopularIngredientsWithImages();
        return ResponseEntity.ok(recommended); // [{ ingredientName: "김치", imageUrl: "..." }, ...]
    }
}
