package com.recipe.smartrecipe.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewRequest {
    
    @NotNull(message = "Recipe ID is required")
    private Long recipeId;
    
    @NotBlank(message = "Review text is required")
    @Size(max = 1000, message = "Review text must not exceed 1000 characters")
    private String reviewText;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    private LocalDateTime reviewDate; // Optional - will default to current time if not provided
}
