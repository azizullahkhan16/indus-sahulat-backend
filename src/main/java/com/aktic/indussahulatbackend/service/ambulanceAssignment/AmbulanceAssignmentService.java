package com.aktic.indussahulatbackend.service.ambulanceAssignment;

import com.aktic.indussahulatbackend.exception.customexceptions.AmbulanceAssignmentNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.AmbulanceNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.AmbulanceProviderNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.DriverNotFoundException;
import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import com.aktic.indussahulatbackend.model.request.AmbulanceAssignmentRequest;
import com.aktic.indussahulatbackend.model.request.LocationDTO;
import com.aktic.indussahulatbackend.model.response.AmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.EventAmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.repository.ambulance.AmbulanceRepository;
import com.aktic.indussahulatbackend.repository.ambulanceAssignment.AmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.ambulanceDriver.AmbulanceDriverRepository;
import com.aktic.indussahulatbackend.repository.ambulanceProvider.AmbulanceProviderRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.service.socket.SocketService;
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
public class AmbulanceAssignmentService {

    private final SnowflakeIdGenerator idGenerator;
    private final AmbulanceAssignmentRepository ambulanceAssignmentRepository;
    private final AmbulanceProviderRepository ambulanceProviderRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final AmbulanceDriverRepository ambulanceDriverRepository;
    private final AuthService authService;
    private final SocketService socketService;

