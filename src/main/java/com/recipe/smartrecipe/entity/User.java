package com.recipe.smartrecipe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", nullable = false, length = 100)
    private String username;
    
    @Column(name = "email", nullable = false, length = 255)
    private String email;
    
    @Column(name = "password", nullable = true, length = 255)
    private String password;
    
    @Column(name = "hash", nullable = true, length = 255)
    private String hash;
    
    @Column(name = "password_hash", nullable = true, length = 255)
    private String passwordHash;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
