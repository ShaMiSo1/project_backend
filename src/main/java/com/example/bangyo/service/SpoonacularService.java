package com.example.bangyo.service;

import com.example.bangyo.dto.IngredientDetails;
import com.example.bangyo.dto.IngredientSearchResponse;
import com.example.bangyo.dto.IngredientSearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Spoonacular API를 호출하는 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SpoonacularService {

    @Value("${spoonacular.apiKey}") // 환경 변수로 주입
    private String spoonApiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.spoonacular.com")
            .build();

    // 한국어 재료 이름을 영어로 매핑하는 간단한 매핑 테이블
    private static final Map<String, String> KOREAN_TO_ENGLISH = new HashMap<>();

    static {
        KOREAN_TO_ENGLISH.put("김치", "kimchi");
        // 추가적인 매핑이 필요하면 여기에 추가
    }

    /**
     * 재료 검색
     * @param query 검색어 (한국어 이름)
     * @return 검색 결과 (재료 이름, 이미지, id)
     */
    public Mono<IngredientSearchResponse> searchIngredients(String query) {
        String englishQuery = KOREAN_TO_ENGLISH.getOrDefault(query, query); // 매핑된 영어 이름 사용
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/food/ingredients/autocomplete")
                        .queryParam("query", englishQuery)
                        .queryParam("number", 10) // 검색 결과 수 증가
                        .queryParam("apiKey", spoonApiKey)
                        .build())
                .retrieve()
                .bodyToMono(IngredientSearchResponse.class)
                .doOnError(error -> log.error("Error during searchIngredients API call: {}", error.getMessage()))
                .onErrorResume(error -> {
                    // 에러 발생 시 빈 응답을 반환하거나 적절히 처리
                    return Mono.empty();
                });
    }

    /**
     * 재료 이름으로 상세 정보 검색 (이미지 URL 포함)
     * @param ingredientName 재료 이름 (한국어 또는 영어)
     * @return 재료 상세 정보
     */
    public Mono<IngredientDetails> getIngredientDetails(String ingredientName) {
        return searchIngredients(ingredientName)
                .flatMap(response -> {
                    if (response.getResults() != null && !response.getResults().isEmpty()) {
                        IngredientSearchResult result = response.getResults().get(0);
                        String imageUrl = result.getImage() != null ?
                                "https://spoonacular.com/cdn/ingredients_100x100/" + result.getImage() :
                                "https://via.placeholder.com/30";
                        return Mono.just(new IngredientDetails(result.getName(), imageUrl));
                    } else {
                        log.warn("No results found for ingredient: {}", ingredientName);
                        return Mono.empty(); // 결과가 없을 경우 빈 Mono 반환
                    }
                })
                // switchIfEmpty 제거
                .doOnError(error -> log.error("Error during getIngredientDetails: {}", error.getMessage()));
    }
}
