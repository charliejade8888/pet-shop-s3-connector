//package com.tyrell.replicant.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
////http://localhost:13000/swagger-ui/#/
//@Profile(value = {"swagger"})
//@Configuration
//public class SwaggerConfiguration {
//
//  @Bean
//  public Docket api() {
//    return new Docket(DocumentationType.SWAGGER_2)
//        .select()
//        .apis(RequestHandlerSelectors.any())
//        .paths(path -> path.equals("/message")) // NOPMD
//        .build()
//        .apiInfo(new ApiInfoBuilder()
//            .title("Pet Shop")
//            .build());
//  }
//
//}