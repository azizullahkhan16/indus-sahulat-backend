package com.aktic.indussahulatbackend.model.entity;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
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
@Table(name = "incident_events")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class IncidentEvent {
    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, updatable = false)
    private Patient patient;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ambulance_provider_id")
    private AmbulanceProvider ambulanceProvider;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ambulance_assignment_id")
    private EventAmbulanceAssignment ambulanceAssignment;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_assignment_id")
    private EventHospitalAssignment hospitalAssignment;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "pickup_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "pickup_longitude"))
    })
    private Location pickupLocation;

    @Column(name = "pickup_time")
    private Instant pickupTime;  // Pickup time

    @Column(name = "dropOff_time")
    private Instant dropOffTime;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "dropOff_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "dropOff_longitude"))
    })
    private Location dropOffLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "live_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "live_longitude"))
    })
    private Location liveLocation;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    private void setDefaultStatus() {
        if (this.status == null) {
            this.status = EventStatus.CREATED;
        }
    }

}
