package com.aktic.indussahulatbackend.model.request;

import com.aktic.indussahulatbackend.model.enums.NotificationType;
import com.aktic.indussahulatbackend.model.enums.ReceiverType;
import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDTO {
    private Long receiverId;
    private ReceiverType receiverType;
    private Object payload;
    private NotificationType notificationType;

}
