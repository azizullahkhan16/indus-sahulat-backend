package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.redis.RedisEventAmbulanceAssignment;
import com.aktic.indussahulatbackend.model.response.actor.AmbulanceProviderDTO;

import java.time.Instant;

public class RedisEventAmbulanceAssignmentDTO {
    private Long id;
    private AmbulanceAssignmentDTO ambulanceAssignment;
    private Long eventId;
    private AmbulanceProviderDTO ambulanceProvider;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;

    public RedisEventAmbulanceAssignmentDTO(RedisEventAmbulanceAssignment eventAmbulanceAssignment) {
        this.id = eventAmbulanceAssignment.getId();
        this.ambulanceAssignment = eventAmbulanceAssignment.getAmbulanceAssignment() != null ?
                new AmbulanceAssignmentDTO(eventAmbulanceAssignment.getAmbulanceAssignment()) : null;
        this.ambulanceProvider = eventAmbulanceAssignment.getAmbulanceProvider() != null ?
                new AmbulanceProviderDTO(eventAmbulanceAssignment.getAmbulanceProvider()) : null;
        this.status = eventAmbulanceAssignment.getStatus().name();
        this.createdAt = eventAmbulanceAssignment.getCreatedAt();
        this.updatedAt = eventAmbulanceAssignment.getUpdatedAt();
        this.eventId = eventAmbulanceAssignment.getEvent().getId();
    }
}
