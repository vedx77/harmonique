package com.harmonique.userservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class SwaggerConfig {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

    /**
     * Defines the OpenAPI bean with metadata about the API including
     * title, description, version, contact, license, and security scheme.
     * 
     * This metadata appears in Swagger UI and OpenAPI documentation.
     */
    @Bean
    public OpenAPI apiInfo() {
        logger.info("Creating OpenAPI bean for Swagger configuration");

        return new OpenAPI()
            // API information shown on Swagger UI
            .info(new Info()
                .title("Harmonique - User Service APIs")
                .description("Personal Music Library & Downloader by Vedant Shinde")
                .version("v1.0")
                .contact(new Contact()
                    .name("Vedant Shinde")
                    .email("ved.shinde77@gmail.com")
                    .url("https://github.com/vedx77/harmonique"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
            )
            // Adds global security requirement for Bearer token authentication
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            // Defines components including the security schemes used by the API
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme())
            );
    }

    /**
     * Creates the security scheme object that defines the Bearer JWT
     * authentication scheme used for securing the APIs.
     * 
     * @return SecurityScheme configured for HTTP bearer JWT tokens.
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)      // HTTP authentication scheme
            .bearerFormat("JWT")                  // Token format is JWT
            .scheme("bearer");                    // Using Bearer scheme
    }
}