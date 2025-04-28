package com.aktic.indussahulatbackend.service.redis;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.EventAmbulanceAssignment;
import com.aktic.indussahulatbackend.model.entity.EventHospitalAssignment;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.enums.NotificationType;
import com.aktic.indussahulatbackend.model.enums.ReceiverType;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import com.aktic.indussahulatbackend.model.redis.RedisEventAmbulanceAssignment;
import com.aktic.indussahulatbackend.model.redis.RedisEventHospitalAssignment;
import com.aktic.indussahulatbackend.model.redis.RedisEventLiveLocation;
import com.aktic.indussahulatbackend.model.request.NotificationRequestDTO;
import com.aktic.indussahulatbackend.model.response.EventAmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.EventHospitalAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.repository.eventAmbulanceAssignment.EventAmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.eventHospitalAssignment.EventHospitalAssignmentRepository;
import com.aktic.indussahulatbackend.repository.redis.RedisEventAmbulanceAssignmentRepository;
import com.aktic.indussahulatbackend.repository.redis.RedisEventHospitalAssignmentRepository;
import com.aktic.indussahulatbackend.repository.redis.RedisEventLiveLocationRepository;
import com.aktic.indussahulatbackend.service.notification.NotificationService;
import com.aktic.indussahulatbackend.service.socket.SocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class RedisService {
    private final RedisEventAmbulanceAssignmentRepository redisEventAmbulanceAssignmentRepository;
    private final EventAmbulanceAssignmentRepository eventAmbulanceAssignmentRepository;
    private final NotificationService notificationService;
    private final SocketService socketService;
    private final RedisEventHospitalAssignmentRepository redisEventHospitalAssignmentRepository;
    private final EventHospitalAssignmentRepository eventHospitalAssignmentRepository;
    private final RedisEventLiveLocationRepository redisEventLiveLocationRepository;

    @Autowired
    public RedisService(
            RedisEventAmbulanceAssignmentRepository redisEventAmbulanceAssignmentRepository,
            EventAmbulanceAssignmentRepository eventAmbulanceAssignmentRepository,
            @Lazy NotificationService notificationService,
            @Lazy SocketService socketService,
            RedisEventHospitalAssignmentRepository redisEventHospitalAssignmentRepository,
            EventHospitalAssignmentRepository eventHospitalAssignmentRepository,
            RedisEventLiveLocationRepository redisEventLiveLocationRepository
    ) {
        this.redisEventAmbulanceAssignmentRepository = redisEventAmbulanceAssignmentRepository;
        this.eventAmbulanceAssignmentRepository = eventAmbulanceAssignmentRepository;
        this.notificationService = notificationService;
        this.socketService = socketService;
        this.redisEventHospitalAssignmentRepository = redisEventHospitalAssignmentRepository;
        this.eventHospitalAssignmentRepository = eventHospitalAssignmentRepository;
        this.redisEventLiveLocationRepository = redisEventLiveLocationRepository;
    }

    @Transactional
    public void saveEventAmbulanceAssignment(Long ambulanceAssignmentId) {
        try {
            RedisEventAmbulanceAssignment eventAmbulanceAssignment = RedisEventAmbulanceAssignment.builder()
                    .id(ambulanceAssignmentId)
                    .build();

            redisEventAmbulanceAssignmentRepository.save(eventAmbulanceAssignment);
            log.info("EventAmbulanceAssignment saved to Redis with ID: {}", ambulanceAssignmentId);
        } catch (Exception e) {
            log.error("Error saving EventAmbulanceAssignment to Redis", e);
        }
    }

    @Transactional
    public void saveEventHospitalAssignment(Long hospitalAssignmentId) {
        try {
            RedisEventHospitalAssignment eventHospitalAssignment = RedisEventHospitalAssignment.builder()
                    .id(hospitalAssignmentId)
                    .build();

            redisEventHospitalAssignmentRepository.save(eventHospitalAssignment);
            log.info("EventHospitalAssignment saved to Redis with ID: {}", hospitalAssignmentId);
        } catch (Exception e) {
            log.error("Error saving EventHospitalAssignment to Redis", e);
        }
    }

    @Transactional
    public void saveEventLiveLocation(Long eventId, Location liveLocation) {
        try {
            RedisEventLiveLocation redisEventLiveLocation = RedisEventLiveLocation.builder()
                    .eventId(eventId)
                    .liveLocation(liveLocation)
                    .build();

            redisEventLiveLocationRepository.save(redisEventLiveLocation);
            log.info("Live location updated for EventAmbulanceAssignment with ID: {}", eventId);
        } catch (Exception e) {
            log.error("Error updating live location for EventAmbulanceAssignment", e);
        }
    }

    @Transactional
    public Location getEventLiveLocation(Long eventId) {
        try {
            RedisEventLiveLocation redisEventLiveLocation = redisEventLiveLocationRepository.findById(eventId)
                    .orElseThrow(() -> new NoSuchElementException("Live location not found for EventAmbulanceAssignment with ID: " + eventId));

            return redisEventLiveLocation.getLiveLocation();
        } catch (Exception e) {
            log.error("Error fetching live location for EventAmbulanceAssignment", e);
            return null;
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
    public void deleteEventHospitalAssignment(Long id) {
        try {
            redisEventHospitalAssignmentRepository.deleteById(id);
            log.info("EventHospitalAssignment with ID {} deleted from Redis", id);
        } catch (Exception e) {
            log.error("Error deleting EventHospitalAssignment from Redis", e);
        }
    }

    @Transactional
    public void deleteEventLiveLocation(Long eventId) {
        try {
            redisEventLiveLocationRepository.deleteById(eventId);
            log.info("Live location for EventAmbulanceAssignment with ID {} deleted from Redis", eventId);
        } catch (Exception e) {
            log.error("Error deleting live location for EventAmbulanceAssignment", e);
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

            EventAmbulanceAssignment assignment = eventAmbulanceAssignmentRepository.updateStatusAndEventStatus(
                    id,
                    RequestStatus.EXPIRED.name(),
                    EventStatus.QUESTIONNAIRE_FILLED.name()
            ).orElseThrow(() -> new NoSuchElementException("EventAmbulanceAssignment not found with ID: " + id));

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

    @Transactional
    public void handleEventHospitalAssignmentExpiration(String expiredKey) {
        try {
            Long id = extractIdFromKey(expiredKey);

            if (id == null) {
                log.warn("Invalid expired key format: {}", expiredKey);
                return;
            }

            EventHospitalAssignment assignment = eventHospitalAssignmentRepository
                    .updateEventHospitalAssignmentStatusById(id, RequestStatus.EXPIRED.name())
                    .orElseThrow(() -> new NoSuchElementException("EventHospitalAssignment not found with ID: " + id));

            NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                    .receiverId(assignment.getEvent().getAmbulanceAssignment().getAmbulanceAssignment().getAmbulanceDriver().getId())
                    .receiverType(ReceiverType.AMBULANCE_DRIVER)
                    .payload(new EventHospitalAssignmentDTO(assignment))
                    .notificationType(NotificationType.EVENT_HOSPITAL_ASSIGN_EXPIRED)
                    .build();

            notificationService.sendNotification(notificationRequestDTO);

            socketService.sendUpdatedEvent(new IncidentEventDTO(assignment.getEvent()));

            log.info("EventHospitalAssignment with ID {} marked as expired.", id);
        } catch (Exception e) {
            log.error("Error handling event hospital assignment expiration", e);
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
