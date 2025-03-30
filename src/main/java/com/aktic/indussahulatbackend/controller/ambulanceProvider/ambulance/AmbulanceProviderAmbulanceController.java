package com.aktic.indussahulatbackend.controller.ambulanceProvider.ambulance;

import com.aktic.indussahulatbackend.model.entity.Ambulance_Assignment;
import com.aktic.indussahulatbackend.model.request.AmbulanceAssignmentRequest;
import com.aktic.indussahulatbackend.model.request.FormRequest;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.service.ambulance.AmbulanceService;
import com.aktic.indussahulatbackend.service.ambulanceAssignment.AmbulanceAssignmentService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse<Ambulance_Assignment>> assignDriver(@RequestBody AmbulanceAssignmentRequest ambulanceAssignmentRequest)
    {
        return ambulanceAssignmentService.assignDriver(ambulanceAssignmentRequest);
    }
}

