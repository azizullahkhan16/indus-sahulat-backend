package com.aktic.indussahulatbackend.service.incidentEvent;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import com.aktic.indussahulatbackend.model.request.LocationDTO;
import com.aktic.indussahulatbackend.model.request.UpdateAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.EventAmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.model.response.ResponseDTO;
import com.aktic.indussahulatbackend.repository.ambulanceAssignment.AmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.eventAmbulanceAssignment.EventAmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.incidentEvent.IncidentEventRepository;
import com.aktic.indussahulatbackend.repository.response.ResponseRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentEventService {

    private final IncidentEventRepository incidentEventRepository;
    private final SnowflakeIdGenerator idGenerator;
    private final AuthService authService;
    private final ResponseRepository responseRepository;
    private final AmbulanceAssignmentRepository ambulanceAssignmentRepository;
    private final EventAmbulanceAssignmentRepository eventAmbulanceAssignmentRepository;

    @Transactional
    public ResponseEntity<ApiResponse<IncidentEventDTO>> createEvent(LocationDTO locationDTO) {
        try{
            Patient patient = (Patient) authService.getCurrentUser();

            IncidentEvent event = IncidentEvent.builder()
                    .id(idGenerator.nextId())
                    .patient(patient)
                    .pickupLocation(new Location(locationDTO.getLatitude(), locationDTO.getLongitude()))
                    .build();

            IncidentEvent incidentEvent = incidentEventRepository.save(event);

            return new ResponseEntity<>(new ApiResponse<>(true, "Event created successfully", new IncidentEventDTO(incidentEvent)), HttpStatus.CREATED);
        }catch(Exception e) {
            log.error("Error creating event: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Error creating event", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<Page<IncidentEventDTO>>> getActiveEvents(Integer pageNumber, Integer limit) {
        try{
            int page = (pageNumber != null && pageNumber > 0) ? pageNumber : 0;
            int size = (limit != null && limit > 0) ? limit : 10;

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<IncidentEvent> activeEvents = incidentEventRepository.findByStatus(EventStatus.CREATED, pageable);

            return new ResponseEntity<>(new ApiResponse<>(true, "Active events fetched successfully", activeEvents.map(IncidentEventDTO::new)), HttpStatus.OK);


        }catch(Exception e) {
            log.error("Error fetching active events: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Error fetching active events", null), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEvent(Long eventId) {
        try{
            IncidentEvent incidentEvent = incidentEventRepository.findById(eventId)
                    .orElseThrow(() -> new NoSuchElementException("Event not found"));

            // extract the responses from the incidentEvent
            List<Response> responses = responseRepository.findByEvent(incidentEvent);

            // map responses to DTOs if needed
             List<ResponseDTO> patientResponse = responses.stream()
                    .map(ResponseDTO::new)
                    .collect(Collectors.toList());

            Map<String, Object> eventMap = Map.of(
                    "event", new IncidentEventDTO(incidentEvent),
                    "patientResponse", patientResponse
            );

            return new ResponseEntity<>(new ApiResponse<>(true, "Event fetched successfully", eventMap), HttpStatus.OK);

        }catch(NoSuchElementException e){
            log.error("Event not found: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Event not found", null), HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            log.error("Error fetching event: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Error fetching event", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAssignedEvent() {
        try {
            // Get the current authenticated ambulance driver
            AmbulanceDriver driver = (AmbulanceDriver) authService.getCurrentUser();

            // Find the single active ambulance assignment for this driver
            AmbulanceAssignment ambulanceAssignment =
                    ambulanceAssignmentRepository.findByAmbulanceDriverAndIsActiveTrue(driver);

            if (ambulanceAssignment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No active ambulance assignment found for driver", null));
            }

            // Find the active event ambulance assignment
            Optional<EventAmbulanceAssignment> eventAssignmentOpt =
                    eventAmbulanceAssignmentRepository.findByAmbulanceAssignment(ambulanceAssignment);

            if (eventAssignmentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No active event assigned to this ambulance", null));
            }

            if(eventAssignmentOpt.get().getStatus() != RequestStatus.REQUESTED) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No active event assigned to this ambulance", null));
            }

            Map<String, Object> response = Map.of(
                    "eventAmbulanceAssignment", new EventAmbulanceAssignmentDTO(eventAssignmentOpt.get()),
                    "event", new IncidentEventDTO(eventAssignmentOpt.get().getEvent())
            );

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Assigned events retrieved successfully", response)
            );

        }  catch (Exception e) {
            log.error("Error fetching assigned events: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching assigned events", null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<IncidentEventDTO>> updateAssignment(Long eventAmbulanceAssignmentId, UpdateAssignmentDTO updateAssignmentDTO) {
        try{
            AmbulanceDriver ambulanceDriver = (AmbulanceDriver) authService.getCurrentUser();
            // Find the active event ambulance assignment
            EventAmbulanceAssignment eventAssignment =
                    eventAmbulanceAssignmentRepository.findById(eventAmbulanceAssignmentId).orElseThrow(
                            () -> new NoSuchElementException("Event ambulance assignment not found")
                    );

            if(!Objects.equals(eventAssignment.getAmbulanceAssignment().getAmbulanceDriver().getId(), ambulanceDriver.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "You are not authorized to update this assignment", null));
            }

            if(eventAssignment.getStatus() != RequestStatus.REQUESTED) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No active event assigned to this ambulance", null));
            }

            eventAssignment.setStatus(RequestStatus.valueOf(updateAssignmentDTO.getStatus()));
            // Update the status of the assignment
            if(updateAssignmentDTO.getStatus().equals(RequestStatus.ACCEPTED.name())) {
                eventAssignment.getEvent().setStatus(EventStatus.DRIVER_ACCEPTED);
                eventAssignment.getEvent().setAmbulanceAssignment(eventAssignment);
//                incidentEventRepository.save(eventAssignment.getEvent());
            }
            EventAmbulanceAssignment updatedEventAssignment = eventAmbulanceAssignmentRepository.save(eventAssignment);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Assigned events updated successfully", new IncidentEventDTO(updatedEventAssignment.getEvent()))
            );

        }catch (Exception e) {
            log.error("Error updating assignment: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Error updating assignment", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<IncidentEventDTO>> getActiveEvent() {
        try {
            // Get the current authenticated patient
            Patient patient = (Patient) authService.getCurrentUser();
            // Find the most recent active event for this patient
            Optional<IncidentEvent> activeEventOpt = incidentEventRepository
                    .findFirstByPatientAndStatusNotIn(
                            patient,
                            Arrays.asList(EventStatus.PATIENT_ADMITTED, EventStatus.CANCELLED),
                            Sort.by("createdAt").descending()
                    );

            if (activeEventOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No active event found for this patient", null));
            }

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Active event retrieved successfully", new IncidentEventDTO(activeEventOpt.get()))
            );

        } catch (Exception e) {
            log.error("Error fetching active event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error fetching active event", null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<IncidentEventDTO>> driverArrivedAtPickup(Long eventId) {
        try{
            AmbulanceDriver driver = (AmbulanceDriver) authService.getCurrentUser();
            IncidentEvent incidentEvent = incidentEventRepository.findById(eventId)
                    .orElseThrow(() -> new NoSuchElementException("Event not found"));

            // Check if the event is in the correct status
            if (incidentEvent.getStatus() != EventStatus.DRIVER_ACCEPTED) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Event is not in the correct status", null));
            }

            // Check if there’s an assignment and the driver is authorized
            EventAmbulanceAssignment eventAmbulanceAssignment = incidentEvent.getAmbulanceAssignment();
            if (eventAmbulanceAssignment == null || eventAmbulanceAssignment.getAmbulanceAssignment() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "No ambulance assignment found for this event", null));
            }

            AmbulanceDriver assignedDriver = eventAmbulanceAssignment.getAmbulanceAssignment().getAmbulanceDriver();
            if (assignedDriver == null || !assignedDriver.getId().equals(driver.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "You are not authorized to update this assignment", null));
            }

            // Update the event status
            incidentEvent.setStatus(EventStatus.DRIVER_ARRIVED);
            IncidentEvent updatedEvent = incidentEventRepository.save(incidentEvent);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Driver arrived at pickup location", new IncidentEventDTO(updatedEvent))
            );

        }catch (NoSuchElementException e) {
            log.error("Event not found: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Event not found", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error updating assignment: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Error updating assignment", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
