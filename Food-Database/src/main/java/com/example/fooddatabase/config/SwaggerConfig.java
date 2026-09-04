package com.example.fooddatabase.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI configuration.
 *
 * Once the app is running, access the UI at:
 *   http://localhost:8081/swagger-ui/index.html
 *
 * The raw OpenAPI JSON spec is at:
 *   http://localhost:8081/v3/api-docs
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI foodDatabaseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Food Database API")
                        .description("Microservice for managing food and nutrition master data. " +
                                "Used by Meal Planning and Grocery Planning services.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Food Database Service")
                                .email("admin@fooddatabase.com")));
    }
}
