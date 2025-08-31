package com.recipe.smartrecipe.service;

import com.recipe.smartrecipe.dto.RecipeRequest;
import com.recipe.smartrecipe.entity.Recipe;
import com.recipe.smartrecipe.entity.RecipeRequestEntity;
import com.recipe.smartrecipe.entity.User;
import com.recipe.smartrecipe.repository.RecipeRepository;
import com.recipe.smartrecipe.repository.RecipeRequestRepository;
import com.recipe.smartrecipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeRequestRepository recipeRequestRepository;
    private final UserRepository userRepository;

    // ===== RECIPE GENERATION =====
    public String generateRecipe(RecipeRequest request, Long userId) {
        log.info("Generating recipe for user {} with: {}", userId, request);
        
        // Ensure user exists, create demo user if needed
        User user = ensureUserExists(userId);
        
        // First save the recipe request
        RecipeRequestEntity requestEntity = new RecipeRequestEntity();
        requestEntity.setUserId(user.getId());
        requestEntity.setIngredients(request.getIngredients());
        requestEntity.setMealType(request.getMealType());
        requestEntity.setCuisine(request.getCuisine());
        requestEntity.setCookingTime(request.getCookingTime());
        requestEntity.setComplexity(request.getComplexity());
        
        RecipeRequestEntity savedRequest = recipeRequestRepository.save(requestEntity);
        log.info("Recipe request saved with ID: {}", savedRequest.getId());
        
        // Generate the recipe content
        String recipeContent = generateMockRecipe(request);
        
        // Save the generated recipe
        Recipe recipe = new Recipe();
        recipe.setUserId(user.getId());
        recipe.setRequestId(savedRequest.getId());
        recipe.setContent(recipeContent);
        
        Recipe savedRecipe = recipeRepository.save(recipe);
        log.info("Recipe saved to database with ID: {}", savedRecipe.getId());
        
        return recipeContent;
    }

    // ===== USER MANAGEMENT =====
    
    private User ensureUserExists(Long userId) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        
        try {
            // Create demo user if it doesn't exist
            User demoUser = new User();
            // Don't set ID, let MySQL auto-generate it
            demoUser.setUsername("demo_user_" + userId);
            demoUser.setEmail("demo" + userId + "@example.com");
            demoUser.setPassword("demo_password");
            demoUser.setHash("demo_hash");
            demoUser.setPasswordHash("demo_password_hash");
            
            User savedUser = userRepository.save(demoUser);
            log.info("Created demo user with ID: {}", savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            log.error("Failed to create user: {}", e.getMessage());
            // If user creation fails, create a minimal user object for the session
            User tempUser = new User();
            tempUser.setId(userId);
            tempUser.setUsername("temp_user");
            tempUser.setEmail("temp@example.com");
            return tempUser;
        }
    }

    // ===== RECIPE CRUD OPERATIONS =====
    
    // Create Recipe
    public Recipe createRecipe(Recipe recipe) {
        log.info("Creating new recipe for user: {}", recipe.getUserId());
        recipe.setCreatedAt(LocalDateTime.now());
        return recipeRepository.save(recipe);
    }
    
    // Read Recipe by ID
    public Optional<Recipe> getRecipeById(Long id) {
        log.info("Fetching recipe by ID: {}", id);
        return recipeRepository.findById(id);
    }
    
    // Read All Recipes for User
    public List<Recipe> getUserRecipes(Long userId, int limit) {
        log.info("Fetching {} recent recipes for user {}", limit, userId);
        return recipeRepository.findRecentRecipesByUserId(userId, limit);
    }
    
    // Read All Recipes (Admin)
    public List<Recipe> getAllRecipes() {
        log.info("Fetching all recipes");
        return recipeRepository.findAll();
    }
    
    // Update Recipe
    public Optional<Recipe> updateRecipe(Long id, Recipe recipeDetails) {
        log.info("Updating recipe with ID: {}", id);
        
        return recipeRepository.findById(id).map(existingRecipe -> {
            existingRecipe.setContent(recipeDetails.getContent());
            existingRecipe.setUserId(recipeDetails.getUserId());
            existingRecipe.setRequestId(recipeDetails.getRequestId());
            return recipeRepository.save(existingRecipe);
        });
    }
    
    // Delete Recipe
    public boolean deleteRecipe(Long id) {
        log.info("Deleting recipe with ID: {}", id);
        
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            log.info("Recipe deleted successfully");
            return true;
        } else {
            log.warn("Recipe with ID {} not found for deletion", id);
            return false;
        }
    }

    // ===== RECIPE REQUEST CRUD OPERATIONS =====
    
    // Create Recipe Request
    public RecipeRequestEntity createRecipeRequest(RecipeRequestEntity request) {
        log.info("Creating new recipe request for user: {}", request.getUserId());
        request.setCreatedAt(LocalDateTime.now());
        return recipeRequestRepository.save(request);
    }
    
    // Read Recipe Request by ID
    public Optional<RecipeRequestEntity> getRecipeRequestById(Long id) {
        log.info("Fetching recipe request by ID: {}", id);
        return recipeRequestRepository.findById(id);
    }
    
    // Read All Recipe Requests for User
    public List<RecipeRequestEntity> getUserRequests(Long userId, int limit) {
        log.info("Fetching {} recent requests for user {}", limit, userId);
        return recipeRequestRepository.findRecentRequestsByUserId(userId, limit);
    }
    
    // Read All Recipe Requests (Admin)
    public List<RecipeRequestEntity> getAllRecipeRequests() {
        log.info("Fetching all recipe requests");
        return recipeRequestRepository.findAll();
    }
    
    // Update Recipe Request
    public Optional<RecipeRequestEntity> updateRecipeRequest(Long id, RecipeRequestEntity requestDetails) {
        log.info("Updating recipe request with ID: {}", id);
        
        return recipeRequestRepository.findById(id).map(existingRequest -> {
            existingRequest.setIngredients(requestDetails.getIngredients());
            existingRequest.setMealType(requestDetails.getMealType());
            existingRequest.setCuisine(requestDetails.getCuisine());
            existingRequest.setCookingTime(requestDetails.getCookingTime());
            existingRequest.setComplexity(requestDetails.getComplexity());
            return recipeRequestRepository.save(existingRequest);
        });
    }
    
    // Delete Recipe Request
    public boolean deleteRecipeRequest(Long id) {
        log.info("Deleting recipe request with ID: {}", id);
        
        if (recipeRequestRepository.existsById(id)) {
            recipeRequestRepository.deleteById(id);
            log.info("Recipe request deleted successfully");
            return true;
        } else {
            log.warn("Recipe request with ID {} not found for deletion", id);
            return false;
        }
    }

    // ===== SEARCH AND FILTER OPERATIONS =====
    
    // Search Recipes by Ingredients
    public List<Recipe> searchRecipesByIngredients(Long userId, String ingredient) {
        log.info("Searching recipes for user {} containing ingredient: {}", userId, ingredient);
        return recipeRepository.findByUserIdAndIngredientsContaining(userId, ingredient);
    }
    
    // Get Recipes by Meal Type
    public List<Recipe> getRecipesByMealType(Long userId, String mealType) {
        log.info("Fetching {} recipes for user {}", mealType, userId);
        return recipeRepository.findByUserIdAndMealType(userId, mealType);
    }
    
    // Get Recipes by Cuisine
    public List<Recipe> getRecipesByCuisine(Long userId, String cuisine) {
        log.info("Fetching {} recipes for user {}", cuisine, userId);
        return recipeRepository.findByUserIdAndCuisine(userId, cuisine);
    }
    
    // Get Recipes by Complexity
    public List<Recipe> getRecipesByComplexity(Long userId, String complexity) {
        log.info("Fetching {} recipes for user {}", complexity, userId);
        return recipeRepository.findByUserIdAndComplexity(userId, complexity);
    }
    
    // Get Recipes by Cooking Time
    public List<Recipe> getRecipesByCookingTime(Long userId, String cookingTime) {
        log.info("Fetching {} recipes for user {}", cookingTime, userId);
        return recipeRepository.findByUserIdAndCookingTime(userId, cookingTime);
    }

    // ===== UTILITY METHODS =====
    
    private String generateMockRecipe(RecipeRequest request) {
        StringBuilder recipe = new StringBuilder();
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        
        recipe.append("🍳 ").append(request.getMealType()).append(" RECIPE\n");
        recipe.append("⏰ Generated on: ").append(currentTime).append("\n");
        recipe.append("🌍 Cuisine: ").append(request.getCuisine()).append("\n");
        recipe.append("⏱️  Cooking Time: ").append(formatCookingTime(request.getCookingTime())).append("\n");
        recipe.append("📊 Difficulty: ").append(request.getComplexity()).append("\n\n");
        
        recipe.append("📝 INGREDIENTS:\n");
        String[] ingredients = request.getIngredients().split(",");
        for (int i = 0; i < ingredients.length; i++) {
            recipe.append("   ").append(i + 1).append(". ").append(ingredients[i].trim()).append("\n");
        }
        
        // Generate unique instructions based on ingredients and preferences
        recipe.append("\n👨‍🍳 INSTRUCTIONS:\n");
        String[] instructions = generateUniqueInstructions(request);
        for (int i = 0; i < instructions.length; i++) {
            recipe.append("   ").append(i + 1).append(". ").append(instructions[i]).append("\n");
        }
        
        // Generate unique cooking tips based on cuisine and complexity
        recipe.append("\n💡 COOKING TIPS:\n");
        String[] tips = generateUniqueCookingTips(request);
        for (String tip : tips) {
            recipe.append("   • ").append(tip).append("\n");
        }
        
        // Generate dynamic prep time, servings, and calories
        recipe.append("\n⏰ Estimated Prep Time: ").append(getDynamicPrepTime(request)).append("\n");
        recipe.append("👥 Servings: ").append(getDynamicServings(request)).append("\n");
        recipe.append("🔥 Calories per serving: ").append(getDynamicCalories(request)).append("\n\n");
        
        recipe.append("Bon appétit! 🎉");
        
        return recipe.toString();
    }
    
    private String[] generateUniqueInstructions(RecipeRequest request) {
        String[] ingredients = request.getIngredients().toLowerCase().split(",");
        String cuisine = request.getCuisine().toLowerCase();
        String complexity = request.getComplexity().toLowerCase();
        String cookingTime = request.getCookingTime();
        Random random = new Random();
        
        // Base instructions that adapt to ingredients and preferences
        List<String> baseInstructions = new ArrayList<>();
        
        // Step 1: Ingredient preparation (varies by ingredients)
        if (ingredients.length > 0) {
            String firstIngredient = ingredients[0].trim();
            if (firstIngredient.contains("egg")) {
                baseInstructions.add("Crack and whisk the eggs in a bowl, season with salt and pepper");
            } else if (firstIngredient.contains("milk")) {
                baseInstructions.add("Measure and warm the milk slightly (not boiling)");
            } else if (firstIngredient.contains("tomato")) {
                baseInstructions.add("Wash and dice the tomatoes into small cubes");
            } else if (firstIngredient.contains("potato")) {
                baseInstructions.add("Wash and dice the potatoes into small cubes");
            } else if (firstIngredient.contains("onion")) {
                baseInstructions.add("Peel and finely dice the onion");
            } else if (firstIngredient.contains("chicken")) {
                baseInstructions.add("Cut the chicken into bite-sized pieces and season with salt and pepper");
            } else {
                baseInstructions.add("Prepare your ingredients by washing and chopping as needed");
            }
        }
        
        // Step 2: Cooking method (varies by cuisine and complexity)
        if (cuisine.equals("italian")) {
            baseInstructions.add(getRandomVariation("italian_cooking", random));
        } else if (cuisine.equals("asian")) {
            baseInstructions.add(getRandomVariation("asian_cooking", random));
        } else if (cuisine.equals("mexican")) {
            baseInstructions.add(getRandomVariation("mexican_cooking", random));
        } else if (cuisine.equals("indian")) {
            baseInstructions.add(getRandomVariation("indian_cooking", random));
        } else {
            baseInstructions.add(getRandomVariation("general_cooking", random));
        }
        
        // Step 3: Ingredient cooking (varies by ingredients and combinations)
        if (ingredients.length > 1) {
            // Special handling for common ingredient combinations
            boolean hasEggs = request.getIngredients().toLowerCase().contains("egg");
            boolean hasTomatoes = request.getIngredients().toLowerCase().contains("tomato");
            boolean hasMilk = request.getIngredients().toLowerCase().contains("milk");
            boolean hasPotatoes = request.getIngredients().toLowerCase().contains("potato");
            boolean hasOnions = request.getIngredients().toLowerCase().contains("onion");
            
            if (hasEggs && hasTomatoes && !hasMilk) {
                // Scrambled eggs with tomatoes
                baseInstructions.add("Add diced tomatoes to the pan and sauté for 2-3 minutes until softened");
                baseInstructions.add("Pour the whisked eggs over the tomatoes and cook, stirring gently until eggs are set");
            } else if (hasMilk && hasEggs) {
                // Custard or creamy egg dish
                baseInstructions.add("Slowly pour the warm milk into the eggs while whisking constantly");
                baseInstructions.add("Cook over low heat, stirring continuously until thickened to custard consistency");
            } else if (hasPotatoes && hasOnions) {
                // Potato and onion dish
                baseInstructions.add("Add diced onions to the pan and sauté until translucent");
                baseInstructions.add("Add potato cubes and cook, stirring occasionally, until potatoes are tender");
            } else if (hasTomatoes && hasOnions) {
                // Tomato and onion base
                baseInstructions.add("Sauté onions until golden, then add tomatoes and cook until they break down");
            } else {
                baseInstructions.add("Add your prepared ingredients to the pan in order of cooking time needed");
                baseInstructions.add("Cook each ingredient until tender before adding the next");
            }
        } else {
            baseInstructions.add("Add your prepared ingredients to the pan and cook until fragrant and tender");
        }
        
        // Step 4: Seasoning and finishing (varies by cuisine)
        if (cuisine.equals("italian")) {
            baseInstructions.add(getRandomVariation("italian_seasoning", random));
        } else if (cuisine.equals("mexican")) {
            baseInstructions.add(getRandomVariation("mexican_seasoning", random));
        } else if (cuisine.equals("asian")) {
            baseInstructions.add(getRandomVariation("asian_seasoning", random));
        } else if (cuisine.equals("indian")) {
            baseInstructions.add(getRandomVariation("indian_seasoning", random));
        } else if (cuisine.equals("french")) {
            baseInstructions.add(getRandomVariation("french_seasoning", random));
        } else {
            baseInstructions.add("Season with salt, pepper, and herbs that complement your ingredients");
        }
        
        // Step 5: Final cooking (varies by complexity and cooking time)
        if (cookingTime.equals("UNDER_30")) {
            baseInstructions.add(getRandomVariation("quick_cooking", random));
        } else if (cookingTime.equals("MIN_30_60")) {
            baseInstructions.add(getRandomVariation("medium_cooking", random));
        } else {
            baseInstructions.add(getRandomVariation("slow_cooking", random));
        }
        
        // Step 6: Serving (varies by complexity and meal type)
        if (complexity.equals("beginner")) {
            baseInstructions.add("Serve hot and enjoy your delicious " + request.getMealType().toLowerCase() + "!");
        } else if (complexity.equals("intermediate")) {
            baseInstructions.add("Plate with care, ensuring good visual presentation before serving");
        } else {
            baseInstructions.add("Plate beautifully with garnishes and arrange ingredients artistically");
        }
        
        return baseInstructions.toArray(new String[0]);
    }
    
    private String getRandomVariation(String type, Random random) {
        switch (type) {
            case "italian_cooking":
                String[] italianMethods = {
                    "Heat extra virgin olive oil in a large pan over medium heat",
                    "Warm olive oil in a deep skillet until shimmering",
                    "Heat a generous amount of olive oil in a heavy-bottomed pan"
                };
                return italianMethods[random.nextInt(italianMethods.length)];
                
            case "asian_cooking":
                String[] asianMethods = {
                    "Heat a wok or large pan with a tablespoon of vegetable oil until smoking hot",
                    "Get your wok smoking hot with oil before adding ingredients",
                    "Heat oil in a wok until it's almost smoking, then add aromatics"
                };
                return asianMethods[random.nextInt(asianMethods.length)];
                
            case "mexican_cooking":
                String[] mexicanMethods = {
                    "Heat a cast-iron skillet over medium-high heat with oil",
                    "Get your comal or skillet very hot before starting",
                    "Heat oil in a heavy pan until it shimmers and is hot"
                };
                return mexicanMethods[random.nextInt(mexicanMethods.length)];
                
            case "indian_cooking":
                String[] indianMethods = {
                    "Heat ghee or oil in a deep pan and add whole spices until fragrant",
                    "Warm oil in a kadai and temper with whole spices",
                    "Heat oil and add whole spices, letting them crackle and release aroma"
                };
                return indianMethods[random.nextInt(indianMethods.length)];
                
            case "general_cooking":
                String[] generalMethods = {
                    "Heat a large pan over medium heat with cooking oil",
                    "Warm oil in a skillet until it's hot but not smoking",
                    "Heat a pan with oil over medium heat until shimmering"
                };
                return generalMethods[random.nextInt(generalMethods.length)];
                
            case "italian_seasoning":
                String[] italianSeasonings = {
                    "Season with Italian herbs like basil, oregano, and garlic",
                    "Add fresh basil, dried oregano, and minced garlic for authentic flavor",
                    "Finish with Italian seasoning blend and fresh garlic"
                };
                return italianSeasonings[random.nextInt(italianSeasonings.length)];
                
            case "mexican_seasoning":
                String[] mexicanSeasonings = {
                    "Season with cumin, chili powder, and fresh cilantro",
                    "Add ground cumin, smoked paprika, and chopped cilantro",
                    "Season with Mexican spices and finish with fresh herbs"
                };
                return mexicanSeasonings[random.nextInt(mexicanSeasonings.length)];
                
            case "asian_seasoning":
                String[] asianSeasonings = {
                    "Season with soy sauce, ginger, and garlic",
                    "Add light soy sauce, fresh ginger, and minced garlic",
                    "Season with Asian sauces and aromatics for authentic flavor"
                };
                return asianSeasonings[random.nextInt(asianSeasonings.length)];
                
            case "indian_seasoning":
                String[] indianSeasonings = {
                    "Add ground spices like turmeric, cumin, and coriander",
                    "Season with garam masala, turmeric, and ground spices",
                    "Add Indian spice blend and ground aromatics"
                };
                return indianSeasonings[random.nextInt(indianSeasonings.length)];
                
            case "french_seasoning":
                String[] frenchSeasonings = {
                    "Finish with fresh herbs like thyme, rosemary, and a splash of wine",
                    "Add French herbs and deglaze with white wine",
                    "Season with herbes de Provence and finish with wine"
                };
                return frenchSeasonings[random.nextInt(frenchSeasonings.length)];
                
            case "quick_cooking":
                String[] quickMethods = {
                    "Cook quickly over medium-high heat until all ingredients are well combined and heated through",
                    "Stir-fry over high heat for quick, even cooking",
                    "Cook rapidly over medium-high heat to preserve texture and flavor"
                };
                return quickMethods[random.nextInt(quickMethods.length)];
                
            case "medium_cooking":
                String[] mediumMethods = {
                    "Simmer over medium heat for 15-20 minutes until flavors meld and develop depth",
                    "Cook gently over medium heat to allow flavors to combine",
                    "Simmer slowly to develop rich, layered flavors"
                };
                return mediumMethods[random.nextInt(mediumMethods.length)];
                
            case "slow_cooking":
                String[] slowMethods = {
                    "Cook over low heat for 30-45 minutes until rich, complex flavors develop",
                    "Simmer gently over low heat to build deep, complex flavors",
                    "Cook slowly to allow all flavors to meld and develop richness"
                };
                return slowMethods[random.nextInt(slowMethods.length)];
                
            default:
                return "Cook until ingredients are well combined and flavorful";
        }
    }
    
    private String[] generateUniqueCookingTips(RecipeRequest request) {
        String cuisine = request.getCuisine().toLowerCase();
        String complexity = request.getComplexity().toLowerCase();
        String[] ingredients = request.getIngredients().toLowerCase().split(",");
        Random random = new Random();
        
        List<String> tips = new ArrayList<>();
        
        // Tip 1: Cuisine-specific advice
        if (cuisine.equals("italian")) {
            tips.add(getRandomTip("italian_tips", random));
        } else if (cuisine.equals("mexican")) {
            tips.add(getRandomTip("mexican_tips", random));
        } else if (cuisine.equals("asian")) {
            tips.add(getRandomTip("asian_tips", random));
        } else if (cuisine.equals("indian")) {
            tips.add(getRandomTip("indian_tips", random));
        } else if (cuisine.equals("french")) {
            tips.add(getRandomTip("french_tips", random));
        } else {
            tips.add("Experiment with different cooking methods to discover new flavors");
        }
        
        // Tip 2: Complexity-based advice
        if (complexity.equals("beginner")) {
            tips.add(getRandomTip("beginner_tips", random));
        } else if (complexity.equals("intermediate")) {
            tips.add(getRandomTip("intermediate_tips", random));
        } else {
            tips.add(getRandomTip("advanced_tips", random));
        }
        
        // Tip 3: Ingredient-specific tips
        if (request.getIngredients().toLowerCase().contains("egg")) {
            if (request.getIngredients().toLowerCase().contains("milk")) {
                tips.add("For creamier eggs, add a splash of milk before whisking");
            } else {
                tips.add("For fluffier eggs, add a splash of water before whisking");
            }
        }
        if (request.getIngredients().toLowerCase().contains("tomato")) {
            if (request.getIngredients().toLowerCase().contains("egg")) {
                tips.add("Use ripe tomatoes for the best flavor in your egg dish");
            } else {
                tips.add("Use ripe tomatoes for the best flavor, or roast them for deeper taste");
            }
        }
        if (request.getIngredients().toLowerCase().contains("milk")) {
            if (request.getIngredients().toLowerCase().contains("egg")) {
                tips.add("Warm milk slightly before using to prevent curdling in custards");
            } else {
                tips.add("Use whole milk for richer flavor, or skim for lighter dishes");
            }
        }
        if (request.getIngredients().toLowerCase().contains("potato")) {
            tips.add("Cut potatoes into uniform sizes for even cooking");
        }
        if (request.getIngredients().toLowerCase().contains("onion")) {
            tips.add("Let onions cook slowly to develop natural sweetness");
        }
        
        // Tip 4: Cooking time specific tips
        if (request.getCookingTime().equals("UNDER_30")) {
            tips.add(getRandomTip("quick_tips", random));
        } else if (request.getCookingTime().equals("MIN_30_60")) {
            tips.add(getRandomTip("medium_tips", random));
        } else {
            tips.add(getRandomTip("slow_tips", random));
        }
        
        // Tip 5: Meal type specific tips
        if (request.getMealType().equals("BREAKFAST")) {
            tips.add("Prep ingredients the night before for a stress-free morning");
        } else if (request.getMealType().equals("LUNCH")) {
            tips.add("This recipe works great for meal prep and leftovers");
        } else if (request.getMealType().equals("DINNER")) {
            tips.add("Pair with a simple side dish for a complete meal");
        } else if (request.getMealType().equals("SNACK")) {
            tips.add("Perfect for sharing or enjoying as a light meal");
        }
        
        // Tip 6: General cooking advice
        tips.add("Taste as you cook and adjust seasoning gradually");
        tips.add("Don't be afraid to make this recipe your own with personal touches");
        
        return tips.toArray(new String[0]);
    }
    
    private String getRandomTip(String type, Random random) {
        switch (type) {
            case "italian_tips":
                String[] italianTips = {
                    "Use extra virgin olive oil for authentic Italian flavor",
                    "Finish with a drizzle of good quality olive oil",
                    "Use fresh herbs for the most authentic taste"
                };
                return italianTips[random.nextInt(italianTips.length)];
                
            case "mexican_tips":
                String[] mexicanTips = {
                    "Toast your spices briefly in a dry pan to enhance their flavor",
                    "Use fresh lime juice to brighten the flavors",
                    "Add a pinch of Mexican oregano for authentic taste"
                };
                return mexicanTips[random.nextInt(mexicanTips.length)];
                
            case "asian_tips":
                String[] asianTips = {
                    "Prepare all ingredients before starting (mise en place) for quick cooking",
                    "Use high heat for authentic stir-fry technique",
                    "Finish with a splash of sesame oil for authentic flavor"
                };
                return asianTips[random.nextInt(asianTips.length)];
                
            case "indian_tips":
                String[] indianTips = {
                    "Bloom whole spices in hot oil to release their essential oils",
                    "Use fresh ginger and garlic for the best flavor",
                    "Finish with fresh cilantro for authentic Indian taste"
                };
                return indianTips[random.nextInt(indianTips.length)];
                
            case "french_tips":
                String[] frenchTips = {
                    "Use butter and wine to create rich, layered flavors",
                    "Deglaze the pan with wine to capture all the flavors",
                    "Use fresh herbs and quality butter for authentic French cooking"
                };
                return frenchTips[random.nextInt(frenchTips.length)];
                
            case "beginner_tips":
                String[] beginnerTips = {
                    "This recipe is perfect for beginner cooks - take your time and don't rush",
                    "Don't worry about perfection, focus on learning and enjoying the process",
                    "Keep it simple and build your confidence step by step"
                };
                return beginnerTips[random.nextInt(beginnerTips.length)];
                
            case "intermediate_tips":
                String[] intermediateTips = {
                    "Try adjusting the seasoning to develop your palate and confidence",
                    "Experiment with different herb combinations to find your favorites",
                    "Practice your knife skills while preparing ingredients"
                };
                return intermediateTips[random.nextInt(intermediateTips.length)];
                
            case "advanced_tips":
                String[] advancedTips = {
                    "Feel free to experiment with advanced techniques and flavor combinations",
                    "Try different cooking methods to achieve different textures",
                    "Use this as a base recipe and add your own creative twists"
                };
                return advancedTips[random.nextInt(advancedTips.length)];
                
            case "quick_tips":
                String[] quickTips = {
                    "Keep ingredients small and uniform for quick, even cooking",
                    "Use high heat for fast cooking while preserving texture",
                    "Prep everything before starting to ensure quick execution"
                };
                return quickTips[random.nextInt(quickTips.length)];
                
            case "medium_tips":
                String[] mediumTips = {
                    "Low and slow cooking develops deeper, more complex flavors",
                    "Take time to build layers of flavor during cooking",
                    "Medium heat allows flavors to develop without burning"
                };
                return mediumTips[random.nextInt(mediumTips.length)];
                
            case "slow_tips":
                String[] slowTips = {
                    "Long cooking times allow flavors to meld and develop richness",
                    "Patience is key - let the flavors develop naturally",
                    "Low heat prevents burning while building complex flavors"
                };
                return slowTips[random.nextInt(slowTips.length)];
                
            default:
                return "Experiment with different techniques to discover what works best for you";
        }
    }
    
    private String formatCookingTime(String cookingTime) {
        switch (cookingTime) {
            case "UNDER_30": return "Under 30 minutes";
            case "MIN_30_60": return "30-60 minutes";
            case "OVER_60": return "Over 60 minutes";
            default: return cookingTime;
        }
    }
    
    private String getDynamicPrepTime(RecipeRequest request) {
        String[] ingredients = request.getIngredients().toLowerCase().split(",");
        String complexity = request.getComplexity().toLowerCase();
        String cookingTime = request.getCookingTime();
        Random random = new Random();
        
        int basePrepTime = 15; // Reduced base preparation time
        
        // Add time for ingredient preparation
        if (request.getIngredients().toLowerCase().contains("salmon")) {
            basePrepTime += 8 + random.nextInt(4); // Add 8-12 minutes for salmon preparation
        }
        if (request.getIngredients().toLowerCase().contains("chicken")) {
            basePrepTime += 8 + random.nextInt(4); // Add 8-12 minutes for chicken preparation
        }
        if (request.getIngredients().toLowerCase().contains("beef")) {
            basePrepTime += 10 + random.nextInt(5); // Add 10-15 minutes for beef preparation
        }
        if (request.getIngredients().toLowerCase().contains("fish")) {
            basePrepTime += 8 + random.nextInt(4); // Add 8-12 minutes for fish preparation
        }
        if (request.getIngredients().toLowerCase().contains("egg")) {
            basePrepTime += 5 + random.nextInt(3); // Add 5-8 minutes for egg preparation
        }
        if (request.getIngredients().toLowerCase().contains("tomato")) {
            basePrepTime += 3 + random.nextInt(2); // Add 3-5 minutes for tomato preparation
        }
        if (request.getIngredients().toLowerCase().contains("milk")) {
            basePrepTime += 3 + random.nextInt(2); // Add 3-5 minutes for milk preparation
        }
        if (request.getIngredients().toLowerCase().contains("potato")) {
            basePrepTime += 8 + random.nextInt(4); // Add 8-12 minutes for potato preparation
        }
        if (request.getIngredients().toLowerCase().contains("onion")) {
            basePrepTime += 3 + random.nextInt(2); // Add 3-5 minutes for onion preparation
        }
        if (request.getIngredients().toLowerCase().contains("rice")) {
            basePrepTime += 5 + random.nextInt(3); // Add 5-8 minutes for rice preparation
        }
        if (request.getIngredients().toLowerCase().contains("pasta")) {
            basePrepTime += 3 + random.nextInt(2); // Add 3-5 minutes for pasta preparation
        }
        if (request.getIngredients().toLowerCase().contains("carrot")) {
            basePrepTime += 5 + random.nextInt(3); // Add 5-8 minutes for carrot preparation
        }
        if (request.getIngredients().toLowerCase().contains("spinach")) {
            basePrepTime += 3 + random.nextInt(2); // Add 3-5 minutes for spinach preparation
        }
        if (request.getIngredients().toLowerCase().contains("mushroom")) {
            basePrepTime += 5 + random.nextInt(3); // Add 5-8 minutes for mushroom preparation
        }
        
        // Adjust for complexity (but respect cooking time preference)
        if (complexity.equals("intermediate")) {
            basePrepTime += 5 + random.nextInt(3); // Add 5-8 minutes for intermediate complexity
        } else if (complexity.equals("advanced")) {
            basePrepTime += 8 + random.nextInt(4); // Add 8-12 minutes for advanced complexity
        }
        
        // RESPECT the cooking time preference - this is the key fix!
        if (cookingTime.equals("UNDER_30")) {
            // For UNDER_30, ensure total time doesn't exceed 30 minutes
            basePrepTime = Math.min(basePrepTime, 25); // Cap at 25 minutes to stay under 30 total
        } else if (cookingTime.equals("MIN_30_60")) {
            basePrepTime += 10 + random.nextInt(5); // Add 10-15 minutes for longer cooking times
        } else if (cookingTime.equals("OVER_60")) {
            basePrepTime += 15 + random.nextInt(10); // Add 15-25 minutes for very long cooking times
        }
        
        return basePrepTime + " minutes";
    }
    
    private String getDynamicServings(RecipeRequest request) {
        String[] ingredients = request.getIngredients().toLowerCase().split(",");
        String complexity = request.getComplexity().toLowerCase();
        String cookingTime = request.getCookingTime();
        Random random = new Random();
        
        int baseServings = 2; // Default servings
        
        if (request.getIngredients().toLowerCase().contains("egg")) {
            baseServings += 1 + random.nextInt(2); // Add 1-3 servings for egg dishes
        }
        if (request.getIngredients().toLowerCase().contains("tomato")) {
            baseServings += 1; // Add 1 serving for tomato dishes
        }
        if (request.getIngredients().toLowerCase().contains("milk")) {
            baseServings += 1; // Add 1 serving for milk-based dishes
        }
        if (request.getIngredients().toLowerCase().contains("potato")) {
            baseServings += 1 + random.nextInt(2); // Add 1-3 servings for potato dishes
        }
        if (request.getIngredients().toLowerCase().contains("onion")) {
            baseServings += 1; // Add 1 serving for onion dishes
        }
        
        if (complexity.equals("intermediate")) {
            baseServings += 1; // Add 1 serving for intermediate complexity
        } else if (complexity.equals("advanced")) {
            baseServings += 1 + random.nextInt(2); // Add 1-3 servings for advanced complexity
        }
        
        if (cookingTime.equals("MIN_30_60")) {
            baseServings += 1; // Add 1 serving for longer cooking times
        } else if (cookingTime.equals("OVER_60")) {
            baseServings += 1 + random.nextInt(2); // Add 1-3 servings for very long cooking times
        }
        
        // Ensure reasonable range
        baseServings = Math.max(2, Math.min(8, baseServings));
        
        if (baseServings == 1) {
            return "1 serving";
        } else if (baseServings == 2) {
            return "2 servings";
        } else if (baseServings <= 4) {
            return "2-4 servings";
        } else if (baseServings <= 6) {
            return "4-6 servings";
        } else {
            return "6-8 servings";
        }
    }
    
    private String getDynamicCalories(RecipeRequest request) {
        String[] ingredients = request.getIngredients().toLowerCase().split(",");
        String complexity = request.getComplexity().toLowerCase();
        String cookingTime = request.getCookingTime();
        Random random = new Random();
        
        int baseCalories = 350; // Default calories per serving
        
        if (request.getIngredients().toLowerCase().contains("egg")) {
            baseCalories += 100 + random.nextInt(50); // Add 100-150 calories for egg dishes
        }
        if (request.getIngredients().toLowerCase().contains("tomato")) {
            baseCalories += 50 + random.nextInt(25); // Add 50-75 calories for tomato dishes
        }
        if (request.getIngredients().toLowerCase().contains("milk")) {
            baseCalories += 100 + random.nextInt(50); // Add 100-150 calories for milk-based dishes
        }
        if (request.getIngredients().toLowerCase().contains("potato")) {
            baseCalories += 150 + random.nextInt(75); // Add 150-225 calories for potato dishes
        }
        if (request.getIngredients().toLowerCase().contains("onion")) {
            baseCalories += 50 + random.nextInt(25); // Add 50-75 calories for onion dishes
        }
        
        if (complexity.equals("intermediate")) {
            baseCalories += 50 + random.nextInt(25); // Add 50-75 calories for intermediate complexity
        } else if (complexity.equals("advanced")) {
            baseCalories += 100 + random.nextInt(50); // Add 100-150 calories for advanced complexity
        }
        
        if (cookingTime.equals("MIN_30_60")) {
            baseCalories += 50 + random.nextInt(25); // Add 50-75 calories for longer cooking times
        } else if (cookingTime.equals("OVER_60")) {
            baseCalories += 100 + random.nextInt(50); // Add 100-150 calories for very long cooking times
        }
        
        // Round to nearest 25 for cleaner display
        baseCalories = ((baseCalories + 12) / 25) * 25;
        
        return "~" + baseCalories + " kcal";
    }
}
