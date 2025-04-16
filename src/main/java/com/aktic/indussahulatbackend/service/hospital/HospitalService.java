package com.aktic.indussahulatbackend.service.hospital;

import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.enums.NotificationType;
import com.aktic.indussahulatbackend.model.enums.ReceiverType;
import com.aktic.indussahulatbackend.model.request.NotificationRequestDTO;
import com.aktic.indussahulatbackend.model.request.SendAdmitRequestDTO;
import com.aktic.indussahulatbackend.model.response.EventHospitalAssignmentDTO;
import com.aktic.indussahulatbackend.repository.eventHospitalAssignment.EventHospitalAssignmentRepository;
import com.aktic.indussahulatbackend.repository.hospital.HospitalRepository;
import com.aktic.indussahulatbackend.repository.incidentEvent.IncidentEventRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.service.notification.NotificationService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.JsonObjectConverter;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

    public ResponseEntity<ApiResponse<List<Hospital>>> getAllHospitals() {
        List<Hospital> hospitals = hospitalRepository.findAll();
        if (hospitals.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(false, "No Hospitals available", null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse<>(true, "Hospitals fetched successfully", hospitals), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<EventHospitalAssignmentDTO>> sendAdmitRequest(SendAdmitRequestDTO sendAdmitRequestDTO) {
        try {
            AmbulanceDriver driver = (AmbulanceDriver) authService.getCurrentUser();

            Hospital hospital = hospitalRepository.findById(sendAdmitRequestDTO.getHospitalId())
                    .orElseThrow(() -> new NoSuchElementException("Hospital not found"));


            IncidentEvent event = incidentEventRepository.findById(sendAdmitRequestDTO.getEventId())
                    .orElseThrow(() -> new NoSuchElementException("Event not found"));

            if (!event.getStatus().equals(EventStatus.DRIVER_ARRIVED)) {
                return new ResponseEntity<>(new ApiResponse<>(false, "Event is not in a state to send admit request", null), HttpStatus.BAD_REQUEST);
            }

            EventAmbulanceAssignment eventAmbulanceAssignment = event.getAmbulanceAssignment();

            if (eventAmbulanceAssignment != null && !eventAmbulanceAssignment.getAmbulanceAssignment().getAmbulanceDriver().getId().equals(driver.getId())) {
                return new ResponseEntity<>(new ApiResponse<>(false, "You are not authorized to send admit request for this event", null), HttpStatus.UNAUTHORIZED);
            }

            EventHospitalAssignment eventHospitalAssignment = EventHospitalAssignment.builder()
                    .id(idGenerator.nextId())
                    .hospital(hospital)
                    .event(event)
                    .build();

            eventHospitalAssignment = eventHospitalAssignmentRepository.save(eventHospitalAssignment);
            EventHospitalAssignmentDTO eventHospitalAssignmentDTO = new EventHospitalAssignmentDTO(eventHospitalAssignment);

            // Notify the hospital about the admit request
            NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                    .receiverId(eventHospitalAssignment.getHospital().getId())
                    .receiverType(ReceiverType.HOSPITAL_ADMIN)
                    .notificationType(NotificationType.EVENT_HOSPITAL_ASSIGN_REQUEST)
                    .payload(eventHospitalAssignmentDTO)
                    .build();

            notificationService.sendNotification(notificationRequestDTO);

            return new ResponseEntity<>(new ApiResponse<>(true, "Admit request sent successfully", eventHospitalAssignmentDTO), HttpStatus.OK);

        } catch (NoSuchElementException e) {
            log.error("Error occurred while sending admit request: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error occurred while sending admit request: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Failed to send admit request", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
