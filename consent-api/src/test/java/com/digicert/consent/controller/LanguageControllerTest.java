package com.digicert.consent.controller;

import com.digicert.consent.service.LanguageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LanguageControllerTest {

    @Mock
    private LanguageService languageService;

    @InjectMocks
    private LanguageController languageController;

    @Test
    public void reloadLanguagesTest() throws IOException {
        languageController.reloadLanguages();
        verify(languageService).reloadLanguages();
    }

    @Test
    public void getLanguagesTest() {
        languageController.getLanguages();
        verify(languageService).getLanguages();
    }

    @Test
    public void getLanguageByIsoCodeTest() {
        languageController.getLanguageByIsoCode("isoCode");
        verify(languageService).getLanguageByIsoCode("isoCode");
    }
}
