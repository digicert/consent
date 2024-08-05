package com.digicert.consent.controller;


import com.digicert.consent.dto.ClientConsentDto;
import com.digicert.consent.entities.ClientConsentEntity;
import com.digicert.consent.service.ClientConsentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // Get previous client consent
    @GetMapping("/client-consent/individual/{individualId}/product/{name}/locale/{locale}")
    public ClientConsentEntity getPreviousClientConsent(@PathVariable String individualId,
                                                        @PathVariable String name,
                                                        @PathVariable String locale) {
        return clientConsentService.findPreviousClientConsent(individualId, name, locale);
    }

}
