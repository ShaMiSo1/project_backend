// src/main/java/com/example/bangyo/controller/IngredientSearchController.java
package com.example.bangyo.controller;

import com.example.bangyo.dto.IngredientDto;
import com.example.bangyo.service.SpoonacularService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/spoonacular")
@RequiredArgsConstructor
public class IngredientSearchController {

    private final SpoonacularService spoonacularService;

    @GetMapping("/search")
    public Mono<ResponseEntity<IngredientDto>> searchIngredients(@RequestParam("query") String query) {
        return spoonacularService.getIngredientDetails(query)
                .map(details -> ResponseEntity.ok(details))
                .defaultIfEmpty(ResponseEntity.status(404).body(null));
    }
}
