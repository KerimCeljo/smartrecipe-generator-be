package com.recipe.smartrecipe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LoggedMealRequest {
    @NotBlank(message = "User email is required")
    @Email(message = "Invalid email format")
    private String userEmail;

    @NotBlank(message = "Recipe title is required")
    private String recipeTitle;

    @NotBlank(message = "Ingredients are required")
    private String ingredients;

    @NotBlank(message = "Cooking time is required")
    private String cookingTime;

    @NotBlank(message = "Content is required")
    private String content;

    private LocalDateTime loggedAt; // Optional, defaults to now if not provided
}
