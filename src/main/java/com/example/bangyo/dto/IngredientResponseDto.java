// src/main/java/com/example/bangyo/dto/IngredientResponseDto.java
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
public class IngredientResponseDto {
    private String name;
    private String imageUrl;
}
