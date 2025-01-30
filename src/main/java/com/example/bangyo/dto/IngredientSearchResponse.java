// src/main/java/com/example/bangyo/dto/IngredientSearchResponse.java
package com.example.bangyo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientSearchResponse {
    private List<IngredientSearchResult> results;
}
