package com.aktic.indussahulatbackend.model.redis;

import com.aktic.indussahulatbackend.constant.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "event_hospital_assignment_request", timeToLive = Constants.EVENT_HOSPITAL_ASSIGNMENT_TTL)
public class RedisEventHospitalAssignment {
    @Id
    private Long id;
}
