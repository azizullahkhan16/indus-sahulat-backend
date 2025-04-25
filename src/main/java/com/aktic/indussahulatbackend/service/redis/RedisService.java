package com.aktic.indussahulatbackend.service.redis;

import com.aktic.indussahulatbackend.model.entity.EventAmbulanceAssignment;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.enums.NotificationType;
import com.aktic.indussahulatbackend.model.enums.ReceiverType;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import com.aktic.indussahulatbackend.model.redis.RedisEventAmbulanceAssignment;
import com.aktic.indussahulatbackend.model.request.NotificationRequestDTO;
import com.aktic.indussahulatbackend.model.response.EventAmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.repository.eventAmbulanceAssignment.EventAmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.redis.RedisEventAmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.service.notification.NotificationService;
import com.aktic.indussahulatbackend.service.socket.SocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final RedisEventAmbulanceAssignmentRepository redisEventAmbulanceAssignmentRepository;
    private final EventAmbulanceAssignmentRepository eventAmbulanceAssignmentRepository;
    private final NotificationService notificationService;
    private final SocketService socketService;

    @Transactional
    public void saveEventAmbulanceAssignment(EventAmbulanceAssignment ambulanceAssignment) {
        try {
            RedisEventAmbulanceAssignment eventAmbulanceAssignment = RedisEventAmbulanceAssignment.builder()
                    .id(ambulanceAssignment.getId())
                    .ambulanceAssignment(ambulanceAssignment.getAmbulanceAssignment())
                    .ambulanceProvider(ambulanceAssignment.getAmbulanceProvider())
                    .event(ambulanceAssignment.getEvent())
                    .status(ambulanceAssignment.getStatus())
                    .createdAt(ambulanceAssignment.getCreatedAt())
                    .updatedAt(ambulanceAssignment.getUpdatedAt())
                    .build();

            redisEventAmbulanceAssignmentRepository.save(eventAmbulanceAssignment);
            log.info("EventAmbulanceAssignment saved to Redis with ID: {}", ambulanceAssignment.getId());
        } catch (Exception e) {
            log.error("Error saving EventAmbulanceAssignment to Redis", e);
        }
    }

    @Transactional
    public void deleteEventAmbulanceAssignment(Long id) {
        try {
            redisEventAmbulanceAssignmentRepository.deleteById(id);
            log.info("EventAmbulanceAssignment with ID {} deleted from Redis", id);
        } catch (Exception e) {
            log.error("Error deleting EventAmbulanceAssignment from Redis", e);
        }
    }

    @Transactional
    public void handleEventAmbulanceAssignmentExpiration(String expiredKey) {
        try {
            Long id = extractIdFromKey(expiredKey);

            if (id == null) {
                log.warn("Invalid expired key format: {}", expiredKey);
                return;
            }

            // Fetch the EventAmbulanceAssignment from the database
            Optional<EventAmbulanceAssignment> assignmentOptional = eventAmbulanceAssignmentRepository.findById(id);

            if (!assignmentOptional.isPresent()) {
                log.warn("EventAmbulanceAssignment with ID {} not found.", id);
                return;
            }

            EventAmbulanceAssignment assignment = assignmentOptional.get();

            assignment.getEvent().setStatus(EventStatus.QUESTIONNAIRE_FILLED);
            assignment.setStatus(RequestStatus.EXPIRED);

            // Save the updated assignment back to the database
            assignment = eventAmbulanceAssignmentRepository.save(assignment);

            NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                    .receiverId(assignment.getAmbulanceProvider().getId())
                    .receiverType(ReceiverType.AMBULANCE_PROVIDER)
                    .payload(new EventAmbulanceAssignmentDTO(assignment))
                    .notificationType(NotificationType.EVENT_AMBULANCE_ASSIGN_EXPIRED)
                    .build();

            notificationService.sendNotification(notificationRequestDTO);

            socketService.sendUpdatedEvent(new IncidentEventDTO(assignment.getEvent()));
            log.info("EventAmbulanceAssignment with ID {} marked as expired.", id);


        } catch (Exception e) {
            log.error("Error handling event ambulance assignment expiration", e);
        }
    }

    private Long extractIdFromKey(String expiredKey) {
        try {
            String[] parts = expiredKey.split(":");
            return Long.parseLong(parts[1]);
        } catch (Exception e) {
            log.error("Failed to extract ID from expired key: {}", expiredKey, e);
            return null;
        }
    }
}
