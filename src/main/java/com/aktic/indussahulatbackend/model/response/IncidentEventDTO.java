package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.EventHospitalAssignment;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.request.LocationDTO;
import com.aktic.indussahulatbackend.model.response.actor.AmbulanceProviderDTO;
import com.aktic.indussahulatbackend.model.response.actor.PatientDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class IncidentEventDTO {
    private Long id;
    private PatientDTO patient;
    private AmbulanceProviderDTO ambulanceProvider;
    private EventAmbulanceAssignmentDTO eventAmbulanceAssignment;
    private EventHospitalAssignmentDTO eventHospitalAssignment;
    private Location pickupLocation;
    private Location dropoffLocation;
    private String pickupAddress;
    private String status;
    private Instant pickupTime;
    private Instant dropOffTime;
    private Location liveLocation;
    private List<HospitalDTO> patientPreferredHospitals;
    private Instant createdAt;
    private Instant updatedAt;

    public IncidentEventDTO(IncidentEvent incidentEvent) {
        this.id = incidentEvent.getId();
        this.patient = new PatientDTO(incidentEvent.getPatient());
        this.ambulanceProvider = incidentEvent.getAmbulanceProvider() != null ? new AmbulanceProviderDTO(incidentEvent.getAmbulanceProvider()) : null;
        this.eventAmbulanceAssignment = incidentEvent.getAmbulanceAssignment() != null ? new EventAmbulanceAssignmentDTO(incidentEvent.getAmbulanceAssignment()) : null;
        this.eventHospitalAssignment = incidentEvent.getHospitalAssignment() != null ? new EventHospitalAssignmentDTO(incidentEvent.getHospitalAssignment()) : null;
        this.pickupLocation = incidentEvent.getPickupLocation();
        this.pickupAddress = incidentEvent.getPickupAddress();
        this.dropoffLocation = incidentEvent.getDropOffLocation();
        this.status = incidentEvent.getStatus().name();
        this.pickupTime = incidentEvent.getPickupTime();
        this.dropOffTime = incidentEvent.getDropOffTime();
        this.liveLocation = incidentEvent.getLiveLocation();

        // Fix for patientPreferredHospital (convert list)
        this.patientPreferredHospitals = incidentEvent.getPatientPreferredHospitals() != null
                ? incidentEvent.getPatientPreferredHospitals().stream()
                .map(HospitalDTO::new)
                .collect(Collectors.toList())
                : null;

        this.createdAt = incidentEvent.getCreatedAt();
        this.updatedAt = incidentEvent.getUpdatedAt();
    }

}

