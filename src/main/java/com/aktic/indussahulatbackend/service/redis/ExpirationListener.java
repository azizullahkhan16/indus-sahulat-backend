package com.aktic.indussahulatbackend.service.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@Slf4j
@RequiredArgsConstructor
public class ExpirationListener implements MessageListener {

    private final RedisService redisService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("Received expired key: {}", expiredKey);

        // Decide based on key pattern
        if (expiredKey.startsWith("event_hospital_assignment_request")) {
            log.info("Handling event hospital assignment expiration for key: {}", expiredKey);
            redisService.handleEventHospitalAssignmentExpiration(expiredKey);

        } else if (expiredKey.startsWith("event_ambulance_assignment_request")) {
            log.info("Handling event ambulance assignment expiration for key: {}", expiredKey);
            redisService.handleEventAmbulanceAssignmentExpiration(expiredKey);

        } else {
            log.warn("No handler found for expired key: {}", expiredKey);
        }
    }
}