package com.digicert.consent.service;

import com.digicert.consent.dto.ProductTemplateDto;
import com.digicert.consent.entities.ConsentTemplateEntity;
import com.digicert.consent.entities.ProductEntity;
import com.digicert.consent.entities.ProductTemplateEntity;
import com.digicert.consent.repositories.ConsentTemplateRepository;
import com.digicert.consent.repositories.ProductRepository;
import com.digicert.consent.repositories.ProductTemplateRepository;
import com.digicert.consent.service.ConsentTemplateService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
public class ConsentTemplateServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductTemplateRepository productTemplateRepository;

    @Mock
    private ConsentTemplateRepository consentTemplateRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ConsentTemplateService consentTemplateService;

    private ProductEntity productEntity;
    private ProductTemplateEntity productTemplateEntity;
    private ConsentTemplateEntity consentTemplateEntity;
    private JsonNode responseJson;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        productEntity = new ProductEntity();
        productEntity.setId(UUID.randomUUID().toString());
        productEntity.setName("Test Product");

        productTemplateEntity = new ProductTemplateEntity();
        productTemplateEntity.setId(UUID.randomUUID().toString());
        productTemplateEntity.setProductId(productEntity.getId());
        productTemplateEntity.setActive(true);
        productTemplateEntity.setConsentTemplateId(UUID.randomUUID().toString());

        consentTemplateEntity = new ConsentTemplateEntity();
        consentTemplateEntity.setId(productTemplateEntity.getConsentTemplateId());
        consentTemplateEntity.setTemplateJson("{ \"key\": \"value\" }");

        responseJson = mock(JsonNode.class);
    }

    @Test
    public void testGetActiveConsentTemplates() throws Exception {
        // Set up mock behavior
        when(productRepository.findByName("Test Product")).thenReturn(Optional.of(productEntity));
        when(productTemplateRepository.findAllByProductId(productEntity.getId())).thenReturn(List.of(productTemplateEntity));
        when(consentTemplateRepository.findById(productTemplateEntity.getConsentTemplateId())).thenReturn(Optional.of(consentTemplateEntity));
        when(objectMapper.readTree(consentTemplateEntity.getTemplateJson())).thenReturn(responseJson);

        // Call the method under test
        List<ProductTemplateDto> result = consentTemplateService.getActiveConsentTemplates("Test Product");

        // Verify the results
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        assertTrue(result.get(0).isActive());
        assertEquals(responseJson, result.get(0).getResponseJson());

        // Verify the mock interactions
        verify(productRepository, times(1)).findByName("Test Product");
        verify(productTemplateRepository, times(1)).findAllByProductId(productEntity.getId());
        verify(consentTemplateRepository, times(1)).findById(productTemplateEntity.getConsentTemplateId());
        verify(objectMapper, times(1)).readTree(consentTemplateEntity.getTemplateJson());
    }

    @Test
    public void testGetActiveConsentTemplates_productNotFound() {
        // Set up mock behavior
        when(productRepository.findByName("NonExistentProduct")).thenReturn(Optional.empty());

        // call the method and verify exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            consentTemplateService.getActiveConsentTemplates("NonExistentProduct");
        });

        assertEquals("Product not found: NonExistentProduct", exception.getMessage());

        // Verify the mock interactions
        verify(productRepository, times(1)).findByName("NonExistentProduct");
        verifyNoMoreInteractions(productTemplateRepository, consentTemplateRepository, objectMapper);
    }



}
