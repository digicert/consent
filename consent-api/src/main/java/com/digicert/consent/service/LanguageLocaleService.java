package com.digicert.consent.service;

import com.digicert.consent.config.LanguageLocaleConfig;
import com.digicert.consent.config.initializer.CustomInitializer;
import com.digicert.consent.entities.LanguageEntity;
import com.digicert.consent.entities.LocaleEntity;
import com.digicert.consent.entities.LocaleLanguageEntity;
import com.digicert.consent.repositories.LanguageLocaleRepository;
import com.digicert.consent.repositories.LanguageRepository;
import com.digicert.consent.repositories.LocaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class LanguageLocaleService implements CustomInitializer {

    private final ObjectMapper objectMapper;

    private List<LocaleLanguageEntity> localeLanguages;

    private final LanguageLocaleRepository languageLocaleRepository;

    private final LanguageRepository languageRepository;

    private final LocaleRepository localeRepository;

    public LanguageLocaleService(LanguageLocaleConfig languageLocaleConfig, LanguageLocaleRepository languageLocaleRepository,
                                 LanguageRepository languageRepository, LocaleRepository localeRepository) {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        this.languageLocaleRepository = languageLocaleRepository;
        this.localeLanguages = languageLocaleConfig.getLocaleLanguages();
        this.languageRepository = languageRepository;
        this.localeRepository = localeRepository;
    }

    /*@PostConstruct
    @DependsOn({"languageService", "localeService"})
    public void loadLocaleLanguages() {
        try {
            reloadLocaleLanguages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void init() {
        try {
            reloadLocaleLanguages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reloadLocaleLanguages() throws IOException {
        Resource resource = new ClassPathResource("isofiles/locales_languages.yml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        LanguageLocaleConfig newConfig = objectMapper.readValue(yaml, LanguageLocaleConfig.class);
        if (newConfig.getLocaleLanguages() != null) {
            localeLanguages = newConfig.getLocaleLanguages();
        }
        if(localeLanguages != null && !localeLanguages.isEmpty()) {
            for (LocaleLanguageEntity localeLanguage : localeLanguages) {
                // Get the existing language id using iso code
                Optional<LanguageEntity> languageEntity =
                        languageRepository.findByIsoCode(localeLanguage.getLocaleId());
                // Get the existing locale id using locale
                Optional<LocaleEntity> localeEntity =
                        localeRepository.findByLocale(localeLanguage.getLanguageId());
                // Save only if LanguageLocaleRepository do not contain the language and locale id's
                if (languageEntity.isPresent() && localeEntity.isPresent()) {
                    Optional<LocaleLanguageEntity> existingLocaleLanguage =
                            languageLocaleRepository.findByLanguageIdAndLocaleId(languageEntity.get().getId(),
                                    localeEntity.get().getId());
                    if (existingLocaleLanguage.isPresent()) {
                        existingLocaleLanguage.get().setLanguageId(languageEntity.get().getId());
                        existingLocaleLanguage.get().setLocaleId(localeEntity.get().getId());
                        languageLocaleRepository.save(existingLocaleLanguage.get());
                    } else {
                        LocaleLanguageEntity newLocaleLanguage = new LocaleLanguageEntity();
                        newLocaleLanguage.setLanguageId(languageEntity.get().getId());
                        newLocaleLanguage.setLocaleId(localeEntity.get().getId());
                        languageLocaleRepository.save(newLocaleLanguage);
                    }
                }
            }
        }
    }


}
