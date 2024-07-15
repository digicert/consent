package com.digicert.consent.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class ProductTemplateDto {
    private String name;
    private boolean active;
    private JsonNode responseJson;
}
