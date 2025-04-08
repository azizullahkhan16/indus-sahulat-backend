package com.aktic.indussahulatbackend.service.incidentEvent;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import com.aktic.indussahulatbackend.model.request.CreateEventDTO;
import com.aktic.indussahulatbackend.model.request.LocationDTO;
import com.aktic.indussahulatbackend.model.request.UpdateAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.EventAmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.EventHospitalAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.model.response.ResponseDTO;
import com.aktic.indussahulatbackend.repository.ambulanceAssignment.AmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.eventAmbulanceAssignment.EventAmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.eventHospitalAssignment.EventHospitalAssignmentRepository;
import com.aktic.indussahulatbackend.repository.incidentEvent.IncidentEventRepository;
import com.aktic.indussahulatbackend.repository.response.ResponseRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import jakarta.validation.Valid;
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

import java.time.Instant;
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
    private final EventHospitalAssignmentRepository eventHospitalAssignmentRepository;

    @Transactional
    public ResponseEntity<ApiResponse<IncidentEventDTO>> createEvent(CreateEventDTO createEventDTO) {
        try{
            Patient patient = (Patient) authService.getCurrentUser();

            IncidentEvent event = IncidentEvent.builder()
                    .id(idGenerator.nextId())
                    .patient(patient)
                    .pickupLocation(new Location(createEventDTO.getLocation().getLatitude(),
                            createEventDTO.getLocation().getLongitude()))
                    .pickupAddress(createEventDTO.getAddress())
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

            if (eventAssignmentOpt.isEmpty() || eventAssignmentOpt.get().getEvent().getStatus() == EventStatus.CANCELLED ||
                    eventAssignmentOpt.get().getEvent().getStatus() == EventStatus.PATIENT_ADMITTED) {
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
    public ResponseEntity<ApiResponse<IncidentEventDTO>> updateAmbulanceAssignment(Long eventAmbulanceAssignmentId, UpdateAssignmentDTO updateAssignmentDTO) {
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

    @Transactional
    public ResponseEntity<ApiResponse<Page<Map<String, Object>>>> getAdmitRequest(Integer pageNumber, Integer limit) {
        try{
            // Get the current authenticated HospitalAdmin
            HospitalAdmin hospitalAdmin = (HospitalAdmin) authService.getCurrentUser();
            // Get the associated hospital
            Hospital hospital = hospitalAdmin.getHospital();

            // Default pagination values
            int page = (pageNumber != null && pageNumber >= 0) ? pageNumber : 0; // Allow page 0
            int size = (limit != null && limit > 0) ? limit : 10;

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            // Find EventAmbulanceAssignments by hospital and REQUESTED status
            Page<EventHospitalAssignment> admitRequests = eventHospitalAssignmentRepository
                    .findByHospitalAndStatus(hospital, RequestStatus.REQUESTED, pageable);

            if (admitRequests.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No admit requests found for this hospital", null));
            }

            // Map EventAmbulanceAssignment and IncidentEvent to Map<String, Object>
            Page<Map<String, Object>> admitRequestMaps = admitRequests.map(assignment -> {
                Map<String, Object> map = new HashMap<>();
                map.put("event", new IncidentEventDTO(assignment.getEvent()));
                map.put("eventHospitalAssignment", new EventHospitalAssignmentDTO(assignment));
                return map;
            });

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Admit requests fetched successfully", admitRequestMaps)
            );
        }catch (Exception e) {
            log.error("Error fetching admit requests: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Error fetching admit requests", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<IncidentEventDTO>> updateHospitalAssignment(Long eventHospitalAssignmentId, UpdateAssignmentDTO updateAssignmentDTO) {
        try{
            // Find the active event ambulance assignment
            EventHospitalAssignment eventAssignment =
                    eventHospitalAssignmentRepository.findById(eventHospitalAssignmentId).orElseThrow(
                            () -> new NoSuchElementException("Event hospital assignment not found")
                    );

            if(eventAssignment.getStatus() != RequestStatus.REQUESTED) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No active event assigned to this hospital", null));
            }

            eventAssignment.setStatus(RequestStatus.valueOf(updateAssignmentDTO.getStatus()));
            // Update the status of the assignment
            if(updateAssignmentDTO.getStatus().equals(RequestStatus.ACCEPTED.name())) {
                eventAssignment.getEvent().setStatus(EventStatus.HOSPITAL_ASSIGNED);
                eventAssignment.getEvent().setHospitalAssignment(eventAssignment);
                eventAssignment.getEvent().setDropOffLocation(eventAssignment.getHospital().getAddress());
            }
            EventHospitalAssignment updatedEventAssignment = eventHospitalAssignmentRepository.save(eventAssignment);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Hospital assigned successfully", new IncidentEventDTO(updatedEventAssignment.getEvent()))
            );

        }catch (NoSuchElementException e) {
            log.error("Event not found: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error updating assignment: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Error updating assignment", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<IncidentEventDTO>> patientPicked(Long eventId) {
        try{
            AmbulanceDriver driver = (AmbulanceDriver) authService.getCurrentUser();
            IncidentEvent incidentEvent = incidentEventRepository.findById(eventId)
                    .orElseThrow(() -> new NoSuchElementException("Event not found"));

            // Check if the event is in the correct status
            if (incidentEvent.getStatus() != EventStatus.HOSPITAL_ASSIGNED) {
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
            incidentEvent.setStatus(EventStatus.PATIENT_PICKED);
            incidentEvent.setPickupTime(Instant.now());
            IncidentEvent updatedEvent = incidentEventRepository.save(incidentEvent);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Patient Picked from the location successfully", new IncidentEventDTO(updatedEvent))
            );

        }catch (NoSuchElementException e) {
            log.error("Event not found: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Event not found", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error updating assignment: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Error updating assignment", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<IncidentEventDTO>> patientDropOff(Long eventId) {
        try{
            AmbulanceDriver driver = (AmbulanceDriver) authService.getCurrentUser();
            IncidentEvent incidentEvent = incidentEventRepository.findById(eventId)
                    .orElseThrow(() -> new NoSuchElementException("Event not found"));

            // Check if the event is in the correct status
            if (incidentEvent.getStatus() != EventStatus.PATIENT_PICKED) {
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
            incidentEvent.setStatus(EventStatus.PATIENT_ADMITTED);
            incidentEvent.setDropOffTime(Instant.now());
            IncidentEvent updatedEvent = incidentEventRepository.save(incidentEvent);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Patient arrived at hospital successfully", new IncidentEventDTO(updatedEvent))
            );

        }catch (NoSuchElementException e) {
            log.error("Event not found: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Event not found", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error updating assignment: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Error updating assignment", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<ApiResponse<IncidentEventDTO>> updateLiveLocation(Long eventId, @Valid LocationDTO locationDTO) {
        try{
            AmbulanceDriver driver = (AmbulanceDriver) authService.getCurrentUser();
            IncidentEvent incidentEvent = incidentEventRepository.findById(eventId)
                    .orElseThrow(() -> new NoSuchElementException("Event not found"));

            // Check if the event is in the correct status
            if (incidentEvent.getStatus() == EventStatus.CREATED
                    || incidentEvent.getStatus() == EventStatus.AMBULANCE_ASSIGNED
                    ||  incidentEvent.getStatus() == EventStatus.PATIENT_ADMITTED
                    || incidentEvent.getStatus() == EventStatus.CANCELLED
            ) {
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
            incidentEvent.setLiveLocation(new Location(locationDTO.getLatitude(), locationDTO.getLongitude()));
            IncidentEvent updatedEvent = incidentEventRepository.save(incidentEvent);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Location updated successfully", new IncidentEventDTO(updatedEvent))
            );

        }catch (NoSuchElementException e) {
            log.error("Event not found: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Event not found", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error updating assignment: {}", e.getMessage());
            return new ResponseEntity<>(new ApiResponse<>(false, "Error updating assignment", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse<IncidentEventDTO>> cancelEvent(Long eventId) {
        try{
            Patient patient = (Patient) authService.getCurrentUser();
            IncidentEvent incidentEvent = incidentEventRepository.findById(eventId)
                    .orElseThrow(() -> new NoSuchElementException("Event not found"));

            // Check if the event is in the correct status
            if (incidentEvent.getStatus() == EventStatus.PATIENT_ADMITTED
                    || incidentEvent.getStatus() == EventStatus.CANCELLED
            ) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Event is not in the correct status", null));
            }

            if(!Objects.equals(incidentEvent.getPatient().getId(), patient.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "You are not authorized to update this event", null));
            }

            // Update the event status
            incidentEvent.setStatus(EventStatus.CANCELLED);
            IncidentEvent updatedEvent = incidentEventRepository.save(incidentEvent);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Incident event cancelled successfully", new IncidentEventDTO(updatedEvent))
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
