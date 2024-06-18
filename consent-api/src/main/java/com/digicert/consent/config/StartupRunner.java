package com.digicert.consent.config;

import com.digicert.consent.config.initializer.CustomInitializer;
import com.digicert.consent.service.ConsentTemplateService;
import com.digicert.consent.service.LanguageLocaleService;
import com.digicert.consent.service.LanguageService;
import com.digicert.consent.service.LocaleService;
import com.digicert.consent.service.ProductService;
import com.digicert.consent.service.ProductTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class StartupRunner {

    private final List<CustomInitializer> initializers;

    @Autowired
    public StartupRunner(LanguageService languageService, LocaleService localeService,
                         LanguageLocaleService languageLocaleService, ConsentTemplateService consentTemplateService,
                         ProductService productService, ProductTemplateService productTemplateService) {
        this.initializers = Arrays.asList(languageService, localeService, languageLocaleService,
                consentTemplateService, productService, productTemplateService);
    }

    @Bean
    public ApplicationRunner initializerRunner() {
        return args -> {
            for (CustomInitializer initializer : initializers) {
                initializer.init();
            }
        };
    }

}
