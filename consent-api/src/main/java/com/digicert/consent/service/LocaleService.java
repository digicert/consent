package com.digicert.consent.service;

import com.digicert.consent.config.LocaleConfig;
import com.digicert.consent.config.initializer.CustomInitializer;
import com.digicert.consent.entities.LocaleEntity;
import com.digicert.consent.repositories.LocaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class LocaleService implements CustomInitializer {

    private final ObjectMapper objectMapper;

    private List<LocaleEntity> locales;

    private final LocaleRepository localeRepository;

    public LocaleService(LocaleConfig localeConfig, LocaleRepository localeRepository) {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        this.localeRepository = localeRepository;
        this.locales = localeConfig.getLocales();
    }

    @Override
    public void init() {
        try {
            reloadLocales();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void reloadLocales() throws IOException {
        Resource resource = new ClassPathResource("isofiles/locales.yml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        LocaleConfig newConfig = objectMapper.readValue(yaml, LocaleConfig.class);
        if (newConfig.getLocales() != null) {
            locales = newConfig.getLocales();
        }
        if(locales != null && !locales.isEmpty()) {
            for (LocaleEntity locale : locales) {
                Optional<LocaleEntity> existingLocales =
                        localeRepository.findByLocale(locale.getLocale());
                if (existingLocales.isPresent()) {
                    existingLocales.get().setLocale(locale.getLocale());
                    localeRepository.save(existingLocales.get());
                } else {
                    localeRepository.save(locale);
                }
            }
        }
    }

    public List<LocaleEntity> getLocales() {
        return localeRepository.findAll();
    }


}
