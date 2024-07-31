package com.digicert.consent.controller;

import com.digicert.consent.service.IdProviderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IdentityProviderControllerTest {

    @Mock
    private IdProviderService idProviderService;

    @InjectMocks
    private IdentityProviderController identityProviderController;

    @Test
    public void updateStatusTest() {
        identityProviderController.updateStatus("message");
        verify(idProviderService).updateIdProviderStatus("message");
    }
}
