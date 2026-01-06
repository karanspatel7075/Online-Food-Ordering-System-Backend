package com.PavitraSoft.FoodApplication.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI foodAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Food Application API")
                        .description("""
                                Backend APIs for Food Ordering Application
                                
                                Features:
                                - User Authentication
                                - Restaurant & Menu Management
                                - Cart & Order Flow
                                - Role Based Access (USER / RESTAURANT_ADMIN / ADMIN)
                                """)
                        .version("1.0.0"));
    }
}
