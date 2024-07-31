package com.digicert.consent.controller;

import com.digicert.consent.service.ConsentTemplateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ConsentTemplateService consentTemplateService;

    @InjectMocks
    private ProductController productController;

    @Test
    public void getProductsAndTemplatesTest() {
        productController.getProductsAndTemplates("name");
        verify(consentTemplateService).getActiveConsentTemplates("name");
    }

}
