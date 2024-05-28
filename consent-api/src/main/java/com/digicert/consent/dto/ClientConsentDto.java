package com.digicert.consent.dto;

import lombok.Data;

@Data
public class ClientConsentDto {
    private String id;
    private String productTemplateId;
    private String individualId;
    private String date;
    private String outputReason;
}
