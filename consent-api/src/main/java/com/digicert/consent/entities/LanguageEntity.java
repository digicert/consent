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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "language")
public class LanguageEntity {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false,
            columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "language")
    private String language;

    @Column(name = "iso_code")
    private String isoCode;

}
