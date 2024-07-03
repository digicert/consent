package com.digicert.consent.service;

import com.digicert.consent.config.ProductTemplateConfig;
import com.digicert.consent.config.initializer.CustomInitializer;
import com.digicert.consent.config.model.ProductTemplateModel;
import com.digicert.consent.entities.ConsentTemplateEntity;
import com.digicert.consent.entities.LanguageEntity;
import com.digicert.consent.entities.LocaleEntity;
import com.digicert.consent.entities.LocaleLanguageEntity;
import com.digicert.consent.entities.ProductEntity;
import com.digicert.consent.entities.ProductTemplateEntity;
import com.digicert.consent.repositories.ConsentTemplateRepository;
import com.digicert.consent.repositories.LanguageLocaleRepository;
import com.digicert.consent.repositories.LanguageRepository;
import com.digicert.consent.repositories.LocaleRepository;
import com.digicert.consent.repositories.ProductRepository;
import com.digicert.consent.repositories.ProductTemplateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class ProductTemplateService implements CustomInitializer {

    private final ObjectMapper objectMapper;

    private final LanguageRepository languageRepository;

    private final LocaleRepository LocaleRepository;

    private final LanguageLocaleRepository languageLocaleRepository;

    private final ConsentTemplateRepository consentTemplateRepository;

    private final ProductTemplateRepository productTemplateRepository;

    private List<ProductTemplateModel> productTemplates;

    private final ProductRepository productRepository;

    @Autowired
    public ProductTemplateService(ProductTemplateConfig productTemplateConfig,
                                  LanguageRepository languageRepository, LocaleRepository LocaleRepository,
                                  LanguageLocaleRepository languageLocaleRepository,
                                  ConsentTemplateRepository consentTemplateRepository,
                                  ProductTemplateRepository productTemplateRepository,
                                  ProductRepository productRepository) {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        this.languageRepository = languageRepository;
        this.LocaleRepository = LocaleRepository;
        this.languageLocaleRepository = languageLocaleRepository;
        this.consentTemplateRepository = consentTemplateRepository;
        this.productTemplates = productTemplateConfig.getProductTemplates();
        this.productTemplateRepository = productTemplateRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void init() {
        try {
            createOrUpdateProductTemplate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createOrUpdateProductTemplate() throws IOException {
        Resource resource = new ClassPathResource("consent/product_template.yml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        ProductTemplateConfig newConfig = objectMapper.readValue(yaml, ProductTemplateConfig.class);
        if (newConfig.getProductTemplates() != null) {
            productTemplates = newConfig.getProductTemplates();
        }
        if(productTemplates != null && !productTemplates.isEmpty()) {
            for (ProductTemplateModel productTemplate : productTemplates) {
                Optional<LanguageEntity> existingLanguages = languageRepository.findByIsoCode(productTemplate.getLanguage());
                Optional<LocaleEntity> existingLocales = LocaleRepository.findByLocale(productTemplate.getLocale());
                if (existingLocales.isPresent() && existingLocales.isPresent()) {
                    Optional<LocaleLanguageEntity> existingLocaleLanguages = languageLocaleRepository
                            .findByLanguageIdAndLocaleId(existingLanguages.get().getId(), existingLocales.get().getId());
                    if (existingLocaleLanguages.isPresent()) {
                        List<ConsentTemplateEntity> consentTemplateEntity =
                                consentTemplateRepository.findByLocaleLanguageId(existingLocaleLanguages.get().getId());

                        for (ConsentTemplateEntity consentTemplate : consentTemplateEntity) {
                            Optional<ProductEntity> product = productRepository.findByName(productTemplate.getName());
                            if (product.isPresent()) {
                                Optional<ProductTemplateEntity> existingProductTemplate =
                                        productTemplateRepository
                                                .findByConsentTemplateIdAndProductId(consentTemplate.getId(), product.get().getId());
                                ProductTemplateEntity productTemplateEntity;
                                if (existingProductTemplate.isPresent()) {
                                    productTemplateEntity = existingProductTemplate.get();
                                } else {
                                    productTemplateEntity = new ProductTemplateEntity();
                                    productTemplateEntity.setConsentTemplateId(consentTemplate.getId());
                                    productTemplateEntity.setProductId(product.get().getId());
                                }
                                productTemplateEntity.setActive(productTemplate.isActive());
                                productTemplateRepository.save(productTemplateEntity);
                            }
                        }
                    }
                }
            }
        }
    }

}
