package com.digicert.consent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdentityProviderStatusEvent {

    private int version;
    private StatusEvent data;
    private String id = UUID.randomUUID().toString();
    private String queuedDate = new Date().toString();
    private int retryCount;

}
