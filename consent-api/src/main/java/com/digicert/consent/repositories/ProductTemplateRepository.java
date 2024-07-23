package com.digicert.consent.repositories;

import com.digicert.consent.entities.ProductTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductTemplateRepository extends JpaRepository<ProductTemplateEntity, String> {
    Optional<ProductTemplateEntity> findByConsentTemplateIdAndProductId(String consentTemplateId, String productId);

    Optional<ProductTemplateEntity> findByProductId(String productId);

    List<ProductTemplateEntity> findAllByProductId(String productId);

    List<ProductTemplateEntity> findAllByTemplateAndActive(String template, boolean active);

}
