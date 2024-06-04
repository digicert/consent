package com.digicert.consent.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LanguageDto {
    private String id;
    private String language;
    private String isoCode;
}
