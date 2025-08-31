package com.recipe.smartrecipe.controller;

import com.recipe.smartrecipe.dto.RecipeRequest;
import com.recipe.smartrecipe.dto.RecipeResponse;
import com.recipe.smartrecipe.dto.EmailRequest;
import com.recipe.smartrecipe.entity.Recipe;
import com.recipe.smartrecipe.entity.RecipeRequestEntity;
import com.recipe.smartrecipe.service.RecipeService;
import com.recipe.smartrecipe.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RequiredArgsConstructor
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;
    private final EmailService emailService;

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
}
