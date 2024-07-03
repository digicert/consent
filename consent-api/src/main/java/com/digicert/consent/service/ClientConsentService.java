package com.digicert.consent.service;

import com.digicert.consent.dto.ClientConsentDto;
import com.digicert.consent.entities.ClientConsentEntity;
import com.digicert.consent.entities.ProductEntity;
import com.digicert.consent.repositories.ClientConsentRepository;
import com.digicert.consent.repositories.ProductRepository;
import com.digicert.consent.repositories.ProductTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ClientConsentService {

    private final ProductRepository productRepository;

    private final ProductTemplateRepository productTemplateRepository;

    private final ClientConsentRepository clientConsentRepository;


    public ClientConsentService(ProductRepository productRepository, ProductTemplateRepository productTemplateRepository, ClientConsentRepository clientConsentRepository) {
        this.productRepository = productRepository;
        this.productTemplateRepository = productTemplateRepository;
        this.clientConsentRepository = clientConsentRepository;
    }

    public void saveClientConsent(ClientConsentDto clientConsentDto) {
        Optional<ProductEntity> products =
                productRepository.findByName(clientConsentDto.getProductName());
        if (products.isPresent()) {
            productTemplateRepository.findByProductId(products.get().getId())
                    .ifPresent(productTemplateEntity -> {
                        clientConsentRepository.save(ClientConsentEntity.builder()
                                .productTemplateId(productTemplateEntity.getId())
                                .individualId(clientConsentDto.getIndividualId())
                                .date(new Date())
                                .optOutReason("Accept")
                                .build());
                    });
        }

    }
}
