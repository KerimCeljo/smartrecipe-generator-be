package com.recipe.smartrecipe;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartRecipeApplication {

    public static void main(String[] args) {
        // Load environment variables from .env file (only for local development)
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();
            
            // Set system properties from .env file
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
            });
            System.out.println("✅ Loaded environment variables from .env file");
        } catch (Exception e) {
            System.out.println("ℹ️  No .env file found, using system environment variables");
        }
        
        SpringApplication.run(SmartRecipeApplication.class, args);
        System.out.println("🍳 Smart Recipe Generator Backend is running!");
        System.out.println("📍 API available at: http://localhost:8080");
        System.out.println("📖 API Documentation: http://localhost:8080/swagger-ui.html");
    }
}
