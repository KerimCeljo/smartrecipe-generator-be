package com.recipe.smartrecipe.repository;

import com.recipe.smartrecipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    List<Recipe> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT r FROM Recipe r WHERE r.userId = :userId ORDER BY r.createdAt DESC LIMIT :limit")
    List<Recipe> findRecentRecipesByUserId(@Param("userId") Long userId, @Param("limit") int limit);
    
    // Find recipes by user ID and meal type
    @Query("SELECT r FROM Recipe r JOIN RecipeRequestEntity req ON r.requestId = req.id WHERE r.userId = :userId AND req.mealType = :mealType ORDER BY r.createdAt DESC")
    List<Recipe> findByUserIdAndMealType(@Param("userId") Long userId, @Param("mealType") String mealType);
    
    // Find recipes by user ID and cuisine
    @Query("SELECT r FROM Recipe r JOIN RecipeRequestEntity req ON r.requestId = req.id WHERE r.userId = :userId AND req.cuisine = :cuisine ORDER BY r.createdAt DESC")
    List<Recipe> findByUserIdAndCuisine(@Param("userId") Long userId, @Param("cuisine") String cuisine);
    
    // Find recipes by user ID containing specific ingredient
    @Query("SELECT r FROM Recipe r JOIN RecipeRequestEntity req ON r.requestId = req.id WHERE r.userId = :userId AND req.ingredients LIKE %:ingredient% ORDER BY r.createdAt DESC")
    List<Recipe> findByUserIdAndIngredientsContaining(@Param("userId") Long userId, @Param("ingredient") String ingredient);
    
    // Find recipes by user ID and complexity
    @Query("SELECT r FROM Recipe r JOIN RecipeRequestEntity req ON r.requestId = req.id WHERE r.userId = :userId AND req.complexity = :complexity ORDER BY r.createdAt DESC")
    List<Recipe> findByUserIdAndComplexity(@Param("userId") Long userId, @Param("complexity") String complexity);
    
    // Find recipes by user ID and cooking time
    @Query("SELECT r FROM Recipe r JOIN RecipeRequestEntity req ON r.requestId = req.id WHERE r.userId = :userId AND req.cookingTime = :cookingTime ORDER BY r.createdAt DESC")
    List<Recipe> findByUserIdAndCookingTime(@Param("userId") Long userId, @Param("cookingTime") String cookingTime);
}
