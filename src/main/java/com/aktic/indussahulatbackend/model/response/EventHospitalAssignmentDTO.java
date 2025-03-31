package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.entity.EventAmbulanceAssignment;
import com.aktic.indussahulatbackend.model.entity.EventHospitalAssignment;
import com.aktic.indussahulatbackend.model.entity.Hospital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EventHospitalAssignmentDTO {
    private Long id;
    private Hospital hospital;
    private Long eventId;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;

    public EventHospitalAssignmentDTO(EventHospitalAssignment eventHospitalAssignment) {
        this.id = eventHospitalAssignment.getId();
        this.hospital = eventHospitalAssignment.getHospital();
        this.status = eventHospitalAssignment.getStatus().name();
        this.createdAt = eventHospitalAssignment.getCreatedAt();
        this.updatedAt = eventHospitalAssignment.getUpdatedAt();
        this.eventId = eventHospitalAssignment.getEvent().getId();
    }
}
