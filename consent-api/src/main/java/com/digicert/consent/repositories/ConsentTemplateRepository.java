package com.digicert.consent.repositories;

import com.digicert.consent.entities.ConsentTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentTemplateRepository extends JpaRepository<ConsentTemplateEntity, String> {
    ConsentTemplateEntity findByLocaleLanguageId(String localeLanguageId);
}
