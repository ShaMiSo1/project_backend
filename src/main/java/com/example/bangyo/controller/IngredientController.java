// src/main/java/com/example/bangyo/controller/IngredientController.java
package com.example.bangyo.controller;

import com.example.bangyo.dto.IngredientDto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    @GetMapping("/common")
    public List<IngredientDto> getCommonIngredients() {
        // 예시: 하드코딩된 재료들
        List<IngredientDto> list = new ArrayList<>();
        list.add(IngredientDto.builder()
                .id(1L)
                .ingredientName("계란")
                .imageUrl("/images/egg.png")
                .quantity(null) // 사용자 냉장고용 필드이므로 null로 설정
                .build());
        list.add(IngredientDto.builder()
                .id(2L)
                .ingredientName("양파")
                .imageUrl("/images/onion.png")
                .quantity(null)
                .build());
        list.add(IngredientDto.builder()
                .id(3L)
                .ingredientName("돼지고기")
                .imageUrl("/images/pork.png")
                .quantity(null)
                .build());
        list.add(IngredientDto.builder()
                .id(4L)
                .ingredientName("김치")
                .imageUrl("/images/kimchi.png")
                .quantity(null)
                .build());
        list.add(IngredientDto.builder()
                .id(5L)
                .ingredientName("두부")
                .imageUrl("/images/tofu.png")
                .quantity(null)
                .build());
        return list;
    }
}
