package com.digicert.consent.controller;


import com.digicert.consent.dto.ClientConsentDto;
import com.digicert.consent.service.ClientConsentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientConsentController {

    private final ClientConsentService clientConsentService;

    public ClientConsentController(ClientConsentService clientConsentService) {
        this.clientConsentService = clientConsentService;
    }

    @PostMapping("/client-consent")
    public ResponseEntity<?> saveClientConsent(@RequestBody ClientConsentDto clientConsentDto) {
        clientConsentService.saveClientConsent(clientConsentDto);
        return ResponseEntity.ok().build();
    }

}
