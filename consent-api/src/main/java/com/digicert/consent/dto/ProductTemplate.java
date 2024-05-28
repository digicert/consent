package com.digicert.consent.dto;

import lombok.Data;

@Data
public class ProductTemplate {
    private String id;
    private String productId;
    private String consentTemplateId;
    private String active;
}
