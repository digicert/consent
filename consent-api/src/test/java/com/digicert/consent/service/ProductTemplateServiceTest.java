package com.digicert.consent.service;

import com.digicert.consent.config.ProductTemplateConfig;
import com.digicert.consent.entities.ConsentTemplateEntity;
import com.digicert.consent.entities.ProductEntity;
import com.digicert.consent.entities.ProductTemplateEntity;
import com.digicert.consent.repositories.ConsentTemplateRepository;
import com.digicert.consent.repositories.ProductRepository;
import com.digicert.consent.repositories.ProductTemplateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductTemplateServiceTest {

    @Mock
    private ConsentTemplateRepository consentTemplateRepository;

    @Mock
    private ProductTemplateRepository productTemplateRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ProductTemplateConfig productTemplateConfig;

    @InjectMocks
    private ProductTemplateService productTemplateService;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper(new YAMLFactory());
    }

    @Test
    public void createOrUpdateProductTemplateTest() throws IOException {
        Resource resource = new ClassPathResource("consent/product_template.yml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        ProductTemplateConfig newConfig = objectMapper.readValue(yaml, ProductTemplateConfig.class);
        when(productTemplateRepository.findAllByTemplateAndActive(newConfig.getProductTemplates().get(0).getTemplate(),
                newConfig.getProductTemplates().get(0).isActive()))
                .thenReturn(List.of(setUpProductTemplateEntity()));
        when(productRepository.findByName(newConfig.getProductTemplates().get(0).getName()))
                .thenReturn(Optional.of(setUpProductEntity()));
        when(consentTemplateRepository.findByType(newConfig.getProductTemplates().get(0).getTemplate()))
                .thenReturn(Optional.of(setUpConsentTemplateEntity()));
        productTemplateService.createOrUpdateProductTemplate();
        assertEquals(newConfig.getProductTemplates().get(0).getTemplate(),
                productTemplateRepository.findAllByTemplateAndActive(newConfig.getProductTemplates().get(0).getTemplate(),
                        newConfig.getProductTemplates().get(0).isActive()).get(0).getTemplate());
        assertEquals(newConfig.getProductTemplates().get(0).getName(),
                productRepository.findByName(newConfig.getProductTemplates().get(0).getName()).get().getName());
        assertEquals(newConfig.getProductTemplates().get(0).getTemplate(),
                consentTemplateRepository.findByType(newConfig.getProductTemplates().get(0).getTemplate()).get().getType());
    }

    @Test
    public void createOrUpdateProductTemplatesNotExistTest() throws IOException {
        Resource resource = new ClassPathResource("consent/product_template.yml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        ProductTemplateConfig newConfig = objectMapper.readValue(yaml, ProductTemplateConfig.class);
        // return empty list of templates
        when(productTemplateRepository.findAllByTemplateAndActive(newConfig.getProductTemplates().get(0).getTemplate(),
                newConfig.getProductTemplates().get(0).isActive()))
                .thenReturn(List.of());
        when(productRepository.findByName(newConfig.getProductTemplates().get(0).getName()))
                .thenReturn(Optional.of(setUpProductEntity()));
        when(consentTemplateRepository.findByType(newConfig.getProductTemplates().get(0).getTemplate()))
                .thenReturn(Optional.of(setUpConsentTemplateEntity()));
        productTemplateService.createOrUpdateProductTemplate();
    }

    private ProductTemplateEntity setUpProductTemplateEntity() {
        return ProductTemplateEntity.builder()
                .productId("1")
                .consentTemplateId("1")
                .template("eu_qualified_manual.json")
                .active(true)
                .build();
    }

    private ProductEntity setUpProductEntity() {
        return ProductEntity.builder()
                .id("1")
                .name("eu_qualified_manual")
                .build();
    }

    private ConsentTemplateEntity setUpConsentTemplateEntity() {
        return ConsentTemplateEntity.builder()
                .id("1")
                .localeLanguageId("1")
                .templateJson("templateJson")
                .type("eu_qualified_manual.json")
                .build();
    }
}
