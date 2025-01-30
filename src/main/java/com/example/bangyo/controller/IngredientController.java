package com.example.bangyo.controller;

import com.example.bangyo.dto.CommonIngredientDto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    @GetMapping("/common")
    public List<CommonIngredientDto> getCommonIngredients() {
        // 예시: 하드코딩된 재료들
        List<CommonIngredientDto> list = new ArrayList<>();
        list.add(new CommonIngredientDto(1L, "계란",  "/images/egg.png"));
        list.add(new CommonIngredientDto(2L, "양파",  "/images/onion.png"));
        list.add(new CommonIngredientDto(3L, "돼지고기", "/images/pork.png"));
        list.add(new CommonIngredientDto(4L, "김치",  "/images/kimchi.png"));
        list.add(new CommonIngredientDto(5L, "두부",  "/images/tofu.png"));
        return list;
    }
}
