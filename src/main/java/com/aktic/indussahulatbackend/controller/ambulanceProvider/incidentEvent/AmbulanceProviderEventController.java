package com.aktic.indussahulatbackend.controller.ambulanceProvider.incidentEvent;

import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.service.incidentEvent.IncidentEventService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/ambulance-provider")
@RequiredArgsConstructor
public class AmbulanceProviderEventController {

    private final IncidentEventService eventService;

    @GetMapping("/active-events")
    public ResponseEntity<ApiResponse<Page<IncidentEventDTO>>> getActiveEvents(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer limit
    ) {
        return eventService.getActiveEvents(pageNumber, limit);
    }

    @GetMapping("/get-event/{eventId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEvent(
            @PathVariable Long eventId
    ) {
        return eventService.getEvent(eventId);
    }
}
