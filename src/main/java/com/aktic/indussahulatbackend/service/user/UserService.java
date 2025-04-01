package com.aktic.indussahulatbackend.service.user;

import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import com.aktic.indussahulatbackend.model.entity.Patient;
import com.aktic.indussahulatbackend.model.response.actor.AmbulanceDriverDTO;
import com.aktic.indussahulatbackend.model.response.actor.AmbulanceProviderDTO;
import com.aktic.indussahulatbackend.model.response.actor.PatientDTO;
import com.aktic.indussahulatbackend.repository.ambulanceDriver.AmbulanceDriverRepository;
import com.aktic.indussahulatbackend.repository.ambulanceProvider.AmbulanceProviderRepository;
import com.aktic.indussahulatbackend.repository.patient.PatientRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final PatientRepository patientRepository;
    private final AmbulanceDriverRepository ambulanceDriverRepository;
    private final AmbulanceProviderRepository ambulanceProviderRepository;
    private final AuthService authService;

    @Transactional
    public ResponseEntity<ApiResponse<PatientDTO>> getPatientInfo() {
        try{
            Patient currentUser = (Patient) authService.getCurrentUser();

            Patient currentPatient = patientRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new NoSuchElementException("Patient not found"));

            return ResponseEntity.ok(new ApiResponse<>(true, "Patient info fetched successfully", new PatientDTO(currentPatient)));

        }catch (NoSuchElementException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch(Exception e){
            log.error("Error occurred while fetching patient info: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<AmbulanceDriverDTO>> getAmbulanceDriverInfo() {
        try{
            AmbulanceDriver currentUser = (AmbulanceDriver) authService.getCurrentUser();

            AmbulanceDriver currentAmbulanceDriver = ambulanceDriverRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new NoSuchElementException("Ambulance Driver not found"));

            System.out.println("Current Ambulance Driver: " + currentAmbulanceDriver);

            return ResponseEntity.ok(new ApiResponse<>(true, "Ambulance Driver info fetched successfully", new AmbulanceDriverDTO(currentAmbulanceDriver)));

        }catch (NoSuchElementException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }catch (Exception e) {
            log.error("Error occurred while fetching ambulance driver info: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<AmbulanceProviderDTO>> getAmbulanceProviderInfo() {
        try{
            AmbulanceProvider currentUser = (AmbulanceProvider) authService.getCurrentUser();

            AmbulanceProvider currentAmbulanceProvider = ambulanceProviderRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new NoSuchElementException("Ambulance Provider not found"));

            return ResponseEntity.ok(new ApiResponse<>(true, "Ambulance Provider info fetched successfully", new AmbulanceProviderDTO(currentAmbulanceProvider)));
        }catch (NoSuchElementException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }catch (Exception e) {
            log.error("Error occurred while fetching ambulance driver info: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }
}
