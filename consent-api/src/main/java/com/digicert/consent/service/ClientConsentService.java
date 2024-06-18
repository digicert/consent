package com.digicert.consent.service;

import com.digicert.consent.dto.ClientConsentDto;
import com.digicert.consent.entities.ClientConsentEntity;
import com.digicert.consent.entities.ProductEntity;
import com.digicert.consent.repositories.ClientConsentRepository;
import com.digicert.consent.repositories.ProductRepository;
import com.digicert.consent.repositories.ProductTemplateRepository;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class ClientConsentService {

    private final ProductRepository productRepository;

    private final ProductTemplateRepository productTemplateRepository;

    private final ClientConsentRepository clientConsentRepository;

    //private final DateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

    public ClientConsentService(ProductRepository productRepository, ProductTemplateRepository productTemplateRepository, ClientConsentRepository clientConsentRepository) {
        this.productRepository = productRepository;
        this.productTemplateRepository = productTemplateRepository;
        this.clientConsentRepository = clientConsentRepository;
    }

    public void saveClientConsent(ClientConsentDto clientConsentDto) {
        Optional<ProductEntity> products =
                productRepository.findByName(clientConsentDto.getProductName());
        /*Date date = null;
        try {
            date = dateFormat.parse(LocalDateTime.now().toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }*/
        if (products.isPresent()) {
            // Date finalDate = date;
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
