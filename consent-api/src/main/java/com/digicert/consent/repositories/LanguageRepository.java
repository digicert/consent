package com.digicert.consent.repositories;

import com.digicert.consent.entities.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, String> {
    @Query(value = "SELECT l FROM LanguageEntity l WHERE l.isoCode = :isoCode")
    Optional<LanguageEntity> findByIsoCode(String isoCode);
}
