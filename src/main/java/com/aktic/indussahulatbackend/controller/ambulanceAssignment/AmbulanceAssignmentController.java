package com.aktic.indussahulatbackend.controller.ambulanceAssignment;

import com.aktic.indussahulatbackend.model.entity.Ambulance_Assignment;
import com.aktic.indussahulatbackend.model.request.AmbulanceAssignmentRequest;
import com.aktic.indussahulatbackend.service.ambulanceAssignment.AmbulanceAssignmentService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ambulance-assignment")
@RequiredArgsConstructor
public class AmbulanceAssignmentController
{
    private final AmbulanceAssignmentService ambulanceAssignmentService;

    @PostMapping("/assign-driver")
    public ResponseEntity<ApiResponse<Ambulance_Assignment>> assignDriver(@RequestBody AmbulanceAssignmentRequest ambulanceAssignmentRequest)
    {
        return ambulanceAssignmentService.assignDriver(ambulanceAssignmentRequest);
    }

}
