package com.digicert.consent.config;

import com.digicert.consent.entities.LocaleEntity;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "")
public class LocaleConfig {

    private List<LocaleEntity> locales;

    public List<LocaleEntity> getLocales() {
        return locales;
    }

    public void setLocales(List<LocaleEntity> locales) {
        this.locales = locales;
    }
}
