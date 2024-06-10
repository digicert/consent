package com.digicert.consent.config;

import com.digicert.consent.config.model.ConsentTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "")
public class ConsentTemplateConfig {

    private List<ConsentTemplate> consentTemplate;

    public List<ConsentTemplate> getConsentTemplate() {
        return consentTemplate;
    }

    public void setConsentTemplate(List<ConsentTemplate> consentTemplate) {
        this.consentTemplate = consentTemplate;
    }
}
