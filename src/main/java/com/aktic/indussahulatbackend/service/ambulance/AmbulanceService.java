package com.aktic.indussahulatbackend.service.ambulance;

import com.aktic.indussahulatbackend.exception.customexceptions.AmbulanceNotFoundException;
import com.aktic.indussahulatbackend.model.common.eventState.AmbulanceAssignedState;
import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import com.aktic.indussahulatbackend.model.request.AssignEventAmbulanceDTO;
import com.aktic.indussahulatbackend.model.response.AmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.EventAmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.repository.ambulance.AmbulanceRepository;
import com.aktic.indussahulatbackend.repository.ambulanceAssignment.AmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.eventAmbulanceAssignment.EventAmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.incidentEvent.IncidentEventRepository;
import com.aktic.indussahulatbackend.repository.patient.PatientRepository;
import com.aktic.indussahulatbackend.repository.question.QuestionRepository;
import com.aktic.indussahulatbackend.repository.response.ResponseRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


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
    private final AmbulanceAssignmentRepository ambulanceAssignmentRepository;
    private final EventAmbulanceAssignmentRepository eventAmbulanceAssignmentRepository;
    private final IncidentEventRepository incidentEventRepository;

//    public AmbulanceType determineCategory(FormRequest formRequest) {
//        boolean isUnconscious = false;
//        boolean hasChestPain = false;
//        boolean hasSevereBleeding = false;
//        boolean hasBreathingIssue = false;
//        boolean hasDiabetes = false;
//        boolean hasHeartDisease = false;
//        boolean hasCancer = false;
//        boolean hasAllergies = false;
//        boolean hasAccident = false;
//        boolean hadHeartAttack = false;
//        boolean hadStrokeInjury = false;
//        boolean hasTrauma = false;
//        boolean isNeonatal = false;
//        boolean needOxygenSupply = false;
//
//        List<FormRequest.Answer> answers = formRequest.getAnswerList();
//        Patient currentUser = (Patient) authService.getCurrentUser();
//        Long patientId = currentUser.getId();
//
//        Patient patient = patientRepository.findById(patientId)
//                .orElseThrow(() -> new PatientNotFoundException(PatientNotFoundException.DEFAULT_MESSAGE));
//
//        for (FormRequest.Answer answer : answers) {
//            Question question = questionRepository.findById(answer.getQuestionId())
//                    .orElseThrow(() -> new QuestionNotFoundException(QuestionNotFoundException.DEFAULT_MESSAGE));
//
//            List<String> availableOptions = question.getOptions().stream()
//                    .map(Option::getOptionText)
//                    .toList();
//
//            for (String res : answer.getResponses()) {
//                responseRepository.save(
//                        Response.builder().id(idGenerator.nextId())
//                                .patient(patient)
//                                .question(question)
//                                .response(res)
//
//                                .build());
//                if (availableOptions.contains(res)) {
//                    switch (res) {
//                        case "Unresponsive" -> isUnconscious = true;
//                        case "Chest Pain" -> hasChestPain = true;
//                        case "Severe Bleeding" -> hasSevereBleeding = true;
//                        case "Difficulty Breathing" -> hasBreathingIssue = true;
//                        case "Diabetes" -> hasDiabetes = true;
//                        case "Heart Disease" -> hasHeartDisease = true;
//                        case "Cancer" -> hasCancer = true;
//                        case "Accident" -> hasAccident = true;
//                        case "Heart Attack" -> hadHeartAttack = true;
//                        case "Stroke Injury" -> hadStrokeInjury = true;
//                        case "Neonatal" -> isNeonatal = true;
//                        case "Trauma Care" -> hasTrauma = true;
//                        case "Oxygen Supply" -> needOxygenSupply = true;
//                        case "Other" -> System.out.println("Other symptoms.");
//                        default -> System.out.println("Unknown response: " + res);
//                    }
//                }
//            }
//        }
//
//        return (isUnconscious || hasChestPain || hasBreathingIssue || hasSevereBleeding) ?
//                AmbulanceType.ADVANCED : AmbulanceType.NORMAL;
//    }
//
//    public ResponseEntity<ApiResponse<List<AmbulanceDTO>>> getAvailableAmbulances(FormRequest formRequest) {
//        try {
//            AmbulanceType category = determineCategory(formRequest);
//            if (category == null) {
//                throw new NoSuchElementException("Cannot find category for the given form request.");
//            }
//            List<Ambulance> ambulanceList = ambulanceRepository.findByAmbulanceType(category);
//
//            if (ambulanceList == null || ambulanceList.isEmpty()) {
//                throw new AmbulanceNotFoundException("No available ambulances found for the given category.");
//            }
//            List<AmbulanceDTO> ambulanceDTOList = ambulanceList.stream().map(
//                    ambulance -> new AmbulanceDTO(
//                            ambulance.getAmbulanceType(),
//                            ambulance.getCompany().getId(),
//                            ambulance.getId(),
//                            ambulance.getColor(),
//                            ambulance.getImage(),
//                            ambulance.getLicensePlate(),
//                            ambulance.getMake(),
//                            ambulance.getModel(),
//                            ambulance.getYear()
//                    )
//            ).toList();
//
//            return ResponseEntity.ok(new ApiResponse<>(true, "Available ambulances retrieved successfully.", ambulanceDTOList));
//        } catch (NoSuchElementException e) {
//            log.error("Error: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(false, e.getMessage(), null));
//        }
//    }

    @Transactional
    public ResponseEntity<ApiResponse<Page<AmbulanceDTO>>> getAllUnassignedAmbulances(Integer pageNumber, Integer limit) {
        try {
            int page = (pageNumber != null && pageNumber > 0) ? pageNumber : 0;
            int size = (limit != null && limit > 0) ? limit : 10;

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            AmbulanceProvider ambulanceProvider = (AmbulanceProvider) authService.getCurrentUser();
            Company providerCompany = ambulanceProvider.getCompany();

            Page<Ambulance> allAmbulances = ambulanceRepository.findByCompany(providerCompany,pageable);

            List<Ambulance> unassignedAmbulances = allAmbulances.stream()
                    .filter(ambulance -> !ambulanceAssignmentRepository.existsByAmbulanceAndIsActiveTrue(ambulance))
                    .toList();

            List<AmbulanceDTO> ambulanceDTOList = unassignedAmbulances.stream()
                    .map(AmbulanceDTO::new)
                    .toList();
            Page<AmbulanceDTO> ambulanceDTOPage = new PageImpl<>(ambulanceDTOList, pageable, ambulanceDTOList.size());

            return new ResponseEntity<>(new ApiResponse<>(true, "Unassigned ambulances retrieved successfully.", ambulanceDTOPage), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<AmbulanceDTO>> getAmbulance(Long id) {
        try {
            AmbulanceProvider ambulanceProvider = (AmbulanceProvider) authService.getCurrentUser();
            Company providerCompany = ambulanceProvider.getCompany();

            Ambulance ambulance = ambulanceRepository.findById(id).orElseThrow(()-> new AmbulanceNotFoundException(AmbulanceNotFoundException.DEFAULT_MESSAGE));

            if (!ambulance.getCompany().getId().equals(providerCompany.getId())) {
                throw new AmbulanceNotFoundException("Ambulance not found in your company.");
            }

            AmbulanceDTO ambulanceDTO = new AmbulanceDTO(ambulance);

            return new ResponseEntity<>(new ApiResponse<>(true, "Ambulance retrieved successfully.", ambulanceDTO), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<List<AmbulanceAssignmentDTO>>> getAvailableAmbulances(Long eventId) {
        try {
            // Get the current authenticated AmbulanceProvider
            AmbulanceProvider ambulanceProvider = (AmbulanceProvider) authService.getCurrentUser();

            // Get the company associated with the AmbulanceProvider
            Company providerCompany = ambulanceProvider.getCompany();

            // 1. Get all ambulance assignments for the provider's company
            List<AmbulanceAssignment> allAssignments = ambulanceAssignmentRepository.findByAmbulanceProviderCompany(providerCompany);
            if (allAssignments.isEmpty()) {
                throw new NoSuchElementException("No ambulance assignments found for this company.");
            }
            // 2. Filter available ambulances
            List<AmbulanceAssignmentDTO> availableAmbulances = new ArrayList<>();

            for (AmbulanceAssignment assignment : allAssignments) {
                // Check if this assignment is linked to any event
                Optional<EventAmbulanceAssignment> eventAssignment =
                        eventAmbulanceAssignmentRepository
                                .findByAmbulanceAssignmentAndStatus(assignment, RequestStatus.REJECTED);

                // If no event assignment exists, ambulance is available
                if (eventAssignment.isEmpty()) {
                    availableAmbulances.add(new AmbulanceAssignmentDTO(assignment));
                    continue;
                }

                // If there is an event assignment, check the event status
                IncidentEvent event = eventAssignment.get().getEvent();

                // If event is completed or cancelled, ambulance is available
                if (event.getStatus() == EventStatus.PATIENT_ADMITTED
                        || event.getStatus() == EventStatus.CANCELLED
                        || eventAssignment.get().getStatus() == RequestStatus.REJECTED) {
                    availableAmbulances.add(new AmbulanceAssignmentDTO(assignment));
                }
            }

            // Return successful response with available ambulances
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Available ambulances retrieved successfully", availableAmbulances)
            );

        }catch (NoSuchElementException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<EventAmbulanceAssignmentDTO>> assignAmbulance(AssignEventAmbulanceDTO eventAmbulanceAssignmentDTO) {
        try {
            AmbulanceProvider ambulanceProvider = (AmbulanceProvider) authService.getCurrentUser();
            // Check if event exists and the status is CREATED
            IncidentEvent event = incidentEventRepository.findById(eventAmbulanceAssignmentDTO.getEventId())
                    .orElseThrow(() -> new NoSuchElementException("Event not found."));

            if(event.getAmbulanceAssignment() != null) {
                throw new IllegalStateException("Ambulance is already assigned to this event");
            }

            // Check if ambulance assignment exists
            AmbulanceAssignment ambulanceAssignment = ambulanceAssignmentRepository.findById(eventAmbulanceAssignmentDTO.getAmbulanceAssignmentId())
                    .orElseThrow(() -> new NoSuchElementException("Ambulance assignment not found"));

            // Check if this ambulance is already assigned to another active event
            Optional<EventAmbulanceAssignment> existingAssignment =
                    eventAmbulanceAssignmentRepository.findByAmbulanceAssignment(ambulanceAssignment);

            if (existingAssignment.isPresent()) {
                IncidentEvent assignedEvent = existingAssignment.get().getEvent();
                if (assignedEvent.getStatus() != EventStatus.PATIENT_ADMITTED
                        && assignedEvent.getStatus() != EventStatus.CANCELLED
                        && existingAssignment.get().getStatus() != RequestStatus.REJECTED) {
                    throw new IllegalStateException("Ambulance is already assigned to an active event");
                }
            }

            event.nextState(new AmbulanceAssignedState());

            // Create new event ambulance assignment
            EventAmbulanceAssignment eventAmbulanceAssignment = EventAmbulanceAssignment.builder()
                    .id(idGenerator.nextId())
                    .event(event)
                    .ambulanceProvider(ambulanceProvider)
                    .ambulanceAssignment(ambulanceAssignment)
                    .build();

            EventAmbulanceAssignment savedAssignment = eventAmbulanceAssignmentRepository.save(eventAmbulanceAssignment);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Ambulance assigned successfully", new EventAmbulanceAssignmentDTO(savedAssignment))
            );

        } catch (NoSuchElementException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred", null));
        }
    }

    public ResponseEntity<ApiResponse<EventAmbulanceAssignmentDTO>> getStatus(Long id) {
        try {
            EventAmbulanceAssignment eventAmbulanceAssignment = eventAmbulanceAssignmentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Event ambulance assignment not found"));

            EventAmbulanceAssignmentDTO eventAmbulanceAssignmentDTO = new EventAmbulanceAssignmentDTO(eventAmbulanceAssignment);

            return new ResponseEntity<>(new ApiResponse<>(true,"Event Ambulance Assignment fetched successfully",eventAmbulanceAssignmentDTO),HttpStatus.OK);

        } catch (NoSuchElementException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
        catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred", null));
        }
    }
}