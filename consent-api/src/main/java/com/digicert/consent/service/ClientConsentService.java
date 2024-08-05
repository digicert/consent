package com.digicert.consent.service;

import com.digicert.consent.dto.ClientConsentDto;
import com.digicert.consent.entities.ClientConsentEntity;
import com.digicert.consent.entities.ClientConsentMetadataEntity;
import com.digicert.consent.entities.LocaleEntity;
import com.digicert.consent.entities.ProductEntity;
import com.digicert.consent.repositories.ClientConsentMetadataRepository;
import com.digicert.consent.repositories.ClientConsentRepository;
import com.digicert.consent.repositories.LocaleRepository;
import com.digicert.consent.repositories.ProductRepository;
import com.digicert.consent.repositories.ProductTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ClientConsentService {

    private final ProductRepository productRepository;

    private final ProductTemplateRepository productTemplateRepository;

    private final ClientConsentRepository clientConsentRepository;

    private final ClientConsentMetadataRepository clientConsentMetadataRepository;
    private final LocaleRepository localeRepository;


    public ClientConsentService(ProductRepository productRepository, ProductTemplateRepository productTemplateRepository,
                                ClientConsentRepository clientConsentRepository,
                                ClientConsentMetadataRepository clientConsentMetadataRepository, LocaleRepository localeRepository) {
        this.productRepository = productRepository;
        this.productTemplateRepository = productTemplateRepository;
        this.clientConsentRepository = clientConsentRepository;
        this.clientConsentMetadataRepository = clientConsentMetadataRepository;
        this.localeRepository = localeRepository;
    }

    public void saveClientConsent(ClientConsentDto clientConsentDto) {
        Optional<ProductEntity> products =
                productRepository.findByName(clientConsentDto.getProductName());
        AtomicReference<ClientConsentEntity> clientConsentEntity =
                new AtomicReference<>(new ClientConsentEntity());
        if (products.isPresent()) {
            productTemplateRepository.findByProductId(products.get().getId())
                    .ifPresent(productTemplateEntity -> {
                        clientConsentEntity.set(clientConsentRepository.save(ClientConsentEntity.builder()
                                .productTemplateId(productTemplateEntity.getId())
                                .individualId(clientConsentDto.getIndividualId())
                                .date(new Date())
                                .optOutReason(clientConsentDto.getOutputReason())
                                .build()));
                    });
        }
        if (clientConsentDto.getMetadata() != null) {
            clientConsentDto.getMetadata().forEach(clientConsentMetadataDto -> {
                int key = 0;
                clientConsentMetadataRepository.save(ClientConsentMetadataEntity.builder()
                        .clientConsentId(clientConsentEntity.get().getId())
                        .metadataKey(clientConsentDto.getMetadata().get(key).getMetadataKey())
                        .metadataValue(clientConsentDto.getMetadata().get(key).getMetadataValue())
                        .build());
                key += 1;
            });
        }

    }

    public ClientConsentEntity findPreviousClientConsent(String individualId, String productName, String locale) {
        Optional<ProductEntity> productEntity =
                productRepository.findByName(productName);
        Optional<ClientConsentEntity> clientConsentIndividual =
                clientConsentRepository.findClientConsentEntityByIndividualId(individualId);
        Optional<LocaleEntity> localeEntity =
                localeRepository.findByLocale(locale);

        if (!productEntity.isPresent()) {
            return null;
        }
        Optional<ClientConsentEntity> products = clientConsentRepository
                .findClientConsentEntityByIndividualIdAndProductIdAndLocale(clientConsentIndividual.get().getId(), productEntity.get().getId(), localeEntity.get().getId());
        if (products.isPresent()) {
            return products.get();
        }
        return null;
    }
}
