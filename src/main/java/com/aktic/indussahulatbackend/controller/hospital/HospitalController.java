package com.aktic.indussahulatbackend.controller.hospital;

import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.service.hospital.HospitalService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HospitalController
{

    @Autowired
    HospitalService hospitalService;

    @GetMapping("/getHospitals")
    public ResponseEntity<ApiResponse<List<Hospital>>> getAllHospitals()
    {
        List<Hospital> hospitals = hospitalService.getAllHospitals();
        ApiResponse<List<Hospital>> apiResponse = new ApiResponse<>(true,"Hospitals fetched successfully", hospitals);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
