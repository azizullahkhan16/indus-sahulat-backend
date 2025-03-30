package com.aktic.indussahulatbackend.controller.ambulanceDriver.hospital;

import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.service.hospital.HospitalService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ambulance-driver")
@RequiredArgsConstructor
public class AmbulanceDriverHospitalController
{
    private final HospitalService hospitalService;

    @GetMapping("/get-hospitals")
    public ResponseEntity<ApiResponse<List<Hospital>>> getAllHospitals()
    {
        return hospitalService.getAllHospitals();
    }
}
