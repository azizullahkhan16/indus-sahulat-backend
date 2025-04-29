package com.aktic.indussahulatbackend.service.hospital;

import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.enums.NotificationType;
import com.aktic.indussahulatbackend.model.enums.ReceiverType;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import com.aktic.indussahulatbackend.model.request.NotificationRequestDTO;
import com.aktic.indussahulatbackend.model.request.SendAdmitRequestDTO;
import com.aktic.indussahulatbackend.model.response.EventHospitalAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.repository.eventHospitalAssignment.EventHospitalAssignmentRepository;
import com.aktic.indussahulatbackend.repository.hospital.HospitalRepository;
import com.aktic.indussahulatbackend.repository.incidentEvent.IncidentEventRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.service.notification.NotificationService;
import com.aktic.indussahulatbackend.service.redis.RedisService;
import com.aktic.indussahulatbackend.service.socket.SocketService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.JsonObjectConverter;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalService {
    private final HospitalRepository hospitalRepository;
    private final IncidentEventRepository incidentEventRepository;
    private final AuthService authService;
    private final SnowflakeIdGenerator idGenerator;
    private final EventHospitalAssignmentRepository eventHospitalAssignmentRepository;
    private final NotificationService notificationService;
    private final SocketService socketService;
    private final RedisService redisService;

    @Transactional
    public ResponseEntity<ApiResponse<List<Hospital>>> getAllHospitals(Long eventId) {
        try {
            List<Hospital> hospitals;

            if (eventId != null) {
                IncidentEvent incidentEvent = incidentEventRepository.findById(eventId)
                        .orElseThrow(() -> new NoSuchElementException("Event not found"));

                List<Hospital> preferredHospitals = incidentEvent.getPatientPreferredHospitals();

                if (preferredHospitals != null && !preferredHospitals.isEmpty()) {
                    hospitals = preferredHospitals;
                } else {
                    hospitals = hospitalRepository.findAll();
                }
            } else {
                hospitals = hospitalRepository.findAll();
            }

            if (hospitals.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse<>(false, "No hospitals available", null), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new ApiResponse<>(true, "Hospitals fetched successfully", hospitals), HttpStatus.OK);

        } catch (NoSuchElementException e) {
            log.error("Event not found: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error occurred while fetching hospitals: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Failed to fetch hospitals", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional
    public ResponseEntity<ApiResponse<EventHospitalAssignmentDTO>> sendAdmitRequest(SendAdmitRequestDTO sendAdmitRequestDTO) {
        try {
            AmbulanceDriver driver = (AmbulanceDriver) authService.getCurrentUser();

            IncidentEvent event = incidentEventRepository.findById(sendAdmitRequestDTO.getEventId())
                    .orElseThrow(() -> new NoSuchElementException("Event not found"));

            EventAmbulanceAssignment eventAmbulanceAssignment = event.getAmbulanceAssignment();

            // Check if driver is authorized
            if (eventAmbulanceAssignment == null || !eventAmbulanceAssignment.getAmbulanceAssignment().getAmbulanceDriver().getId().equals(driver.getId())) {
                return new ResponseEntity<>(new ApiResponse<>(false, "You are not authorized to send admit request for this event", null), HttpStatus.UNAUTHORIZED);
            }

            // Event must be at least DRIVER_ACCEPTED
            if (event.getStatus().compareTo(EventStatus.DRIVER_ACCEPTED) < 0) {
                return new ResponseEntity<>(new ApiResponse<>(false, "Hospital selection allowed only after driver accepted the event", null), HttpStatus.BAD_REQUEST);
            }

            Hospital hospital = hospitalRepository.findById(sendAdmitRequestDTO.getHospitalId())
                    .orElseThrow(() -> new NoSuchElementException("Hospital not found"));

            List<Hospital> preferredHospitals = event.getPatientPreferredHospitals(); // Assume a getter is available

            boolean preferencesNullified = false;

            if (preferredHospitals != null && !preferredHospitals.isEmpty()) {
                // Check if all preferred hospitals have their requests rejected
                List<EventHospitalAssignment> preferredAssignments = eventHospitalAssignmentRepository.findByEventAndHospitalIn(event, preferredHospitals);
                
                if (preferredAssignments.size() == preferredHospitals.size()) {
                    preferencesNullified = preferredAssignments.stream()
                            .allMatch(a -> a.getStatus() == RequestStatus.REJECTED);
                }

                if (!preferencesNullified) {
                    // Preferences are still active
                    if (!preferredHospitals.contains(hospital)) {
                        return new ResponseEntity<>(new ApiResponse<>(false, "Hospital is not among patient's preferred hospitals", null), HttpStatus.BAD_REQUEST);
                    }
                }
            } else {
                // No preferences set
                if (!event.getStatus().equals(EventStatus.DRIVER_ARRIVED)) {
                    return new ResponseEntity<>(new ApiResponse<>(false, "Hospital selection allowed only after driver arrival when no patient preference exists", null), HttpStatus.BAD_REQUEST);
                }
            }

            // Check if admit request already sent
            Optional<EventHospitalAssignment> existingHospitalAssignment =
                    eventHospitalAssignmentRepository.findByEventAndHospitalAndStatusIn(
                            event, hospital, List.of(RequestStatus.REQUESTED, RequestStatus.ACCEPTED));

            if (existingHospitalAssignment.isPresent()) {
                return new ResponseEntity<>(new ApiResponse<>(false, "Admit request already sent to this hospital", null), HttpStatus.BAD_REQUEST);
            }

            // Create new admit request
            EventHospitalAssignment eventHospitalAssignment = EventHospitalAssignment.builder()
                    .id(idGenerator.nextId())
                    .hospital(hospital)
                    .event(event)
                    .build();

            eventHospitalAssignment = eventHospitalAssignmentRepository.save(eventHospitalAssignment);

            redisService.saveEventHospitalAssignment(eventHospitalAssignment.getId());

            EventHospitalAssignmentDTO eventHospitalAssignmentDTO = new EventHospitalAssignmentDTO(eventHospitalAssignment);

            // Notify hospital admin
            NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                    .receiverId(eventHospitalAssignment.getHospital().getId())
                    .receiverType(ReceiverType.HOSPITAL_ADMIN)
                    .notificationType(NotificationType.EVENT_HOSPITAL_ASSIGN_REQUEST)
                    .payload(eventHospitalAssignmentDTO)
                    .build();

            notificationService.sendNotification(notificationRequestDTO);

            // Notify through socket
            Map<String, Object> admitRequestMap = Map.of(
                    "event", new IncidentEventDTO(eventHospitalAssignment.getEvent()),
                    "eventHospitalAssignment", eventHospitalAssignmentDTO
            );

            socketService.sendNewAdmitRequest(admitRequestMap);

            return new ResponseEntity<>(new ApiResponse<>(true, "Admit request sent successfully", eventHospitalAssignmentDTO), HttpStatus.OK);

        } catch (NoSuchElementException e) {
            log.error("Error occurred while sending admit request: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error occurred while sending admit request: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Failed to send admit request", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional
    public ResponseEntity<ApiResponse<EventHospitalAssignmentDTO>> getAdmitRequestInfo(Long eventHospitalAssignmentId) {
        try {
            EventHospitalAssignment eventHospitalAssignment = eventHospitalAssignmentRepository.findById(eventHospitalAssignmentId)
                    .orElseThrow(() -> new NoSuchElementException("Admit request not found"));

            return new ResponseEntity<>(new ApiResponse<>(true, "Admit request fetched successfully", new EventHospitalAssignmentDTO(eventHospitalAssignment)), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.error("Error occurred while fetching admit request info: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error occurred while fetching admit request info: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Failed to fetch admit request info", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
