package com.aktic.indussahulatbackend.service.notification;

import com.aktic.indussahulatbackend.model.entity.Notification;
import com.aktic.indussahulatbackend.model.request.NotificationRequestDTO;
import com.aktic.indussahulatbackend.repository.notification.NotificationRepository;
import com.aktic.indussahulatbackend.service.socket.SocketService;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final SocketService socketService;
    private final NotificationRepository notificationRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    @Transactional
    public void sendNotification(NotificationRequestDTO notificationRequestDTO) {
        try {
            Notification notification = Notification.builder()
                    .id(snowflakeIdGenerator.nextId())
                    .receiverId(notificationRequestDTO.getReceiverId())
                    .receiverType(notificationRequestDTO.getReceiverType())
                    .payload(notificationRequestDTO.getPayload())
                    .notificationType(notificationRequestDTO.getNotificationType())
                    .build();

            notification = notificationRepository.save(notification);

            socketService.sendNotificationToUser(notification);

        } catch (Exception e) {
            log.error("Error sending notification: {}", e.getMessage(), e);
        }
    }
}
