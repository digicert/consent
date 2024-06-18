package com.digicert.consent.dto;

import lombok.Data;

@Data
public class ClientConsentDto {
    private String productName;
    private String individualId;
    private String outputReason;
}
