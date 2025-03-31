package com.aktic.indussahulatbackend.service.ambulance;

import com.aktic.indussahulatbackend.exception.customexceptions.AmbulanceNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.PatientNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.QuestionNotFoundException;
import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.enums.AmbulanceType;
import com.aktic.indussahulatbackend.model.request.FormRequest;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.repository.ambulance.AmbulanceRepository;
import com.aktic.indussahulatbackend.repository.patient.PatientRepository;
import com.aktic.indussahulatbackend.repository.question.QuestionRepository;
import com.aktic.indussahulatbackend.repository.response.ResponseRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AmbulanceService {
    private final SnowflakeIdGenerator idGenerator;
    private final AmbulanceRepository ambulanceRepository;
    private final PatientRepository patientRepository;
    private final QuestionRepository questionRepository;
    private final ResponseRepository responseRepository;
    private final AuthService authService;

    public AmbulanceType determineCategory(FormRequest formRequest) {
        boolean isUnconscious = false;
        boolean hasChestPain = false;
        boolean hasSevereBleeding = false;
        boolean hasBreathingIssue = false;
        boolean hasDiabetes = false;
        boolean hasHeartDisease = false;
        boolean hasCancer = false;
        boolean hasAllergies = false;
        boolean hasAccident = false;
        boolean hadHeartAttack = false;
        boolean hadStrokeInjury = false;
        boolean hasTrauma = false;
        boolean isNeonatal = false;
        boolean needOxygenSupply = false;

        List<FormRequest.Answer> answers = formRequest.getAnswerList();
        Patient currentUser = (Patient) authService.getCurrentUser();
        Long patientId = currentUser.getId();

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(PatientNotFoundException.DEFAULT_MESSAGE));

        for (FormRequest.Answer answer : answers) {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new QuestionNotFoundException(QuestionNotFoundException.DEFAULT_MESSAGE));

            List<String> availableOptions = question.getOptions().stream()
                    .map(Option::getOptionText)
                    .toList();

            for (String res : answer.getResponses()) {
                responseRepository.save(
                        Response.builder().id(idGenerator.nextId())
                                .patient(patient)
                                .question(question)
                                .response(res)

                                .build());
                if (availableOptions.contains(res)) {
                    switch (res) {
                        case "Unresponsive" -> isUnconscious = true;
                        case "Chest Pain" -> hasChestPain = true;
                        case "Severe Bleeding" -> hasSevereBleeding = true;
                        case "Difficulty Breathing" -> hasBreathingIssue = true;
                        case "Diabetes" -> hasDiabetes = true;
                        case "Heart Disease" -> hasHeartDisease = true;
                        case "Cancer" -> hasCancer = true;
                        case "Accident" -> hasAccident = true;
                        case "Heart Attack" -> hadHeartAttack = true;
                        case "Stroke Injury" -> hadStrokeInjury = true;
                        case "Neonatal" -> isNeonatal = true;
                        case "Trauma Care" -> hasTrauma = true;
                        case "Oxygen Supply" -> needOxygenSupply = true;
                        case "Other" -> System.out.println("Other symptoms.");
                        default -> System.out.println("Unknown response: " + res);
                    }
                }
            }
        }

        return (isUnconscious || hasChestPain || hasBreathingIssue || hasSevereBleeding) ?
                AmbulanceType.ADVANCED : AmbulanceType.NORMAL;
    }

    public ResponseEntity<ApiResponse<List<AmbulanceDTO>>> getAvailableAmbulances(FormRequest formRequest) {
        try {
            AmbulanceType category = determineCategory(formRequest);
            if (category == null) {
                throw new NoSuchElementException("Cannot find category for the given form request.");
            }
            List<Ambulance> ambulanceList = ambulanceRepository.findByAmbulanceType(category);

            if (ambulanceList == null || ambulanceList.isEmpty()) {
                throw new AmbulanceNotFoundException("No available ambulances found for the given category.");
            }
            List<AmbulanceDTO> ambulanceDTOList = ambulanceList.stream().map(
                    ambulance -> new AmbulanceDTO(
                            ambulance.getAmbulanceType(),
                            ambulance.getCompany().getId(),
                            ambulance.getId(),
                            ambulance.getColor(),
                            ambulance.getImage(),
                            ambulance.getLicensePlate(),
                            ambulance.getMake(),
                            ambulance.getModel(),
                            ambulance.getYear()
                    )
            ).toList();

            return ResponseEntity.ok(new ApiResponse<>(true, "Available ambulances retrieved successfully.", ambulanceDTOList));
        } catch (NoSuchElementException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    public ResponseEntity<ApiResponse<List<AmbulanceDTO>>> getAllAmbulances() {
        try {
        AmbulanceProvider ambulanceProvider = (AmbulanceProvider) authService.getCurrentUser();
        Company providerCompany = ambulanceProvider.getCompany();

        List<Ambulance> ambulancesList = ambulanceRepository.findByCompany(providerCompany);

        if (ambulancesList == null || ambulancesList.isEmpty()) {
            throw new AmbulanceNotFoundException("No ambulances found for this company.");
        }

        List<AmbulanceDTO> List = ambulancesList.stream().map(
                ambulance -> new AmbulanceDTO(
                        ambulance.getAmbulanceType(),
                        ambulance.getCompany().getId(),
                        ambulance.getId(),
                        ambulance.getColor(),
                        ambulance.getImage(),
                        ambulance.getLicensePlate(),
                        ambulance.getMake(),
                        ambulance.getModel(),
                        ambulance.getYear()
                )
        ).toList();

        return new ResponseEntity<>(new ApiResponse<>(true, "Ambulances retrieved successfully.", List), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    public ResponseEntity<ApiResponse<AmbulanceDTO>> getAmbulance(Long id) {
        try {
            AmbulanceProvider ambulanceProvider = (AmbulanceProvider) authService.getCurrentUser();
            Company providerCompany = ambulanceProvider.getCompany();

            Ambulance ambulance = ambulanceRepository.findById(id).orElseThrow(()-> new AmbulanceNotFoundException(AmbulanceNotFoundException.DEFAULT_MESSAGE));

            if (!ambulance.getCompany().getId().equals(providerCompany.getId())) {
                throw new AmbulanceNotFoundException("Ambulance not found in your company.");
            }

            AmbulanceDTO ambulanceDTO = new AmbulanceDTO(
                    ambulance.getAmbulanceType(),
                    ambulance.getCompany().getId(),
                    ambulance.getId(),
                    ambulance.getColor(),
                    ambulance.getImage(),
                    ambulance.getLicensePlate(),
                    ambulance.getMake(),
                    ambulance.getModel(),
                    ambulance.getYear()
            );

            return new ResponseEntity<>(new ApiResponse<>(true, "Ambulance retrieved successfully.", ambulanceDTO), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}