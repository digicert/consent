package com.digicert.consent.config;

import com.digicert.consent.config.model.ConsentModel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "")
public class ConsentTemplateConfig {

    private List<ConsentModel> consentModel;

    public List<ConsentModel> getConsentTemplate() {
        return consentModel;
    }

    public void setConsentTemplate(List<ConsentModel> consentModel) {
        this.consentModel = consentModel;
    }
}
