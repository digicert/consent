package com.digicert.consent.repositories;

import com.digicert.consent.entities.LocaleLanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageLocaleRepository extends JpaRepository<LocaleLanguageEntity, String> {
    Optional<LocaleLanguageEntity> findByLanguageIdAndLocaleId(String languageId, String localeId);

    Optional<LocaleLanguageEntity> findByLocaleId(String localeId);
}
