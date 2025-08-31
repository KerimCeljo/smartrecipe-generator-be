package com.recipe.smartrecipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartRecipeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartRecipeApplication.class, args);
        System.out.println("🍳 Smart Recipe Generator Backend is running!");
        System.out.println("📍 API available at: http://localhost:8080");
        System.out.println("📖 API Documentation: http://localhost:8080/swagger-ui.html");
    }
}
