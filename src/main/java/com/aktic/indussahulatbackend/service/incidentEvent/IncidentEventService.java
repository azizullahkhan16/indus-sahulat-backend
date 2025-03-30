package com.aktic.indussahulatbackend.service.incidentEvent;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.repository.incidentEvent.IncidentEventRepository;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentEventService {

    private final IncidentEventRepository incidentEventRepository;
    private final SnowflakeIdGenerator idGenerator;

    public ResponseEntity<ApiResponse<IncidentEvent>> createEvent(Location location)
    {
        IncidentEvent event = IncidentEvent.builder()
                .id(idGenerator.nextId())
                                .pickup(location).build();
        incidentEventRepository.save(event);
        return new ResponseEntity<>(new ApiResponse<>(true,"Event created successfully",event), HttpStatus.OK);
    }

}
