package com.digicert.consent.config;

import com.digicert.consent.entities.LocaleLanguageEntity;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "")
public class LanguageLocaleConfig {

    private List<LocaleLanguageEntity> localeLanguages;

    public List<LocaleLanguageEntity> getLocaleLanguages() {
        return localeLanguages;
    }

    public void setLocaleLanguages(List<LocaleLanguageEntity> localeLanguages) {
        this.localeLanguages = localeLanguages;
    }
}
