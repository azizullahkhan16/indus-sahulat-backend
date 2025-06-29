package com.aktic.indussahulatbackend.controller.ambulanceProvider.ambulance;

import com.aktic.indussahulatbackend.model.request.AmbulanceAssignmentRequest;
import com.aktic.indussahulatbackend.model.request.AssignEventAmbulanceDTO;
import com.aktic.indussahulatbackend.model.response.EventAmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.model.response.AmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.service.ambulance.AmbulanceService;
import com.aktic.indussahulatbackend.service.ambulanceAssignment.AmbulanceAssignmentService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ambulance-provider")
@RequiredArgsConstructor
public class AmbulanceProviderAmbulanceController
{
    private final AmbulanceService ambulanceService;
    private final AmbulanceAssignmentService ambulanceAssignmentService;

//    @PostMapping("/get-ambulances")
//    public ResponseEntity<ApiResponse<List<AmbulanceDTO>>> getAvailableAmbulances(@Valid @RequestBody FormRequest formRequest)
//    {
//        return ambulanceService.getAvailableAmbulances(formRequest);
//    }

    @PostMapping("/assign-driver")
    public ResponseEntity<ApiResponse<AmbulanceAssignmentDTO>> assignDriver(@Valid @RequestBody AmbulanceAssignmentRequest ambulanceAssignmentRequest)
    {
        return ambulanceAssignmentService.assignDriver(ambulanceAssignmentRequest);
    }

    @GetMapping("/get-ambulances")
    public ResponseEntity<ApiResponse<Page<AmbulanceDTO>>> getAllAmbulances(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer limit
    )
    {
        return ambulanceService.getAllUnassignedAmbulances(pageNumber,limit);
    }

    @GetMapping("/get-ambulance/{id}")
    public ResponseEntity<ApiResponse<AmbulanceDTO>> getAmbulance(@PathVariable Long id)
    {
        return ambulanceService.getAmbulance(id);
    }

    @GetMapping("/assigned-ambulances")
    public ResponseEntity<ApiResponse<Page<AmbulanceAssignmentDTO>>> getActiveAssignments(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer limit
    )
    {
        return ambulanceAssignmentService.getAllActiveAssignments(pageNumber,limit);
    }

    @PatchMapping("/unassign-ambulance/{id}")
    public ResponseEntity<ApiResponse> UnassignAnAmbulance(@PathVariable Long id)
    {
        return ambulanceAssignmentService.unassignAmbulance(id);
    }

    @GetMapping("/available-ambulances/{eventId}")
    public ResponseEntity<ApiResponse<List<AmbulanceAssignmentDTO>>> getAvailableAmbulances(@PathVariable Long eventId)
    {
        return ambulanceService.getAvailableAmbulances(eventId);
    }

    @PostMapping("/assign-ambulance")
    public ResponseEntity<ApiResponse<EventAmbulanceAssignmentDTO>> assignAmbulance(@Valid @RequestBody AssignEventAmbulanceDTO eventAmbulanceAssignmentDTO)
    {
        return ambulanceService.assignAmbulance(eventAmbulanceAssignmentDTO);
    }

    @GetMapping("/event-ambulance-assignment/{eventAmbulanceId}")
    public ResponseEntity<ApiResponse<EventAmbulanceAssignmentDTO>> getEventAmbulanceAssignment(@PathVariable Long eventAmbulanceId){
        return ambulanceService.getStatus(eventAmbulanceId);
    }
}

