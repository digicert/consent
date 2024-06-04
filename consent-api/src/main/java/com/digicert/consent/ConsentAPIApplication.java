package com.digicert.consent;

import com.digicert.consent.config.LanguageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(LanguageConfig.class)
public class ConsentAPIApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsentAPIApplication.class, args);
    }
}