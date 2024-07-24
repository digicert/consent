package com.digicert.consent.service;

import com.digicert.consent.dto.IdentityProviderStatusEvent;
import com.digicert.consent.dto.StatusEvent;
import com.digicert.consent.entities.IdProviderStatusEntity;
import com.digicert.consent.repositories.IdProviderStatusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IdProviderService {

    private final IdProviderStatusRepository idProviderStatusRepository;

    private SimpMessagingTemplate messagingTemplate;

    public Boolean updateIdProviderStatus(String message) {
        StatusEvent eventDetails = getEventDetails(message);
        Optional<IdProviderStatusEntity> foundEntityOpt = idProviderStatusRepository.findIdProviderStatusEntitiesByIdentId(eventDetails.getIdentId());
        if (foundEntityOpt.isPresent()) {
            return updateStatusEntity(foundEntityOpt.get(), eventDetails);
        } else {
            return insertStatusEntity(eventDetails);
        }
    }

    private void notifyClient(StatusEvent eventDetails) {
        messagingTemplate.convertAndSend("/topic/data-updates", eventDetails);
    }

    private Boolean updateStatusEntity(IdProviderStatusEntity foundEntity, StatusEvent eventDetails) {
        foundEntity.setStatus(eventDetails.getStatus());
        foundEntity.setStatusDate(Date.from(Instant.now()));
        IdProviderStatusEntity savedEntity = idProviderStatusRepository.save(foundEntity);
        if (savedEntity.getStatus().equals(eventDetails.getStatus())) {
            notifyClient(eventDetails);
            return true;
        } else {
            return false;
        }
    }

    private Boolean insertStatusEntity(StatusEvent eventDetails) {
        IdProviderStatusEntity createdEntity = idProviderStatusRepository.save(IdProviderStatusEntity.builder()
                .identId(eventDetails.getIdentId())
                .status(eventDetails.getStatus())
                .statusDate(Date.from(Instant.now()))
                .providerName(eventDetails.getProviderName())
                .build());
        if (createdEntity.getStatus().equals(eventDetails.getStatus())) {
            notifyClient(eventDetails);
            return true;
        } else {
            return false;
        }
    }

    private StatusEvent getEventDetails(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            IdentityProviderStatusEvent providerEvent = mapper.readValue(message, IdentityProviderStatusEvent.class);
            return StatusEvent.builder()
                    .identId(providerEvent.getData().getIdentId())
                    .providerName(providerEvent.getData().getProviderName())
                    .status(providerEvent.getData().getStatus())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

}
