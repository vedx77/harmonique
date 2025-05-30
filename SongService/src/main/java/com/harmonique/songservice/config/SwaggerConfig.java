package com.harmonique.songservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger (OpenAPI) documentation.
 * This sets up API metadata and security definitions for Swagger UI.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Defines the OpenAPI configuration for Swagger UI.
     *
     * @return OpenAPI instance with project metadata and JWT security
     */
    @Bean
    OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Harmonique - Song Service APIs")
                        .description("Personal Music Library & Downloader by Vedant Shinde")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Vedant Shinde")
                                .email("ved.shinde77@gmail.com")
                                .url("https://github.com/vedx77/harmonique")) // GitHub repo
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                
                // Apply Bearer Authentication globally in Swagger UI
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))

                // Register security scheme
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    /**
     * Helper method to define the JWT bearer authentication scheme.
     *
     * @return SecurityScheme configured for Bearer JWT
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)      // HTTP-based scheme
                .bearerFormat("JWT")                 // JWT format
                .scheme("bearer");                   // Bearer token type
    }
}