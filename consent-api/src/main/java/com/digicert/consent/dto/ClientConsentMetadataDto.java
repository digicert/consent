package com.digicert.consent.dto;

import lombok.Data;

@Data
public class ClientConsentMetadataDto {

    private String id;
    private String clientConsentId;
    private String metadataKey;
    private String metadataValue;

}
