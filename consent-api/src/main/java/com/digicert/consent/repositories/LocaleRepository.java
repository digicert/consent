package com.digicert.consent.repositories;

import com.digicert.consent.entities.LocaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocaleRepository extends JpaRepository<LocaleEntity, String> {

    // find by locale
    @Query(value = "SELECT l FROM LocaleEntity l WHERE l.locale = :locale")
    Optional<LocaleEntity> findByLocale(String locale);
}
