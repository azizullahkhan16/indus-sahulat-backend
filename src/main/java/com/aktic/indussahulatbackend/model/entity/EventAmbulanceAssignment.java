package com.aktic.indussahulatbackend.model.entity;

import com.aktic.indussahulatbackend.model.enums.RequestStatus;
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
@Table(name = "event_ambulance_assignments")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EventAmbulanceAssignment {
    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ambulance_assignment_id", nullable = false, updatable = false)
    private AmbulanceAssignment ambulanceAssignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ambulance_provider_id", nullable = false)
    private AmbulanceProvider ambulanceProvider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false, updatable = false)
    private IncidentEvent event;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    private void onCreate() {
        if (status == null) {
            this.status = RequestStatus.REQUESTED;
        }
    }
}
