package com.recipe.smartrecipe.repository;

import com.recipe.smartrecipe.entity.RecipeRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRequestRepository extends JpaRepository<RecipeRequestEntity, Long> {
    
    List<RecipeRequestEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT r FROM RecipeRequestEntity r WHERE r.userId = :userId ORDER BY r.createdAt DESC LIMIT :limit")
    List<RecipeRequestEntity> findRecentRequestsByUserId(@Param("userId") Long userId, @Param("limit") int limit);
}
