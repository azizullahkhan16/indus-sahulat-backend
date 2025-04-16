package com.aktic.indussahulatbackend.model.entity;

import com.aktic.indussahulatbackend.model.enums.NotificationType;
import com.aktic.indussahulatbackend.model.enums.ReceiverType;
import com.aktic.indussahulatbackend.util.JsonObjectConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Notification {
    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "receiver_type", nullable = false)
    private ReceiverType receiverType;

    @Column(columnDefinition = "TEXT", name = "payload")
    @Convert(converter = JsonObjectConverter.class)
    private Object payload;

    @Column(name = "is_read")
    private Boolean isRead;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        this.isRead = false;
    }
}