    public ResponseEntity<ApiResponse<AmbulanceAssignmentDTO>> assignDriver(AmbulanceAssignmentRequest ambulanceAssignmentRequest) {
        try {
            AmbulanceProvider ambulanceProvider = (AmbulanceProvider) authService.getCurrentUser();

            Long providerId = ambulanceProvider.getId();

            AmbulanceProvider provider = ambulanceProviderRepository.findById(providerId)
                    .orElseThrow(() -> new AmbulanceProviderNotFoundException(AmbulanceProviderNotFoundException.DEFAULT_MESSAGE));

            Long ambulanceId = ambulanceAssignmentRequest.getAmbulanceId();
            Ambulance ambulance = ambulanceRepository.findById(ambulanceId)
                    .orElseThrow(() -> new AmbulanceNotFoundException(AmbulanceNotFoundException.DEFAULT_MESSAGE));

            Long driverId = ambulanceAssignmentRequest.getDriverId();
            AmbulanceDriver driver = ambulanceDriverRepository.findById(driverId)
                    .orElseThrow(() -> new DriverNotFoundException(DriverNotFoundException.DEFAULT_MESSAGE));

            if (!ambulanceProvider.getCompany().getId().equals(driver.getCompany().getId()) || !ambulanceProvider.getCompany().getId().equals(ambulance.getCompany().getId())) {
                throw new NoSuchElementException("Cannot assign since your company does not matches with the ambulance or drivers company.");
            }
            if (ambulanceAssignmentRepository.existsByAmbulanceDriverAndIsActiveTrue(driver)) {
                return new ResponseEntity<>(new ApiResponse<>(false, "Driver already assigned", null), HttpStatus.NOT_FOUND);
            }
            if (ambulanceAssignmentRepository.existsByAmbulanceAndIsActiveTrue(ambulance)) {
                return new ResponseEntity<>(new ApiResponse<>(false, "Ambulance already assigned", null), HttpStatus.NOT_FOUND);
            }
            AmbulanceAssignment ambulanceAssignment = AmbulanceAssignment.builder()
                    .id(idGenerator.nextId())
                    .ambulanceProvider(provider)
                    .ambulance(ambulance)
                    .ambulanceDriver(driver)
                    .isActive(true)
                    .build();
            ambulanceAssignment = ambulanceAssignmentRepository.save(ambulanceAssignment);

            AmbulanceAssignmentDTO ambulanceAssignmentDTO = new AmbulanceAssignmentDTO(ambulanceAssignment);

            socketService.sendNewAmbulanceAssignmentToDriver(ambulanceAssignmentDTO);

            return new ResponseEntity<>(new ApiResponse<>(true, "Ambulance Driver assigned successfully", ambulanceAssignmentDTO), HttpStatus.OK);  //Returning null cause cant return the ambulanceAssignment obj due to lazy.
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new ApiResponse<>(false, e.getMessage(), null), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Error occurred while assigning ambulance drivers: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<Page<AmbulanceAssignmentDTO>>> getAllActiveAssignments(Integer pageNumber, Integer limit) {
        try {
            AmbulanceProvider provider = (AmbulanceProvider) authService.getCurrentUser();
            Company company = provider.getCompany();

            int page = (pageNumber != null && pageNumber > 0) ? pageNumber : 0;
            int size = (limit != null && limit > 0) ? limit : 10;

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<AmbulanceAssignment> ambulanceAssignments = ambulanceAssignmentRepository.findByIsActiveTrueAndAmbulanceCompanyId(company.getId(), pageable);

            Page<AmbulanceAssignmentDTO> ambulanceAssignmentDTOPage = ambulanceAssignments.map(AmbulanceAssignmentDTO::new);

            return new ResponseEntity<>(new ApiResponse<>(true, "Ambulance Assignments fetched successfully", ambulanceAssignmentDTOPage), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new ApiResponse<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error occurred while retrieving active ambulance assignments: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> unassignAmbulance(Long id) {
        try {

            AmbulanceProvider provider = (AmbulanceProvider) authService.getCurrentUser();
            Company providerCompany = provider.getCompany();

            AmbulanceAssignment ambulanceAssignment = ambulanceAssignmentRepository.findById(id).orElseThrow(() -> new AmbulanceAssignmentNotFoundException(AmbulanceAssignmentNotFoundException.DEFAULT_MESSAGE));

            if (!ambulanceAssignment.getAmbulanceProvider().getCompany().equals(providerCompany)) {
                return new ResponseEntity<>(new ApiResponse(false, "You can only unassign ambulances assigned by your company.", null), HttpStatus.FORBIDDEN);
            }

            ambulanceAssignmentRepository.unassignById(id);
            return new ResponseEntity<>(new ApiResponse(true, "Ambulance Unassigned successfully", null), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while unassigning ambulance: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    public ResponseEntity<ApiResponse<AmbulanceAssignmentDTO>> getMyAmbulance() {
        try {
            AmbulanceDriver driver = (AmbulanceDriver) authService.getCurrentUser();

            AmbulanceAssignment ambulanceAssignment = ambulanceAssignmentRepository.findByAmbulanceDriverAndIsActiveTrue(driver);

            if (ambulanceAssignment == null) {
                return new ResponseEntity<>(new ApiResponse<>(false, "No ambulance assigned yet.", null), HttpStatus.NOT_FOUND);
            }

            AmbulanceAssignmentDTO ambulanceAssignmentDTO = new AmbulanceAssignmentDTO(ambulanceAssignment);

            return new ResponseEntity<>(new ApiResponse<>(true, "Ambulance Assignment fetched successfully", ambulanceAssignmentDTO), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred while retrieving ambulance assignments: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<AmbulanceAssignmentDTO>> updateDriverLocation(LocationDTO locationDTO) {
        try {
            // find active ambulance assignment for the diver
            AmbulanceDriver driver = (AmbulanceDriver) authService.getCurrentUser();

            AmbulanceAssignment ambulanceAssignment = ambulanceAssignmentRepository.findByAmbulanceDriverAndIsActiveTrue(driver);
            if (ambulanceAssignment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No active ambulance assignment found", null));
            }

            // update the location of the driver
            ambulanceAssignment.setDriverLocation(new Location(locationDTO.getLatitude(), locationDTO.getLongitude()));
            ambulanceAssignment = ambulanceAssignmentRepository.save(ambulanceAssignment);

            return ResponseEntity.ok(new ApiResponse<>(true, "Driver location updated successfully", new AmbulanceAssignmentDTO(ambulanceAssignment)));

        } catch (Exception e) {
            log.error("Error occurred while updating driver location: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    public Boolean isEventAmbulanceAssigned(EventAmbulanceAssignment eventAmbulanceAssignment) {
        return eventAmbulanceAssignment.getStatus() == RequestStatus.ACCEPTED
                || eventAmbulanceAssignment.getStatus() == RequestStatus.REQUESTED;
    }
}
