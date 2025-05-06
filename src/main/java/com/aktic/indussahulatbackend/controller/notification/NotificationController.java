package com.aktic.indussahulatbackend.controller.notification;

import com.aktic.indussahulatbackend.model.entity.Notification;
import com.aktic.indussahulatbackend.service.notification.NotificationService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/get-notifications")
    public ResponseEntity<ApiResponse<Page<Notification>>> getNotifications(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        return notificationService.getNotifications(pageNumber, limit);
    }

    @PatchMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<ApiResponse<Notification>> markAsRead(
            @PathVariable Long notificationId
    ) {
        return notificationService.markNotificationAsRead(notificationId);
    }
}
