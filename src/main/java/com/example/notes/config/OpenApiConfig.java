package com.example.notes.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 🎓 Educational Note: API Documentation Configuration
 *
 * This class configures how Swagger/OpenAPI displays your API info.
 * It's purely for "Visibility" (Phase 3 of the roadmap).
 *
 * The SecurityScheme adds an "Authorize" button to Swagger UI.
 * After logging in, paste your JWT token there (without "Bearer " prefix)
 * and all subsequent requests will include the Authorization header.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

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

        SecurityScheme securityScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Paste your JWT token below (without the 'Bearer ' prefix).");

        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme));
    }
}
