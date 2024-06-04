package com.digicert.consent.config;

import com.digicert.consent.entities.LanguageEntity;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "")
public class LanguageConfig {

    private List<LanguageEntity> languages;

    public List<LanguageEntity> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguageEntity> languages) {
        this.languages = languages;
    }

}
