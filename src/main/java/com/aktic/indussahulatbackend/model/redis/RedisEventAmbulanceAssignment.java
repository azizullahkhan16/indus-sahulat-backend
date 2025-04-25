package com.aktic.indussahulatbackend.model.redis;

import com.aktic.indussahulatbackend.constant.Constants;
import com.aktic.indussahulatbackend.model.entity.AmbulanceAssignment;
import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "event_ambulance_assignment_request", timeToLive = Constants.EVENT_AMBULANCE_ASSIGNMENT_TTL)
public class RedisEventAmbulanceAssignment implements Serializable {
    @Id
    private Long id;

    private AmbulanceAssignment ambulanceAssignment;

    private AmbulanceProvider ambulanceProvider;

    private IncidentEvent event;

    private RequestStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        if (status == null) {
            this.status = RequestStatus.REQUESTED;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

}
