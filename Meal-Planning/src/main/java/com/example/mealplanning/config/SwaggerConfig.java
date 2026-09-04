package com.example.mealplanning.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI mealPlanningOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Meal Planning Microservice API")
                        .description("""
                                REST API for managing users' meal plans and logged meals.
                                
                                This service stores meal plans and meals with foodId references.
                                Nutrition data is fetched on-demand from the Food Database Service.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Meal Planning Service")
                                .email("support@mealplanning.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:3001")
                                .description("Local Development Server")
                ));
    }
}
