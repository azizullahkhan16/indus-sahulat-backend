package com.aktic.indussahulatbackend.service.socket;

import com.aktic.indussahulatbackend.constant.SocketEndpoint;
import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.entity.Notification;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.request.LocationDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.repository.incidentEvent.IncidentEventRepository;
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

    public void sendUpdatedEvent(IncidentEventDTO incidentEvent) {
        try {
            log.info("Sending updated event: {}", incidentEvent);
            messagingTemplate.convertAndSend(SocketEndpoint.INCIDENT_EVENT_UPDATE.getPath() + incidentEvent.getId(), incidentEvent);

            // Handle event termination
            if (incidentEvent.getStatus().equals(EventStatus.PATIENT_ADMITTED.name())
                    || incidentEvent.getStatus().equals(EventStatus.CANCELLED.name())) {
                handleEventTermination(incidentEvent);
            }
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

            messagingTemplate.convertAndSend(SocketEndpoint.INCIDENT_EVENT_LIVE_LOCATION.getPath() + eventId, locationDTO);

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


    public void sendNotificationToUser(Notification notification) {
        try {
            log.info("Sending notification to user: {}", notification);
            messagingTemplate.convertAndSend(SocketEndpoint.USER_NOTIFICATION.getPath() + notification.getReceiverId(), notification);
        } catch (Exception e) {
            log.error("Error sending notification: {}", e.getMessage(), e);
        }
    }

    private void handleEventTermination(IncidentEventDTO incidentEventDTO) {
        try {
            if (!incidentEventDTO.getStatus().equals(EventStatus.PATIENT_ADMITTED.name())
                    && !incidentEventDTO.getStatus().equals(EventStatus.CANCELLED.name())) {
                log.info("Event can not terminated {}", incidentEventDTO.getId());
                return;
            }

            // Clean up pending updates and tasks
            pendingUpdates.remove(incidentEventDTO.getId());
            ScheduledFuture<?> task = scheduledTasks.remove(incidentEventDTO.getId());
            if (task != null) {
                task.cancel(false);
            }

            log.info("Terminated event {}", incidentEventDTO.getId());
        } catch (Exception e) {
            log.error("Error handling event termination: {}", e.getMessage(), e);
        }
    }
}
