package com.tyrell.replicant.config;

import java.util.Properties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class YamlPropertySourceFactory implements PropertySourceFactory {

  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) {
    YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
    var resource = encodedResource.getResource();
    factory.setResources(resource);
    Properties properties = factory.getObject();
    if (properties == null) {
      throw new IllegalArgumentException("no properties specified");
    }
    String filename = resource.getFilename();
    if (filename == null) {
      throw new IllegalArgumentException("no property file specified");
    }
    return new PropertiesPropertySource(filename, properties);
  }

}
