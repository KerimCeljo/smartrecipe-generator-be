package com.recipe.smartrecipe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailRequest {
    
    @NotBlank(message = "Email address is required")
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @NotBlank(message = "Recipe content is required")
    private String recipeContent;
    
    @NotBlank(message = "Recipe title is required")
    private String recipeTitle;
}
