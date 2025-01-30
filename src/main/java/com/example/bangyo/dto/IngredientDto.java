// src/main/java/com/example/bangyo/dto/IngredientDto.java
package com.example.bangyo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDto {
    private String ingredientName; // 재료 이름 (예: "김치")
    private int quantity;          // 재료 수량 (예: 2)
    private String imageUrl;       // 재료 이미지 URL (예: "https://...")
}
