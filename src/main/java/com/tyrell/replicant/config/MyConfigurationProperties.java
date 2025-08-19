package com.tyrell.replicant.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

// lombok used as Spring Boot 3.2+ has issues with records and @PropertySource
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "yaml")
@PropertySource(value = "classpath:aws.yml", factory = YamlPropertySourceFactory.class)
public class MyConfigurationProperties {

  private String authMethod;
  private String accessKey;
  private String secretKey;
  private String serviceEndpoint;
  private String signingRegion;


}