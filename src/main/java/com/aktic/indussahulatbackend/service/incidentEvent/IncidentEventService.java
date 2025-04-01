package com.aktic.indussahulatbackend.service.incidentEvent;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.entity.Patient;
import com.aktic.indussahulatbackend.model.entity.Response;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.request.LocationDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.model.response.ResponseDTO;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentEventService {

    private final IncidentEventRepository incidentEventRepository;
    private final SnowflakeIdGenerator idGenerator;
    private final AuthService authService;
    private final ResponseRepository responseRepository;

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
}
