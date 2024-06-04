package com.digicert.consent.service;

import com.digicert.consent.config.LanguageConfig;
import com.digicert.consent.dto.LanguageDto;
import com.digicert.consent.entities.LanguageEntity;
import com.digicert.consent.repositories.LanguageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {

    private final ObjectMapper objectMapper;
    private List<LanguageEntity> languages;

    private final LanguageRepository languageRepository;

    @Autowired
    public LanguageService(ObjectMapper objectMapper, LanguageConfig languageConfig, LanguageRepository languageRepository) {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        this.languageRepository = languageRepository;
        this.languages = languageConfig.getLanguages();
    }

    @PostConstruct
    public void loadLanguages() throws IOException {
        reloadLanguages();
    }

    public void reloadLanguages() throws IOException {
        Resource resource = new ClassPathResource("languages.yml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        LanguageConfig newConfig = objectMapper.readValue(yaml, LanguageConfig.class);

        // Save the languages to the database
        for (LanguageEntity language : languages) {
            Optional<LanguageEntity> existingLanguage = languageRepository.findByIsoCode(language.getIsoCode());
            if (existingLanguage.isPresent()) {
                existingLanguage.get().setLanguage(language.getLanguage());
                languageRepository.save(existingLanguage.get());
            } else {
                languageRepository.save(language);
            }
        }
    }

    public List<LanguageEntity> getLanguages() {
        return languageRepository.findAll();
    }

    public LanguageDto getLanguageByIsoCode(String isoCode) {
        Optional<LanguageEntity> languageEntity = Optional.ofNullable(languageRepository.findByIsoCode(isoCode).orElse(null));
        if (languageEntity.isPresent()) {
            return LanguageDto.builder()
                    .id(languageEntity.get().getId())
                    .language(languageEntity.get().getLanguage())
                    .isoCode(languageEntity.get().getIsoCode())
                    .build();
        } else {
            return null;
        }
    }
}
