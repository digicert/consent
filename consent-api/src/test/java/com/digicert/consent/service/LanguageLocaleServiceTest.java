package com.digicert.consent.service;

import com.digicert.consent.config.LanguageLocaleConfig;
import com.digicert.consent.entities.LanguageEntity;
import com.digicert.consent.entities.LocaleEntity;
import com.digicert.consent.entities.LocaleLanguageEntity;
import com.digicert.consent.repositories.LanguageLocaleRepository;
import com.digicert.consent.repositories.LanguageRepository;
import com.digicert.consent.repositories.LocaleRepository;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class LanguageLocaleServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private LanguageLocaleService languageLocaleService;

    @Mock
    private LanguageLocaleConfig languageLocaleConfig;

    @Mock
    private LanguageLocaleRepository languageLocaleRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private LocaleRepository localeRepository;



    @Test
    public void init() {
        languageLocaleService.init();
    }

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper(new YAMLFactory());
    }

    @Test
    public void reloadLocaleLanguagesTest() throws IOException {
        Resource resource = new ClassPathResource("isofiles/locales_languages.yml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        LanguageLocaleConfig newConfig = objectMapper.readValue(yaml, LanguageLocaleConfig.class);
        when(languageRepository.findByIsoCode(newConfig.getLocaleLanguages().get(0).getLocaleId()))
                .thenReturn(java.util.Optional.of(new LanguageEntity()));
        when(localeRepository.findByLocale(newConfig.getLocaleLanguages().get(0).getLanguageId()))
                .thenReturn(Optional.of(setUpLocaleEntity()));
        when(languageRepository.findByIsoCode(newConfig.getLocaleLanguages().get(0).getLocaleId()))
                .thenReturn(Optional.of(setUpLanguageEntity()));
        languageLocaleService.reloadLocaleLanguages();
    }

    @Test
    public void reloadLocaleLanguagesPresentTest() throws IOException {
        Resource resource = new ClassPathResource("isofiles/locales_languages.yml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        LanguageLocaleConfig newConfig = objectMapper.readValue(yaml, LanguageLocaleConfig.class);
        when(languageRepository.findByIsoCode(newConfig.getLocaleLanguages().get(0).getLocaleId()))
                .thenReturn(java.util.Optional.of(new LanguageEntity()));
        when(localeRepository.findByLocale(newConfig.getLocaleLanguages().get(0).getLanguageId()))
                .thenReturn(Optional.of(setUpLocaleEntity()));
        when(languageRepository.findByIsoCode(newConfig.getLocaleLanguages().get(0).getLocaleId()))
                .thenReturn(Optional.of(setUpLanguageEntity()));
        when(languageLocaleRepository.findByLanguageIdAndLocaleId(any(), any()))
                .thenReturn(Optional.of(setUpLocaleLanguageEntity()));
        languageLocaleService.reloadLocaleLanguages();
    }

    private LanguageEntity setUpLanguageEntity() {
        return LanguageEntity.builder()
                .language("English")
                .build();
    }

    private LocaleEntity setUpLocaleEntity() {
        return LocaleEntity.builder()
                .locale("en_US")
                .build();
    }

    private LocaleLanguageEntity setUpLocaleLanguageEntity() {
        return LocaleLanguageEntity.builder()
                .languageId("1")
                .localeId("1")
                .build();
    }


}
