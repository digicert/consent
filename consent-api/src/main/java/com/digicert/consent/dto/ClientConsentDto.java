package com.digicert.consent.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClientConsentDto {
    private String productName;
    private String template;
    private String individualId;
    private String outputReason;
    private List<ClientConsentMetadataDto> metadata;
}
