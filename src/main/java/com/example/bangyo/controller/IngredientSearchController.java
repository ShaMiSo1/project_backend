package com.example.bangyo.controller;

import com.example.bangyo.dto.IngredientDetails;
import com.example.bangyo.service.SpoonacularService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spoonacular")
@RequiredArgsConstructor
public class IngredientSearchController {

    private final SpoonacularService spoonacularService;

    @GetMapping("/search")
    public ResponseEntity<?> searchIngredients(@RequestParam("query") String query) { // 매개변수 이름 명시
        // 비동기 처리 예시 (block()은 동기적으로 결과를 기다립니다)
        IngredientDetails details = spoonacularService.getIngredientDetails(query).block();
        if (details != null) {
            return ResponseEntity.ok(details);
        } else {
            return ResponseEntity.status(404).body("재료를 찾을 수 없습니다.");
        }
    }
}
