package com.g4solutions.typefrenzy.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TypeFrenzyConfigProperties {

  public static final Integer DEFAULT_PAGE_SIZE = 20;

  @Bean
  @Qualifier("typeFrenzyProperties")
  @ConfigurationProperties(prefix = "frenzy")
  public Map<String, String> all() {
    return new HashMap<>();
  }

  public Integer defaultPageSize() {
    return Optional
        .ofNullable(this.all().get("paging.default-page-size"))
        .map(Integer::valueOf)
        .orElse(TypeFrenzyConfigProperties.DEFAULT_PAGE_SIZE);
  }

  public String jwtSecret() {
    return Optional
        .ofNullable(this.all().get("jwt.secret"))
        .orElseThrow(() -> new IllegalArgumentException("Missing jwt.secret value"));
  }

  public String dataFilesPath() {
    return Optional
        .ofNullable(this.all().get("data.files-path"))
        .orElseThrow(() -> new IllegalArgumentException("Missing data.files-path value"));
  }
}
