package com.aktic.indussahulatbackend.controller.ambulanceDriver.ambulance;

import com.aktic.indussahulatbackend.model.request.LocationDTO;
import com.aktic.indussahulatbackend.model.response.AmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.service.ambulanceAssignment.AmbulanceAssignmentService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ambulance-driver")
@RequiredArgsConstructor
public class AmbulanceDriverAmbulanceController {
    private final AmbulanceAssignmentService ambulanceAssignmentService;

    @GetMapping("/get-my-ambulance")
    public ResponseEntity<ApiResponse<AmbulanceAssignmentDTO>> myAmbulance() {
        return ambulanceAssignmentService.getMyAmbulance();
    }

    @PatchMapping("/my-location")
    public ResponseEntity<ApiResponse<AmbulanceAssignmentDTO>> updateDriverLocation(
            @Valid @RequestBody LocationDTO locationDTO
    ) {
        return ambulanceAssignmentService.updateDriverLocation(locationDTO);
    }
}
