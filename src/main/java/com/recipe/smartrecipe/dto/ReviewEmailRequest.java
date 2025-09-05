package com.recipe.smartrecipe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewEmailRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Review content is required")
    private String reviewContent;
    
    @NotBlank(message = "Recipe title is required")
    private String recipeTitle;
    
    private String reviewerName;
    private Integer rating;
}
