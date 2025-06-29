package com.aktic.indussahulatbackend.model.redis;

import com.aktic.indussahulatbackend.constant.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "event_ambulance_assignment_request", timeToLive = Constants.EVENT_AMBULANCE_ASSIGNMENT_TTL)
public class RedisEventAmbulanceAssignment implements Serializable {
    @Id
    private Long id;

}
