package com.example.notes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 🎓 Educational Note: API Documentation Configuration
 * 
 * This class configures how Swagger/OpenAPI displays your API info.
 * It's purely for "Visibility" (Phase 3 of the roadmap).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("learn@example.com");
        contact.setName("Backend Learner");

        Info info = new Info()
                .title("Notes API")
                .version("1.0")
                .contact(contact)
                .description("A professional REST API for managing personal notes. " +
                        "Includes User management and Note operations.");

        return new OpenAPI().info(info);
    }
}
