package com.aktic.indussahulatbackend.controller.ambulanceAssignment;

import com.aktic.indussahulatbackend.model.entity.Ambulance_Assignment;
import com.aktic.indussahulatbackend.model.request.AmbulanceAssignmentRequest;
import com.aktic.indussahulatbackend.service.ambulanceAssignment.AmbulanceAssignmentService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AmbulanceAssignmentController
{
    @Autowired
    private AmbulanceAssignmentService ambulanceAssignmentService;

    @PostMapping("/assignDriver")
    public ResponseEntity<ApiResponse<Ambulance_Assignment>> assignDriver(@RequestBody AmbulanceAssignmentRequest ambulanceAssignmentRequest)
    {
        Ambulance_Assignment amb_assign = ambulanceAssignmentService.assignDriver(ambulanceAssignmentRequest);
        ApiResponse<Ambulance_Assignment> response = new ApiResponse<>(true,"Driver assigned to Ambulance successfully",amb_assign);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
