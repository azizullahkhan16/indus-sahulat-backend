package com.aktic.indussahulatbackend.controller.ambulanceDriver.event;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.request.UpdateAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.service.incidentEvent.IncidentEventService;
import com.aktic.indussahulatbackend.util.ApiResponse;
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
    public ResponseEntity<ApiResponse<IncidentEventDTO>> updateAssignment(
            @PathVariable Long eventAmbulanceAssignmentId,
            @RequestBody UpdateAssignmentDTO updateAssignmentDTO
    ) {
        return incidentEventService.updateAssignment(eventAmbulanceAssignmentId, updateAssignmentDTO);
    }

    @PatchMapping("/driver-arrived/{eventId}")
    public ResponseEntity<ApiResponse<IncidentEventDTO>> driverArrivedAtPickup(@PathVariable Long eventId) {
        return incidentEventService.driverArrivedAtPickup(eventId);
    }
}
