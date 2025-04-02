package com.aktic.indussahulatbackend.controller.ambulanceDriver.event;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.request.LocationDTO;
import com.aktic.indussahulatbackend.model.request.UpdateAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.service.incidentEvent.IncidentEventService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ambulance-driver")
@RequiredArgsConstructor
public class AmbulanceDriverEventController {

    private final IncidentEventService incidentEventService;

    @GetMapping("/assigned-event")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAssignedEvent() {
        return incidentEventService.getAssignedEvent();
    }

    @PatchMapping("/update-assignment/{eventAmbulanceAssignmentId}")
    public ResponseEntity<ApiResponse<IncidentEventDTO>> updateAmbulanceAssignment(
            @PathVariable Long eventAmbulanceAssignmentId,
            @RequestBody UpdateAssignmentDTO updateAssignmentDTO
    ) {
        return incidentEventService.updateAmbulanceAssignment(eventAmbulanceAssignmentId, updateAssignmentDTO);
    }

    @PatchMapping("/driver-arrived/{eventId}")
    public ResponseEntity<ApiResponse<IncidentEventDTO>> driverArrivedAtPickup(@PathVariable Long eventId) {
        return incidentEventService.driverArrivedAtPickup(eventId);
    }

    @PatchMapping("/patient-picked/{eventId}")
    public ResponseEntity<ApiResponse<IncidentEventDTO>> patientPicked(@PathVariable Long eventId) {
        return incidentEventService.patientPicked(eventId);
    }

    @PatchMapping("/patient-dropOff/{eventId}")
    public ResponseEntity<ApiResponse<IncidentEventDTO>> patientDropOff(@PathVariable Long eventId) {
        return incidentEventService.patientDropOff(eventId);
    }

    @PatchMapping("/update-live-location/{eventId}")
    public ResponseEntity<ApiResponse<IncidentEventDTO>> updateLiveLocation(@PathVariable Long eventId,
                                                                            @Valid @RequestBody LocationDTO locationDTO) {
        return incidentEventService.updateLiveLocation(eventId, locationDTO);
    }
}
