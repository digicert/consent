package com.digicert.consent.service;

import com.digicert.consent.config.ProductTemplateConfig;
import com.digicert.consent.config.initializer.CustomInitializer;
import com.digicert.consent.config.ProductTemplateModel;
import com.digicert.consent.entities.ConsentTemplateEntity;
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

    private final ConsentTemplateRepository consentTemplateRepository;

    private final ProductTemplateRepository productTemplateRepository;

    private List<ProductTemplateModel> productTemplates;

    private final ProductRepository productRepository;

    @Autowired
    public ProductTemplateService(ProductTemplateConfig productTemplateConfig,
                                  LanguageRepository languageRepository, LocaleRepository localeRepository,
                                  LanguageLocaleRepository languageLocaleRepository,
                                  ConsentTemplateRepository consentTemplateRepository,
                                  ProductTemplateRepository productTemplateRepository,
                                  ProductRepository productRepository) {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
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
        if (productTemplates != null && !productTemplates.isEmpty()) {
            for (ProductTemplateModel productTemplate : productTemplates) {
                // check if product template exists if exists update else create
                List<ProductTemplateEntity> existingProductTemplates = productTemplateRepository
                        .findAllByTemplateAndActive(productTemplate.getTemplate(), productTemplate.isActive());
                Optional<ProductEntity> productEntity =
                        productRepository.findByName(productTemplate.getName());
                Optional<ConsentTemplateEntity> consentTemplateEntity =
                        consentTemplateRepository.findByType(productTemplate.getTemplate());
                if (existingProductTemplates.isEmpty() && productEntity.isPresent()) {
                    // save a new product template
                    productTemplateRepository.save(ProductTemplateEntity.builder()
                            .productId(productEntity.get().getId())
                            .consentTemplateId(consentTemplateEntity.get().getId())
                            .active(productTemplate.isActive())
                            .template(productTemplate.getTemplate())
                            .build());
                } else {
                    // update existing product template
                    for (ProductTemplateEntity existingProductTemplate : existingProductTemplates) {
                        existingProductTemplate.setActive(productTemplate.isActive());
                        productTemplateRepository.save(existingProductTemplate);
                    }
                }
            }
        }
    }

}
