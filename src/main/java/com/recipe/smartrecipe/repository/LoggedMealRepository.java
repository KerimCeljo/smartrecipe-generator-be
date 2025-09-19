package com.recipe.smartrecipe.repository;

import com.recipe.smartrecipe.entity.LoggedMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoggedMealRepository extends JpaRepository<LoggedMeal, Long> {
    List<LoggedMeal> findByUserEmailOrderByLoggedAtDesc(String userEmail);
    List<LoggedMeal> findByUserEmailAndRecipeTitleContainingIgnoreCaseOrderByLoggedAtDesc(String userEmail, String recipeTitle);
}
