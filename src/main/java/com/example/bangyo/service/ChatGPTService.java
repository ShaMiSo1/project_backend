package com.example.bangyo.service;

import com.example.bangyo.dto.IngredientDetails;
import com.example.bangyo.dto.IngredientResponseDto;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenAI GPT API를 호출하는 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGPTService {

    @Value("${openai.apiKey}")
    private String openAiApiKey;

    private final SpoonacularService spoonacularService;

    /**
     * ChatGPT에게 "한국 가정에서 냉장고에 가장 많이 보관하는 재료 10가지를 알려줘" 요청
     * @return 추천 재료 목록 with images
     */
    public List<IngredientResponseDto> getPopularIngredientsWithImages() {
        List<String> popularIngredients = getPopularIngredients();
        List<IngredientResponseDto> responseList = new ArrayList<>();

        for (String ingredientName : popularIngredients) {
            try {
                // Spoonacular API를 통해 이미지 URL 가져오기
                IngredientDetails details = spoonacularService.getIngredientDetails(ingredientName).block();
                String imageUrl = details != null ? details.getImageUrl() : "https://via.placeholder.com/30";

                responseList.add(new IngredientResponseDto(ingredientName, imageUrl));
            } catch (Exception e) {
                log.error("Error fetching details for ingredient {}: {}", ingredientName, e.getMessage());
                // 실패한 재료에 대해서는 기본 이미지 URL 할당
                responseList.add(new IngredientResponseDto(ingredientName, "https://via.placeholder.com/30"));
            }
        }

        return responseList;
    }

    /**
     * ChatGPT에게 인기 재료 목록 요청
     * @return 인기 재료 목록
     */
    private List<String> getPopularIngredients() {
        OpenAiService service = new OpenAiService(openAiApiKey, Duration.ofSeconds(30));

        ChatMessage systemMsg = new ChatMessage("system", "You are a helpful assistant and respond in Korean.");
        ChatMessage userMsg = new ChatMessage("user", "한국 가정에서 냉장고에 가장 많이 보관하는 재료 10가지를 알려줘. 짧게.");

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(systemMsg, userMsg))
                .maxTokens(200) // 토큰 수 증가
                .temperature(0.7)
                .build();

        ChatCompletionResult result = service.createChatCompletion(request);
        String response = result.getChoices().get(0).getMessage().getContent();

        // 간단한 파싱: 줄바꿈을 기준으로 분리하고, 숫자와 점을 제거
        String[] lines = response.split("\n");
        List<String> ingredients = new ArrayList<>();
        for (String line : lines) {
            String clean = line.replaceAll("^\\d+\\.\\s*", "").trim();
            if (!clean.isEmpty()) {
                ingredients.add(clean);
            }
        }
        return ingredients;
    }
}
