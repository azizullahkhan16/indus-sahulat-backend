package com.aktic.indussahulatbackend.service.incidentEvent;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.repository.incidentEvent.IncidentEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentEventService {

    private final IncidentEventRepository incidentEventRepository;

    public IncidentEvent createEvent(Location location)
    {
        IncidentEvent event = IncidentEvent.builder()
                                .pickup(location).build();
        return incidentEventRepository.save(event);
    }

}
