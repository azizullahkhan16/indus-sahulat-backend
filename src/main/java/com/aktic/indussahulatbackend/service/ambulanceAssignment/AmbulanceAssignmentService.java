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
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmbulanceAssignmentService
{

    private final SnowflakeIdGenerator idGenerator;
    private final AmbulanceAssignmentRepository ambulanceAssignmentRepository;
    private final AmbulanceProviderRepository ambulanceProviderRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceDriverRepository ambulanceDriverRepository;

    public ResponseEntity<ApiResponse<Ambulance_Assignment>> assignDriver(AmbulanceAssignmentRequest ambulanceAssignmentRequest)
    {
        try{
            Long providerId = ambulanceAssignmentRequest.getAmbulance_providerId();
            AmbulanceProvider provider = ambulanceProviderRepository.findById(providerId).orElseThrow(()-> new AmbulanceProviderNotFoundException(AmbulanceProviderNotFoundException.DEFAULT_MESSAGE));
            Long ambulanceId = ambulanceAssignmentRequest.getAmbulance_id();
            Ambulance ambulance = ambulanceRepository.findById(ambulanceId).orElseThrow(()-> new AmbulanceNotFoundException(AmbulanceNotFoundException.DEFAULT_MESSAGE));
            Long driverId = ambulanceAssignmentRequest.getDriver_id();
            AmbulanceDriver driver = ambulanceDriverRepository.findById(driverId).orElseThrow(()-> new DriverNotFoundException(DriverNotFoundException.DEFAULT_MESSAGE));
            Ambulance_Assignment ambulanceAssignment = Ambulance_Assignment.builder()
                    .id(idGenerator.nextId())
                    .ambulanceProvider(provider)
                    .ambulance(ambulance)
                    .ambulanceDriver(driver)
                    .build();
            ambulanceAssignmentRepository.save(ambulanceAssignment);
            return new ResponseEntity<>(new ApiResponse<>(true, "Ambulance Driver assigned successfully", ambulanceAssignment), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while assigning ambulance drivers: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }
}
