package com.recipe.smartrecipe.repository;

import com.recipe.smartrecipe.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // Find all reviews for a specific recipe
    List<Review> findByRecipeIdOrderByReviewDateDesc(Long recipeId);
    
    // Find all reviews by a specific user
    List<Review> findByUserIdOrderByReviewDateDesc(Long userId);
    
    // Find reviews for a specific recipe and user
    List<Review> findByRecipeIdAndUserIdOrderByReviewDateDesc(Long recipeId, Long userId);
    
    // Get average rating for a recipe
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.recipe.id = :recipeId")
    Double getAverageRatingByRecipeId(@Param("recipeId") Long recipeId);
    
    // Get review count for a recipe
    @Query("SELECT COUNT(r) FROM Review r WHERE r.recipe.id = :recipeId")
    Long getReviewCountByRecipeId(@Param("recipeId") Long recipeId);
    
    // Find recent reviews (last N reviews)
    @Query("SELECT r FROM Review r ORDER BY r.reviewDate DESC")
    List<Review> findRecentReviews();
}
