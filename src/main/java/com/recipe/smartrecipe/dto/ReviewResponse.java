package com.recipe.smartrecipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    
    private Long id;
    private Long recipeId;
    private String recipeTitle;
    private Long userId;
    private String reviewText;
    private Integer rating;
    private LocalDateTime reviewDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public ReviewResponse(Long id, Long recipeId, Long userId, String reviewText, 
                         Integer rating, LocalDateTime reviewDate, 
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.recipeId = recipeId;
        this.userId = userId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
