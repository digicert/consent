package com.digicert.consent.repositories;

import com.digicert.consent.entities.ClientConsentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientConsentRepository extends JpaRepository<ClientConsentEntity, String> {

    Optional<ClientConsentEntity> findClientConsentEntityByIndividualId(String individualId);

    @Query(value = "SELECT cc FROM ClientConsentEntity cc " +
            "JOIN  ProductTemplateEntity pt on cc.productTemplateId = pt.id " +
            "JOIN ConsentTemplateEntity ct on pt.id = ct.id " +
            "JOIN LocaleLanguageEntity ll on ct.id = ll.id " +
            "JOIN LocaleEntity l on ll.id = l.id " +
            " WHERE cc.id = :individualId " +
            " AND pt.id = :productId " +
            " AND l.id = :localeId " +
            " ORDER BY cc.date DESC", nativeQuery = false)
    Optional<ClientConsentEntity>
    findClientConsentEntityByIndividualIdAndProductIdAndLocale(@Param("individualId") String individualId,
                                                               @Param("productId") String productId,
                                                               @Param("localeId") String localeId);

    @Query(value = "SELECT cc FROM ClientConsentEntity cc " +
            "JOIN ProductTemplateEntity pt on cc.productTemplateId = pt.id " +
            "JOIN ConsentTemplateEntity ct on pt.consentTemplateId = ct.id " +
            "JOIN ProductEntity p on pt.productId = p.id " +
            "JOIN ConsentTemplateEntity cte on pt.consentTemplateId = cte.id " +
            "WHERE cc.individualId = :individualId " +
            "AND p.id = :productId " +
            "ORDER BY cc.date DESC", nativeQuery = false)
    Optional<ClientConsentEntity>
    findClientConsentEntityByIndividualIdAndProductId(String individualId, String productId);




}
