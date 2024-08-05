package com.digicert.consent.controller;

import com.digicert.consent.dto.VerificationEmailRequestDto;
import com.digicert.consent.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class EmailController {

    @Autowired
    private EmailService emailService;


    @PostMapping("/verification-request/send")
    public ResponseEntity<?> sendVerificationRequest(@RequestBody VerificationEmailRequestDto emailRequest) {
        emailService.sendFaceToFaceVerificationEmail(emailRequest);
        return ResponseEntity.ok().build();
    }

}
