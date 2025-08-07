package com.tyrell.replicant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI petShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pet Shop S3 Connector API")
                        .description("REST API for Pet Shop S3 file management and connectivity")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Tyrell Corporation")
                                .email("support@tyrell.com")))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Development server"));
    }
}

