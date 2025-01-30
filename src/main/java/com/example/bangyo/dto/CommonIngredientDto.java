package com.example.bangyo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonIngredientDto {
    private Long id;         // 예) 1, 2, 3...
    private String name;     // 예) "계란"
    private String imageUrl; // 예) "/images/egg.png"
}
