package com.aktic.indussahulatbackend.model.entity;

import com.aktic.indussahulatbackend.model.common.Location;
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
@Table(name = "ambulance_assignments")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AmbulanceAssignment {
    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ambulance_provider_id", nullable = false, updatable = false)
    private AmbulanceProvider ambulanceProvider;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ambulance_id", nullable = false, updatable = false)
    private Ambulance ambulance;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ambulance_driver_id", nullable = false, updatable = false)
    private AmbulanceDriver ambulanceDriver;

    @Column(name = "is_active")
    private Boolean isActive;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "driver_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "driver_longitude"))
    })
    private Location driverLocation;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
