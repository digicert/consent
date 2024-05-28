package com.digicert.consent.dto;

import lombok.Data;

@Data
public class ConsentTemplateDto {
    private String id;
    private String localeLanguageId;
    private String templatePdf;
}
