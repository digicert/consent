package com.digicert.consent.controller;

import com.digicert.consent.dto.VerificationEmailRequestDto;
import com.digicert.consent.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {

    @InjectMocks
    private EmailController emailController;

    @Mock
    private EmailService emailService;

    @Test
    void voidsendFaceToFaceVerificationEmailTest() {
        emailController.sendVerificationRequest(new VerificationEmailRequestDto());
        verify(emailService).sendFaceToFaceVerificationEmail(any(VerificationEmailRequestDto.class));
    }
}
