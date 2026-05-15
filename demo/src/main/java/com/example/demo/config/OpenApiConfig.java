package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Demo API",
        version = "1.0",
        description = "API para la aplicación de yogurt"
    )
)
public class OpenApiConfig {
}