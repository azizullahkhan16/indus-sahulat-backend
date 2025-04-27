package com.aktic.indussahulatbackend.model.redis;

import com.aktic.indussahulatbackend.constant.Constants;
import com.aktic.indussahulatbackend.model.common.Location;
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
@RedisHash(value = "event_live_location")
public class RedisEventLiveLocation implements Serializable {
    @Id
    private Long eventId;

    private Location liveLocation;
}
