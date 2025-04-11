package com.aktic.indussahulatbackend.controller.patient.event;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.request.CreateEventDTO;
import com.aktic.indussahulatbackend.model.request.LocationDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.service.incidentEvent.IncidentEventService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientEventController
{

   private final IncidentEventService incidentEventService;

    @PostMapping("/create-event")
    public ResponseEntity<ApiResponse<IncidentEventDTO>> createEvent(
            @Valid @RequestBody CreateEventDTO createEventDTO
            ) {
        return incidentEventService.createEvent(createEventDTO);
    }

    @GetMapping("/get-active-event")
    public ResponseEntity<ApiResponse<IncidentEventDTO>> getPatientActiveEvent() {
        return incidentEventService.getPatientActiveEvent();
    }

    @PatchMapping("/cancel-event/{eventId}")
    public ResponseEntity<ApiResponse<IncidentEventDTO>> cancelEvent(
            @PathVariable Long eventId
            ) {
        return incidentEventService.cancelEvent(eventId);
    }
}
