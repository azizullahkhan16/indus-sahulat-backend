package com.aktic.indussahulatbackend.controller.hospital;

import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.service.hospital.HospitalService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hospital")
@RequiredArgsConstructor
public class HospitalController
{
    private final HospitalService hospitalService;

    @GetMapping("/get-hospitals")
    public ResponseEntity<ApiResponse<List<Hospital>>> getAllHospitals()
    {
        List<Hospital> hospitals = hospitalService.getAllHospitals();
        ApiResponse<List<Hospital>> apiResponse = new ApiResponse<>(true,"Hospitals fetched successfully", hospitals);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
