package com.digicert.consent.service;


import com.digicert.consent.config.ProductConfig;
import com.digicert.consent.entities.ProductEntity;
import com.digicert.consent.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.core.io.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ProductConfig productConfig;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper(new YAMLFactory());
    }

    @Test
    public void createOrUpdateProduct() throws IOException {
        Resource resource = new ClassPathResource("consent/products.yml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        ProductConfig newConfig = objectMapper.readValue(yaml, ProductConfig.class);
        when(productRepository.findByName(newConfig.getProducts().get(0).getName()))
                .thenReturn(Optional.of(new ProductEntity()));
        productService.createOrUpdateProduct();
    }

    @Test
    public void init() {
        productService.init();
    }

}


