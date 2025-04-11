package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.entity.EventAmbulanceAssignment;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.response.actor.AmbulanceProviderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EventAmbulanceAssignmentDTO {
    private Long id;
    private AmbulanceAssignmentDTO ambulanceAssignment;
    private Long eventId;
    private AmbulanceProviderDTO ambulanceProvider;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;

    public EventAmbulanceAssignmentDTO(EventAmbulanceAssignment eventAmbulanceAssignment) {
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
