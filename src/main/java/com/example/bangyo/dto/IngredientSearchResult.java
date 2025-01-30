// src/main/java/com/example/bangyo/dto/IngredientSearchResult.java
package com.example.bangyo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientSearchResult {
    private Long id;
    private String name;
    private String image;
}
