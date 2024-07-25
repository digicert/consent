package com.digicert.consent.service;

import com.digicert.consent.config.LocaleConfig;
import com.digicert.consent.entities.LocaleEntity;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocaleServiceTest {

    @Mock
    private LocaleRepository localeRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private LocaleConfig localeConfig;

    @InjectMocks
    private LocaleService localeService;

    @Test
    public void init() {
        localeService.init();
    }

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper(new YAMLFactory());
    }

    @Test
    public void reloadLocalesTest() throws IOException {
        Resource resource = new ClassPathResource("isofiles/locales.yml");
        String yaml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        LocaleConfig newConfig = objectMapper.readValue(yaml, LocaleConfig.class);
        when(localeRepository.findByLocale(newConfig.getLocales().get(0).getLocale()))
                .thenReturn(java.util.Optional.of(new LocaleEntity()));
        localeService.reloadLocales();
        assertEquals(newConfig.getLocales().get(0).getLocale(),
                localeRepository.findByLocale(newConfig.getLocales().get(0).getLocale()).get().getLocale());
    }

    private LocaleEntity setLocaleEntity() {
        return LocaleEntity.builder()
                .locale("en")
                .build();
    }
}
