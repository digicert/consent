package com.digicert.consent.service;

import com.digicert.consent.config.LanguageConfig;
import com.digicert.consent.entities.LanguageEntity;
import com.digicert.consent.repositories.LanguageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LanguageServiceTest {

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LanguageService languageService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private LanguageConfig languageConfig;

    @Test
    public void init() {
        languageService.init();
    }

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper(new YAMLFactory());
    }

    @Test
    public void reloadLanguagesTest() throws IOException {
        Resource resource = new ClassPathResource("isofiles/languages.yml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        LanguageConfig newConfig = objectMapper.readValue(yaml, LanguageConfig.class);
        when(languageRepository.findByIsoCode(newConfig.getLanguages().get(0).getIsoCode()))
                .thenReturn(Optional.of(new LanguageEntity()));
        languageService.reloadLanguages();
    }

    @Test
    public void getLanguagesTest() {
        when(languageRepository.findAll())
                .thenReturn(List.of(setUpLanguageEntity()));
        languageService.getLanguages();
        assertEquals("English", languageService.getLanguages().get(0).getLanguage());
    }

    private LanguageEntity setUpLanguageEntity() {
        return LanguageEntity.builder()
                .id("1")
                .language("English")
                .isoCode("en")
                .build();
    }

}
