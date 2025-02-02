package com.example.bangyo.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDto {
    private Long id;                 // 예) 1, 2, 3...

    @NotBlank(message = "재료 이름은 필수입니다.")
    private String ingredientName;   // 예) "계란"

    private String imageUrl;         // 예) "/images/egg.png"

    private Integer quantity;        // 재료 수량 (예: 2) - 사용자 냉장고용
}