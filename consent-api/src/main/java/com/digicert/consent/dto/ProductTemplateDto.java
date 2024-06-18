package com.digicert.consent.dto;

import lombok.Data;

@Data
public class ProductTemplateDto {
    private String id;
    private String productId;
    private String consentTemplateId;
    private String active;
}
