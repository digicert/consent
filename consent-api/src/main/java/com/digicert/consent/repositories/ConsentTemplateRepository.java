package com.digicert.consent.repositories;

import com.digicert.consent.entities.ConsentTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsentTemplateRepository extends JpaRepository<ConsentTemplateEntity, String> {
    Optional<ConsentTemplateEntity> findByLocaleLanguageId(String localeLanguageId);
}
