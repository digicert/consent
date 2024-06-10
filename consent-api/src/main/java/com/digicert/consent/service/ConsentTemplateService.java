package com.digicert.consent.service;

import com.digicert.consent.config.ConsentTemplateConfig;
import com.digicert.consent.config.model.ConsentTemplate;
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

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ConsentTemplateService {

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

    @PostConstruct
    public void init() {

        try {
            Resource resource = new ClassPathResource("consent/consent_template.yml");
            String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            ConsentTemplateConfig consentTemplateConfig = new ObjectMapper(new YAMLFactory())
                    .readValue(yaml, ConsentTemplateConfig.class);
            List<ConsentTemplate> consentTemplates = consentTemplateConfig.getConsentTemplate();
            if (consentTemplates != null) {
                for (ConsentTemplate consentTemplate : consentTemplates) {
                    CreateOrUpdateConsentTemplate(consentTemplate);
                }
            } else {
                throw new RuntimeException("Consent templates not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void CreateOrUpdateConsentTemplate(ConsentTemplate consentTemplate) {

        Context context = new Context();
        context.setVariable("title", consentTemplate.getTitle());
        context.setVariable("content", consentTemplate.getContent());

        String htmlContent = templateEngine.process("consent_template", context);
        byte[] pdfContent = pdfService.generatePdfFromHtml(htmlContent);
        String pdfContentStr = Base64.getEncoder().encodeToString(pdfContent);

        ConsentTemplateEntity existingTemplate = consentTemplateRepository.findByLocaleLanguageId(consentTemplate.getLocaleLanguageId());
        Optional<LocaleEntity> localeEntity =
                localeRepository.findByLocale(consentTemplate.getLocaleLanguageId());
        Optional<LocaleLanguageEntity> localeLanguageEntity =
                languageLocaleRepository.findByLocaleId(localeEntity.get().getId());
        if (existingTemplate == null) {
            ConsentTemplateEntity newTemplate = new ConsentTemplateEntity();
            newTemplate.setLocaleLanguageId(localeLanguageEntity.get().getId());
            newTemplate.setTemplatePdf(pdfContentStr);
            consentTemplateRepository.save(newTemplate);
        } else {
            existingTemplate.setTemplatePdf(pdfContentStr);
            consentTemplateRepository.save(existingTemplate);
        }
    }
}
