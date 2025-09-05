package com.recipe.smartrecipe.controller;

import com.recipe.smartrecipe.dto.RecipeRequest;
import com.recipe.smartrecipe.dto.RecipeResponse;
import com.recipe.smartrecipe.dto.EmailRequest;
import com.recipe.smartrecipe.dto.ReviewRequest;
import com.recipe.smartrecipe.dto.ReviewResponse;
import com.recipe.smartrecipe.dto.ReviewEmailRequest;
import com.recipe.smartrecipe.entity.Recipe;
import com.recipe.smartrecipe.entity.RecipeRequestEntity;
import com.recipe.smartrecipe.service.RecipeService;
import com.recipe.smartrecipe.service.EmailService;
import com.recipe.smartrecipe.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;
    private final EmailService emailService;
    private final ReviewService reviewService;

    // ===== RECIPE GENERATION =====
    @PostMapping("/generate")
    public ResponseEntity<RecipeResponse> generateRecipe(
            @Valid @RequestBody RecipeRequest request,
            @RequestHeader("X-USER-ID") Long userId) {
        
        log.info("Generating recipe for user {} with ingredients: {}", userId, request.getIngredients());
        
        try {
            String recipeContent = recipeService.generateRecipe(request, userId);
            RecipeResponse response = new RecipeResponse(recipeContent);
            
            log.info("Recipe generated and saved successfully for user {}", userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error generating recipe for user {}: {}", userId, e.getMessage(), e);
            RecipeResponse errorResponse = new RecipeResponse("", "error");
            errorResponse.setMessage("Failed to generate recipe: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // ===== RECIPE CRUD ENDPOINTS =====
    
    // Create Recipe
    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody Recipe recipe) {
        log.info("Creating new recipe");
        try {
            Recipe createdRecipe = recipeService.createRecipe(recipe);
            return ResponseEntity.ok(createdRecipe);
        } catch (Exception e) {
            log.error("Error creating recipe: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get Recipe by ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        log.info("Fetching recipe with ID: {}", id);
        try {
            return recipeService.getRecipeById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching recipe with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get All Recipes for User
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recipe>> getUserRecipes(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        
        log.info("Fetching {} recipes for user {}", limit, userId);
        
        try {
            List<Recipe> recipes = recipeService.getUserRecipes(userId, limit);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            log.error("Error fetching recipes for user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get All Recipes (Admin)
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        log.info("Fetching all recipes");
        try {
            List<Recipe> recipes = recipeService.getAllRecipes();
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            log.error("Error fetching all recipes: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Update Recipe
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody Recipe recipeDetails) {
        
        log.info("Updating recipe with ID: {}", id);
        
        try {
            return recipeService.updateRecipe(id, recipeDetails)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error updating recipe with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Delete Recipe
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id) {
        log.info("Deleting recipe with ID: {}", id);
        
        try {
            boolean deleted = recipeService.deleteRecipe(id);
            if (deleted) {
                return ResponseEntity.ok("Recipe deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error deleting recipe with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ===== RECIPE REQUEST CRUD ENDPOINTS =====
    
    // Create Recipe Request
    @PostMapping("/requests")
    public ResponseEntity<RecipeRequestEntity> createRecipeRequest(@Valid @RequestBody RecipeRequestEntity request) {
        log.info("Creating new recipe request");
        try {
            RecipeRequestEntity createdRequest = recipeService.createRecipeRequest(request);
            return ResponseEntity.ok(createdRequest);
        } catch (Exception e) {
            log.error("Error creating recipe request: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get Recipe Request by ID
    @GetMapping("/requests/{id}")
    public ResponseEntity<RecipeRequestEntity> getRecipeRequestById(@PathVariable Long id) {
        log.info("Fetching recipe request with ID: {}", id);
        try {
            return recipeService.getRecipeRequestById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching recipe request with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get All Recipe Requests for User
    @GetMapping("/user/{userId}/requests")
    public ResponseEntity<List<RecipeRequestEntity>> getUserRequests(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        
        log.info("Fetching {} requests for user {}", limit, userId);
        
        try {
            List<RecipeRequestEntity> requests = recipeService.getUserRequests(userId, limit);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            log.error("Error fetching requests for user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get All Recipe Requests (Admin)
    @GetMapping("/requests")
    public ResponseEntity<List<RecipeRequestEntity>> getAllRecipeRequests() {
        log.info("Fetching all recipe requests");
        try {
            List<RecipeRequestEntity> requests = recipeService.getAllRecipeRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            log.error("Error fetching all recipe requests: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Update Recipe Request
    @PutMapping("/requests/{id}")
    public ResponseEntity<RecipeRequestEntity> updateRecipeRequest(
            @PathVariable Long id,
            @Valid @RequestBody RecipeRequestEntity requestDetails) {
        
        log.info("Updating recipe request with ID: {}", id);
        
        try {
            return recipeService.updateRecipeRequest(id, requestDetails)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error updating recipe request with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Delete Recipe Request
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<String> deleteRecipeRequest(@PathVariable Long id) {
        log.info("Deleting recipe request with ID: {}", id);
        
        try {
            boolean deleted = recipeService.deleteRecipeRequest(id);
            if (deleted) {
                return ResponseEntity.ok("Recipe request deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error deleting recipe request with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ===== SEARCH AND FILTER ENDPOINTS =====
    
    // Search Recipes by Ingredients
    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<Recipe>> searchRecipesByIngredients(
            @PathVariable Long userId,
            @RequestParam String ingredient) {
        
        log.info("Searching recipes for user {} containing ingredient: {}", userId, ingredient);
        
        try {
            List<Recipe> recipes = recipeService.searchRecipesByIngredients(userId, ingredient);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            log.error("Error searching recipes for user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get Recipes by Meal Type
    @GetMapping("/user/{userId}/meal-type/{mealType}")
    public ResponseEntity<List<Recipe>> getRecipesByMealType(
            @PathVariable Long userId,
            @PathVariable String mealType) {
        
        log.info("Fetching {} recipes for user {}", mealType, userId);
        
        try {
            List<Recipe> recipes = recipeService.getRecipesByMealType(userId, mealType);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            log.error("Error fetching {} recipes for user {}: {}", mealType, userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get Recipes by Cuisine
    @GetMapping("/user/{userId}/cuisine/{cuisine}")
    public ResponseEntity<List<Recipe>> getRecipesByCuisine(
            @PathVariable Long userId,
            @PathVariable String cuisine) {
        
        log.info("Fetching {} recipes for user {}", cuisine, userId);
        
        try {
            List<Recipe> recipes = recipeService.getRecipesByCuisine(userId, cuisine);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            log.error("Error fetching {} recipes for user {}: {}", cuisine, userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get Recipes by Complexity
    @GetMapping("/user/{userId}/complexity/{complexity}")
    public ResponseEntity<List<Recipe>> getRecipesByComplexity(
            @PathVariable Long userId,
            @PathVariable String complexity) {
        
        log.info("Fetching {} recipes for user {}", complexity, userId);
        
        try {
            List<Recipe> recipes = recipeService.getRecipesByComplexity(userId, complexity);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            log.error("Error fetching {} recipes for user {}: {}", complexity, userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get Recipes by Cooking Time
    @GetMapping("/user/{userId}/cooking-time/{cookingTime}")
    public ResponseEntity<List<Recipe>> getRecipesByCookingTime(
            @PathVariable Long userId,
            @PathVariable String cookingTime) {
        
        log.info("Fetching {} recipes for user {}", cookingTime, userId);
        
        try {
            List<Recipe> recipes = recipeService.getRecipesByCookingTime(userId, cookingTime);
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            log.error("Error fetching {} recipes for user {}: {}", cookingTime, userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ===== EMAIL SERVICE =====
    
    @PostMapping("/send-email")
    public ResponseEntity<String> sendRecipeEmail(@Valid @RequestBody EmailRequest emailRequest) {
        log.info("Sending recipe email to: {}", emailRequest.getEmail());
        
        try {
            boolean emailSent = emailService.sendRecipeEmail(
                emailRequest.getEmail(), 
                emailRequest.getRecipeContent(), 
                emailRequest.getRecipeTitle()
            );
            
            if (emailSent) {
                log.info("Recipe email sent successfully to: {}", emailRequest.getEmail());
                return ResponseEntity.ok("Recipe sent successfully to " + emailRequest.getEmail());
            } else {
                log.error("Failed to send recipe email to: {}", emailRequest.getEmail());
                return ResponseEntity.internalServerError().body("Failed to send email");
            }
            
        } catch (Exception e) {
            log.error("Error sending recipe email to {}: {}", emailRequest.getEmail(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error sending email: " + e.getMessage());
        }
    }

    // ===== HEALTH CHECK =====
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Recipe service is healthy! 🍳");
    }

    // ===== EMAIL TEST ENDPOINT =====
    @PostMapping("/test-email")
    public ResponseEntity<String> testEmail(@RequestParam String email) {
        log.info("Testing email service with: {}", email);
        
        try {
            boolean emailSent = emailService.sendRecipeEmail(
                email, 
                "This is a test email from Smart Recipe Generator! 🍳", 
                "Test Recipe"
            );
            
            if (emailSent) {
                return ResponseEntity.ok("Test email sent successfully to " + email);
            } else {
                return ResponseEntity.internalServerError().body("Failed to send test email");
            }
            
        } catch (Exception e) {
            log.error("Error sending test email to {}: {}", email, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // ===== ENVIRONMENT CHECK ENDPOINT =====
    @GetMapping("/check-env")
    public ResponseEntity<String> checkEnvironment() {
        try {
            // This will help us see if environment variables are loaded
            return ResponseEntity.ok("Environment check - check logs for details");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // ===== REVIEW ENDPOINTS =====
    
    // Create Review
    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request) {
        log.info("Creating review for recipe ID: {}", request.getRecipeId());
        
        try {
            ReviewResponse review = reviewService.createReview(request);
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            log.error("Error creating review: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get Reviews by Recipe ID
    @GetMapping("/{recipeId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsByRecipeId(@PathVariable Long recipeId) {
        log.info("Fetching reviews for recipe ID: {}", recipeId);
        
        try {
            List<ReviewResponse> reviews = reviewService.getReviewsByRecipeId(recipeId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            log.error("Error fetching reviews for recipe {}: {}", recipeId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get Reviews by User ID
    @GetMapping("/user/{userId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@PathVariable Long userId) {
        log.info("Fetching reviews for user ID: {}", userId);
        
        try {
            List<ReviewResponse> reviews = reviewService.getReviewsByUserId(userId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            log.error("Error fetching reviews for user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get Review by ID
    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long id) {
        log.info("Fetching review with ID: {}", id);
        
        try {
            return reviewService.getReviewById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching review with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Update Review
    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequest request) {
        
        log.info("Updating review with ID: {}", id);
        
        try {
            return reviewService.updateReview(id, request)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error updating review with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Delete Review
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        log.info("Deleting review with ID: {}", id);
        
        try {
            boolean deleted = reviewService.deleteReview(id);
            if (deleted) {
                return ResponseEntity.ok("Review deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error deleting review with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Get Recipe Statistics
    @GetMapping("/{recipeId}/stats")
    public ResponseEntity<Map<String, Object>> getRecipeStats(@PathVariable Long recipeId) {
        log.info("Fetching stats for recipe ID: {}", recipeId);
        
        try {
            Double averageRating = reviewService.getAverageRating(recipeId);
            Long reviewCount = reviewService.getReviewCount(recipeId);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("averageRating", averageRating);
            stats.put("reviewCount", reviewCount);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching stats for recipe {}: {}", recipeId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Send Review via Email
    @PostMapping("/reviews/send-email")
    public ResponseEntity<String> sendReviewEmail(@Valid @RequestBody ReviewEmailRequest emailRequest) {
        log.info("Sending review email to: {}", emailRequest.getEmail());
        
        try {
            String emailContent = createReviewEmailContent(
                emailRequest.getReviewContent(),
                emailRequest.getRecipeTitle(),
                emailRequest.getReviewerName(),
                emailRequest.getRating()
            );
            
            boolean emailSent = emailService.sendRecipeEmail(
                emailRequest.getEmail(),
                emailContent,
                "Review for: " + emailRequest.getRecipeTitle()
            );
            
            if (emailSent) {
                log.info("Review email sent successfully to: {}", emailRequest.getEmail());
                return ResponseEntity.ok("Review sent successfully to " + emailRequest.getEmail());
            } else {
                log.error("Failed to send review email to: {}", emailRequest.getEmail());
                return ResponseEntity.internalServerError().body("Failed to send email");
            }
            
        } catch (Exception e) {
            log.error("Error sending review email to {}: {}", emailRequest.getEmail(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error sending email: " + e.getMessage());
        }
    }
    
    // Helper method to create review email content
    private String createReviewEmailContent(String reviewContent, String recipeTitle, 
                                          String reviewerName, Integer rating) {
        String stars = "★".repeat(rating != null ? rating : 0);
        String reviewer = reviewerName != null ? reviewerName : "Anonymous";
        
        return String.format("""
            🍳 Recipe Review
            
            Recipe: %s
            Reviewer: %s
            Rating: %s (%d/5)
            
            Review:
            %s
            
            ---
            Generated by Smart Recipe Generator
            """, recipeTitle, reviewer, stars, rating != null ? rating : 0, reviewContent);
    }
}
