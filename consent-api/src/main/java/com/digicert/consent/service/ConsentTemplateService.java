package com.digicert.consent.service;

import com.digicert.consent.config.ConsentTemplateConfig;
import com.digicert.consent.config.initializer.CustomInitializer;
import com.digicert.consent.config.model.ConsentModel;
import com.digicert.consent.entities.ConsentTemplateEntity;
import com.digicert.consent.entities.LocaleEntity;
import com.digicert.consent.entities.LocaleLanguageEntity;
import com.digicert.consent.repositories.ConsentTemplateRepository;
import com.digicert.consent.repositories.LanguageLocaleRepository;
import com.digicert.consent.repositories.LocaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ConsentTemplateService implements CustomInitializer {

    private final ConsentTemplateRepository consentTemplateRepository;
    private final PdfService pdfService;
    private final SpringTemplateEngine templateEngine;

    private final LocaleRepository localeRepository;

    private final LanguageLocaleRepository languageLocaleRepository;

    @Autowired
    public ConsentTemplateService(ConsentTemplateRepository consentTemplateRepository,
                                  PdfService pdfService, SpringTemplateEngine templateEngine, LocaleRepository localeRepository,
                                  LanguageLocaleRepository languageLocaleRepository) {
        this.consentTemplateRepository = consentTemplateRepository;
        this.pdfService = pdfService;
        this.templateEngine = templateEngine;
        this.localeRepository = localeRepository;
        this.languageLocaleRepository = languageLocaleRepository;
    }

    @Override
    public void init() {
        callCreateOrUpdateConsentTemplate();
    }

    private void callCreateOrUpdateConsentTemplate() {
        try {
            Resource resource = new ClassPathResource("consent/consent_template.yml");
            String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            ConsentTemplateConfig consentTemplateConfig = new ObjectMapper(new YAMLFactory())
                    .readValue(yaml, ConsentTemplateConfig.class);
            List<ConsentModel> consentModels = consentTemplateConfig.getConsentTemplate();
            if (consentModels != null) {
                CreateOrUpdateConsentTemplate(consentModels);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void CreateOrUpdateConsentTemplate(List<ConsentModel> consentModels) {

        for (ConsentModel consentModel : consentModels) {
            try {
                Context context = new Context();
                context.setVariable("title", consentModel.getTitle());
                context.setVariable("content", consentModel.getContent());
                context.setVariable("type", consentModel.getType());

                Resource resource = new ClassPathResource("templates/"+consentModel.getType());
                if (!resource.exists()) {
                    throw new RuntimeException("Template not found: " + consentModel.getType());
                }

                String jsonTemplate = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

                Optional<LocaleEntity> localeEntity = localeRepository.findByLocale(consentModel.getLocaleLanguageId());
                Optional<LocaleLanguageEntity> languageLocale = languageLocaleRepository.findByLocaleId(localeEntity.get().getId());
                List<ConsentTemplateEntity> existingTemplates = consentTemplateRepository.findByLocaleLanguageId(languageLocale.get().getId());

                ConsentTemplateEntity consentTemplateEntity = null;
                for (ConsentTemplateEntity template : existingTemplates) {
                    if (template.getType().equals(consentModel.getType())) {
                        consentTemplateEntity = template;
                        break;
                    }
                }

                if (consentTemplateEntity != null) {
                    // Update existing template
                    consentTemplateEntity.setTemplateJson(jsonTemplate);
                } else {
                    // Create new template
                    consentTemplateEntity = ConsentTemplateEntity.builder()
                            .localeLanguageId(languageLocale.get().getId())
                            .type(consentModel.getType())
                            .templateJson(jsonTemplate)
                            .build();
                }

                consentTemplateRepository.save(consentTemplateEntity);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }
}}
