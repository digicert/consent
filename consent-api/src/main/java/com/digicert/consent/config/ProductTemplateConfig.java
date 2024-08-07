package com.digicert.consent.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "")
public class ProductTemplateConfig {
    private List<ProductTemplateModel> productTemplates;

    public List<ProductTemplateModel> getProductTemplates() {
        return productTemplates;
    }

    public void setProductTemplates(List<ProductTemplateModel> productTemplates) {
        this.productTemplates = productTemplates;
    }
}