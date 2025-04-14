package com.aktic.indussahulatbackend.service.socket;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.repository.incidentEvent.IncidentEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final IncidentEventRepository incidentEventRepository;

    @Transactional(readOnly = true)
    public void joinIncidentEvent(Long eventId) {
        try {
            // Fetch event with patient eagerly
            IncidentEvent event = incidentEventRepository.findById(eventId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));

            messagingTemplate.convertAndSend("/user/event/" + eventId, new IncidentEventDTO(event));
        } catch (Exception e) {
            log.error("Error joining event: {}", e.getMessage(), e);
        }
    }
}
