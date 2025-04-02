package com.aktic.indussahulatbackend.controller.hospitalAdmin.event;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.entity.Response;
import com.aktic.indussahulatbackend.model.request.UpdateAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.service.incidentEvent.IncidentEventService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hospital-admin")
@RequiredArgsConstructor
public class HospitalAdminEventController {

    private final IncidentEventService eventService;

    @GetMapping("/admit-requests")
    public ResponseEntity<ApiResponse<Page<Map<String, Object>>>> getAdmitRequests(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        return eventService.getAdmitRequest(pageNumber, limit);
    }

    @PatchMapping("/patient-request/{eventHospitalAssignmentId}")
    public ResponseEntity<ApiResponse<IncidentEventDTO>> updateHospitalAssignment(
            @PathVariable Long eventHospitalAssignmentId,
            @Valid @RequestBody UpdateAssignmentDTO updateAssignmentDTO
            ) {
        return eventService.updateHospitalAssignment(eventHospitalAssignmentId, updateAssignmentDTO);
    }
}
