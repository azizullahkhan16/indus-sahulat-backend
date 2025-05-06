package com.aktic.indussahulatbackend.model.request;

import com.aktic.indussahulatbackend.model.enums.NotificationType;
import com.aktic.indussahulatbackend.model.enums.NotificationReceiverType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDTO {
    private Long receiverId;
    private NotificationReceiverType receiverType;
    private Object payload;
    private NotificationType notificationType;

}
