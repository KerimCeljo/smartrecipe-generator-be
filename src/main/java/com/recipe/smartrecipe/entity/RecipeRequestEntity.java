package com.recipe.smartrecipe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipe_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "ingredients", nullable = false, length = 1000)
    private String ingredients;
    
    @Column(name = "meal_type", nullable = false, length = 50)
    private String mealType;
    
    @Column(name = "cuisine", nullable = false, length = 50)
    private String cuisine;
    
    @Column(name = "cooking_time", nullable = false, length = 50)
    private String cookingTime;
    
    @Column(name = "complexity", nullable = false, length = 50)
    private String complexity;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
