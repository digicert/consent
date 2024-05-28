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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locale")
public class LocaleEntity {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false,
            columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "locale")
    private String locale;

}
