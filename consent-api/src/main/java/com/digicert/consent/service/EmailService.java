package com.digicert.consent.service;

import com.digicert.consent.config.initializer.CustomInitializer;
import com.digicert.consent.dto.VerificationEmailRequestDto;
import com.digicert.consent.email.DigimailClient;
import com.digicert.consent.email.MailToResource;
import com.digicert.consent.email.UnbrandedEmailMessageResource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailService {

    private final DigimailClient digimailClient;


    public void sendFaceToFaceVerificationEmail(VerificationEmailRequestDto emailRequest) {

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("subject", "Manual F2F Request");
        dataModel.put("country", emailRequest.getCountry());
        dataModel.put("firstName", emailRequest.getFirstName());
        dataModel.put("countryCode", emailRequest.getCountryDialingCode());
        dataModel.put("phone", emailRequest.getPhoneNumber());
        dataModel.put("email", emailRequest.getToEmailAddress());

        MailToResource mailToResource = MailToResource.builder()
                .emailAddress("qv.cas.nl@digicert.com")
                .properName(emailRequest.getFirstName() + " " + emailRequest.getLastName())
                .build();

        UnbrandedEmailMessageResource unbrandedEmailMessageResource = UnbrandedEmailMessageResource.builder()
                .to(mailToResource)
                .data(dataModel)
                .transactionId(UUID.randomUUID().toString())
                .build();


        digimailClient.sendEmailTemplate(unbrandedEmailMessageResource,
                "consent_manual_f2f_verification_request_english");

    }

}
