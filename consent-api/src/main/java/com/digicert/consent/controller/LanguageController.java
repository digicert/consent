package com.digicert.consent.controller;

import com.digicert.consent.dto.LanguageDto;
import com.digicert.consent.entities.LanguageEntity;
import com.digicert.consent.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class LanguageController {

    private final LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping("/languages")
    public List<LanguageEntity> getLanguages() {
        return languageService.getLanguages();
    }

    @PostMapping("reload-languages")
    public void reloadLanguages() throws IOException {
        languageService.reloadLanguages();
    }

    // Get the language by iso code
    @GetMapping("/languages/{isoCode}")
    public ResponseEntity<LanguageDto> getLanguageByIsoCode(@PathVariable String isoCode) {
        return ResponseEntity.ok(languageService.getLanguageByIsoCode(isoCode));
    }
}
