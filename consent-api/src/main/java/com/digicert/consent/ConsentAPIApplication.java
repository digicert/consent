package com.digicert.consent;

import com.digicert.consent.config.ConsentTemplateConfig;
import com.digicert.consent.config.LanguageConfig;
import com.digicert.consent.config.LanguageLocaleConfig;
import com.digicert.consent.config.LocaleConfig;
import com.digicert.consent.config.ProductConfig;
import com.digicert.consent.config.ProductTemplateConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({LanguageConfig.class, LocaleConfig.class,
        LanguageLocaleConfig.class, ConsentTemplateConfig.class, ProductConfig.class,
        ProductTemplateConfig.class})
public class ConsentAPIApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsentAPIApplication.class, args);
    }
}