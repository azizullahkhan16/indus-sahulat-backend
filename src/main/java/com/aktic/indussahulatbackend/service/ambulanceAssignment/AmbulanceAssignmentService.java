package com.aktic.indussahulatbackend.service.ambulanceAssignment;

import com.aktic.indussahulatbackend.exception.customexceptions.AmbulanceNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.AmbulanceProviderNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.DriverNotFoundException;
import com.aktic.indussahulatbackend.model.entity.Ambulance;
import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import com.aktic.indussahulatbackend.model.entity.AmbulanceAssignment;
import com.aktic.indussahulatbackend.model.request.AmbulanceAssignmentRequest;
import com.aktic.indussahulatbackend.model.response.AmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.repository.ambulance.AmbulanceRepository;
import com.aktic.indussahulatbackend.repository.ambulanceAssignment.AmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.ambulanceDriver.AmbulanceDriverRepository;
import com.aktic.indussahulatbackend.repository.ambulanceProvider.AmbulanceProviderRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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
    private final AuthService authService;

    public ResponseEntity<ApiResponse<AmbulanceAssignmentDTO>> assignDriver(AmbulanceAssignmentRequest ambulanceAssignmentRequest)
    {
        try{
            AmbulanceProvider ambulanceProvider = (AmbulanceProvider) authService.getCurrentUser();
            Long providerId = ambulanceProvider.getId();
            AmbulanceProvider provider = ambulanceProviderRepository.findById(providerId).orElseThrow(()-> new AmbulanceProviderNotFoundException(AmbulanceProviderNotFoundException.DEFAULT_MESSAGE));
            Long ambulanceId = ambulanceAssignmentRequest.getAmbulance_id();
            Ambulance ambulance = ambulanceRepository.findById(ambulanceId).orElseThrow(()-> new AmbulanceNotFoundException(AmbulanceNotFoundException.DEFAULT_MESSAGE));
            Long driverId = ambulanceAssignmentRequest.getDriver_id();
            AmbulanceDriver driver = ambulanceDriverRepository.findById(driverId).orElseThrow(()-> new DriverNotFoundException(DriverNotFoundException.DEFAULT_MESSAGE));
            if (!ambulanceProvider.getCompany().getId().equals(driver.getCompany().getId()) || !ambulanceProvider.getCompany().getId().equals(ambulance.getCompany().getId()))
            {
                throw new NoSuchElementException("Cannot assign since your company does not matches with the ambulance or drivers company.");
            }
            if (ambulanceAssignmentRepository.existsByAmbulanceDriverAndIsActiveTrue(driver))
            {
                return new ResponseEntity<>(new ApiResponse<>(false,"Driver already assigned",null), HttpStatus.NOT_FOUND);
            }
            if (ambulanceAssignmentRepository.existsByAmbulanceAndIsActiveTrue(ambulance))
            {
                return new ResponseEntity<>(new ApiResponse<>(false,"Ambulance already assigned",null), HttpStatus.NOT_FOUND);
            }
            AmbulanceAssignment ambulanceAssignment = AmbulanceAssignment.builder()
                    .id(idGenerator.nextId())
                    .ambulanceProvider(provider)
                    .ambulance(ambulance)
                    .ambulanceDriver(driver)
                    .isActive(true)
                    .build();
            ambulanceAssignmentRepository.save(ambulanceAssignment);

            AmbulanceAssignmentDTO ambulanceAssignmentDTO = new AmbulanceAssignmentDTO(ambulanceAssignment);

            return new ResponseEntity<>(new ApiResponse<>(true, "Ambulance Driver assigned successfully", ambulanceAssignmentDTO), HttpStatus.OK);  //Returning null cause cant return the ambulanceAssignment obj due to lazy.
        } catch (Exception e) {
            log.error("Error occurred while assigning ambulance drivers: {}", e.getMessage());
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<Page<AmbulanceAssignmentDTO>>> getAllActiveAssignments(Integer pageNumber, Integer limit)
    {
        try {
            int page = (pageNumber != null && pageNumber > 0) ? pageNumber : 0;
            int size = (limit != null && limit > 0) ? limit : 10;

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<AmbulanceAssignment> ambulanceAssignments = ambulanceAssignmentRepository.findByIsActiveTrue(pageable);

            if (ambulanceAssignments.isEmpty())
            {
                throw new NoSuchElementException("No active ambulance assignments found.");
            }

            Page<AmbulanceAssignmentDTO> ambulanceAssignmentDTOPage = ambulanceAssignments.map(AmbulanceAssignmentDTO::new);

            return new ResponseEntity<>(new ApiResponse<>(true,"Ambulance Assignments fetched successfully", ambulanceAssignmentDTOPage), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while retrieving active ambulance assignments: {}", e.getMessage());
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> unassignAmbulance(Long id) {
        ambulanceAssignmentRepository.unassignById(id);
        return new ResponseEntity<>(new ApiResponse(true,"Ambulance Unassigned successfully",null), HttpStatus.OK);
    }
}
