package com.g4solutions.typefrenzy.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class TypeFrenzyMvcConfiguration implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // end your path with a /
    registry.addResourceHandler("/filesystem/**")
        .addResourceLocations("file:/opt/typefrenzy/images/");
  }
}