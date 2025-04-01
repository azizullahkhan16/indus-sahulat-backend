package com.aktic.indussahulatbackend.service.ambulanceDriver;


import com.aktic.indussahulatbackend.exception.customexceptions.AmbulanceNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.DriverNotFoundException;
import com.aktic.indussahulatbackend.model.entity.Ambulance;
import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import com.aktic.indussahulatbackend.model.entity.Company;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.model.response.ambulanceDriver.AmbulanceDriverDTO;
import com.aktic.indussahulatbackend.repository.ambulanceAssignment.AmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.ambulanceDriver.AmbulanceDriverRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    public ResponseEntity<ApiResponse<List<AmbulanceDriverDTO>>> getAllUnassignedDriver() {
        try {
            AmbulanceProvider ambulanceProvider = (AmbulanceProvider) authService.getCurrentUser();
            Company providerCompany = ambulanceProvider.getCompany();

            List<AmbulanceDriver> driversList = ambulanceDriverRepository.findByCompany(providerCompany);

            List<AmbulanceDriver> unassignedDrivers = driversList.stream()
                    .filter(driver -> !ambulanceAssignmentRepository.existsByAmbulanceDriverAndIsActiveTrue(driver))
                    .toList();

            if (unassignedDrivers.isEmpty()) {
                throw new DriverNotFoundException("No unassigned drivers found for this company.");
            }

            List<AmbulanceDriverDTO> List = unassignedDrivers.stream().map(
                    driver -> new AmbulanceDriverDTO(
                            driver.getId(),
                            driver.getFirstName(),
                            driver.getLastName(),
                            driver.getEmail(),
                            driver.getCNIC(),
                            driver.getPhone(),
                            driver.getAge(),
                            driver.getImage()
                    )
            ).toList();

            return new ResponseEntity<>(new ApiResponse<>(true, "Drivers retrieved successfully.", List), HttpStatus.OK);
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

            AmbulanceDriverDTO ambulanceDriverDTO = new AmbulanceDriverDTO(
                    ambulanceDriver.getId(),
                    ambulanceDriver.getFirstName(),
                    ambulanceDriver.getLastName(),
                    ambulanceDriver.getEmail(),
                    ambulanceDriver.getCNIC(),
                    ambulanceDriver.getPhone(),
                    ambulanceDriver.getAge(),
                    ambulanceDriver.getImage()
            );

            return new ResponseEntity<>(new ApiResponse<>(true, "Ambulance Driver retrieved successfully.", ambulanceDriverDTO), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
