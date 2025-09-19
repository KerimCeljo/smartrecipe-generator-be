package com.recipe.smartrecipe.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LoggedMealResponse {
    private Long id;
    private String userEmail;
    private String recipeTitle;
    private String ingredients;
    private String cookingTime;
    private String content;
    private LocalDateTime loggedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
