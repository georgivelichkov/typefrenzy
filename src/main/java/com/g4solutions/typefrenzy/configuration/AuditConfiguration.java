package com.g4solutions.typefrenzy.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {
    "com.g4solutions.typefrenzy.repository"}, bootstrapMode = BootstrapMode.DEFERRED)
public class AuditConfiguration {

}
