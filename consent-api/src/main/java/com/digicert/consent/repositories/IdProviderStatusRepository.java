package com.digicert.consent.repositories;

import com.digicert.consent.entities.IdProviderStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IdProviderStatusRepository extends JpaRepository<IdProviderStatusEntity, String> {

    Optional<IdProviderStatusEntity> findIdProviderStatusEntitiesByIdentId(String identId);

}
