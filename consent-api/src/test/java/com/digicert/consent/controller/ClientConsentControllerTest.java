package com.digicert.consent.controller;

import com.digicert.consent.dto.ClientConsentDto;
import com.digicert.consent.dto.ClientConsentMetadataDto;
import com.digicert.consent.service.ClientConsentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientConsentControllerTest {

    @Mock
    private ClientConsentService clientConsentService;

    @InjectMocks
    private ClientConsentController clientConsentController;

    @Test
    public void saveClientConsentTest() {
        clientConsentController.saveClientConsent(new ClientConsentDto());
        verify(clientConsentService).saveClientConsent(any(ClientConsentDto.class));
    }




}
