package com.digicert.consent.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;


@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client_consent")
public class ClientConsentEntity {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false,
            columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "product_template_id")
    private String productTemplateId;

    @Column(name = "individual_id")
    private String individualId;

    @Column(name = "date")
    private Date date;

    @Column(name = "optout_reason")
    private String optOutReason;

    @ManyToOne
    @JoinColumn(name = "product_template_id" , insertable = false, updatable = false)
    private ProductTemplateEntity productTemplateEntity;

}
