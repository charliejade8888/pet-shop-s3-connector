package com.tyrell.replicant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "yaml")
@PropertySource(value = "classpath:aws.yml", factory = YamlPropertySourceFactory.class)
public class MyConfigurationProperties {

  private String authMethod;
  private String accessKey;
  private String secretKey;
  private String serviceEndpoint;
  private String signingRegion;

  public String getSigningRegion() {
    return signingRegion;
  }

  public void setSigningRegion(String signingRegion) {
    this.signingRegion = signingRegion;
  }

  public String getServiceEndpoint() {
    return serviceEndpoint;
  }

  public void setServiceEndpoint(String serviceEndpoint) {
    this.serviceEndpoint = serviceEndpoint;
  }

  public String getAuthMethod() {
    return authMethod;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setAuthMethod(String authMethod) {
    this.authMethod = authMethod;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

}