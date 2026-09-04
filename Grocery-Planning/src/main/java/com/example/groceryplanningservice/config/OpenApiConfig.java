package com.example.groceryplanningservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Springdoc OpenAPI (Swagger) configuration.
 *
 * <p>Swagger UI is available at: <a href="http://localhost:3002/swagger-ui.html">
 * http://localhost:3002/swagger-ui.html</a>
 * <p>API docs JSON is available at: <a href="http://localhost:3002/v3/api-docs">
 * http://localhost:3002/v3/api-docs</a>
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI groceryPlanningOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Grocery Planning Service API")
                        .description("API for managing grocery lists and grocery items.")
                        .version("1.0.0"));
    }
}
