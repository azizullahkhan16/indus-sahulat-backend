package com.aktic.indussahulatbackend.service.ambulanceAssignment;

import com.aktic.indussahulatbackend.exception.customexceptions.AmbulanceNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.AmbulanceProviderNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.DriverNotFoundException;
import com.aktic.indussahulatbackend.model.entity.Ambulance;
import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import com.aktic.indussahulatbackend.model.entity.Ambulance_Assignment;
import com.aktic.indussahulatbackend.model.request.AmbulanceAssignmentRequest;
import com.aktic.indussahulatbackend.repository.ambulance.AmbulanceRepository;
import com.aktic.indussahulatbackend.repository.ambulanceAssignment.AmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.ambulanceDriver.AmbulanceDriverRepository;
import com.aktic.indussahulatbackend.repository.ambulanceProvider.AmbulanceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmbulanceAssignmentService
{
    @Autowired
    private AmbulanceAssignmentRepository ambulanceAssignmentRepository;
    @Autowired
    private AmbulanceProviderRepository ambulanceProviderRepository;
    @Autowired
    private AmbulanceRepository ambulanceRepository;
    @Autowired
    private AmbulanceDriverRepository ambulanceDriverRepository;

    public Ambulance_Assignment assignDriver(AmbulanceAssignmentRequest ambulanceAssignmentRequest)
    {
        Long providerId = ambulanceAssignmentRequest.getAmbulance_providerId();
        AmbulanceProvider provider = ambulanceProviderRepository.findById(providerId).orElseThrow(()-> new AmbulanceProviderNotFoundException(AmbulanceProviderNotFoundException.DEFAULT_MESSAGE));
        Long ambulanceId = ambulanceAssignmentRequest.getAmbulance_id();
        Ambulance ambulance = ambulanceRepository.findById(ambulanceId).orElseThrow(()-> new AmbulanceNotFoundException(AmbulanceNotFoundException.DEFAULT_MESSAGE));
        Long driverId = ambulanceAssignmentRequest.getDriver_id();
        AmbulanceDriver driver = ambulanceDriverRepository.findById(driverId).orElseThrow(()-> new DriverNotFoundException(DriverNotFoundException.DEFAULT_MESSAGE));
        Ambulance_Assignment ambulanceAssignment = Ambulance_Assignment.builder()
                                                    .ambulanceProvider(provider)
                                                    .ambulance(ambulance)
                                                    .ambulanceDriver(driver)
                                                    .build();
        ambulanceAssignmentRepository.save(ambulanceAssignment);
        return ambulanceAssignment;
    }
}
