package com.aktic.indussahulatbackend.controller.ambulanceProvider.ambulance;

import com.aktic.indussahulatbackend.model.entity.AmbulanceAssignment;
import com.aktic.indussahulatbackend.model.request.AmbulanceAssignmentRequest;
import com.aktic.indussahulatbackend.model.request.FormRequest;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.model.response.ambulanceAssignment.AmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.service.ambulance.AmbulanceService;
import com.aktic.indussahulatbackend.service.ambulanceAssignment.AmbulanceAssignmentService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/get-ambulances")
    public ResponseEntity<ApiResponse<List<AmbulanceDTO>>> getAvailableAmbulances(@Valid @RequestBody FormRequest formRequest)
    {
        return ambulanceService.getAvailableAmbulances(formRequest);
    }

    @PostMapping("/assign-driver")
    public ResponseEntity<ApiResponse<AmbulanceAssignmentDTO>> assignDriver(@RequestBody AmbulanceAssignmentRequest ambulanceAssignmentRequest)
    {
        return ambulanceAssignmentService.assignDriver(ambulanceAssignmentRequest);
    }

    @GetMapping("/get-ambulances")
    public ResponseEntity<ApiResponse<List<AmbulanceDTO>>> getAllAmbulances()
    {
        return ambulanceService.getAllUnassignedAmbulances();
    }
    
    @GetMapping("/get-ambulance/{id}")
    public ResponseEntity<ApiResponse<AmbulanceDTO>> getAmbulance(@PathVariable Long id)
    {
        return ambulanceService.getAmbulance(id);
    }

    @GetMapping("/assigned-ambulances")
    public ResponseEntity<ApiResponse<List<AmbulanceAssignmentDTO>>> getActiveAssignments()
    {
        return ambulanceAssignmentService.getAllActiveAssignments();
    }

    @PatchMapping("/unassign-ambulance/{id}")
    public ResponseEntity<ApiResponse> UnassignAnAmbulance(@PathVariable Long id)
    {
        return ambulanceAssignmentService.unassignAmbulance(id);
    }
}

