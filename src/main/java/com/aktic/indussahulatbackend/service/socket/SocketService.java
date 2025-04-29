package com.aktic.indussahulatbackend.service.socket;

import com.aktic.indussahulatbackend.constant.Constants;
import com.aktic.indussahulatbackend.constant.SocketEndpoint;
import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.entity.Notification;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.redis.RedisEventLiveLocation;
import com.aktic.indussahulatbackend.model.request.LocationDTO;
import com.aktic.indussahulatbackend.model.response.AmbulanceAssignmentDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.repository.incidentEvent.IncidentEventRepository;
import com.aktic.indussahulatbackend.repository.redis.RedisEventLiveLocationRepository;
import com.aktic.indussahulatbackend.service.redis.RedisService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Cache<Long, Location> inMemoryCache = Caffeine.newBuilder()
            .maximumSize(Constants.MAX_ACTIVE_INCIDENT_EVENTS)
            .build();
    private final RedisService redisService;

    public void sendUpdatedEvent(IncidentEventDTO incidentEvent) {
        try {
            log.info("Sending updated event: {}", incidentEvent);
            messagingTemplate.convertAndSend(SocketEndpoint.INCIDENT_EVENT_UPDATE.getPath() + incidentEvent.getId(), incidentEvent);
        } catch (Exception e) {
            log.error("Error sending updated event: {}", e.getMessage(), e);
        }
    }

    public void updateLiveLocation(Long eventId, LocationDTO locationDTO) {
        try {
            log.info("Updating live location for event ID: {}", eventId);
            messagingTemplate.convertAndSend(SocketEndpoint.INCIDENT_EVENT_LIVE_LOCATION.getPath() + eventId, locationDTO);

            Location prevLocation = inMemoryCache.getIfPresent(eventId);

            if (prevLocation != null) {
                double distance = calculateDistance(prevLocation, new Location(locationDTO.getLatitude(), locationDTO.getLongitude()));

                if (distance < Constants.DISTANCE_THRESHOLD_METERS) {
                    return;
                }
            }

            redisService.saveEventLiveLocation(eventId, new Location(locationDTO.getLatitude(), locationDTO.getLongitude()));

            inMemoryCache.put(eventId, new Location(locationDTO.getLatitude(), locationDTO.getLongitude()));
        } catch (Exception e) {
            log.error("Error updating live location: {}", e.getMessage(), e);
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

    public void sendNewActiveEvent(IncidentEventDTO incidentEvent) {
        try {
            log.info("Sending new active event: {}", incidentEvent);
            messagingTemplate.convertAndSend(SocketEndpoint.ACTIVE_INCIDENT_EVENT.getPath(), incidentEvent);
        } catch (Exception e) {
            log.error("Error sending new active event: {}", e.getMessage(), e);
        }
    }

    public void sendNewAdmitRequest(Map<String, Object> admitRequest) {
        try {
            log.info("Sending new admit request: {}", admitRequest);
            messagingTemplate.convertAndSend(SocketEndpoint.NEW_ADMIT_REQUEST.getPath(), admitRequest);
        } catch (Exception e) {
            log.error("Error sending new admit request: {}", e.getMessage(), e);
        }
    }

    public void sendNewAmbulanceAssignmentToDriver(AmbulanceAssignmentDTO ambulanceAssignmentDTO) {
        try {
            log.info("Sending new ambulance assignment to driver: {}", ambulanceAssignmentDTO);
            messagingTemplate.convertAndSend(SocketEndpoint.AMBULANCE_ASSIGNMENT_TO_DRIVER.getPath() + ambulanceAssignmentDTO.getAmbulanceDriver().getId(), ambulanceAssignmentDTO);
        } catch (Exception e) {
            log.error("Error sending new ambulance assignment to driver: {}", e.getMessage(), e);
        }
    }


    private double calculateDistance(Location prevLocation, Location currentLocation) {
        final int EARTH_RADIUS = 6371000; // meters

        double lat1 = prevLocation.getLatitude();
        double lon1 = prevLocation.getLongitude();
        double lat2 = currentLocation.getLatitude();
        double lon2 = currentLocation.getLongitude();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

}
