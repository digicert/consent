package com.digicert.consent.service;

import com.digicert.consent.dto.ClientConsentDto;
import com.digicert.consent.dto.ClientConsentMetadataDto;
import com.digicert.consent.entities.ClientConsentEntity;
import com.digicert.consent.entities.ProductEntity;
import com.digicert.consent.entities.ProductTemplateEntity;
import com.digicert.consent.repositories.ClientConsentMetadataRepository;
import com.digicert.consent.repositories.ClientConsentRepository;
import com.digicert.consent.repositories.ProductRepository;
import com.digicert.consent.repositories.ProductTemplateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientConsentServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductTemplateRepository productTemplateRepository;

    @Mock
    private ClientConsentRepository clientConsentRepository;

    @Mock
    private ClientConsentMetadataRepository clientConsentMetadataRepository;

    @InjectMocks
    private ClientConsentService clientConsentService;

    @Test
    public void clientConsentServiceTest() {
        ClientConsentDto clientConsentDto = setUpClientConsentDto();
        when(productRepository.findByName(clientConsentDto.getProductName()))
                .thenReturn(Optional.of(setUpProductEntity()));
        clientConsentService.saveClientConsent(clientConsentDto);
    }

    @Test
    public void saveClientConsentProductFoundTest() {
        ClientConsentDto clientConsentDto = setUpClientConsentDto();
        ClientConsentEntity mockConsent = new ClientConsentEntity();
        mockConsent.setIndividualId("Test Individual");
        AtomicReference<ClientConsentEntity> clientConsentEntity = new AtomicReference<>(mockConsent);
        when(productRepository.findByName(clientConsentDto.getProductName()))
                .thenReturn(Optional.of(setUpProductEntity()));
        when(productTemplateRepository.findByProductId("1"))
                .thenReturn(Optional.of(setUpProductTemplateEntity()));
        when(clientConsentRepository.save(any()))
                .thenReturn(setUpClientConsentEntity());
        clientConsentService.saveClientConsent(clientConsentDto);
        assertTrue(clientConsentEntity.get().getIndividualId().equals("Test Individual"));
    }

    private ProductTemplateEntity setUpProductTemplateEntity() {
        return ProductTemplateEntity.builder()
                .id("1")
                .productId("1")
                .build();
    }

    private ClientConsentEntity setUpClientConsentEntity() {
        return ClientConsentEntity.builder()
                .individualId("Test Individual")
                .build();
    }

    @Test
    public void findPreviousClientConsentTest() {
        ClientConsentDto clientConsentDto = setUpClientConsentDto();
        when(productRepository.findByName(clientConsentDto.getProductName()))
                .thenReturn(Optional.of(setUpProductEntity()));
        when(clientConsentRepository.findClientConsentEntityByIndividualIdAndProductId("Test Individual", "1"))
                .thenReturn(Optional.of(new ClientConsentEntity()));
        assertNotEquals(null, clientConsentService.findPreviousClientConsent("Test Individual", "Test Product", "Test Locale"));
    }

    private ClientConsentDto setUpClientConsentDto() {
        ClientConsentDto clientConsentDto = new ClientConsentDto();
        clientConsentDto.setProductName("Test Product");
        clientConsentDto.setTemplate("Test Template");
        clientConsentDto.setIndividualId("Test Individual");
        clientConsentDto.setOutputReason("Test Reason");
        clientConsentDto.setMetadata(List.of(setUpClientConsentMetadataDto()));
        return clientConsentDto;
    }

    private ClientConsentMetadataDto setUpClientConsentMetadataDto() {
        ClientConsentMetadataDto clientConsentMetadataDto = new ClientConsentMetadataDto();
        clientConsentMetadataDto.setMetadataKey("Test Key");
        clientConsentMetadataDto.setMetadataValue("Test Value");
        return clientConsentMetadataDto;
    }

    private ProductEntity setUpProductEntity() {
        return ProductEntity.builder()
                .id("1")
                .name("Test Product")
                .build();
    }
}
