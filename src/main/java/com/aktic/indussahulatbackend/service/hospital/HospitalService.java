package com.aktic.indussahulatbackend.service.hospital;

import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.repository.hospital.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalService
{
    private final HospitalRepository hospitalRepository;

    public List<Hospital> getAllHospitals()
    {
        return hospitalRepository.findAll();
    }

}
