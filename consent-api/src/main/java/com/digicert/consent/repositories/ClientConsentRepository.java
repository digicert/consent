package com.digicert.consent.repositories;

import com.digicert.consent.entities.ClientConsentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientConsentRepository extends JpaRepository<ClientConsentEntity, String> {

}
