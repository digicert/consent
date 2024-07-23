package com.digicert.consent.service;

import com.digicert.consent.config.ProductConfig;
import com.digicert.consent.entities.ProductEntity;
import com.digicert.consent.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductConfig productConfig;

    @InjectMocks
    private ProductService productService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper(new YAMLFactory());
        productService = new ProductService(productConfig, productRepository);
    }

    @Test
    public void testCreateOrUpdateProduct() throws IOException {
        // Mock resource for products.yml
        String yamlContent = "---\nproducts:\n  - name: \"Product 1\"\n  - name: \"Product 2\"";

        // Mock StreamUtils
        try (var streamUtilsMockedStatic = mockStatic(StreamUtils.class)) {
            streamUtilsMockedStatic.when(() -> StreamUtils.copyToString((InputStream) any(), eq(StandardCharsets.UTF_8)))
                    .thenReturn(yamlContent);

            // Mock repository behavior
            when(productRepository.findByName("Product 1")).thenReturn(Optional.empty());
            when(productRepository.findByName("Product 2")).thenReturn(Optional.of(new ProductEntity()));

            // Call the actual method
            assertDoesNotThrow(() -> productService.createOrUpdateProduct());

            // Verify interactions
            verify(productRepository, times(1)).save(argThat(product -> product.getName().equals("Product 1")));
            verify(productRepository, times(1)).save(argThat(product -> product.getName().equals("Product 2")));
        }
    }

    @Test
    public void testCreateOrUpdateProductWithEmptyProducts() throws IOException {
        // Mock resource for products.yml
        String yamlContent = "---\nproducts: []";

        // Mock StreamUtils
        try (var streamUtilsMockedStatic = mockStatic(StreamUtils.class)) {
            streamUtilsMockedStatic.when(() -> StreamUtils.copyToString((InputStream) any(), eq(StandardCharsets.UTF_8)))
                    .thenReturn(yamlContent);

            // Call the actual method
            assertDoesNotThrow(() -> productService.createOrUpdateProduct());

            // Verify interactions
            verify(productRepository, times(0)).save(any());
        }
    }
}
