package com.digicert.consent.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client_consent_metadata")
public class ClientConsentMetadataEntity {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false,
            columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "client_consent_id")
    private String clientConsentId;

    @Column(name = "metadata_key")
    private String metadataKey;

    @Column(name = "metadata_value")
    private String metadataValue;

}
