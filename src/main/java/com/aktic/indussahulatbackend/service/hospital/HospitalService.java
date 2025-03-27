package com.aktic.indussahulatbackend.service.hospital;

import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.repository.hospital.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService
{
    @Autowired
    private HospitalRepository hospitalRepository;

    public List<Hospital> getAllHospitals()
    {
        return hospitalRepository.findAll();
    }

}
