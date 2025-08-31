package com.recipe.smartrecipe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponse {
    private String content;
    private String status;
    private String message;
    
    public RecipeResponse(String content) {
        this.content = content;
        this.status = "success";
        this.message = "Recipe generated successfully";
    }
    
    public RecipeResponse(String content, String status) {
        this.content = content;
        this.status = status;
        this.message = status.equals("success") ? "Recipe generated successfully" : "Failed to generate recipe";
    }
}
