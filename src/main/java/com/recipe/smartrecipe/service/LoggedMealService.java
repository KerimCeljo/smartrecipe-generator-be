package com.recipe.smartrecipe.service;

import com.recipe.smartrecipe.dto.LoggedMealRequest;
import com.recipe.smartrecipe.dto.LoggedMealResponse;
import com.recipe.smartrecipe.entity.LoggedMeal;
import com.recipe.smartrecipe.repository.LoggedMealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoggedMealService {
    
    private final LoggedMealRepository loggedMealRepository;
    
    /**
     * Create a new logged meal
     */
    @Transactional
    public LoggedMealResponse createLoggedMeal(LoggedMealRequest request) {
        log.info("Creating logged meal for user: {}", request.getUserEmail());
        
        // Set logged date to current time if not provided
        LocalDateTime loggedAt = request.getLoggedAt() != null ? 
                request.getLoggedAt() : LocalDateTime.now();
        
        // Create logged meal entity
        LoggedMeal loggedMeal = new LoggedMeal(
                request.getUserEmail(),
                request.getRecipeTitle(),
                request.getIngredients(),
                request.getCookingTime(),
                request.getContent(),
                loggedAt
        );
        
        // Save logged meal
        LoggedMeal savedLoggedMeal = loggedMealRepository.save(loggedMeal);
        log.info("Logged meal created with ID: {}", savedLoggedMeal.getId());
        
        return convertToResponse(savedLoggedMeal);
    }
    
    /**
     * Get all logged meals for a user
     */
    public List<LoggedMealResponse> getLoggedMealsByUserEmail(String userEmail) {
        log.info("Fetching logged meals for user: {}", userEmail);
        
        List<LoggedMeal> loggedMeals = loggedMealRepository.findByUserEmailOrderByLoggedAtDesc(userEmail);
        return loggedMeals.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Search logged meals by recipe title
     */
    public List<LoggedMealResponse> searchLoggedMealsByRecipeTitle(String userEmail, String recipeTitle) {
        log.info("Searching logged meals for user: {} with recipe title: {}", userEmail, recipeTitle);
        
        List<LoggedMeal> loggedMeals = loggedMealRepository.findByUserEmailAndRecipeTitleContainingIgnoreCaseOrderByLoggedAtDesc(userEmail, recipeTitle);
        return loggedMeals.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert LoggedMeal entity to LoggedMealResponse DTO
     */
    private LoggedMealResponse convertToResponse(LoggedMeal loggedMeal) {
        LoggedMealResponse response = new LoggedMealResponse();
        response.setId(loggedMeal.getId());
        response.setUserEmail(loggedMeal.getUserEmail());
        response.setRecipeTitle(loggedMeal.getRecipeTitle());
        response.setIngredients(loggedMeal.getIngredients());
        response.setCookingTime(loggedMeal.getCookingTime());
        response.setContent(loggedMeal.getContent());
        response.setLoggedAt(loggedMeal.getLoggedAt());
        response.setCreatedAt(loggedMeal.getCreatedAt());
        response.setUpdatedAt(loggedMeal.getUpdatedAt());
        return response;
    }
}
