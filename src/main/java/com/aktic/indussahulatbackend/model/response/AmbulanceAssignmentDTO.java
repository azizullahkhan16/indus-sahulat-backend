package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.entity.Ambulance;
import com.aktic.indussahulatbackend.model.entity.AmbulanceAssignment;
import com.aktic.indussahulatbackend.model.response.actor.AmbulanceDriverDTO;
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
public class AmbulanceAssignmentDTO {
    private Long id;
    private Ambulance ambulance;
    private AmbulanceDriverDTO ambulanceDriver;
    private AmbulanceProviderDTO ambulanceProvider;
    private Instant createdAt;
    private Instant updatedAt;

    public AmbulanceAssignmentDTO(AmbulanceAssignment ambulanceAssignment) {
        this.id = ambulanceAssignment.getId();
        this.ambulance = ambulanceAssignment.getAmbulance();
        this.ambulanceDriver = new AmbulanceDriverDTO(ambulanceAssignment.getAmbulanceDriver());
        this.ambulanceProvider = new AmbulanceProviderDTO(ambulanceAssignment.getAmbulanceProvider());
        this.createdAt = ambulanceAssignment.getCreatedAt();
        this.updatedAt = ambulanceAssignment.getUpdatedAt();
    }
}
