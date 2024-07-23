package com.digicert.consent.repositories;

import com.digicert.consent.entities.ClientConsentMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientConsentMetadataRepository extends JpaRepository<ClientConsentMetadataEntity, String> {
}
