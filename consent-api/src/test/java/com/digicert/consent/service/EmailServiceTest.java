package com.digicert.consent.service;

import com.digicert.consent.dto.VerificationEmailRequestDto;
import com.digicert.consent.email.DigimailClient;
import com.digicert.consent.email.UnbrandedEmailMessageResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private DigimailClient digimailClient;

    @Test
    void sendFaceToFaceVerificationEmailTest() {

        VerificationEmailRequestDto emailRequest = new VerificationEmailRequestDto();
        emailRequest.setCountry("USA");
        emailRequest.setFirstName("John");
        emailRequest.setLastName("Doe");
        emailRequest.setCountryDialingCode("+1");
        emailRequest.setPhoneNumber("1234567890");
        emailRequest.setToEmailAddress("john.doe@example.com");

        // Act
        emailService.sendFaceToFaceVerificationEmail(emailRequest);

        // Assert
        ArgumentCaptor<UnbrandedEmailMessageResource> emailCaptor = ArgumentCaptor.forClass(UnbrandedEmailMessageResource.class);
        verify(digimailClient).sendEmailTemplate(emailCaptor.capture(), eq("consent_manual_f2f_verification_request_english"));

        UnbrandedEmailMessageResource capturedEmail = emailCaptor.getValue();
        assertEquals("qv.cas.nl@digicert.com", capturedEmail.getTo().getEmailAddress());
        assertEquals("John Doe", capturedEmail.getTo().getProperName());
        assertEquals("Manual F2F Request", capturedEmail.getData().get("subject"));
        assertEquals("USA", capturedEmail.getData().get("country"));
        assertEquals("John", capturedEmail.getData().get("firstName"));
        assertEquals("+1", capturedEmail.getData().get("countryCode"));
        assertEquals("1234567890", capturedEmail.getData().get("phone"));
        assertEquals("john.doe@example.com", capturedEmail.getData().get("email"));
    }

}
