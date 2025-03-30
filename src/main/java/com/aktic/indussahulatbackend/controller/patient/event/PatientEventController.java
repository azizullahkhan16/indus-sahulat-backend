package com.aktic.indussahulatbackend.controller.patient.event;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.service.incidentEvent.IncidentEventService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientEventController
{

   private final IncidentEventService incidentEventService;

    @PostMapping("/create-event")
    public ResponseEntity<ApiResponse<IncidentEvent>> createEvent(@RequestBody Location location)
    {
        return incidentEventService.createEvent(location);
    }
}
