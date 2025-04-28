package com.aktic.indussahulatbackend.controller.ambulanceDriver.hospital;

import com.aktic.indussahulatbackend.model.entity.EventHospitalAssignment;
import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.model.request.SendAdmitRequestDTO;
import com.aktic.indussahulatbackend.model.response.EventHospitalAssignmentDTO;
import com.aktic.indussahulatbackend.service.hospital.HospitalService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ambulance-driver")
@RequiredArgsConstructor
public class AmbulanceDriverHospitalController {
    private final HospitalService hospitalService;

    @GetMapping("/get-hospitals")
    public ResponseEntity<ApiResponse<List<Hospital>>> getAllHospitals(@RequestParam(required = false) Long eventId) {

        return hospitalService.getAllHospitals(eventId);
    }

    @PostMapping("/send-admit-request")
    public ResponseEntity<ApiResponse<EventHospitalAssignmentDTO>> sendAdmitRequest(
            @Valid @RequestBody SendAdmitRequestDTO sendAdmitRequestDTO
    ) {
        return hospitalService.sendAdmitRequest(sendAdmitRequestDTO);
    }

    @GetMapping("/get-admit-request/{eventHospitalAssignmentId}")
    public ResponseEntity<ApiResponse<EventHospitalAssignmentDTO>> getAdmitRequest(
            @PathVariable Long eventHospitalAssignmentId
    ) {
        return hospitalService.getAdmitRequestInfo(eventHospitalAssignmentId);
    }
}
