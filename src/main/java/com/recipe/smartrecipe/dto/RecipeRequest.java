package com.recipe.smartrecipe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequest {
    
    @NotBlank(message = "Ingredients are required")
    private String ingredients;
    
    @NotNull(message = "Meal type is required")
    private String mealType;
    
    @NotNull(message = "Cuisine is required")
    private String cuisine;
    
    @NotNull(message = "Cooking time is required")
    private String cookingTime;
    
    @NotNull(message = "Complexity level is required")
    private String complexity;
}
