package com.digicert.consent.service;

import com.digicert.consent.config.ConsentTemplateConfig;
import com.digicert.consent.config.initializer.CustomInitializer;
import com.digicert.consent.config.model.ConsentModel;
import com.digicert.consent.dto.ConsentTemplateDto;
import com.digicert.consent.dto.ProductTemplateDto;
import com.digicert.consent.entities.ConsentTemplateEntity;
import com.digicert.consent.entities.LocaleEntity;
import com.digicert.consent.entities.LocaleLanguageEntity;
import com.digicert.consent.entities.ProductEntity;
import com.digicert.consent.entities.ProductTemplateEntity;
import com.digicert.consent.repositories.ConsentTemplateRepository;
import com.digicert.consent.repositories.LanguageLocaleRepository;
import com.digicert.consent.repositories.LocaleRepository;
import com.digicert.consent.repositories.ProductRepository;
import com.digicert.consent.repositories.ProductTemplateRepository;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConsentTemplateService implements CustomInitializer {

    private final ConsentTemplateRepository consentTemplateRepository;
    private final PdfService pdfService;
    private final SpringTemplateEngine templateEngine;

    private final LocaleRepository localeRepository;

    private final LanguageLocaleRepository languageLocaleRepository;

    private final ProductTemplateRepository productTemplateRepository;

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public ConsentTemplateService(ConsentTemplateRepository consentTemplateRepository,
                                  PdfService pdfService, SpringTemplateEngine templateEngine, LocaleRepository localeRepository,
                                  LanguageLocaleRepository languageLocaleRepository,
                                  ProductTemplateRepository productTemplateRepository,
                                  ProductRepository productRepository,
                                  ObjectMapper objectMapper) {
        this.consentTemplateRepository = consentTemplateRepository;
        this.pdfService = pdfService;
        this.templateEngine = templateEngine;
        this.localeRepository = localeRepository;
        this.languageLocaleRepository = languageLocaleRepository;
        this.productTemplateRepository = productTemplateRepository;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void init() {
        callCreateOrUpdateConsentTemplate();
    }

    public void callCreateOrUpdateConsentTemplate() {
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

                // check if consent template entry exists if exists update else create
                Optional<ConsentTemplateEntity> existingConsentTemplate = consentTemplateRepository.findByType(consentModel.getType());
                if (existingConsentTemplate.isPresent()) {
                    // update existing consent template
                    existingConsentTemplate.get().setTemplateJson(jsonTemplate);
                    consentTemplateRepository.save(existingConsentTemplate.get());
                } else {
                    // create new consent template
                    ConsentTemplateEntity consentTemplateEntity = ConsentTemplateEntity.builder()
                            .type(consentModel.getType())
                            .templateJson(jsonTemplate)
                            .build();
                    consentTemplateRepository.save(consentTemplateEntity);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public List<ProductTemplateDto> getActiveConsentTemplates(String name) {
        ProductEntity product = productRepository.findByName(name).orElseThrow(() ->
                new RuntimeException("Product not found: " + name));
        List<ProductTemplateEntity> activeTemplates = productTemplateRepository.findAllByProductId(product.getId());
        List<ProductTemplateDto> productTemplateDtos = new ArrayList<>();
        for (ProductTemplateEntity productTemplateEntity : activeTemplates) {
            Optional<ConsentTemplateEntity> consentTemplateEntity =
                    consentTemplateRepository.findById(productTemplateEntity.getConsentTemplateId());
            if (consentTemplateEntity.isPresent()) {
                ProductTemplateDto productTemplateDto = new ProductTemplateDto();
                productTemplateDto.setName(name);
                productTemplateDto.setActive(productTemplateEntity.isActive());
                try {
                    JsonNode responseJson = objectMapper.readTree(consentTemplateEntity.get().getTemplateJson());
                    productTemplateDto.setResponseJson(responseJson);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                productTemplateDtos.add(productTemplateDto);
            }

        }
        return productTemplateDtos;
    }

}
