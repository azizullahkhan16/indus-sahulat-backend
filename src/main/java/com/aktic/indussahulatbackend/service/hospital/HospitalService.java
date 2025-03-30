package com.aktic.indussahulatbackend.service.hospital;

import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.repository.hospital.HospitalRepository;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalService
{
    private final HospitalRepository hospitalRepository;

    public ResponseEntity<ApiResponse<List<Hospital>>> getAllHospitals()
    {
        List<Hospital> hospitals = hospitalRepository.findAll();
        if (hospitals.isEmpty())
        {
            return new ResponseEntity<>(new ApiResponse<>(false,"No Hospitals available",null),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse<>(true,"Hospitals fetched successfully",hospitals), HttpStatus.OK);
    }
}
