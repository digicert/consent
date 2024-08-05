package com.digicert.consent.client;

import com.digicert.consent.config.DigimailConfig;
import com.digicert.consent.email.DigimailClient;
import com.digicert.consent.email.MailToResource;
import com.digicert.consent.email.UnbrandedEmailMessageResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DigimailClientTest {

    @InjectMocks
    private DigimailClient digimailClient;

    @Mock
    private DigimailConfig config;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void sendEmailTemplateTest() {
        //given
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("subject", "Verify your identity");
        dataModel.put("individualFullName", "Jane Doe");
        dataModel.put("validationLink", "https://certcentral.digicert.eu/link/biometric-consent/?t=8bfa793b-a3fa-4ebb-b1e4-c801f61c1780");

        MailToResource mailToResource = MailToResource.builder()
                .emailAddress("test@gmail.com")
                .properName("Jane Doe")
                .build();

        UnbrandedEmailMessageResource unbrandedEmailMessageResource = UnbrandedEmailMessageResource.builder()
                .to(mailToResource)
                .data(dataModel)
                .transactionId("412412412412")
                .build();

        when(restTemplate.exchange(any(RequestEntity.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));


        digimailClient.sendEmailTemplate(unbrandedEmailMessageResource, "templateName");

        verify(restTemplate).exchange(any(RequestEntity.class), eq(Void.class));


    }


}
