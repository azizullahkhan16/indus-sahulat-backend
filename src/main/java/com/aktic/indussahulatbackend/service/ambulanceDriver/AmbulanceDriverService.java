package com.aktic.indussahulatbackend.service.ambulanceDriver;


import com.aktic.indussahulatbackend.exception.customexceptions.DriverNotFoundException;
import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import com.aktic.indussahulatbackend.model.entity.Company;
import com.aktic.indussahulatbackend.model.response.actor.AmbulanceDriverDTO;
import com.aktic.indussahulatbackend.repository.ambulanceAssignment.AmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.ambulanceDriver.AmbulanceDriverRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmbulanceDriverService {

    private final AmbulanceDriverRepository ambulanceDriverRepository;
    private final AuthService authService;
    private final AmbulanceAssignmentRepository ambulanceAssignmentRepository;


    public ResponseEntity<ApiResponse<Page<AmbulanceDriverDTO>>> getAllUnassignedDriver(Integer pageNumber, Integer limit) {
        try {
            int page = (pageNumber != null && pageNumber > 0) ? pageNumber : 0;
            int size = (limit != null && limit > 0) ? limit : 10;

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            AmbulanceProvider ambulanceProvider = (AmbulanceProvider) authService.getCurrentUser();
            Company providerCompany = ambulanceProvider.getCompany();

            Page<AmbulanceDriver> driversList = ambulanceDriverRepository.findByCompany(providerCompany,pageable);

            List<AmbulanceDriver> unassignedDrivers = driversList.stream()
                    .filter(driver -> !ambulanceAssignmentRepository.existsByAmbulanceDriverAndIsActiveTrue(driver))
                    .toList();

            List<AmbulanceDriverDTO> DriverDTOList = unassignedDrivers.stream()
                    .map(AmbulanceDriverDTO::new)
                    .toList();

            Page<AmbulanceDriverDTO> ambulanceDriverDTOPage = new PageImpl<>(DriverDTOList, pageable, DriverDTOList.size());

            return new ResponseEntity<>(new ApiResponse<>(true, "Drivers retrieved successfully.", ambulanceDriverDTOPage), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    public ResponseEntity<ApiResponse<AmbulanceDriverDTO>> getDriver(Long id) {
        try {
            AmbulanceProvider ambulanceProvider = (AmbulanceProvider) authService.getCurrentUser();
            Company providerCompany = ambulanceProvider.getCompany();

            AmbulanceDriver ambulanceDriver = ambulanceDriverRepository.findById(id).orElseThrow(()-> new DriverNotFoundException(DriverNotFoundException.DEFAULT_MESSAGE));

            if (!ambulanceDriver.getCompany().getId().equals(providerCompany.getId())) {
                throw new DriverNotFoundException("Driver not found in your company.");
            }

            AmbulanceDriverDTO ambulanceDriverDTO = new AmbulanceDriverDTO(ambulanceDriver);

            return new ResponseEntity<>(new ApiResponse<>(true, "Ambulance Driver retrieved successfully.", ambulanceDriverDTO), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
