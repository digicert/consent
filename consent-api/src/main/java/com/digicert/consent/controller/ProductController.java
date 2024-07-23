package com.digicert.consent.controller;

import com.digicert.consent.dto.ConsentTemplateDto;
import com.digicert.consent.dto.ProductTemplateDto;
import com.digicert.consent.service.ConsentTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private final ConsentTemplateService consentTemplateService;

    public ProductController(ConsentTemplateService consentTemplateService) {
        this.consentTemplateService = consentTemplateService;
    }

    // Get all products with active templates and their template json based on name
    @GetMapping("/products/templates/{name}")
    public ResponseEntity<List<ProductTemplateDto>> getProductsAndTemplates(@PathVariable String name) {
        return ResponseEntity.ok(consentTemplateService.getActiveConsentTemplates(name));
    }
}
