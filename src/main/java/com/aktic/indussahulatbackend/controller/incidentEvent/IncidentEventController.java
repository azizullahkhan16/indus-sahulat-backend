package com.aktic.indussahulatbackend.controller.incidentEvent;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.service.incidentEvent.IncidentEventService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IncidentEventController
{
    @Autowired
    IncidentEventService incidentEventService;

    @PostMapping("/createEvent")
    public ResponseEntity<ApiResponse<IncidentEvent>> createEvent(@RequestBody Location location)
    {
        IncidentEvent e = incidentEventService.createEvent(location);
        ApiResponse<IncidentEvent> apiResponse = new ApiResponse<>(true,"Event created successfully",e);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
