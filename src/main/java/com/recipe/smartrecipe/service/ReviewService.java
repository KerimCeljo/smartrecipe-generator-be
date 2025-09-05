package com.recipe.smartrecipe.service;

import com.recipe.smartrecipe.dto.ReviewRequest;
import com.recipe.smartrecipe.dto.ReviewResponse;
import com.recipe.smartrecipe.entity.Recipe;
import com.recipe.smartrecipe.entity.Review;
import com.recipe.smartrecipe.repository.RecipeRepository;
import com.recipe.smartrecipe.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final RecipeRepository recipeRepository;
    
    /**
     * Create a new review
     */
    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {
        log.info("Creating review for recipe ID: {}", request.getRecipeId());
        
        // Find the recipe
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + request.getRecipeId()));
        
        // Set review date to current time if not provided
        LocalDateTime reviewDate = request.getReviewDate() != null ? 
                request.getReviewDate() : LocalDateTime.now();
        
        // Create review entity
        Review review = new Review(
                recipe,
                1L, // Default user ID - you can modify this based on your auth system
                request.getReviewText(),
                request.getRating(),
                reviewDate
        );
        
        // Save review
        Review savedReview = reviewRepository.save(review);
        log.info("Review created with ID: {}", savedReview.getId());
        
        return convertToResponse(savedReview);
    }
    
    /**
     * Get all reviews for a recipe
     */
    public List<ReviewResponse> getReviewsByRecipeId(Long recipeId) {
        log.info("Fetching reviews for recipe ID: {}", recipeId);
        
        List<Review> reviews = reviewRepository.findByRecipeIdOrderByReviewDateDesc(recipeId);
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all reviews by user
     */
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        log.info("Fetching reviews for user ID: {}", userId);
        
        List<Review> reviews = reviewRepository.findByUserIdOrderByReviewDateDesc(userId);
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get review by ID
     */
    public Optional<ReviewResponse> getReviewById(Long id) {
        log.info("Fetching review with ID: {}", id);
        
        return reviewRepository.findById(id)
                .map(this::convertToResponse);
    }
    
    /**
     * Update review
     */
    @Transactional
    public Optional<ReviewResponse> updateReview(Long id, ReviewRequest request) {
        log.info("Updating review with ID: {}", id);
        
        return reviewRepository.findById(id)
                .map(review -> {
                    review.setReviewText(request.getReviewText());
                    review.setRating(request.getRating());
                    if (request.getReviewDate() != null) {
                        review.setReviewDate(request.getReviewDate());
                    }
                    
                    Review updatedReview = reviewRepository.save(review);
                    log.info("Review updated with ID: {}", updatedReview.getId());
                    return convertToResponse(updatedReview);
                });
    }
    
    /**
     * Delete review
     */
    @Transactional
    public boolean deleteReview(Long id) {
        log.info("Deleting review with ID: {}", id);
        
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            log.info("Review deleted with ID: {}", id);
            return true;
        }
        return false;
    }
    
    /**
     * Get average rating for a recipe
     */
    public Double getAverageRating(Long recipeId) {
        log.info("Getting average rating for recipe ID: {}", recipeId);
        
        Double averageRating = reviewRepository.getAverageRatingByRecipeId(recipeId);
        return averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0;
    }
    
    /**
     * Get review count for a recipe
     */
    public Long getReviewCount(Long recipeId) {
        log.info("Getting review count for recipe ID: {}", recipeId);
        
        return reviewRepository.getReviewCountByRecipeId(recipeId);
    }
    
    /**
     * Get recent reviews
     */
    public List<ReviewResponse> getRecentReviews() {
        log.info("Fetching recent reviews");
        
        List<Review> reviews = reviewRepository.findRecentReviews();
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert Review entity to ReviewResponse DTO
     */
    private ReviewResponse convertToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setRecipeId(review.getRecipe().getId());
        response.setRecipeTitle(review.getRecipe().getRecipeTitle());
        response.setUserId(review.getUserId());
        response.setReviewText(review.getReviewText());
        response.setRating(review.getRating());
        response.setReviewDate(review.getReviewDate());
        response.setCreatedAt(review.getCreatedAt());
        response.setUpdatedAt(review.getUpdatedAt());
        return response;
    }
}
