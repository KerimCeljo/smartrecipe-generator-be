package com.recipe.smartrecipe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "logged_meal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "recipe_title", nullable = false)
    private String recipeTitle;

    @Column(name = "ingredients", nullable = false, columnDefinition = "TEXT")
    private String ingredients;

    @Column(name = "cooking_time", nullable = false)
    private String cookingTime;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "logged_at", nullable = false)
    private LocalDateTime loggedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor for creating a new logged meal
    public LoggedMeal(String userEmail, String recipeTitle, String ingredients, 
                     String cookingTime, String content, LocalDateTime loggedAt) {
        this.userEmail = userEmail;
        this.recipeTitle = recipeTitle;
        this.ingredients = ingredients;
        this.cookingTime = cookingTime;
        this.content = content;
        this.loggedAt = loggedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
