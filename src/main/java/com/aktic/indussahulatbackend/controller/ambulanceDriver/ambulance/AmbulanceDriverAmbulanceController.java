package com.aktic.indussahulatbackend.controller.ambulanceDriver.ambulance;

import com.aktic.indussahulatbackend.model.response.AmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.service.ambulanceAssignment.AmbulanceAssignmentService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ambulance-driver")
@RequiredArgsConstructor
public class AmbulanceDriverAmbulanceController {
    private final AmbulanceAssignmentService ambulanceAssignmentService;

    @GetMapping("/get-my-ambulance")
    public ResponseEntity<ApiResponse<AmbulanceAssignmentDTO>> myAmbulance() {
        return ambulanceAssignmentService.getMyAmbulance();
    }
}
