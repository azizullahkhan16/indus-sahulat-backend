package com.aktic.indussahulatbackend.service.socket;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.request.LocationDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.model.response.LiveEventUpdateDTO;
import com.aktic.indussahulatbackend.repository.incidentEvent.IncidentEventRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final IncidentEventRepository incidentEventRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ConcurrentHashMap<Long, AtomicReference<LocationDTO>> pendingUpdates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

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

    public void sendUpdatedEvent(IncidentEventDTO incidentEvent) {
        try {
            messagingTemplate.convertAndSend("/user/event/" + incidentEvent.getId(), incidentEvent);
        } catch (Exception e) {
            log.error("Error sending updated event: {}", e.getMessage(), e);
        }
    }

    public void updateLiveLocation(Long eventId, LocationDTO locationDTO) {
        try {
            // Debounce persistence
            pendingUpdates.computeIfAbsent(eventId, k -> new AtomicReference<>()).set(locationDTO);

            // Cancel existing task if any
            ScheduledFuture<?> existingTask = scheduledTasks.get(eventId);
            if (existingTask != null) {
                existingTask.cancel(false);
            }

            // Schedule new task
            ScheduledFuture<?> newTask = scheduler.schedule(() -> saveLocation(eventId), 10, TimeUnit.SECONDS);
            scheduledTasks.put(eventId, newTask);

            messagingTemplate.convertAndSend("/user/live-location/event/" + eventId, locationDTO);

        } catch (Exception e) {
            log.error("Error updating live location: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public void saveLocation(Long eventId) {
        try {
            AtomicReference<LocationDTO> locationRef = pendingUpdates.get(eventId);
            if (locationRef == null) {
                return; // No updates to save
            }

            LocationDTO locationDTO = locationRef.get();
            if (locationDTO == null) {
                return;
            }

            IncidentEvent event = incidentEventRepository.findById(eventId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));

            event.setLiveLocation(new Location(locationDTO.getLatitude(), locationDTO.getLongitude()));
            incidentEventRepository.save(event);

            log.info("Saved debounced location for event {}: lat={}, lon={}", eventId, locationDTO.getLatitude(), locationDTO.getLongitude());

            // Clean up
            pendingUpdates.remove(eventId);
            scheduledTasks.remove(eventId);

        } catch (Exception e) {
            log.error("Error saving debounced location for event {}: {}", eventId, e.getMessage(), e);
            // Keep pending update for retry if needed
        }
    }
}
