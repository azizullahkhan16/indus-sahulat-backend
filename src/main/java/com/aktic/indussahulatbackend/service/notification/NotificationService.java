package com.aktic.indussahulatbackend.service.notification;

import com.aktic.indussahulatbackend.model.common.UserBase;
import com.aktic.indussahulatbackend.model.entity.Notification;
import com.aktic.indussahulatbackend.model.request.NotificationRequestDTO;
import com.aktic.indussahulatbackend.repository.notification.NotificationRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.service.socket.SocketService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final SocketService socketService;
    private final NotificationRepository notificationRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final AuthService authService;

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

    @Transactional
    public ResponseEntity<ApiResponse<Page<Notification>>> getNotifications(Integer pageNumber, Integer limit) {
        try {
            // Default pagination values
            int page = (pageNumber != null && pageNumber >= 0) ? pageNumber : 0; // Allow page 0
            int size = (limit != null && limit > 0) ? limit : 10;

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            UserBase user = authService.getCurrentUser();

            Page<Notification> notifications = notificationRepository.findByReceiverId(user.getId(), pageable);

            return ResponseEntity.ok(new ApiResponse<>(true, "Notifications fetched successfully", notifications));

        } catch (Exception e) {
            log.error("Error fetching notifications: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Error fetching notifications", null));
        }
    }

    public ResponseEntity<ApiResponse<Notification>> markNotificationAsRead(Long notificationId) {
        try {
            UserBase user = authService.getCurrentUser();

            Notification notification = notificationRepository.findByIdAndReceiverId(notificationId, user.getId())
                    .orElseThrow(() -> new NoSuchElementException("Notification not found"));

            notification.setIsRead(true);

            notificationRepository.save(notification);

            return ResponseEntity.ok(new ApiResponse<>(true, "Notification marked as read", notification));

        } catch (NoSuchElementException e) {
            log.error("Notification not found: {}", e.getMessage());
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Notification not found", null));
        } catch (Exception e) {
            log.error("Error marking notification as read: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Error marking notification as read", null));
        }
    }
}
